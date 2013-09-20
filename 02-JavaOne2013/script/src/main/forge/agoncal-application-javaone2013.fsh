@/* Generates the draft of the application */ ;
@/* ================= */ ;
@/* ==   Plugins   == */ ;
@/* ================= */ ;
@/* If the following plugins are not installed */ ;
@/* forge install-plugin arquillian */ ;
@/* forge install-plugin jrebel */ ;
@/* forge install-plugin primefaces */ ;


clear ;
set ACCEPT_DEFAULTS true ;


@/* ========================== */;
@/* == Creating the project == */;
@/* ========================== */;

new-project --named javaone-javaee7 --topLevelPackage org.javaone.javaee7 --type war --createMain --finalName javaone2013 ;


@/* ================================ */;
@/* == Setting up the persistence == */;
@/* ================================ */;

@/* Setup JPA */;
persistence setup --provider ECLIPSELINK --container GLASSFISH_3 --named javaone2013PU ;


@/* ====================================== */;
@/* == Creating Speaker & Talk Entities == */;
@/* ====================================== */;

@/* Speaker */;
entity --named Speaker ;
field string --named firstname ;
field string --named surname ;
field string --named bio --length 2000 ;
field string --named twitter ;

@/* Talk */;
entity --named Talk ;
field string --named title ;
field string --named description --length 2000 ;
field string --named room ;
field temporal --type DATE --named date ;
field oneToMany --named speakers --fieldType org.javaone.javaee7.model.Speaker.java ;


@/* ================================ */;
@/* == Setting up Bean Validation == */;
@/* ================================ */;

@/* Setup Bean Validation */;
validation setup ;


@/* ========================================== */;
@/* == Adding constraints on Speaker & Talk == */;
@/* ========================================== */;

@/* Constraints on Talk */;
constraint NotNull --onProperty title ;
constraint NotNull --onProperty room ;

cd ../Speaker.java ;
constraint NotNull --onProperty firstname ;
constraint NotNull --onProperty surname ;


@/* ========================================== */;
@/* == Creating Language enum & Talk Entity == */;
@/* ========================================== */;

@/* Language */;
java new-enum-type --named Language --package org.javaone.javaee7.model ;
java new-enum-const ENGLISH ;
java new-enum-const FRENCH ;

@/* Book */;
entity --named Book ;
field string --named isbn ;
field string --named title ;
field string --named author ;
field string --named description --length 2000 ;
field number --type java.lang.Float --named price ;
field number --type java.lang.Integer --named nbOfPages ;
field string --named publisher ;
field temporal --type DATE --named publicationDate ;
field custom --named language --type org.javaone.javaee7.model.Language.java ;
field string --named imageURL ;
field string --named pageURL ;

constraint NotNull --onProperty isbn ;
constraint NotNull --onProperty title ;
constraint NotNull --onProperty author ;
constraint Size --min 10 --max 2000 --onProperty description ;


@/* ===================== */;
@/* == Setting up REST == */;
@/* ===================== */;

@/* Setup JAX-RS */;
rest setup;


@/* ============================= */;
@/* == Creating REST endpoints == */;
@/* ============================= */;

@/* Generate CRUD endpoints for all the @Entities */;
rest endpoint-from-entity ~.model.* ;


@/* ==================== */;
@/* == Setting up JSF == */;
@/* ==================== */;

@/* Turn our Java project into a Web project with JSF, CDI, EJB, and JPA */;
scaffold setup --scaffoldType faces ;

@/* Generate the UI for all the @Entities */ ;
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
project add-dependency org.webjars:bootstrap:2.3.2 ;
project add-dependency org.primefaces:primefaces:3.5 ;
project add-dependency commons-fileupload:commons-fileupload:1.3 ;

project add-repository primefaces-repo http://repository.primefaces.org ;


@/* ========================== */;
@/* == Building the project == */;
@/* ========================== */;

build --notest ;


@/* =================================== */;
@/* == Returning to the project root == */;
@/* =================================== */;

set ACCEPT_DEFAULTS false ;
cd ~~ ;
