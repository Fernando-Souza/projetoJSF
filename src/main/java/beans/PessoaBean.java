package beans;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;

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
        mostrarMsg("Cadastro realizado com sucesso!");
        return "";

    }

    private void mostrarMsg(String msg) {

        FacesContext contexto = FacesContext.getCurrentInstance();
        FacesMessage message = new FacesMessage(msg);
        contexto.addMessage(null, message);

    }

    public String remover() {

        daoGeneric.deletarId(pessoa);
        novo();
        return "";

    }

    public String limpar() {

        pessoa = new Pessoa();
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

    public void pesquisaCep(AjaxBehaviorEvent event) {

        try {

            URL url = new URL("https://viacep.com.br/ws/" + pessoa.getCep() + "/json/");

            URLConnection connection = url.openConnection();

            InputStream is = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            String cep = "";
            StringBuilder jsonCep = new StringBuilder();

            while ((cep = br.readLine()) != null) {

                jsonCep.append(cep);

            }

            Pessoa aux = new Gson().fromJson(jsonCep.toString(), Pessoa.class);

            pessoa.setCep(aux.getCep());
            pessoa.setLogradouro(aux.getLogradouro());
            pessoa.setComplemento(aux.getComplemento());
            pessoa.setBairro(aux.getBairro());
            pessoa.setLocalidade(aux.getLocalidade());
            pessoa.setUf(aux.getUf());
            pessoa.setUnidade(aux.getUnidade());
            pessoa.setIbge(aux.getIbge());
            pessoa.setGia(aux.getGia());

        } catch (Exception e) {

            mostrarMsg("Erro ao consultar o cep");
            e.printStackTrace();

        }

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

    public String deslogar() {

        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        externalContext.getSessionMap().remove("usuarioLogado");

        HttpServletRequest servletRequest = (HttpServletRequest) context.getCurrentInstance().getExternalContext()
                .getRequest();

        servletRequest.getSession().invalidate();

        return "index.xhtml";
    }

    public Boolean permiteAcesso(String acesso) {

        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        Pessoa pessoaUser = (Pessoa) externalContext.getSessionMap().get("usuarioLogado");

        return pessoaUser.getPerfilUser().toLowerCase().equals(acesso.toLowerCase());

    }

}
