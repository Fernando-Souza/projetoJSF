package repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import Util.HibernateUtil;
import entidades.Lancamento;

public class IDaoLancamento_Impl implements IDaoLancamento {

    @Override
    public List<Lancamento> consultar(Long codUser) {
        List<Lancamento> lista = null;
        EntityManager entityManager = HibernateUtil.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        lista = entityManager.createQuery("from Lancamento where usuario.id = " + codUser).getResultList();
        transaction.commit();
        entityManager.close();

        return lista;
    }

}
