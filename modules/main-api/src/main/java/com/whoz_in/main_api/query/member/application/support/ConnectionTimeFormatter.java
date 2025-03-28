package com.whoz_in.main_api.query.member.application.support;

import java.time.Duration;

public class ConnectionTimeFormatter {

    public static String dayHourMinuteTime(Duration duration){
        long minutes = duration.toMinutes();

        int day = (int) (minutes / (60 * 24));
        int hour = (int) ((minutes % (60 * 24)) / 60 );
        int min = (int) (minutes / 60 % 60);

        return String.format("%01d일 %01d시간 %01d분", day, hour, min);
    }

    public static String hourMinuteTime(Duration duration){
        long minutes = duration.toMinutes();
        return hourMinuteTime(minutes);
    }

    public static String hourMinuteTime(Long minute){
        int hour = (int) (minute / 60);
        int min = (int) (minute % 60);
        return String.format("%01d시간 %01d분", hour, min);
    }

}
