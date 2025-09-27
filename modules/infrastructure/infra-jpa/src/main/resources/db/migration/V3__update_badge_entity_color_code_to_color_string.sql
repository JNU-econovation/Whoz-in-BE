-- V3__rename_color_code_to_color_string.sql
ALTER TABLE badge_entity
    CHANGE COLUMN color_code color_string VARCHAR(255);
