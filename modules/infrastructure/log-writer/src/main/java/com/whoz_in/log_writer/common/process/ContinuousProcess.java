package com.whoz_in.log_writer.common.process;

//실행 후 종료되지 않는 프로세스
//꾸준히 출력을 읽을 수 있어야 한다.
public abstract class ContinuousProcess {

    //출력이 없을 땐 블로킹되면 안된다.
    public abstract String readLine();
    public abstract boolean isAlive();
    public abstract void terminate();
}
