FROM java:openjdk-8-jdk

ADD target/topbooks-swarm.jar /opt/topbooks-swarm.jar

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/opt/topbooks-swarm.jar"]