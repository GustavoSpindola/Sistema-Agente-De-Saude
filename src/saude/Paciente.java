package saude;

import java.io.Serializable;

public class Paciente implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Etapa {
        PACIENTE("Paciente"),
        TENTATIVA_1("Tentativa 1"),
        TENTATIVA_2("Tentativa 2"),
        VISITA_REALIZADA("Visita Realizada"),
        VISITA_SEM_SUCESSO("Sem Sucesso");

        private final String label;
        Etapa(String label) { this.label = label; }
        public String getLabel() { return label; }
    }

    private int id;
    private String nome;
    private String endereco;
    private String telefone;
    private String cpf;
    private Etapa etapa;
    private String relato;

    public Paciente(int id, String nome, String endereco, String telefone, String cpf) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.cpf = cpf;
        this.etapa = Etapa.PACIENTE;
        this.relato = "";
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getCpf() { return cpf; }
    public Etapa getEtapa() { return etapa; }
    public void setEtapa(Etapa etapa) { this.etapa = etapa; }
    public String getRelato() { return relato; }
    public void setRelato(String relato) { this.relato = relato; }

    @Override
    public String toString() { return nome; }
}