@/* Generates the draft of the application */;

clear ;
set ACCEPT_DEFAULTS true ;

@/* ========================== */;
@/* == Creating the project == */;
@/* ========================== */;

new-project --named agoncal-application-javaone2013 --topLevelPackage org.agoncal.application.javaone2013 --type war ;


@/* =========================== */;
@/* == Setting up the project == */;
@/* =========================== */;

@/* Setup JPA */;
persistence setup --provider ECLIPSELINK --container GLASSFISH_3 --named javaone2013PU ;

@/* Setup Bean Validation */;
validation setup --provider HIBERNATE_VALIDATOR ;

@/* Setup CDI */;
beans setup ;

@/* Setup JAX-RS */;
rest setup;

@/* Turn our Java project into a Web project with JSF, CDI, EJB, and JPA */;
scaffold setup --scaffoldType faces ;


@/* =========================== */;
@/* == Creating JPA Entities == */;
@/* =========================== */;

@/* Language */;
java new-enum-type --named Language --package org.agoncal.application.javaone2013.model ;
java new-enum-const ENGLISH ;
java new-enum-const FRENCH ;

@/* Books */;
entity --named Book ;
field string --named isbn ;
field string --named title ;
field string --named author ;
field string --named description ;
field number --type java.lang.Float --named price ;
field number --type java.lang.Integer --named nbOfPage ;
field temporal --type DATE --named publicationDate ;
field string --named publisher ;
field custom --named Language --type org.agoncal.application.javaone2013.model.Language.java

constraint NotNull --onProperty isbn ;
constraint NotNull --onProperty title ;
constraint NotNull --onProperty author ;
constraint Size --min 10 --max 2000 --onProperty description ;

@/* Speakers */;
entity --named Speaker ;
field string --named name ;
field string --named surname ;
field string --named bio ;
field string --named twitter ;

constraint NotNull --onProperty name ;
constraint NotNull --onProperty surname ;

@/* Talks */;
entity --named Talk ;
field string --named title ;
field string --named description ;
field oneToMany --named speakers --fieldType org.agoncal.application.javaone2013.model.Speaker.java
field string --named room ;
field temporal --type DATE --named date

constraint NotNull --onProperty title ;
constraint NotNull --onProperty room ;

@/* Tweets */;

@/* ================= */;
@/* == Scaffolding == */;
@/* ================= */;

@/* Generate the UI for all the @Entities */;
scaffold from-entity ~.model.* ;

@/* Generate CRUD endpoints for all the @Entities */;
rest endpoint-from-entity ~.model.* ;


@/* ========================== */;
@/* == Building the project == */;
@/* ========================== */;

build --notest ;

@/* =================================== */;
@/* == Returning to the project root == */;
@/* =================================== */;

set ACCEPT_DEFAULTS false;
cd ~~;
