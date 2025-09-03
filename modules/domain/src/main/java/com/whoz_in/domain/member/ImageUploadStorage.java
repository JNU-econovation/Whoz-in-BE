package com.whoz_in.domain.member;

import com.whoz_in.domain.member.model.ImageId;

public interface ImageUploadStorage {
    void save(ImageId imageId, byte[]bytes);
}
