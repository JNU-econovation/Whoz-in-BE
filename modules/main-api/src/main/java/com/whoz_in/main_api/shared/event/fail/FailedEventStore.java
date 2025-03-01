package com.whoz_in.main_api.shared.event.fail;

import com.whoz_in.main_api.shared.event.ApplicationEvent;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

// 다른 모듈에서 사용할 수 있도록 static 으로 구성
public class FailedEventStore {
    private static final Queue<ApplicationEvent> eventStore = new ConcurrentLinkedQueue<>();
    private static final int MAX_SIZE = 10000; // 메모리 누수 방지용 최대 크기

    public static void add(ApplicationEvent event) {
        if (eventStore.size() < MAX_SIZE) {
            eventStore.add(event);
        } else {
            eventStore.poll();
        }
    }

    public static List<ApplicationEvent> poll() {
        List<ApplicationEvent> events = new ArrayList<>(eventStore);
        eventStore.clear();
        return events;
    }
}
