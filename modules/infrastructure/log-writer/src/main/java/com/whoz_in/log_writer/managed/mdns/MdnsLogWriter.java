package com.whoz_in.log_writer.managed.mdns;

import com.whoz_in.log_writer.config.NetworkConfig;
import com.whoz_in.log_writer.config.NetworkConfig.Managed;
import com.whoz_in.log_writer.managed.ManagedLog;
import com.whoz_in.log_writer.managed.ManagedLogDAO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MdnsLogWriter {
    private final Map<Managed, MdnsLogProcess> processes;
    private final MdnsLogParser parser;
    private final ManagedLogDAO dao;
    private final String sudoPassword;

    public MdnsLogWriter(ManagedLogDAO dao, MdnsLogParser parser, NetworkConfig config, @Value("${sudo_password}") String sudoPassword) {
        this.dao = dao;
        this.parser = parser;
        this.sudoPassword = sudoPassword;
        this.processes = config.getMdnsList().parallelStream()
                .collect(Collectors.toMap(
                        managed -> managed,
                        managedInfo -> new MdnsLogProcess(managedInfo.command(), sudoPassword)
                ));
    }

    //TODO: 프로세스 살아있는지 확인하고 재시동

    @Scheduled(fixedRate = 10000)
    private void writeLogs() {
        Map<ManagedLog, ManagedLog> logs = new HashMap<>();
        this.processes.entrySet().parallelStream()
                        .forEach(entry -> {
                            Managed managed = entry.getKey();
                            MdnsLogProcess process = entry.getValue();
                            String line;
                            for(;;) {
                                try {
                                    line = process.readLine();
                                    if (line == null) return;
                                    parser.parse(line).ifPresent(
                                            log -> {
                                                logs.put(log, log);
                                                log.setSsid(managed.ssid());
                                            }
                                    );
                                } catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
        System.out.println("[managed - mdns] 저장할 로그 개수 : " + logs.size());

        dao.insertAll(logs.values());
    }

}
