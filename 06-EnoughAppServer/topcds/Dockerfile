FROM openjdk:8-jre-alpine

EXPOSE 8081

ADD target/topcds-swarm.jar /opt/topcds-swarm.jar
ENTRYPOINT ["java", "-jar", "/opt/topcds-swarm.jar"]