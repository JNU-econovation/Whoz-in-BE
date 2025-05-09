package com.whoz_in.api_query_jpa.member.activity.history;

// ActiveHistory에 저장된 유저의 재실 시간이 며칠치인지 나타냄.
// 필요에 따라 단위를 추가할 수 있지만 굳이 필요없을거 같아서 아직 DAY, TOTAL만 있음.
public enum TimeUnit {
    DAY,
    TOTAL
}
