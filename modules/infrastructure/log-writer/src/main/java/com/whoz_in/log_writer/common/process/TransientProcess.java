package com.whoz_in.log_writer.common.process;

import java.util.List;

//실행 후 종료되어 모든 출력을 얻는 프로세스
//출력 스트림을 통한 프로세스와의 상호작용은 없다.
public abstract class TransientProcess {

    //종료되었을 때 출력을 얻는다.
    //종료되지 않았다면 블로킹된다.
    public abstract List<String> results();
}
