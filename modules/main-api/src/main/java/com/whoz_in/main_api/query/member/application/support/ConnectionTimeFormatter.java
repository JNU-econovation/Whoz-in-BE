package com.whoz_in.main_api.query.member.application.support;

import java.time.Duration;

public class ConnectionTimeFormatter {

    public static String dayHourMinuteTime(Duration duration){
        long second = duration.toMillis() / 1000;

        int day = (int) (second / 60 / 60 / 24);
        int hour = (int) (second / 60 / 60 % 24);
        int min = (int) (second / 60 % 60);
        String time = String.format("%02d일 %02d시간 %02d분", day, hour, min);
        return time;
    }

    public static String hourMinuteTime(Duration duration){
        long second = duration.toMillis() / 1000;
        int hour = (int) (second / 60 / 60);
        int min = (int) (second / 60 % 60);
        String time = String.format("%02d시간 %02d분", hour, min);
        return time;
    }

}
