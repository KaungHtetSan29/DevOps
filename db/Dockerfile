# Use the latest MySQL 8.0 image
FROM mysql:8.0

# Set the working directory
WORKDIR /tmp

# Copy all SQL and dump files to /tmp
COPY test_db/*.sql /tmp/
COPY test_db/*.dump /tmp/

# Copy the main SQL file to docker-entrypoint-initdb.d
# Scripts in this folder are executed on container startup
COPY test_db/employees.sql /docker-entrypoint-initdb.d/

# Copy all dump files to docker-entrypoint-initdb.d as well
COPY test_db/*.dump /docker-entrypoint-initdb.d/

# Set the root password
ENV MYSQL_ROOT_PASSWORD=example
