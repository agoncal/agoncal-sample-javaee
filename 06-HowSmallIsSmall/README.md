# How Small is Small

An e-commerce web application that sells books and CDs and interacts with two external web services to get the top selling books and CDs. The idea is to deploy these 3 applications as web applications (WAR) but also as microservices (Fat JARs) and see how much footprint they have. 

## 01 Web Apps

In this module the 3 applications are packaged in war files and deployed in an application server

### CD Book Store

A full Java EE 7 web application that uses most of the Java EE 7 specifications (CDI, EJB, JTA, JPA, Bean Validation, REST, JSF, JAX-RS, JSON-P...) as well as BootStrap, PrimeFaces, JQuery. 

http://localhost:8080/hsisCDBookStore/faces/main.xhtml

### Top Books

A REST interface that returns the Top 3 selling books. It uses a small set of Java EE 7 specification (JAX-RS, JPA and CDI)

http://localhost:8080/hsisTopBooks/topbooks

### Top CDs

A web interface that returns the Top 3 selling CDs. It doesn't use any Java EE specification except servlet, everything is made by end (eg. JSon manually created with no external framework)

http://localhost:8080/hsisTopCDs/topcds

## 02 Microservices

### Wildfly Swarm 

mvn clean install -Pswarm
mvn wildfly-swarm:run -Pswarm

http://localhost:8080/topcds

### KumuluzEE

mvn clean install -Pkumuluz
mvn kumuluzee:run -Pkumuluz