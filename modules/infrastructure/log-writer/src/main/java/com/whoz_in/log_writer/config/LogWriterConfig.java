package com.whoz_in.log_writer.config;

import com.whoz_in.log_writer.common.NetworkInterface;
import java.util.ArrayList;
import java.util.List;

//사용할 모듈에서 구현해야 합니다.
public interface LogWriterConfig {
    //네트워크 로그를 기록하는 방 이름
    String getRoomName();

    //방에서 관리하는 Monitor NI
    NetworkInterface getMonitorNI();

    //방에서 관리하는 Managed NI들
    List<NetworkInterface> getArpNIs();
    List<NetworkInterface> getMdnsNIs();

    //방에서 관리하는 모든 NI들
    default List<NetworkInterface> getNIs(){
        ArrayList<NetworkInterface> nis = new ArrayList<>();
        nis.add(getMonitorNI());
        nis.addAll(getMdnsNIs());
        nis.addAll(getArpNIs());
        return nis;
    }
}
