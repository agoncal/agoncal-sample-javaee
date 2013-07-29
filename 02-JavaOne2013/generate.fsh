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
persistence setup --provider ECLIPSELINK --container GLASSFISH_3 --named petclinicPU ;

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

@/* PetType */;
entity --named PetType ;
field string --named name ;

@/* Specialty */;
entity --named Specialty ;
field string --named name ;

@/* Vet */;
entity --named Vet ;
field string --named firstName ;
constraint NotNull --onProperty firstName ;
field string --named lastName ;
constraint NotNull --onProperty lastName ;
field manyToMany --named specialties --fieldType ~.model.Specialty.java ;

@/* Pet */;
entity --named Pet ;
field string --named name ;
field temporal --type DATE --named birthDate ;
constraint Past --onProperty birthDate ;
field manyToOne --named type --fieldType ~.model.PetType.java ;

@/* Owner */;
entity --named Owner ;
field string --named firstName ;
constraint NotNull --onProperty firstName ;
field string --named lastName ;
constraint NotNull --onProperty lastName ;
field string --named address ;
constraint NotNull --onProperty address ;
field string --named city ;
constraint NotNull --onProperty city ;
field string --named telephone ;
constraint NotNull --onProperty telephone ;
constraint Digits --onProperty telephone --integer 10 --fraction 0 ;
field oneToMany --named pets --fieldType ~.model.Pet.java ;

@/* Visit */;
entity --named Visit ;
field temporal --type DATE --named date ;
constraint Future --onProperty date ;
field string --named description ;
constraint NotNull --onProperty description ;
field manyToOne --named pet --fieldType ~.model.Pet.java ;

@/* Adding relationships to Pet */;
cd ../Pet.java ;
field manyToOne --named owner --fieldType ~.model.Owner.java ;
field oneToMany --named visits --fieldType ~.model.Visit.java ;

@/* Create some beans */;
@/* Vets */;
java new-class --named Vets --package ~.model ;
java new-field  'private List<Vet> vets' ;
java new-method '@XmlElement public List<Vet> getVetList() {if (vets == null) {vets = new ArrayList<Vet>();}return vets;}' ;


@/* =========================== */;
@/* == Building JPA Entities == */;
@/* =========================== */;

echo You need to manually import the classes to Vets. Go to your IDE and fix it before pressing enter ;
wait ;
wait ;
build --notest ;


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

@/* ============================= */;
@/* == Running the application == */;
@/* ============================= */;

@/* There is a bug either in Forge or in GlassFish that doesn't register the JAX-RS Application servlet */;
@/* You need to manually add the following to the web.xml */;
@/* <servlet> */;
@/*   <servlet-name>javax.ws.rs.core.Application</servlet-name> */;
@/*   <load-on-startup>1</load-on-startup> */;
@/* </servlet> */;
