package Util;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ApplicationScoped
public class HibernateUtil implements Serializable {

    private static final long serialVersionUID = 1L;

    private EntityManagerFactory factory = null;

    public HibernateUtil() {

        if (factory == null) {

            factory = Persistence.createEntityManagerFactory("ProjetoJSF");

        }
    }

    @Produces
    @RequestScoped
    public EntityManager getEntityManager() {

        return factory.createEntityManager();
    }

    public Object getPrimaryKey(Object entity) {

        // retorna a primary key do objeto
        return factory.getPersistenceUnitUtil().getIdentifier(entity);
    }

}
