package repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import Util.HibernateUtil;
import entidades.Pessoa;

public class IDaoPessoa_Impl implements IDaoPessoa {

    private EntityManager entityManager = HibernateUtil.getEntityManager();

    @Override
    public Pessoa consultarUsuario(String login, String Senha) {

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Pessoa pessoa = (Pessoa) entityManager
                .createQuery("select p from Pessoa as p where p.login='" + login + "' and p.senha = '" + Senha + "'")
                .getSingleResult();

        transaction.commit();
        entityManager.close();
        return pessoa;
    }

}
