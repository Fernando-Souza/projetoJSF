package repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import entidades.Estado;
import entidades.Pessoa;

@RequestScoped
@Named
public class IDaoPessoa_Impl implements IDaoPessoa, Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private EntityManager entityManager;

    @Override
    public Pessoa consultarUsuario(String login, String Senha) {

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Pessoa pessoa = (Pessoa) entityManager
                .createQuery("select p from Pessoa as p where p.login='" + login + "' and p.senha = '" + Senha + "'")
                .getSingleResult();

        transaction.commit();

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
