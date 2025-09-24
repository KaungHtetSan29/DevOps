FROM mysql:latest

WORKDIR /tmp

# Copy all SQL and dump files into MySQL's init folder
COPY db/test_db/ /docker-entrypoint-initdb.d/

# Set root password properly
ENV MYSQL_ROOT_PASSWORD=example
