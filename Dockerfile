FROM openjdk:18
COPY ./target/DevOps-0.1.0.4-jar-with-dependencies.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "DevOps-0.1.0.4-jar-with-dependencies.jar"]