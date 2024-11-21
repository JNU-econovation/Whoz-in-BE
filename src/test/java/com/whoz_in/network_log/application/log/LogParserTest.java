package com.whoz_in.network_log.application.log;

import com.whoz_in.network_log.infra.managed.mdns.parser.DefaultLogParser;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LogParserTest {

    private final DefaultLogParser defaultLogParser = new DefaultLogParser();
    
    @Test
    @DisplayName("LogLine 입력 -> src_mac 파싱")
    public void src_mac_test(){
        String log = "Nov 15, 2024 15:29:01.169884000 KST\t8c:55:4a:ad:1a:9c\t01:00:5e:00:00:fb\t10.20.11.57\t224.0.0.251";

        Map<String, String> result = defaultLogParser.parse(log);

        Assertions.assertEquals(result.get("src_mac"), "8c:55:4a:ad:1a:9c");
    }

    @Test
    @DisplayName("LogLine 입력 -> dst_mac 파싱")
    public void dst_mac_test(){
        String log = "JANUARY 15, 2024 15:29:01.169884000 KST\t8c:55:4a:ad:1a:9c\t01:00:5e:00:00:fb\t10.20.11.57\t224.0.0.251";

        Map<String, String> result = defaultLogParser.parse(log);

        Assertions.assertEquals(result.get("dst_mac"), "01:00:5e:00:00:fb");
    }

    @Test
    @DisplayName("LogLine 입력 -> src_ip 파싱")
    public void src_ip_test(){
        String log = "JANUARY 15, 2024 15:29:01.169884000 KST\t8c:55:4a:ad:1a:9c\t01:00:5e:00:00:fb\t10.20.11.57\t224.0.0.251";

        Map<String, String> result = defaultLogParser.parse(log);

        Assertions.assertEquals(result.get("src_ip"), "10.20.11.57");
    }

    @Test
    @DisplayName("LogLine 입력 -> dst_ip 파싱")
    public void dst_ip_test(){
        String log = "JANUARY 15, 2024 15:29:01.169884000 KST\t8c:55:4a:ad:1a:9c\t01:00:5e:00:00:fb\t10.20.11.57\t224.0.0.251";

        Map<String, String> result = defaultLogParser.parse(log);

        Assertions.assertEquals(result.get("dst_ip"), "224.0.0.251");
    }

    @Test
    @DisplayName("LogLine 입력 -> time 파싱")
    public void time_test(){
        String log = "JANUARY 15, 2024 15:29:01.169884000 KST\t8c:55:4a:ad:1a:9c\t01:00:5e:00:00:fb\t10.20.11.57\t224.0.0.251";

        Map<String, String> result = defaultLogParser.parse(log);

        Assertions.assertEquals(result.get("time"), "JANUARY 15, 2024 15:29:01.169884000 KST");
    }


    @Test
    @DisplayName("LogLine 입력 -> message 파싱")
    public void message_test(){
        String log = "eth:ethertype:ip:udp:mdns\t96:d6:41:d0:8d:48\t10.30.140.166\t224.0.0.251\t01:00:5e:00:00:fb\t김종민의 MacBookAir._companion-link._tcp.local";
        
        Map<String, String> result = defaultLogParser.parse(log);
        
        Assertions.assertEquals(result.get("message"), "김종민의 MacBookAir._companion-link._tcp.local");
    }



}
