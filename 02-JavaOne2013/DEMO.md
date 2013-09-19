# Pensez à commiter les shell dans scripts

# Getting ready

* Export variable export `J1` which points to the code directory (eg. `J1=$CODE_HOME/JavaOne`)
* Intellij IDEA (Cardea)
* Command line (increase font)
* Saved searched
* /!\ DO NOT REFORMAT CODE /!\ or you might get WildcardImportResolver

# Before the demo

* Command line
    * cd `$J1` (to run commands)
        * `./clean.sh`
        * `./setup.sh`
    * cd `$J1` (to run Forge)
    * Start Derby database (`derbyStart.sh`)

# Start

* Start Forge and show some commands
    * TAB, `version`, `list-commands`, `list-commands --all`
* Create project
    * `new-project --named javaone-javaee7 --topLevelPackage org.javaone.javaee7 --type war --createMain --finalName javaone2013`
* Start Cardea (Intellij Idea 13) and create a project
    * Make sure to use Java SE 7
    * Increase Intellij Font ( Editor / Colors & Fonts / Font / Presentation)
* Show project
    * Show `pom.xml` and empty project structure

# Create Speaker and Talk entities

* Setup persistence
    * `persistence setup --provider ECLIPSELINK --container GLASSFISH_3 --named javaone2013PU`
* Create Speaker entity

Speaker

    entity --named Speaker ;
    field string --named firstname ;
    field string --named surname ;
    field string --named bio --length 2000 ;
    field string --named twitter ;

* Do a `ls` on Forge
* Show code in IntelliJ
* Create Talk entity

Talk

    entity --named Talk ;
    field string --named title ;
    field string --named description  --length 2000 ;
    field string --named room ;
    field temporal --type DATE --named date ;
    field oneToMany --named speakers --fieldType org.javaone.javaee7.model.Speaker.java ;

* Show code in IntelliJ

# Add Bean Validation constraints

* Setup Bean Validation
    * `validation setup`
* Adding constraints to Talk

Talk

    constraint NotNull --onProperty title ;
    constraint NotNull --onProperty room ;

* Adding constraints to Speaker

Speaker

    cd ../Speaker.java ;
    constraint NotNull --onProperty firstname ;
    constraint NotNull --onProperty surname ;


* Show Bean Validation code in IntelliJ
* Build the code with Forge `build`
* Say we are using a script to generate the Book and Language entity `run 01-entities.fsh`
* Show code in IntelliJ

# Generate REST endpoints

* Setup REST
    * `rest setup`
* Show `web.xml` in Intellij
* Generate REST endpoints
    * `rest endpoint-from-entity ~.model.*`

# Use REST endpoints

* Setup GlassFish (in exploded mode)
* Run GlassFish
* In browser go to `http://localhost:8080/javaone2013/rest/books`
* In PostMan launch `Get all books`, `Create one book`
* Stop GlassFish

# Generate JSF backing beans and pages

* Setup JSF
    * `scaffold setup --scaffoldType faces`
    * `scaffold from-entity ~.model.*`
* Show code in IntelliJ
* Run GlassFish

# Turn Java EE 6 project into Java EE 7

* Remove Java EE 6 dependencies in `pom.xml` and add new Java EE 7 dependencies
* Replace `http://java.sun.com/xml/ns/` into `http://xmlns.jcp.org/xml/ns/` in all `*.xml` files (`./namespaces.sh`)
* Change to default DS in `persistence.xml` : `<jta-data-source>java:comp/DefaultDataSource</jta-data-source>`
* Get read of `<provider>`
* Change property to `<property name="javax.persistence.schema-generation.database.action" value="drop-and-create"/>`

* Setup JRebel in Intellij Idea


# Use WildFly

# Tweeter

* https://twitter.com/settings/widgets

# References

* https://oracleus.activeevents.com/2013/connect/focusOnDoc.do?focusID=23084



















