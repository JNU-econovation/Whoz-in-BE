-- V4__add_profile_image_to_member_entity.sql

-- 1. member_entity에 profile_image 컬럼 추가
ALTER TABLE member_entity
    ADD COLUMN profile_image BINARY(16);

-- 2. image_entity 테이블 생성
CREATE TABLE image_entity (
                              id BINARY(16) NOT NULL PRIMARY KEY,
                              image_path VARCHAR(255),
                              created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
