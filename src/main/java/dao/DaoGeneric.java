package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import Util.HibernateUtil;

public class DaoGeneric<T> {

    private EntityManager entityManager = HibernateUtil.getEntityManager();

    public void salvar(T entidade) {

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(entidade);
        transaction.commit();
    }

    public T consultar(T entidade) {

        Object id = HibernateUtil.getPrimaryKey(entidade);

        T ent = (T) entityManager.find(entidade.getClass(), id);
        return ent;

    }

    public T consultar(Long id, Class<T> entidade) {

        T ent = entityManager.find(entidade, id);
        return ent;

    }

    public T updateMerge(T entidade) {

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        T entidadeSalva = entityManager.merge(entidade);
        transaction.commit();
        return entidadeSalva;
    }

    public void deletarId(T entidade) {
        Object id = HibernateUtil.getPrimaryKey(entidade);
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager
                .createNativeQuery(
                        "delete from " + entidade.getClass().getSimpleName().toLowerCase() + " where id =" + id)
                .executeUpdate();
        transaction.commit();

    }

    public List<T> listar(Class<T> entidade) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        List<T> lista = entityManager.createQuery("from " + entidade.getName()).getResultList();
        transaction.commit();

        return lista;

    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

}
