package org.agoncal.sample.javaee.howsmallissmall.topbooks.utils;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.logging.Logger;

public class ResourceProducer {

    @Produces
    @PersistenceContext(unitName = "hsisTopBooksPU")
    private EntityManager em;

    @Produces
    public Logger produceLogger(InjectionPoint injectionPoint)
    {
        return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }
}
