package beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import dao.DaoGeneric;
import entidades.Lancamento;
import entidades.Pessoa;
import repository.IDaoLancamento;
import repository.IDaoLancamento_Impl;

@ManagedBean(name = "lancamentoBean")
@ViewScoped
public class LancamentoBean {

    private Lancamento lancamento = new Lancamento();

    private DaoGeneric<Lancamento> daoGeneric = new DaoGeneric<>();
    private List<Lancamento> lancamentos = new ArrayList<>();
    private IDaoLancamento daoLancamento = new IDaoLancamento_Impl();

    public String salvar() {

        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        Pessoa pessoaUser = (Pessoa) externalContext.getSessionMap().get("usuarioLogado");
        lancamento.setUsuario(pessoaUser);
        lancamento = daoGeneric.updateMerge(lancamento);

        carregarLancamentos();

        return "";
    }

    @PostConstruct
    private void carregarLancamentos() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        Pessoa pessoaUser = (Pessoa) externalContext.getSessionMap().get("usuarioLogado");

        lancamentos = daoLancamento.consultar(pessoaUser.getId());

    }

    public String novo() {

        lancamento = new Lancamento();
        return "";
    }

    public String remover() {

        daoGeneric.deletarId(lancamento);
        lancamento = new Lancamento();
        carregarLancamentos();

        return "";
    }

    public Lancamento getLancamento() {
        return lancamento;
    }

    public void setLancamento(Lancamento lancamento) {
        this.lancamento = lancamento;
    }

    public DaoGeneric<Lancamento> getDaoGeneric() {
        return daoGeneric;
    }

    public void setDaoGeneric(DaoGeneric<Lancamento> daoGeneric) {
        this.daoGeneric = daoGeneric;
    }

    public List<Lancamento> getLancamentos() {
        return lancamentos;
    }

    public void setLancamentos(List<Lancamento> lancamentos) {
        this.lancamentos = lancamentos;
    }

}
