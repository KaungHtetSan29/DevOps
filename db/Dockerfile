FROM mysql:8.0
WORKDIR /tmp
COPY test_db/*.sql /docker-entrypoint-initdb.d/
COPY test_db/*.dump /tmp/
ENV MYSQL_ROOT_PASSWORD=example
