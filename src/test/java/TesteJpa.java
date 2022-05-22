import java.util.List;

import javax.persistence.Persistence;

import org.junit.Test;

import Util.HibernateUtil;
import dao.DaoGeneric;
import entidades.Pessoa;

public class TesteJpa {

    @Test
    public void testePersistenceUnit() {

        Persistence.createEntityManagerFactory("ProjetoJSF");
    }

    @Test
    public void testHibernateUtil() {

        HibernateUtil.getEntityManager();
    }

    @Test
    public void testLista() {

        DaoGeneric<Pessoa> daoGeneric = new DaoGeneric<>();

        List<Pessoa> pessoas = daoGeneric.listar(Pessoa.class);

        pessoas.forEach(System.out::println);

    }

}
