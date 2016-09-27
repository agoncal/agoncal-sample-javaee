# Just Enough App Server

## Getting ready

### Setup Raspberry Pi 

- Take all the connectic out and boot the Raspberri
- Then plug things in
- Change keyboard layout : `Menu -> Preferences -> Clavier et Souris -> Clavier -> Keyboard Laout`
- `sudo apt-get update` and `sudo apt-get dist-upgrade`
- Open 3 terminal in the `~/Download` directory and `java -jar` the 3 services

### Setup environment 

- Check JDK / Wildfly / Intellij / Chrome / Terminal / Raspberry Pi
- DEMO variable `set -x DEMO $CODE_HOME/Agoncal/agoncal-sample-javaee/06-EnoughAppServer`
- Increase fonts on Intellij / Chrome / Terminal
- In Intellij IDEA open the project `$DEMO`
- Intellij in _Presentation_ mode _Editor / General / Colors and Fonts_ and _Console Font_ to 22

### Deploy the 3 web apps to WildFly

- Start WildFly `$Tools/JBoss/wildfly-10.1.0.Final/bin` and execute `./standalone.sh`
- Make sure there's no other app deployed on the console at [http://localhost:9990/console/App.html#standalone-deployments](). Undeploy if needed `$DEMO $ ./undeploy.sh`
- Package the three applications `$DEMO $ mvn clean install` 
- Deploy the three applications `$DEMO $ ./deploy.sh`
- Refresh the [http://localhost:8080/cdbookstore/]() pages several times so it loads images in the cache

### Terminal

- Terminal 1 on `$DEMO/topcds`
- Terminal 2 on `$Tools/JBoss/wildfly-10.1.0.Final/bin`
- Terminal 3 on `$DEMO/topbooks`
- Terminal 4 on `$DEMO/cdbookstore`
- Open terminal and increase font

## Demo 1 - Show the 3 Web Apps

- On CD Bookstore [http://localhost:8080/cdbookstore/]() 
    - Log on as `admin/admin`
    - Show *Admin* tab.
    - Logout
    - Log on as `anakin/anakin`
    - Show *Profile* tab
    - Buy a book / Show shopping cart / Checkout

- Explain main page with *Top rated Books and CDs*
    - Show [http://localhost:8080/topcds/]()
    - Show [http://localhost:8080/topbooks/]()
    - Show main pages with *Top rated Books and CDs*
    - Remove `topcds` and then `topbooks` in Widlfy admin console and show the tabs disapearing

## Demo 2 - WildFly Swarm

- Make sure WildFly is stopped
- Terminal 1 `$DEMO/topcds`
    - Show the size of the war : `ll target`
    - Show `pom.xml` with the `swarm` profile and the fractions
    - Build the 3 apps with Swarm `../$ mvn clean install -Pswarm`
    - Show the size of the Uber Jar : `ll target`
    - Unzip the Swarm Uber Jar `unzip target/topcds-swarm.jar -d target/jar`
    - Show the content `tree target/jar` : `_bootstrap`, `m2repo`, `344 directories, 375 files`
    - Execute `java -jar target/topcds-swarm.jar`
    - Show TopCDs at URL [http://localhost:8081/topcds]() (port 8081)
- Terminal 3 on `$DEMO/topbooks`
    - Show `pom.xml` with the fractions
    - Execute `java -jar target/topbooks-swarm.jar`
    - Show the datasource in the logs
    - Show TopBooks at URL [http://localhost:8082/topbooks]() (port 8082)
- Terminal 4 on `$DEMO/cdbookstore`
    - Show `pom.xml` with the fractions
    - Execute `java -jar target/cdbookstore-swarm.jar`
    - Show the EJBs and JNDI names in the logs
    - Show TopBooks at URL [http://localhost:8080/cdbookstore]() (port 8080)
    
## Demo 3 - WildFly Swarm With M2_REPO

- Terminal 1 `$DEMO/topcds`
    - Stop server
    - Show the size of the `topcds-swarm.jar` : `ll target`
    - Show `pom.xml` with the `swarm-m2repo` profile : `bundleDependencies` at `false` instead of `true`
    - Build the apps with Swarm M2_REPO `../$ mvn clean install -Pswarm-m2repo`
    - Show the size of the Uber Jar : `ll target`
    - Execute `java -jar target/topcds-swarm.jar`
    - Show in the logs `Dependencies not bundled, will resolve from local M2REPO`
    - Show TopCDs at URL [http://localhost:8081/topcds]() (port 8081)

## Demo 4 - WildFly Swarm With Management

- Terminal 1 `$DEMO/topcds`
    - Stop server
    - Show `pom.xml` with the management fraction
    - Build the app with Swarm Mmanagement `../$ mvn clean install -Pswarm-management`
    - Execute `java -jar target/topcds-swarm.jar`
    - Show TopCDs at URL [http://localhost:8081/topcds]() (port 8081)
- Terminal 2 on `$Tools/JBoss/wildfly-10.1.0.Final/bin`
    - Run JBoss CLI : `./jboss-cli.sh`
    - `connect`
    - `deployment-info`
    - `/subsystem=undertow:read-resource`
    
## Demo 5 - Docker

- Terminal 1 `$DEMO/topcds`
    - Stop server
    - Show TopBooks at URL [http://localhost:8080/cdbookstore]() (port 8080)
- Launch Docker App on Mac
- Show Dockerfile in TopCDs
- Run the machine `docker-compose up`
- URL [http://localhost:8081/topcds]() (docker.local)
- Launch Kitematic App on Mac

## Demo 6 - Raspberry Pi

