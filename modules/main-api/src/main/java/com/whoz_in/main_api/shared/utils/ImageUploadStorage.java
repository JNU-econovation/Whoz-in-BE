package com.whoz_in.main_api.shared.utils;

import com.whoz_in.domain.member.model.ImageId;

public interface ImageUploadStorage {
    void save(ImageId imageId, byte[]bytes);
}
