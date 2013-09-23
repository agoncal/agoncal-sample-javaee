# Getting ready

* Setup the timer on Android phone
* Export variable export `J1` which points to the code directory (eg. `J1=$CODE_HOME/JavaOne`)
* Start Intellij IDEA (Cardea)
    * Clear list of Open Recent
    * Close all the Intellij Idea projects
* Switch off sound
* Command line (increase font with )
* TextWrangler
    * Clean up all the other tabs
    * Open `DEMO.md`
    * Open `entities.fsh`
    * Open `pom.fsh`
* Finder
	* Saved searched (DDL)
	* Leave one tab open
* Chrome close all tabs
* Make sure Derby is not up and running
* Reboot
* /!\ FOR JBOSS TO WORK /!\ get rid of the duplicates in the insert.sql file
* /!\ DO NOT REFORMAT CODE /!\ or you might get WildcardImportResolver

## Create the Main script project

* Open a NEW terminal (ALT+T)
	* `cd $J1`
	* Forge
* Create project
    * `new-project --named javaone-javaee7-main --topLevelPackage org.javaone.javaee7.main`
      `--createMain`
* Import project in Intellij Idea
    * Make sure to use Java SE 7
    * Increase Intellij Font ( Editor / Colors & Fonts / Font / Presentation)
* Add Jersey dependency
    * `project add-dependency org.glassfish.jersey.core:jersey-client:2.0`

## Before the demo

* Command line
    * cd `$J1` (to run commands)
        * `cp /Users/antoniombp/Documents/Code/github/agoncal-sample-javaee/02-JavaOne2013/script/src/main/shell/setup.sh .`
        * `cp /Users/antoniombp/Documents/Code/github/agoncal-sample-javaee/02-JavaOne2013/script/src/main/shell/clean.sh .`
        * `./clean.sh`
        * `./setup.sh`
    * cd `$J1` (to run Forge) Set tab colour to Orange
    * Start Derby database (`derbyStart.sh`) after the `clean.sh` or it breaks after
    * `clear` all terminals

# Start

* Start Forge and show some commands
    * TAB, `version`, `list-commands`, `list-commands --all`
* Create project
	* `new-project --named javaone-javaee7 --topLevelPackage org.javaone.javaee7 --type war `
       `--finalName javaone2013`

* Start Cardea (Intellij Idea 13) and create a project from `/Users/antoniombp/Documents/Code/JavaOne/javaone-javaee7`
    * Make sure to use Java SE 7
    * Increase Intellij Font ( Editor / Colors & Fonts / Font / Presentation)
* Show project
    * Show `pom.xml` and empty project structure

# Create all the entities with bean valivation

* In TextWrangler show the script `entities.fsh`
* In Forge run the script `run ../entities.fsh`
* Show code in IntelliJ

# Generate REST endpoints

* Setup REST
    * `rest setup`
* Show `web.xml` in Intellij
* Generate REST endpoints
    * `rest endpoint-from-entity ~.model.*` (`~` is alt+n)
* Show the REST endpoints in Intellij

# Use REST the Book endpoint

* Setup GlassFish (in exploded mode)
* Run GlassFish
* In browser go to `http://localhost:8080/javaone2013/rest/books`
* In PostMan 
	* Zoom in with CTRL+2 fingers pad
	* Launch `Get all books`, `Create one book`

# Generate JSF backing beans and pages

* Setup JSF
    * `scaffold setup --scaffoldType faces`
    * `scaffold from-entity ~.model.*`
* Show code in IntelliJ
* Redeploy app in GlassFish

# Turn Java EE 6 project into Java EE 7

## Changing dependencies in pom

* In TextWrangler show the `pom.fsh` script
* Go to the root of the project `cd ~~` (alt+n)
* In Forge run the script `run ../pom.fsh`
* Remove JBoss Java EE 6 dependencies in `pom.xml` by hand

## Changing namespaces and version of XML deployment descriptors

* In Intellij Idea Show all the XML file (CMD+SHIFT+O)
* Show `persistence.xml`, `web.xml` and a JSF page
* Run the `./namespaces.sh` shell script

> SNAPSHOT-1 (15 min)

# Adding data to the database

## Accessing Amazon Web Service with JAX-RS 2.0

* Show Chrome and Amazon WS
* Show JavaEE7-Main project
* Go to `Main` class
* In the `Main`, add the first lines of code with the Intellij IDEA shortcut `j1aws`
* Add the following LoC explaining the JAX-RS 2.0 APIs and what it does :

        Client client = ClientBuilder.newClient();
        Response response = client.target(uriAmazon).request().get();
        String javaee7books = response.readEntity(String.class);

        System.out.println(response.getStatus());
        System.out.println(javaee7books);

* Execute the `Main` (CTRL+SHIFT+R)
* Say that with a bit of XML parsing you can obtain the following SQL file
* Exit Intellij IDEA, Forge and close terminal

# Adding Data to the DB with JPA 2.1

* Execute the shell `sql.sh`
* Open the `persistence.xml` file
* Get read of `<provider>` so it's portable
* Get read of `<jta-data-source>` because it falls into default DS `java:comp/DefaultDataSource`
* Add the properties with the shortcut `j1per`
* Redeploy app into GlassFish
* Search for the file `createJavaOne2013.ddl` (in the finder click on `DDL.search`) and show it

> SNAPSHOT-2 (25 min)

# Change @Stateless to @Transactional in REST endpoints

* Change `@Stateless` with `@Transactional`

# Adding Bean Validation method validation

* In the REST endpoint add a `@NotNull` to the methods that have `Long id)` as a parameter
* In the backing beans add a `@NotNull` to the `findById` methods

# Add the new JSF 2.2 `viewAction`

* In all the `.xhtml` file, replace
	*  `<f:event type="preRenderView" listener=` 
	* with `<f:viewAction action=`

# Beaufity the Book pages with RichFaces components

* Talk about PrimeFaces
* Show the PrimeFaces website (ADD THE WEB SITE TO CHROME FAVORITES)
* Add a Calendar to the Book creation page `create.xhtml`
    * Ine the `PublicationDate` just replace the `<h:inputText>` with `<p:calendar>`

		<h:inputText id="bookBeanBookPublicationDate" value="#{bookBean.book.publicationDate}">
			<f:convertDateTime type="date"/>
		</h:inputText>

* In the Search page (`search.xhtml`) change the Datatable with a PrimeFaces caroussel.
    * Get rid of the `<h:dataTable>` and the `<ui:include>`and replace it with shortcut `j1prime`

         <p:carousel id="carousel" value="#{bookBean.pageItems}" var="_item" style="width:100%" headerText="Java EE 7 Books">
           <h:panelGrid columns="1" style="width:100%" cellpadding="5">
             <h:link outcome="/book/view">
               <f:param name="id" value="#{_item.id}"/>
               <p:graphicImage id="image" value="#{_item.imageURL}" title="#{_item.title}"/>
             </h:link>
           </h:panelGrid>
         </p:carousel>
         <p:dialog/>

> SNAPSHOT-3 (35 min)

# Adding video upload

* In the `TalkBean` JSF backing bean
    * Add the attribute `private javax.servlet.http.Part uploadedVideo;`
    * Create the getters and setters (CMD+N) of the `uploadedVideo` attribute
    * Add an the following method using the shortcut `j1upload`

        public String uploadVideo() throws IOException {
            InputStream is = uploadedVideo.getInputStream();
            Files.copy(is, Paths.get("/Users/antoniombp/Documents/Code/JavaOne/javaone-javaee7/target/javaone2013/resources" + talk.getId() + ".mp4"));
            return "view?faces-redirect=true";
        }

* In the `create.xhtml` file of the Talk
* At the very end, between the `</h:form>` and the `</ui:define>` add the form with `j1form`

        <h:form enctype="multipart/form-data">
            <h:inputFile value="#{talkBean.uploadedVideo}"/>
            <h:commandLink value="Upload video" action="#{talkBean.uploadVideo}" styleClass="btn btn-primary"/>
        </h:form>

* In the `view.xhtml` page of the Talk
    * Add the following, between `<h:outputText/>` and `</h:panelGrid>` before the buttons with `j1video`

            <h:outputLabel value="Video:"/>
            <video src="#{request.contextPath}/resources/#{talkBean.talk.id}.mp4" width="500px" controls="controls"/>
            <h:outputText/>

* Redeploy app on GlassFish
* Choose one talk, upload a video from the `Movies` folder, and upload it
* Show the video

> SNAPSHOT-4 (45 min)

# Beautifying home page

## Little improvements

* Go to the `pageTemplate.xhtml` page
* Get rid of the `'forge-logo.png'`
* Change the name of the `brand` from `Javaone-javaee 7` `Java EE 7 at JavaOne`
* Get rid of the `Customize` by deleting the `<div class="nav-collapse collapse">`

## Tweeter

* Show Twitter widget https://twitter.com/settings/widgets
* At the bottom of `<div id="navigation">` before the `</div>` copy the Twitter URL :

            <a class="twitter-timeline" href="https://twitter.com/search?q=%23JavaEE7" data-widget-id="379317091746078720">Tweets about "#JavaEE7"</a>
            <script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':'https';if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+"://platform.twitter.com/widgets.js";fjs.parentNode.insertBefore(js,fjs);}}(document,"script","twitter-wjs");</script>


## Duke Java EE 7

* Go to the `index.xhtml` page
* Chand the header text from `Welcome to Forge` to `Welcome to JavaOne 2013`
* In the `<h2 class="success">` change the text `Your application is running.` with `Come and Play! with Java EE 7`
* In the `<p>` block, change the content with `j1duke` shortcut :

        <p align="center">
            <h:graphicImage value="#{resource['duke-javaee7.png']}" width="300px"/>
        </p>


> SNAPSHOT-5

# Use WildFly

* In the `persistence.xml` use the JBoss datasource

        <jta-data-source>java:jboss/datasources/ExampleDS</jta-data-source>


# It is possible to develop quickly nice and portable Java EE 7 apps



















