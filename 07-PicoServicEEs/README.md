# Pico ServicEEs

The idea of this sample is to package the simplest web application (WAR) as a microservice (Fat JARs) using different frameworks and see how much footprint they use. 

The web app is a servlet that returns the Top 3 selling CDs. It doesn't use any Java EE specification except servlet, everything is made by end (eg. JSon manually created with no external framework)

http://localhost:8080/topcds

### Wildfly Swarm 

`mvn clean install`
`mvn wildfly-swarm:run`


### KumuluzEE

`mvn clean install -Pkumuluz`
`mvn kumuluzee:run -Pkumuluz`