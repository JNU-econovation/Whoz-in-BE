package com.whoz_in.networklog.application.log;

import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LogParserTest {

    private final DefaultLogParser defaultLogParser = new DefaultLogParser();
    
    @Test
    @DisplayName("src_mac parse 테스트")
    public void parser_test(){
        String log = "Nov 15, 2024 15:29:01.169884000 KST\t8c:55:4a:ad:1a:9c\t01:00:5e:00:00:fb\t10.20.11.57\t224.0.0.251";

        Map<String, String> result = defaultLogParser.parse(log);

        Assertions.assertEquals(result.get("src_mac"), "8c:55:4a:ad:1a:9c");
    }

    @Test
    @DisplayName("dst_mac parse 테스트")
    public void parser_test2(){
        String log = "JANUARY 15, 2024 15:29:01.169884000 KST\t8c:55:4a:ad:1a:9c\t01:00:5e:00:00:fb\t10.20.11.57\t224.0.0.251";

        Map<String, String> result = defaultLogParser.parse(log);

        Assertions.assertEquals(result.get("dst_mac"), "01:00:5e:00:00:fb");
    }

    @Test
    @DisplayName("src_ip parse 테스트")
    public void parser_test3(){
        String log = "JANUARY 15, 2024 15:29:01.169884000 KST\t8c:55:4a:ad:1a:9c\t01:00:5e:00:00:fb\t10.20.11.57\t224.0.0.251";

        Map<String, String> result = defaultLogParser.parse(log);

        Assertions.assertEquals(result.get("src_ip"), "10.20.11.57");
    }

    @Test
    @DisplayName("dst_ip parse 테스트")
    public void parser_test4(){
        String log = "JANUARY 15, 2024 15:29:01.169884000 KST\t8c:55:4a:ad:1a:9c\t01:00:5e:00:00:fb\t10.20.11.57\t224.0.0.251";

        Map<String, String> result = defaultLogParser.parse(log);

        Assertions.assertEquals(result.get("dst_ip"), "224.0.0.251");
    }

}
