-- V4__add_profile_image_to_member_entity.sql

ALTER TABLE member_entity
    ADD COLUMN profile_image VARCHAR(255) NULL;

