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

project add-dependency javax:javaee-api:7.0:provided
project add-repository primefaces-repo http://repository.primefaces.org
