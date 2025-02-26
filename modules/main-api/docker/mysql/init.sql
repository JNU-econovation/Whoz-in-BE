USE whozin;

# network-api 계정 생성 후 권한 부여
CREATE USER 'network-api'@'%' IDENTIFIED BY 'whozinforever';

GRANT USAGE ON whozin.* TO 'network-api'@'%';
GRANT ALL PRIVILEGES ON whozin.* TO 'network-api'@'%'; # TODO: jpa ddl_auto update 대신 flyway 사용하고, 테이블에 권한 주도록 변경 (ae7df4c 참고)

FLUSH PRIVILEGES;