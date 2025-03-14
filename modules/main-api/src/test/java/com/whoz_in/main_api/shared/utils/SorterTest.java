package com.whoz_in.main_api.shared.utils;

import com.whoz_in.main_api.query.member.application.response.MemberInRoomResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SorterTest {

    @Test
    @DisplayName("MemberInRoomResponse Active 상태 기준 정렬 테스트")
    public void Active상태인_Member가_상위에_위치한다(){

        // given
        List<MemberInRoomResponse> responses = new ArrayList<>(List.of(
                new MemberInRoomResponse(1, "1", "1", "1", "1", 0, true),
                new MemberInRoomResponse(2, "2", "2", "2", "2",0, false),
                new MemberInRoomResponse(3, "3", "3", "3", "3", 0, true),
                new MemberInRoomResponse(4, "4", "4", "4", "4",0, false)
        ));

        List<MemberInRoomResponse> answer = new ArrayList<>(List.of(
                new MemberInRoomResponse(1, "1", "1", "1", "1", 0,true),
                new MemberInRoomResponse(3, "3", "3", "3", "3", 0,true),
                new MemberInRoomResponse(2, "2", "2", "2", "2", 0,false),
            new MemberInRoomResponse(4, "4", "4", "4", "4", 0,false)
        ));

        // when
        Sorter.<MemberInRoomResponse>builder()
                .comparator(Comparator.comparing(MemberInRoomResponse::isActive).reversed())
                .build()
                .sort(responses);

        // then
        Assertions.assertIterableEquals(responses, answer);
    }

    @Test
    @DisplayName("MemberInRoomResponse MemberName 기준 정렬 테스트")
    public void MemberName_기준_오름차순_정렬(){
        List<MemberInRoomResponse> responses = new ArrayList<>(List.of(
            new MemberInRoomResponse(1, "1", "D", "1", "1", 0,true),
            new MemberInRoomResponse(2, "2", "C", "2", "2", 0,false),
            new MemberInRoomResponse(3, "3", "B", "3", "3", 0,true),
            new MemberInRoomResponse(4, "4", "A", "4", "4", 0,false)
        ));

        List<MemberInRoomResponse> answer = new ArrayList<>(List.of(
            new MemberInRoomResponse(4, "4", "A", "4", "4", 0,false),
                new MemberInRoomResponse(3, "3", "B", "3", "3", 0,true),
            new MemberInRoomResponse(2, "2", "C", "2", "2", 0,false),
            new MemberInRoomResponse(1, "1", "D", "1", "1", 0,true)
        ));

        Sorter.<MemberInRoomResponse>builder()
                .comparator(Comparator.comparing(MemberInRoomResponse::memberName))
                .build()
                .sort(responses);

        Assertions.assertIterableEquals(responses, answer);


    }

}
