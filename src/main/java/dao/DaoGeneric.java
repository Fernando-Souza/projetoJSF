package dao;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import Util.HibernateUtil;

@Named
@Dependent
public class DaoGeneric<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private EntityManager entityManager;

    @Inject
    private HibernateUtil hibernateUtil;

    @Inject
    public DaoGeneric(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public DaoGeneric() {

    }

    public void salvar(T entidade) {

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(entidade);
        transaction.commit();
    }

    public T consultar(T entidade) {

        Object id = hibernateUtil.getPrimaryKey(entidade);

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
        Object id = hibernateUtil.getPrimaryKey(entidade);
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

    public T consultar(Class<T> entidade, String codigo) {

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        T objeto = (T) entityManager.find(entidade, Long.parseLong(codigo));
        transaction.commit();
        return objeto;
    }

}
