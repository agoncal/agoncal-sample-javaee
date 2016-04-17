FROM java:openjdk-8-jdk

EXPOSE 8082

ADD target/topbooks-swarm.jar /opt/topbooks-swarm.jar
ENTRYPOINT ["java", "-jar", "/opt/topbooks-swarm.jar"]