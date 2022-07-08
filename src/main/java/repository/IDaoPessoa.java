package repository;

import java.util.List;

import javax.faces.model.SelectItem;

import entidades.Pessoa;

public interface IDaoPessoa {

    Pessoa consultarUsuario(String login, String Senha);

    List<SelectItem> listaEstados();

}
