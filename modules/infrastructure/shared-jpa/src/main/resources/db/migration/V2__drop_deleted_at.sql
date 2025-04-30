-- 기존엔 BaseEntity에 deleted_at을 넣어놨어서 삭제라는 개념이 없는 엔티티도 deleted_at을 가지고 있었음
-- 따라서 소프트 딜리트가 적용된 device와 badge_member를 제외하고 deleted_at을 없앤다.
ALTER TABLE member_entity DROP COLUMN deleted_at;
ALTER TABLE badge_entity DROP COLUMN deleted_at;
ALTER TABLE badge_member_entity DROP COLUMN deleted_at;
ALTER TABLE feedback_entity DROP COLUMN deleted_at;
ALTER TABLE device_info_entity DROP COLUMN deleted_at;
ALTER TABLE device_connection_entity DROP COLUMN deleted_at;
ALTER TABLE managed_log_entity DROP COLUMN deleted_at;
ALTER TABLE monitor_log_entity DROP COLUMN deleted_at;
