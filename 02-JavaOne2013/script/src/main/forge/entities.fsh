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
