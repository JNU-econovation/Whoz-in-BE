package com.whoz_in.log_writer.infra.managed.mdns;

import com.whoz_in.log_writer.common.util.NonBlockingBufferedReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

public final class MdnsLogProcess {
    private final Process process;
    private final NonBlockingBufferedReader cbr;

    public MdnsLogProcess(String command, String sudoPassword) {
        try {
            this.process = new ProcessBuilder(command.split(" "))
                    .start();
            //TODO: errorStream 로깅 - process.getErrorStream()
            this.cbr = new NonBlockingBufferedReader(new BufferedReader(new InputStreamReader(process.getInputStream())));

            Writer writer = new OutputStreamWriter(process.getOutputStream());
            writer.write(sudoPassword + System.lineSeparator());
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(command + " 실행 실패");
        }
    }

    /**
     *
     * @return 프로세스의 출력에서 한 줄을 읽어들인다.
     * 읽을 줄이 없을경우 null을 출력한다.
     */
    public String readLine() throws IOException {
        return cbr.readLine();
    }

    public boolean isAlive(){
        return this.process.isAlive();
    }

    public void terminate(){
        this.process.destroy();
    }
}
