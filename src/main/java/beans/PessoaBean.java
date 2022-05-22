package beans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import dao.DaoGeneric;
import entidades.Pessoa;
import repository.IDaoPessoa;
import repository.IDaoPessoa_Impl;

@ViewScoped
@ManagedBean(name = "pessoaBean")
public class PessoaBean {

    private Pessoa pessoa = new Pessoa();
    private DaoGeneric<Pessoa> daoGeneric = new DaoGeneric<>();
    private List<Pessoa> pessoas = null;
    private IDaoPessoa idaoPessoa = new IDaoPessoa_Impl();

    public String salvar() {

        pessoa = daoGeneric.updateMerge(pessoa);
        novo(pessoa);
        return "";

    }

    public String remover() {

        daoGeneric.deletarId(pessoa);
        novo();
        return "";

    }

    public String novo() {

        daoGeneric.salvar(pessoa);
        pessoa = new Pessoa();
        carregarPessoas();
        return "";

    }

    public String novo(Pessoa p) {

        pessoa = daoGeneric.updateMerge(p);
        // pessoa = new Pessoa();
        carregarPessoas();
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

    public List<Pessoa> getPessoas() {
        return pessoas;
    }

    public void setPessoas(List<Pessoa> pessoas) {
        this.pessoas = pessoas;
    }

    @PostConstruct
    public void carregarPessoas() {

        pessoas = daoGeneric.listar(Pessoa.class);
    }

    public String logar() {

        Pessoa pessoaUser = idaoPessoa.consultarUsuario(pessoa.getLogin(), pessoa.getSenha());

        if (pessoaUser != null) {
            FacesContext context = FacesContext.getCurrentInstance();
            ExternalContext externalContext = context.getExternalContext();
            externalContext.getSessionMap().put("usuarioLogado", pessoaUser);

            return "primeirapagina.xhtml";
        }

        return "index.xhtml";
    }

    public Boolean permiteAcesso(String acesso) {

        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        Pessoa pessoaUser = (Pessoa) externalContext.getSessionMap().get("usuarioLogado");

        return pessoaUser.getPerfilUser().toLowerCase().equals(acesso.toLowerCase());

    }

}
