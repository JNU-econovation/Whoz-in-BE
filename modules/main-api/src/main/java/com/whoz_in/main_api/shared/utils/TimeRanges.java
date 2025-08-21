package com.whoz_in.main_api.shared.utils;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/*
    TimeRange를 받아 겹치는 시간 구간을 병합한다.
    예를 들어, [1시, 3시], [2시, 4시], [8시, 9시]로 주어지면 [1시, 4시], [8시, 9시]를 반환한다.
    그리고 계산 기능들을 제공한다.
 */
public final class TimeRanges {
    private final List<TimeRange> ranges;

    public TimeRanges(List<TimeRange> ranges) {
        if (ranges == null || ranges.isEmpty()) {
            this.ranges = List.of();
        } else {
            this.ranges = mergeRanges(ranges);
        }
    }

    public Duration getTotalDuration() {
        return ranges.stream()
                .map(TimeRange::duration)
                .reduce(Duration.ZERO, Duration::plus);
    }

    public Duration getDurationWithoutLastRange(){
        return ranges.stream()
                .limit(ranges.size() - 1)
                .map(TimeRange::duration)
                .reduce(Duration.ZERO, Duration::plus);
    }

    public LocalDateTime getLastRangeStart() {
        return ranges.isEmpty() ? null : ranges.get(ranges.size() - 1).start();
    }

    public LocalDateTime getLastRangeEnd() {
        return ranges.isEmpty() ? null : ranges.get(ranges.size() - 1).end();
    }

    private static List<TimeRange> mergeRanges(List<TimeRange> ranges) {
        List<TimeRange> sorted = ranges.stream()
                .sorted(Comparator.comparing(TimeRange::start))
                .toList();

        List<TimeRange> result = new ArrayList<>();
        for (TimeRange range : sorted) {
            if (result.isEmpty()) {
                result.add(range);
                continue;
            }
            TimeRange last = result.get(result.size() - 1);
            if (last.overlaps(range)) {
                result.set(result.size() - 1, last.merge(range));
            } else {
                result.add(range);
            }
        }
        return result;
    }


    public record TimeRange(LocalDateTime start, LocalDateTime end) {
        public Duration duration() {
            return Duration.between(start, end);
        }

        public boolean overlaps(TimeRange other) {
            return !this.end.isBefore(other.start) && !other.end.isBefore(this.start);
        }

        public TimeRange merge(TimeRange other) {
            return new TimeRange(
                    this.start.isBefore(other.start) ? this.start : other.start,
                    this.end.isAfter(other.end) ? this.end : other.end
            );
        }
    }
}
