FROM debian:bullseye-slim

RUN apt-get update && apt-get install -y --no-install-recommends \
    bash \
    default-mysql-client \
    gzip \
    tar \
    coreutils \
    cron \
    gettext \
 && rm -rf /var/lib/apt/lists/*

RUN ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime && echo "Asia/Seoul" > /etc/timezone

WORKDIR /app
COPY backup-command-db.sh backup-main-api-data.sh cleanup-old-backups.sh crontab.template.txt entrypoint.sh ./
RUN chmod +x *.sh
CMD ["./entrypoint.sh"]
