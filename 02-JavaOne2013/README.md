# Sample - Java EE - Java One 2013

## Purpose of this sample

The application developped in this sample is used during a talk at Java One 2013

## Compile and package

Being Maven centric, you can compile and package it with `mvn clean compile`, `mvn clean package` or `mvn clean install`. The `package` and `install` phase will automatically trigger the unit tests. Once you have your war file, you can deploy it.

## Deploy the sample

This sample has been tested with GlassFish 4.0 in several modes :

* GlassFish runtime : [download GlassFish](http://glassfish.java.net/public/downloadsindex.html), install it, start GlassFish (typing `asadmin start-`domain) and once the application is packaged deploy it (using the admin console or the command line `asadmin deploy target/sampleJavaEEMonster.war`)
* GlassFish embedded : use the [GlassFish Maven Plugin](http://maven-glassfish-plugin.java.net/) by running `mvn clean package embedded-glassfish:run`

## Execute the sample

Once deployed go to [http://localhost:8080/sampleJavaEEMonster](http://localhost:8080/sampleJavaEEMonster) and you'll see a wab page where you can:

* Invoke the Monster component as an EJB
* Invoke the Monster component as a RESTful Web Service at `http://localhost:8080/sampleJavaEEMonster/rest/MonsterRest/TitleFromRest`
* Invoke the Monster component as a Servlet at `http://localhost:8080/sampleJavaEEMonster/MonsterServlet?title=TitleFromServlet`

The purpose of this sample is to execute unit and integration tests. So to execute it you can run :

* `mvn test` : this will execute the unit test `BookTest` with JAXB
* `mvn integration-test` : this will execute both integration tests `BookIT`

# JBoss Forge

I've used [JBoss Forge](http://forge.jboss.org/) to bootstrap the application. It allows me to quickly have some Java EE code and scaffold a UI that I then change. You can check the `generate.fsh` script.


<div class="footer">
    <span class="footerTitle"><span class="uc">a</span>ntonio <span class="uc">g</span>oncalves</span>
</div>
