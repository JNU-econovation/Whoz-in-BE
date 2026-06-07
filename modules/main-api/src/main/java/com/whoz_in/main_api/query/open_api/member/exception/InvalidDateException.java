package com.whoz_in.main_api.query.open_api.member.exception;

import com.whoz_in.shared.WhozinException;

public class InvalidDateException extends WhozinException {
    private InvalidDateException(String message) {
        super("7101", message);
    }

    public static InvalidDateException invalid() {
        return new InvalidDateException("잘못된 날짜 파라미터입니다.");
    }

    public static InvalidDateException futureMonth(int year, int month) {
        return new InvalidDateException("아직 %d년 %d월입니다.".formatted(year, month));
    }

    public static InvalidDateException futureDate(int year, int month, int day) {
        return new InvalidDateException("아직 %d년 %d월 %d일입니다.".formatted(year, month, day));
    }
}
