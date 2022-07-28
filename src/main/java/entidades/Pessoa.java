package entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class Pessoa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nome;
    @NotEmpty(message = "É necessário informar um sobrenome")
    @NotNull(message = "É necessário informar um sobrenome")
    private String sobrenome;
    private Integer idade;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    private Endereco endereco;
    private String nivelProg;
    private String[] linguagens;

    @Temporal(TemporalType.DATE)
    private Date dataNascimento;

    private String[] frameworks;

    private String sexo;

    private Boolean ativo;

    private String login;
    private String senha;
    private String perfilUser;

    @Column(columnDefinition = "text")
    private String fotoIconBase64;

    private String extensão;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] fotoIconBase64Original;

    public Pessoa() {

    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getPerfilUser() {
        return perfilUser;
    }

    public void setPerfilUser(String perfilUser) {
        this.perfilUser = perfilUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String[] getFrameworks() {
        return frameworks;
    }

    public void setFrameworks(String[] frameworks) {
        this.frameworks = frameworks;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNivelProg() {
        return nivelProg;
    }

    public void setNivelProg(String nivelProg) {
        this.nivelProg = nivelProg;
    }

    public String[] getLinguagens() {
        return linguagens;
    }

    public void setLinguagens(String[] linguagens) {
        this.linguagens = linguagens;
    }

    public String getFotoIconBase64() {
        return fotoIconBase64;
    }

    public void setFotoIconBase64(String fotoIconBase64) {
        this.fotoIconBase64 = fotoIconBase64;
    }

    public String getExtensão() {
        return extensão;
    }

    public void setExtensão(String extensão) {
        this.extensão = extensão;
    }

    public byte[] getFotoIconBase64Original() {
        return fotoIconBase64Original;
    }

    public void setFotoIconBase64Original(byte[] fotoIconBase64Original) {
        this.fotoIconBase64Original = fotoIconBase64Original;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Pessoa other = (Pessoa) obj;
        return Objects.equals(dataNascimento, other.dataNascimento) && Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "Pessoa [id=" + id + ", nome=" + nome + ", sobrenome=" + sobrenome + ", idade=" + idade
                + ", dataNascimento=" + dataNascimento + "]";
    }

}
