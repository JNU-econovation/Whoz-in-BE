package com.whoz_in.network_api.common.process;

import java.io.*;

public abstract class AbstractProcess {
    protected final ProcessCommand command;
    protected Process process;
    protected BufferedReader outputReader;
    protected BufferedReader errorReader;

    AbstractProcess(String command) {
        this(ProcessCommand.of(command));
    }

    private AbstractProcess(ProcessCommand command) {
        this.command = command;
    }

    // 서브 클래스의 팩토리 메소드 전용
    protected final void start() {
        try {
            init();
            if (command.isSudoCommand()) {
                enterSudoPassword();
            }
        } catch (IOException e) {
            throw new RuntimeException(getClass().getSimpleName() + " 실행 실패: " + command, e);
        }
    }

    // 필요 시 오버라이딩
    protected void init() throws IOException{
        process = new ProcessBuilder(command.getCommand())
                .redirectErrorStream(true)
                .start();
        outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
    }

    private void enterSudoPassword() throws IOException {
        String sudoPassword = System.getProperty("sudo-password");
        try (Writer writer = new OutputStreamWriter(this.process.getOutputStream())) {
            writer.write(sudoPassword + System.lineSeparator());
            writer.flush();
        }
    }

    public boolean isAlive() {
        return process != null && process.isAlive();
    }

    public void terminate() {
        if (process != null && process.isAlive()) {
            process.destroy();
        }
    }
}
