@/* ======================== */;
@/* == JBoss Forge Script == */;
@/* ======================== */;


@/* ========================================= */;
@/* == Creating and setting up the project == */;
@/* ========================================= */;

set ACCEPT_DEFAULTS true ;

new-project --named horizontal --topLevelPackage org.agoncal.sample.javaee.tierarchitecture.horizontal --type war --finalName sampleJavaEEHorizontal

@/* Setup JPA */;
persistence setup --provider ECLIPSELINK --container GLASSFISH_3 --named sampleJavaEEHorizontalPU ;

@/* Setup Bean Validation */;
validation setup ;

@/* Setup REST */;
rest setup ;


@/* ======================= */;
@/* == Creating Entities == */;
@/* ======================= */;

@/* Author */;
entity --named Author ;
field string --named firstname ;
field string --named surname ;
field string --named bio --length 2000 ;
field string --named twitter ;

@/* Constraints on Author */;
constraint NotNull --onProperty firstname ;
constraint NotNull --onProperty surname ;
constraint Size --max 2000 --onProperty bio ;

@/* Language */;
java new-enum-type --named Language --package org.agoncal.sample.javaee.tierarchitecture.horizontal.model ;
java new-enum-const ENGLISH ;
java new-enum-const FRENCH ;

@/* Book */;
entity --named Book ;
field string --named isbn ;
field string --named title ;
field string --named description --length 2000 ;
field temporal --type DATE --named publicationDate ;
field number --type java.lang.Float --named price ;
field number --type java.lang.Integer --named nbOfPages ;
field string --named publisher ;
field custom --named language --type org.agoncal.sample.javaee.tierarchitecture.horizontal.model.Language.java ;
field string --named imageURL ;
field string --named pageURL ;
field oneToMany --named authors --fieldType org.agoncal.sample.javaee.tierarchitecture.horizontal.model.Author.java ;

@/* Constraints on Book */;
constraint NotNull --onProperty isbn ;
constraint NotNull --onProperty title ;
constraint Size --max 2000 --onProperty description ;


@/* ============================= */;
@/* == Generate REST endpoints == */;
@/* ============================= */;

rest endpoint-from-entity ~.model.* ;


@/* ========================================== */;
@/* == Generate JSF backing beans and pages == */;
@/* ========================================== */;

scaffold setup --scaffoldType faces ;
scaffold from-entity ~.model.* ;


@/* ====================================== */;
@/* == From Java EE 6 to 7 Dependencies == */;
@/* ====================================== */;

project remove-dependency org.hibernate.javax.persistence:hibernate-jpa-2.0-api ;
project remove-dependency javax.validation:validation-api ;
project remove-dependency org.hibernate:hibernate-validator ;
project remove-dependency javax.enterprise:cdi-api ;
project remove-dependency org.jboss.spec.javax.annotation:jboss-annotations-api_1.1_spec ;
project remove-dependency org.jboss.spec.javax.ws.rs:jboss-jaxrs-api_1.1_spec ;
project remove-dependency org.jboss.spec.javax.transaction:jboss-transaction-api_1.1_spec ;
project remove-dependency org.jboss.spec.javax.ejb:jboss-ejb-api_3.1_spec ;
project remove-dependency org.jboss.spec.javax.faces:jboss-jsf-api_2.1_spec ;
project remove-dependency org.jboss.spec.javax.servlet:jboss-servlet-api_3.0_spec ;

project add-dependency javax:javaee-api:7.0:provided ;
