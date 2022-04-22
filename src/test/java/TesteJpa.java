import javax.persistence.Persistence;

import org.junit.Test;

import Util.HibernateUtil;

public class TesteJpa {

    @Test
    public void testePersistenceUnit() {

        Persistence.createEntityManagerFactory("ProjetoJSF");
    }

    @Test
    public void testHibernateUtil() {

        HibernateUtil.getEntityManager();
    }

}
