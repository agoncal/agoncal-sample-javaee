# Generate project

# Generate entities

# Turn Java EE 6 project into Java EE 7

* Remove Java EE 6 dependencies in `pom.xml` and add new Java EE 7 dependencies
* Replace `http://java.sun.com/xml/ns/` into `http://xmlns.jcp.org/xml/ns/` in all `*.xml` files
* Change version number in `beans.xml`, `faces-config.xml`, `persistence.xml`, `validation.xml`, `web.xml`
* Change to default DS in `persistence.xml` : `<jta-data-source>java:comp/DefaultDataSource</jta-data-source>`
* Get read of `<provider>`
* Change property to `<property name="javax.persistence.schema-generation.database.action" value="drop-and-create"/>`
* 
