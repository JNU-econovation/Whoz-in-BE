package com.whoz_in.log_writer.managed.mdns;

import com.whoz_in.log_writer.common.util.NonBlockingBufferedReader;
import com.whoz_in.log_writer.managed.ManagedInfo;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class MdnsLogProcess {
    private final Process process;
    private final NonBlockingBufferedReader cbr;

    //TODO: Info로 변경
    public MdnsLogProcess(ManagedInfo info, String sudoPassword) {
        try {
            this.process = new ProcessBuilder(info.command().split(" "))
                    .redirectError(new File("../error", info.ssid()+".txt"))
                    .start();
            this.cbr = new NonBlockingBufferedReader(new BufferedReader(new InputStreamReader(this.process.getInputStream())));
            Writer writer = new OutputStreamWriter(this.process.getOutputStream());
            writer.write(sudoPassword + System.lineSeparator());
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(info.command() + " 실행 실패");
        }
    }

    /**
     * @return 프로세스의 출력에서 한 줄을 읽어들인다.
     * 읽을 줄이 없을경우 null을 출력한다.
     */
    public String readLine(){
        //여기서 try catch
        try {
            return this.cbr.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isAlive(){
        return this.process.isAlive();
    }

    public void terminate(){
        this.process.destroy();
    }
}
