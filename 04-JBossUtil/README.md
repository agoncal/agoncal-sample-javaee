# Sample - Java EE - JBoss Util

## Purpose of this sample

The purpose of this sample is to show how you can use the JBoss administration API via REST. This helps in enabling or disabiling integration tests depending if JBoss is up and running or not. 

[Read more on my blog](http://antoniogoncalves.org/2013/07/03/monster-component-in-java-ee-7/)

## Compile and package

Being Maven centric, you can compile and package it with `mvn clean compile`, `mvn clean package` or `mvn clean install`. The `package` and `install` phase will automatically trigger the unit tests. Once you have your war file, you can deploy it.

## Deploy the sample

This sample has been tested with WildFly 8.1.0 as well as JBoss 6.2.0 EAP.

## Execute the sample

Once deployed go to [http://localhost:8080/sampleJavaEEMonster](http://localhost:8080/sampleJavaEEMonster) and you'll see a wab page where you can:

* Invoke the REST endpoint at `http://localhost:8080/sampleJavaEEMonster/rest/MonsterRest/TitleFromRest`
* Invoke the Servlet at `http://localhost:8080/sampleJavaEEMonster/MonsterServlet?title=TitleFromServlet`

The purpose of this sample is to execute the integration test with and without JBoss up and running. So to execute it you can run `mvn test` :

* with JBoss running, the tests will be executed
* with JBoss down or the web application not deployed, the tests will be ignored

<div class="footer">
    <span class="footerTitle"><span class="uc">a</span>ntonio <span class="uc">g</span>oncalves</span>
</div>
