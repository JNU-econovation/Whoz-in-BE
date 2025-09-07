package com.whoz_in.main_api.shared.utils;

import com.whoz_in.domain.member.model.ProfileImageId;

public interface ImageUploadStorage {
    void save(ProfileImageId profileImageId, byte[]bytes);
    void delete(ProfileImageId profileImageId);
}
