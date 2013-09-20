# Télécharger la dernière version de Jersey
# Créer des script qui remplace tout par un snapshot

# Getting ready

* Export variable export `J1` which points to the code directory (eg. `J1=$CODE_HOME/JavaOne`)
* Intellij IDEA (Cardea)
* Swtich off sound
* Command line (increase font)
* Open Mou with the `DEMO.md`
* Saved searched
* /!\ DO NOT REFORMAT CODE /!\ or you might get WildcardImportResolver

# Before the demo

* Command line
    * Start Derby database (`derbyStart.sh`)
    * cd `$J1` (to run commands)
        * `/Users/antoniombp/Documents/Code/github/agoncal-sample-javaee/02-JavaOne2013/script/src/main/shell/setup.sh`
        * `./setup.sh`
        * `./clean.sh`
    * cd `$J1` (to run Forge) Set tab colour to Orange

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
* Stop GlassFish

> SNAPSHOT-1

# Turn Java EE 6 project into Java EE 7

* Run Forge script to update `pom.xml`: `run 02-pom.fsh`
* Remove JBoss Java EE 6 dependencies in `pom.xml` by hand
* Replace `http://java.sun.com/xml/ns/` into `http://xmlns.jcp.org/xml/ns/` in all `*.xml` files (`./namespaces.sh`)

# Adding data to the database

## Accessing Amazon Web Service with JAX-RS 2.0

* Show Chrome and Amazon WS
* Open a new terminal with Forge
* Create project
    * `new-project --named javaone-javaee7-main --topLevelPackage org.javaone.javaee7.main --createMain`
* Import project in Intellij Idea
    * Make sure to use Java SE 7
    * Increase Intellij Font ( Editor / Colors & Fonts / Font / Presentation)
* Show project
    * Show `Main` class
    * Show `pom.xml`
* Add Jersey dependency
    * `project add-dependency org.glassfish.jersey.core:jersey-client:2.0-rc1`
* Go to `Main` class
* In the `Main`, add the first lines of code with the Intellij IDEA shortcut `j1aws`
* Add the following LoC explaining the JAX-RS 2.0 APIs and what it does :

        Client client = ClientBuilder.newClient();
        Response response = client.target(uriAmazon).request().get();
        String javaee7books = response.readEntity(String.class);

        System.out.println(uriAmazon.toString());
        System.out.println(response.getStatus());
        System.out.println(javaee7books);

* Execute the `Main` and say that with a bit of XML parsing you can obtain the following SQL file
* Exit Forge and close terminal
* Execute the shell `sql.sh`

# Adding Data to the DB with JPA 2.1

* Open the `persistence.xml` file
* Get read of `<provider>` so it's portable
* Change to default DS to `<jta-data-source>java:comp/DefaultDataSource</jta-data-source>`
* Add the properties with the shortcut `j1per`
* Start GlassFish
* Search for the file `createJavaOne2013.ddl` (in the finder click on `DDL.search`) and show it

> SNAPSHOT-2

# Adding Bean Validation method validation

* In the REST endpoint add a `@NotNull` to the methods that have `Long id)` as a parameter
* In the backing beans add a `@NotNull` to the `findById` methods

# Add the new JSF 2.2 `viewAction`

* In all the `.xhtml` file, replace `<f:event type="preRenderView" listener=` with `<f:viewAction action=`

# Beaufity the Book pages with RichFaces components

* Add PrimeFaces to the project with Forge
* Add a Calendar to the book creation page `create.xhtml`. Replace the following code

		<h:inputText id="bookBeanBookPublicationDate" value="#{bookBean.book.publicationDate}">
			<f:convertDateTime type="date"/>
		</h:inputText>

With

        <p:calendar id="bookBeanBookPublicationDate" value="#{bookBean.book.publicationDate}">
		    <f:convertDateTime type="date"/>
		</p:calendar>

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

> SNAPSHOT-3

# Adding video upload

* In the `TalkBean` JSF backing bean
    * Add the attribute `private javax.servlet.http.Part uploadedVideo;`
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

* Start GlassFish
* Choose one talk, upload a video from the `Movies` folder, and upload it
* Show the video, it doesn't show it
* Restart GlassFish and show the video again

> SNAPSHOT-4

# Beautifying home page

## Tweeter

* Show Twitter widget https://twitter.com/settings/widgets
* Go to the `pageTemplate.xhtml` page
* At the bottom of `<div id="navigation">` before the `</div>` copy the Twitter URL :

            <a class="twitter-timeline" href="https://twitter.com/search?q=%23JavaEE7" data-widget-id="379317091746078720">Tweets about "#JavaEE7"</a>
            <script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':'https';if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+"://platform.twitter.com/widgets.js";fjs.parentNode.insertBefore(js,fjs);}}(document,"script","twitter-wjs");</script>


## Duke Java EE 7

* Go to the `index.xhtml` page
* In the `<h2 class="success">` change the text `Your application is running.` with `Come and Play! with Java EE 7`
* In the `<p>` block, change the content with :

        <p align="center">
            <h:graphicImage value="#{resource['duke-javaee7.png']}" width="300px"/>
        </p>


> SNAPSHOT-5

# Use WildFly



















