FROM openjdk:18
COPY ./target/DevOpsLab11-1.0-SNAPSHOT-jar-with-dependencies.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "DevOpsLab11-1.0-SNAPSHOT-jar-with-dependencies.jar"]