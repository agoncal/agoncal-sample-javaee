package org.agoncal.sample.javaee.howsmallissmall.topbooks.utils;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class ResourceProducer {

    @Produces
    @PersistenceContext(unitName = "hsisTopBooksPU")
    private EntityManager em;

}
