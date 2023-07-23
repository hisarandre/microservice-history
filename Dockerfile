FROM openjdk:17-jdk
COPY target/history-0.0.1-SNAPSHOT.jar history-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/history-0.0.1-SNAPSHOT.jar"]