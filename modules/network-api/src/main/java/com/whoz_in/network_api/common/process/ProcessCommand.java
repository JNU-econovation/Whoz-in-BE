package com.whoz_in.network_api.common.process;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

// AbstractProcess 구현체를 실행시키기 위한 명령어 VO이다. 명령어를 담기 전에 전처리를 수행한다.
// ProcessBuilder가 external command를 직접 실행하기 때문에 쉘의 builtin command는 사용할 수 없습니다.
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProcessCommand{
    private static final boolean IS_SUDO_PRESENT= TransientProcess.create("which sudo")
                .result()
                .contains("/sudo");

    private final boolean isSudoCommand;
    private final String[] command;

    public static ProcessCommand of(String command) {
        if (command.contains("|")) {
            throw new IllegalArgumentException("파이프(|) 문자는 사용할 수 없음. (" + command + ")");
        }

        boolean hasSudo = command.startsWith("sudo "); // sudo로 실행됐는가?
        if (hasSudo) { // sudo로 실행됐는데
            if (!IS_SUDO_PRESENT) { // 시스템에 sudo가 존재하지 않으면
                command = removeSudoPrefix(command); // 명령어에서 sudo를 제거한다.
                hasSudo = false; // 이제 sudo로 실행되는 커맨드가 아님
            } else if (!command.startsWith("sudo -S "))  { // 시스템에 sudo가 존재하고 sudo -S로 시작하지 않으면
                // sudo를 sudo -S로 변경
                command = command.replaceFirst("sudo ", "sudo -S ");
            }
        }
        String[] separatedCommand = command.split(" ");
        return new ProcessCommand(hasSudo, separatedCommand);
    }

    private static String removeSudoPrefix(String command) {
        return command.replaceFirst("^sudo\\s+-S\\s+", "")  // "sudo -S " 제거
                .replaceFirst("^sudo\\s+", "");      // "sudo " 제거
    }

    @Override
    public String toString() {
        return String.join(" ", command);
    }
}
