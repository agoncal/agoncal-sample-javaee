FROM openjdk:8-jre-alpine

EXPOSE 8082

ADD target/topbooks-swarm.jar /opt/topbooks-swarm.jar
ENTRYPOINT ["java", "-jar", "/opt/topbooks-swarm.jar"]