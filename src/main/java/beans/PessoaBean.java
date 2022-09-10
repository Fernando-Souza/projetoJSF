package beans;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.DatatypeConverter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import Util.HibernateUtil;
import dao.DaoGeneric;
import entidades.Cidade;
import entidades.Estado;
import entidades.Pessoa;
import repository.IDaoPessoa;

@ViewScoped
@Named(value = "pessoaBean")
public class PessoaBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Pessoa pessoa = new Pessoa();
    private List<Pessoa> pessoas = new ArrayList<>();

    // não sei o motivo disso fica destacado ?????
    @Inject
    private DaoGeneric<Pessoa> daoGeneric;

    @Inject
    private IDaoPessoa idaoPessoa;

    List<SelectItem> estados;
    List<SelectItem> cidades;

    @Inject
    private HibernateUtil hibernateUtil;

    private Part arquivoFoto;

    public PessoaBean() {
    }

    public Part getArquivoFoto() {
        return arquivoFoto;
    }

    public void setArquivoFoto(Part arquivoFoto) {
        this.arquivoFoto = arquivoFoto;
    }

    public void registraLog() {

        System.out.println("Registra Log");

    }

    public String editar() {

        if (pessoa.getEndereco() != null) {

            Estado estado = pessoa.getEndereco().getCidade().getEstados();
            pessoa.getEndereco().getCidade().setEstados(estado);

            List<Cidade> cidades = hibernateUtil.getEntityManager()
                    .createQuery(
                            "from Cidade where estados.id = " + pessoa.getEndereco().getCidade().getEstados().getId())
                    .getResultList();
            List<SelectItem> selectItemCidade = new ArrayList<>();

            for (Cidade cidade : cidades) {

                selectItemCidade.add(new SelectItem(cidade, cidade.getNome()));

            }

            setCidades(selectItemCidade);

        }

        return "";
    }

    public String salvar() throws IOException {

        if (arquivoFoto.getInputStream() != null) {
            byte[] imagemByte = getByte(arquivoFoto.getInputStream());
            pessoa.setFotoIconBase64Original(imagemByte);

            BufferedImage imageBuffer = ImageIO.read(new ByteArrayInputStream(imagemByte));

            int type = imageBuffer.getType() == 0 ? imageBuffer.TYPE_INT_ARGB : imageBuffer.getType();

            int largura = 50;
            int altura = 50;

            /** criar miniatura **/

            BufferedImage resizeImage = new BufferedImage(altura, largura, type);

            Graphics2D g = resizeImage.createGraphics();
            g.drawImage(imageBuffer, 0, 0, largura, altura, null);

            /** Escrever novamente a imagem em tamanho menor **/
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            String extensao = arquivoFoto.getContentType().split("/")[1];
            ImageIO.write(resizeImage, extensao, baos);

            String miniImagem = "data:" + arquivoFoto.getContentType() + ";base64,"
                    + DatatypeConverter.printBase64Binary(baos.toByteArray());

            pessoa.setFotoIconBase64(miniImagem);
            pessoa.setExtensão(extensao);

        }

        pessoa = daoGeneric.updateMerge(pessoa);
        carregarPessoas();
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

            pessoa.getEndereco().getCidade().setEstados(estado);
            List<Cidade> cidades = hibernateUtil.getEntityManager()
                    .createQuery("from Cidade where estados_id = " + estado.getId()).getResultList();
            List<SelectItem> selectItemCidade = new ArrayList<>();

            for (Cidade cidade : cidades) {

                selectItemCidade.add(new SelectItem(cidade, cidade.getNome()));

            }

            setCidades(selectItemCidade);
        }
    }

    public List<SelectItem> getCidades() {
        return cidades;
    }

    public void setCidades(List<SelectItem> cidades) {
        this.cidades = cidades;
    }

    private byte[] getByte(InputStream is) throws IOException {

        int len;
        int size = 1024;
        byte[] buffer = null;
        if (is instanceof ByteArrayInputStream) {
            size = is.available();
            buffer = new byte[size];
            len = is.read(buffer, 0, size);
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            buffer = new byte[size];
            while ((len = is.read(buffer, 0, size)) != -1) {
                bos.write(buffer, 0, len);
            }

            buffer = bos.toByteArray();
        }

        return buffer;

    }

    public void download() throws IOException {

        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String fileDownloadId = params.get("fileDownloadId");

        Pessoa pessoa = daoGeneric.consultar(Pessoa.class, fileDownloadId);

        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getCurrentInstance()
                .getExternalContext().getResponse();

        response.addHeader("Content-Disposition", "attachment;filename=download." + pessoa.getExtensão());
        response.setContentType("application/octet-stream");
        response.setContentLength(pessoa.getFotoIconBase64Original().length);
        response.getOutputStream().write(pessoa.getFotoIconBase64Original());
        response.getOutputStream().flush();
        FacesContext.getCurrentInstance().responseComplete();

    }

}
