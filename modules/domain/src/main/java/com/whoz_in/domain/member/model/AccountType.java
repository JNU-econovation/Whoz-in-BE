package com.whoz_in.domain.member.model;

import java.util.Arrays;

public enum AccountType {

    ADMIN("admin"),
    USER("user");

    private String accountType;

    AccountType(String accountType) {
        this.accountType = accountType;
    }

    public static AccountType findAccountType(String accountType){
        return Arrays.stream(AccountType.values())
                .filter(at -> at.name().equals(accountType))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("no account type"));
    }

}
