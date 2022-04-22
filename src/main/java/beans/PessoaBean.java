package beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import dao.DaoGeneric;
import entidades.Pessoa;

@ViewScoped
@ManagedBean(name = "pessoaBean")
public class PessoaBean {

    private Pessoa pessoa = new Pessoa();
    private DaoGeneric<Pessoa> daoGeneric = new DaoGeneric<>();

    public String salvar() {

        pessoa = daoGeneric.updateMerge(pessoa);
        return "";

    }

    public String remover() {

        daoGeneric.deletarId(pessoa);
        pessoa = new Pessoa();
        return "";

    }

    public String novo() {

        daoGeneric.salvar(pessoa);
        pessoa = new Pessoa();
        return "";

    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public DaoGeneric<Pessoa> getDaoGeneric() {
        return daoGeneric;
    }

    public void setDaoGeneric(DaoGeneric<Pessoa> daoGeneric) {
        this.daoGeneric = daoGeneric;
    }

}
