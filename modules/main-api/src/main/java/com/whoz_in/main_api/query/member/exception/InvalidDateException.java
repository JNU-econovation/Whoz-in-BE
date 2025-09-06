package com.whoz_in.main_api.query.member.exception;

public class InvalidDateException extends RuntimeException {

    private InvalidDateException(String message) {
        super(message);
    }

    public static InvalidDateException of(int year, int month) {
        return new InvalidDateException(
                String.format("아직 %d년 %d월입니다.", year, month)
        );
    }
}
