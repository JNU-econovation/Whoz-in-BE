package com.whoz_in.main_api.shared.utils;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileImageUtil {

    private static final String IMAGE_BASE_URL = "/images/";
    private static final String IMAGE_EXTENSION = ".jpg";

    /**
     * 프로필 이미지 URL 반환
     * @param profileImageId 회원 프로필 이미지 ID
     * @return 이미지 URL
     */
    public String getProfileImageUrl(UUID profileImageId) {
        if (profileImageId == null) {
            return null;
        }

        return IMAGE_BASE_URL + profileImageId + IMAGE_EXTENSION;
    }
}
