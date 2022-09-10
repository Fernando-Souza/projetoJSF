package repository;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import entidades.Lancamento;

@RequestScoped
@Named
public class IDaoLancamento_Impl implements IDaoLancamento, Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private EntityManager entityManager;

    @Override
    public List<Lancamento> consultarLimit(Long codUser, int limit) {
        List<Lancamento> lista = null;
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        lista = entityManager.createQuery("from Lancamento where usuario.id = " + codUser + "order by id desc")
                .setMaxResults(limit).getResultList();
        transaction.commit();

        return lista;
    }

    @Override
    public List<Lancamento> consultar(Long codUser) {
        List<Lancamento> lista = null;
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        lista = entityManager.createQuery("from Lancamento where usuario.id = " + codUser).getResultList();
        transaction.commit();

        return lista;
    }

}
