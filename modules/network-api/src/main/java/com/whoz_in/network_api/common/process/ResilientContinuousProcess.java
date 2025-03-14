package com.whoz_in.network_api.common.process;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResilientContinuousProcess extends ContinuousProcess {
    private final ScheduledExecutorService scheduler;
    private final long backoffIntervalMs; // 재시작 초기 대기 시간
    private final long backoffStepMs; // 재시작 대기 시간 증가량
    private final long backoffMaxMs; // 재시작 대기 시간 최대값
    private int backoffCount; // 재시작 횟수

    ResilientContinuousProcess(String command) {
        super(command);
        this.backoffIntervalMs = 2000;
        this.backoffStepMs = 2000;
        this.backoffMaxMs = 60000;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public static ResilientContinuousProcess create(String command){
        ResilientContinuousProcess rcp = new ResilientContinuousProcess(command);
        rcp.start();
        return rcp;
    }

    @Override
    protected void init() throws IOException{
        super.init(); // 프로세스 시작
        scheduleRestart(0); // 재시작 스케줄링
    }

    // 주기적으로 프로세스가 종료됐는지 확인하고 재실행함. 계속 종료된다면 확인하고 재실행하는 간격을 늘림
    private void scheduleRestart(long delayMs) {
        scheduler.schedule(() -> {
            if (isAlive()){
                backoffCount = 0;
            }else {
                log.error("[{}] 프로세스가 종료됨! 프로세스를 재실행합니다({})\n에러 스트림: {}", command, backoffCount, readErrorLines());
                restart();
                backoffCount++;
            }
            scheduleRestart(Math.min(backoffMaxMs, backoffIntervalMs + backoffStepMs * backoffCount));
        }, delayMs, TimeUnit.MILLISECONDS);
    }

    // 프로세스 재시작
    private void restart() {
        try {
            super.terminate(); // 프로세스 종료
            super.init(); // 프로세스 시작
            log.warn("[{}] 프로세스 재실행 완료", command);
        } catch (IOException e) {
            log.error("[{}] 프로세스 재실행 실패: ", command, e);
            terminate();
        }
    }

    @Override
    public void terminate() {
        scheduler.shutdownNow(); // 재시작 스케줄러 종료
        super.terminate(); // 프로세스 종료
        backoffCount = 0;
    }
}
