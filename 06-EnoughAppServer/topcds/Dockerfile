FROM java:openjdk-8-jdk

ADD target/topcds-swarm.jar /opt/topcds-swarm.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/opt/topcds-swarm.jar"]