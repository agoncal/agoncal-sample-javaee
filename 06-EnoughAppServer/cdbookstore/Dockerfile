FROM java:openjdk-8-jdk

ADD target/cdbookstore-swarm.jar /opt/cdbookstore-swarm.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/opt/cdbookstore-swarm.jar"]