package com.whoz_in.main_api.shared.event.fail;

import com.whoz_in.main_api.shared.event.ApplicationEvent;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

// 다른 모듈에서 사용할 수 있도록 static 으로 구성
public class FailedEventStore {

    // TODO: 어떤 자료구조를 써야 할지?
    private static final Queue<ApplicationEvent> eventStore = new ConcurrentLinkedDeque<>();

    public static void add(ApplicationEvent event) {
        eventStore.add(event);
    }

    public static List<ApplicationEvent> poll() {
        List<ApplicationEvent> events = new ArrayList<>(eventStore);
        eventStore.clear();
        return events;
    }

}
