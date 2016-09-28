FROM openjdk:8-jre-alpine

EXPOSE 8080

ADD target/cdbookstore-swarm.jar /opt/cdbookstore-swarm.jar
ENTRYPOINT ["java", "-jar", "/opt/cdbookstore-swarm.jar"]