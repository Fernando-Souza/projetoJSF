package repository;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import Util.HibernateUtil;
import entidades.Estado;
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

    @Override
    public List<SelectItem> listaEstados() {

        List<SelectItem> selectItems = new ArrayList<SelectItem>();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        List<Estado> estados = entityManager.createQuery("from Estado").getResultList();

        for (Estado estado : estados) {

            selectItems.add(new SelectItem(estado, estado.getNome()));

        }
        transaction.commit();

        return selectItems;
    }

}
