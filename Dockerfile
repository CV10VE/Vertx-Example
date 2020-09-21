FROM appinair/jdk11-maven
COPY target/Vertx-Examples-1.0-SNAPSHOT.jar /barrings.jar
CMD ["java","-jar","/barrings.jar"]