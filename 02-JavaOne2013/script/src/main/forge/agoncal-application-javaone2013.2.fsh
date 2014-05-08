@/* =================== */ ;
@/* ==   Forge 2.x   == */ ;
@/* =================== */ ;
@/* Generates the draft of the application */ ;


clear ;
export ACCEPT_DEFAULTS = true ;
transaction-track-changes ;


@/* ========================== */;
@/* == Creating the project == */;
@/* ========================== */;

project-new --named javaone-javaee7 --topLevelPackage org.javaone.javaee7 --type war --finalName javaone2013 ;
@/* =/!\/!\/!\/!\ === --createMain  */;


@/* ================================ */;
@/* == Setting up the persistence == */;
@/* ================================ */;

@/* Setup JPA */;
jpa-setup --persistenceUnitName javaone2013PU ;


@/* ====================================== */;
@/* == Creating Speaker & Talk Entities == */;
@/* ====================================== */;

@/* Speaker */;
jpa-new-entity --named Speaker ;
jpa-new-field --named firstname ;
jpa-new-field --named surname ;
jpa-new-field --named bio --length 2000 ;
jpa-new-field --named twitter ;

@/* Talk */;
jpa-new-entity --named Talk ;
jpa-new-field --named title ;
jpa-new-field --named description --length 2000 ;
jpa-new-field --named room ;
jpa-new-field --named date --typeName java.util.Date --temporalType DATE ;
jpa-new-field --named speakers --entity org.javaone.javaee7.model.Speaker --relationshipType One-to-Many ;


@/* ================================ */;
@/* == Setting up Bean Validation == */;
@/* ================================ */;

@/* Setup Bean Validation */;
validation-setup ;
@/* =/!\/!\/!\/!\ === should be constraint-setup  */;


@/* ========================================== */;
@/* == Adding constraints on Speaker & Talk == */;
@/* ========================================== */;

@/* Constraints on Talk */;
constraint-add --onProperty title --constraint NotNull ;
constraint-add --onProperty room --constraint NotNull ;

cd ../Speaker.java ;
constraint-add --onProperty firstname --constraint NotNull ;
constraint-add --onProperty surname --constraint NotNull ;


@/* ========================================== */;
@/* == Creating Language enum & Talk Entity == */;
@/* ========================================== */;

@/* Language */;
java-new-enum --named Language --packageName org.javaone.javaee7.model ;
java-new-enum-const ENGLISH ;
java-new-enum-const FRENCH ;

@/* Book */;
jpa-new-entity --named Book ;
jpa-new-field --named isbn ;
jpa-new-field --named title ;
jpa-new-field --named author ;
jpa-new-field --named description --length 2000 ;
jpa-new-field --named price --typeName java.lang.Float ;
jpa-new-field --named nbOfPages --typeName java.lang.Integer ;
jpa-new-field --named publisher ;
jpa-new-field --named publicationDate --typeName java.util.Date --temporalType DATE ;
jpa-new-field --named language --typeName org.javaone.javaee7.model.Language ;
jpa-new-field --named imageURL ;
jpa-new-field --named pageURL ;

constraint-add --onProperty isbn --constraint NotNull ;
constraint-add --onProperty title --constraint NotNull ;
constraint-add --onProperty author --constraint NotNull ;
constraint-add --onProperty description --constraint Size --min 10 --max 2000 ;


@/* ===================== */;
@/* == Setting up REST == */;
@/* ===================== */;

@/* Setup JAX-RS */;
rest-setup ;


@/* ============================= */;
@/* == Creating REST endpoints == */;
@/* ============================= */;

@/* Generate CRUD endpoints for all the @Entities */;
@/* =/!\/!\/!\/!\ === cannot use *  ~.model.*   */;
rest-endpoint-from-entity --targets org.javaone.javaee7.model.Book org.javaone.javaee7.model.Speaker  org.javaone.javaee7.model.Talk


@/* ==================== */;
@/* == Setting up JSF == */;
@/* ==================== */;

@/* Turn our Java project into a Web project with JSF, CDI, EJB, and JPA */;
scaffold-setup --provider Faces ;

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
