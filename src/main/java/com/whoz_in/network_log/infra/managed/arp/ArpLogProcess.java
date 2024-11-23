package com.whoz_in.network_log.infra.managed.arp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ArpLogProcess {

    private Process process;
    private ProcessBuilder pb;
    private BufferedReader br;
    private BufferedWriter bw;

    public ArpLogProcess(String command){
        try {
            process = new ProcessBuilder(command.split(" ")).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<String> start(String command){
        Set<String> logs = new HashSet<>();

        try{
            String line;
            while((line = br.readLine()) != null){
                logs.add(line);
            }
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        return logs;
    }

}
