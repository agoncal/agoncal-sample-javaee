set ACCEPT_DEFAULTS true ;

@/* ================================ */;
@/* == Setting up the persistence == */;
@/* ================================ */;

@/* Setup JPA */;
persistence setup --provider ECLIPSELINK --container GLASSFISH_3 --named javaone2013PU ;

@/* ================================ */;
@/* == Setting up Bean Validation == */;
@/* ================================ */;

@/* Setup Bean Validation */;
validation setup ;

@/* ======================= */;
@/* == Creating Entities == */;
@/* ======================= */;

@/* Speaker */;
entity --named Speaker ;
field string --named firstname ;
field string --named surname ;
field string --named bio --length 2000 ;
field string --named twitter ;

@/* Constraints on Speaker */;
constraint NotNull --onProperty firstname ;
constraint NotNull --onProperty surname ;

@/* Talk */;
entity --named Talk ;
field string --named title ;
field string --named description  --length 2000 ;
field string --named room ;
field temporal --type DATE --named date ;
field oneToMany --named speakers --fieldType org.javaone.javaee7.model.Speaker.java ;

@/* Constraints on Talk */;
constraint NotNull --onProperty title ;
constraint NotNull --onProperty room ;

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

@/* Constraints on Book */;
constraint NotNull --onProperty isbn ;
constraint NotNull --onProperty title ;
constraint NotNull --onProperty author ;
constraint Size --min 10 --max 2000 --onProperty description ;
