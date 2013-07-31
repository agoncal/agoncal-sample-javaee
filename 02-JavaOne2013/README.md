# Sample - Java EE - Java One 2013

## Purpose of this sample

The application developped in this sample is used during a talk at Java One 2013. It shows new functionalities of Java EE 7 in action :
* JPA: Schema Generation properties
* JTA: @Transactional
* Bean Validation: Method parameter and return value on POJOs
* Default resources: DataSource
* JSF: Pass-through Attributes
* JSF: h:inputFile
* JAX-RS: Client API
* JSON-P: JsonParser and JsonGenerator
* Batch
 
It uses the external tools/librairies
* JBoss Forge
* Twitter Bootstrap

## Compile and package

Being Maven centric, you can compile and package it with `mvn clean compile`, `mvn clean package` or `mvn clean install`. The `package` and `install` phase will automatically trigger the unit tests. Once you have your war file, you can deploy it.

## Deploy the sample

This sample has been tested with GlassFish 4.0 in several modes :

* GlassFish runtime : [download GlassFish](http://glassfish.java.net/public/downloadsindex.html), install it, start GlassFish (typing `asadmin start-`domain) and once the application is packaged deploy it (using the admin console or the command line `asadmin deploy target/sampleJavaEEMonster.war`)
* GlassFish embedded : use the [GlassFish Maven Plugin](http://maven-glassfish-plugin.java.net/) by running `mvn clean package embedded-glassfish:run`

## Execute the sample

Once deployed go to [http://localhost:8080/sampleJavaOne2013](http://localhost:8080/sampleJavaOne2013) and you'll see a wab page where you can:

* Check the Java EE 7 Books
* Get realtime Tweets
* Check the Java EE 7 talks

# JBoss Forge

I've used [JBoss Forge](http://forge.jboss.org/) to bootstrap the application. It allows me to quickly have some Java EE code and scaffold a UI that I then change. You can check the `agoncal-application-javaone2013.fsh` script. Execute it by entering the following commands :
`forge`
`run agoncal-application-javaone2013.fsh`

There is a bug either in Forge or in GlassFish that doesn't register the JAX-RS Application servlet. You need to manually add the following to the web.xml :
 <servlet>
  <servlet-name>javax.ws.rs.core.Application</servlet-name>
  <load-on-startup>1</load-on-startup>
 </servlet>

<div class="footer">
    <span class="footerTitle"><span class="uc">a</span>ntonio <span class="uc">g</span>oncalves</span>
</div>
