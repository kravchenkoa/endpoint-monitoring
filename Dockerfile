FROM java:8
EXPOSE 8080

ADD target/endpoint-monitoring-0.0.1-SNAPSHOT.jar endpoint-monitoring.jar
ENTRYPOINT ["java","-jar","endpoint-monitoring.jar"]