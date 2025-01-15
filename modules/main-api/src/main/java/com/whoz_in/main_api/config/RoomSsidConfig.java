package com.whoz_in.main_api.config;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

//실행시킬 프로세스들이 필요로 하는 정보를 제공하는 역할을 한다.
//다른 모듈이 구현한 LogWriterConfig를 통해 정보를 초기화한다.
@ConfigurationProperties(prefix = "rooms-setting")
public class RoomSsidConfig {
    private final Map<String, List<String>> rooms;

    public List<String> getRooms(){
        return rooms.keySet().stream().toList();
    }
    public List<String> getSsids(String room){
        List<String> ssids = this.rooms.get(room);
        if (ssids == null)
            throw new IllegalStateException("없는 room");
        return ssids;
    }

    @ConstructorBinding
    RoomSsidConfig(List<Room> rooms) {
        this.rooms = rooms.stream().collect(Collectors.toMap(
                Room::name,
                room -> List.copyOf(room.altSsidList())
        ));
    }
}

//yml 읽기용 레코드
record Room(String name, List<String> altSsidList){}