package com.whoz_in.network_api.controller;

/*
사전 지식: network-api가 사용자의 아이피를 보내줄 때(/api/v1/ip) 사용자의 http 요청으로부터 ip를 얻는다.
하지만 요청이 게이트웨이를 거치면서 http 요청의 아이피가 게이트웨이의 아이피로 변경될 수도 있다.
이러면 사용자는 자신의 아이피가 아닌 게이트웨이의 아이피를 얻게 되는 것이다.
따라서 사용자에게 사용자의 아이피를 반환하기 전에 network-api가 연결된 게이트웨이들의 아이피 중 하나가 아닌지 검증해야 한다.
*/
public interface GatewayIpList {
    boolean isGatewayIp(String ip);
}
