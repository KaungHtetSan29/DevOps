FROM openjdk:latest

# Set working directory
WORKDIR /tmp

# Copy the fat JAR into the container
COPY ./target/Lab1-1.0-SNAPSHOT-jar-with-dependencies.jar /tmp/

# Run the JAR
ENTRYPOINT ["java", "-jar", "Lab1-1.0-SNAPSHOT-jar-with-dependencies.jar"]