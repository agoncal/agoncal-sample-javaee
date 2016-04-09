# How Small is Small

An e-commerce web application that sells books and CDs and interacts with two external web services to get the top selling books and CDs. The idea is to deploy these 3 applications as web applications (WAR) but also as microservices (Fat JARs) and see how much footprint they have. 

## Web Apps

In this configuration the 3 applications are packaged in war files and deployed in an application server. You can obtain these 3 war files with a `mvn install` command.

### CD Book Store

A full Java EE 7 web application that uses most of the Java EE 7 specifications (CDI, EJB, JTA, JPA, Bean Validation, REST, JSF, JAX-RS, JSON-P...) as well as BootStrap, PrimeFaces, JQuery. 

http://localhost:8080/cdookstore

### Top Books

A REST interface that returns the Top 3 selling books. It uses a small set of Java EE 7 specification (JAX-RS, JPA and CDI)

http://localhost:8080/topbooks

### Top CDs

A web interface that returns the Top 3 selling CDs. It doesn't use any Java EE specification except servlet, everything is made by end (eg. JSon manually created with no external framework)

http://localhost:8080/topcds

## Microservices

In this configuration the 3 applications are packaged as fat executable jars (embedding the application server). You can obtain these 3 war files with a `mvn install -Pswarm` command.


## Docker

http://fabric8io.github.io/docker-maven-plugin/
