package beans;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import Util.HibernateUtil;
import dao.DaoGeneric;
import entidades.Cidade;
import entidades.Estado;
import entidades.Pessoa;
import repository.IDaoPessoa;
import repository.IDaoPessoa_Impl;

@ViewScoped
@ManagedBean(name = "pessoaBean")
public class PessoaBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Pessoa pessoa = new Pessoa();
    private DaoGeneric<Pessoa> daoGeneric = new DaoGeneric<>();
    private List<Pessoa> pessoas = null;
    private IDaoPessoa idaoPessoa = new IDaoPessoa_Impl();
    List<SelectItem> estados;
    List<SelectItem> cidades;

    public void editar() {

        if (pessoa.getEndereco() != null) {

            Estado estado = pessoa.getEndereco().getCidade().getEstados();
            pessoa.getEndereco().getCidade().setEstados(estado);

            List<Cidade> cidades = HibernateUtil.getEntityManager()
                    .createQuery(
                            "from Cidade where estados.id = " + pessoa.getEndereco().getCidade().getEstados().getId())
                    .getResultList();
            List<SelectItem> selectItemCidade = new ArrayList<>();

            for (Cidade cidade : cidades) {

                selectItemCidade.add(new SelectItem(cidade, cidade.getNome()));

            }

            setCidades(selectItemCidade);

        }
    }

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

            URL url = new URL("https://viacep.com.br/ws/" + pessoa.getEndereco().getCep() + "/json/");

            URLConnection connection = url.openConnection();

            InputStream is = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            String cep = "";
            StringBuilder jsonCep = new StringBuilder();

            while ((cep = br.readLine()) != null) {

                jsonCep.append(cep);

            }

            JSONParser parser = new JSONParser();

            JSONObject json = (JSONObject) parser.parse(jsonCep.toString());

            pessoa.getEndereco().setCep((String) json.get("cep"));
            pessoa.getEndereco().setLogradouro((String) json.get("logradouro"));
            pessoa.getEndereco().setComplemento((String) json.get("complemento"));
            pessoa.getEndereco().setBairro((String) json.get("bairro"));
            pessoa.getEndereco().getCidade().setNome(((String) json.get("localidade")));
            pessoa.getEndereco().getCidade().getEstados().setSigla((String) json.get("sigla"));
            // pessoa.getEndereco().getCidade().setEstados(enderecoaux.getCidade().getEstados());

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

        HttpServletRequest servletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
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

    public List<SelectItem> getEstados() {

        estados = idaoPessoa.listaEstados();

        return estados;
    }

    public void carregarCidades(AjaxBehaviorEvent event) {

        Estado estado = (Estado) ((HtmlSelectOneMenu) event.getSource()).getValue();

        if (estado != null) {

            if (estado != null) {
                pessoa.getEndereco().getCidade().setEstados(estado);
                List<Cidade> cidades = HibernateUtil.getEntityManager()
                        .createQuery("from Cidade where estados_id = " + estado.getId()).getResultList();
                List<SelectItem> selectItemCidade = new ArrayList<>();

                for (Cidade cidade : cidades) {

                    selectItemCidade.add(new SelectItem(cidade, cidade.getNome()));

                }

                setCidades(selectItemCidade);
            }
        }

    }

    public List<SelectItem> getCidades() {
        return cidades;
    }

    public void setCidades(List<SelectItem> cidades) {
        this.cidades = cidades;
    }

}
