package domain;

import java.util.ArrayList;
import java.util.List;

public class Cliente {

    private int id;
    private String nome;
    private String cpf;
    private String dataDeNascimento;
    private int idadeAtual;
    private char genero;
    private List<Endereco> enderecos;
    private List<Contato> contatos;
    private List<Pedido> pedidos;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getDataDeNascimento() {
        return dataDeNascimento;
    }

    public void setDataDeNascimento(String dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
    }

    public int getIdadeAtual() {
        return idadeAtual;
    }

    public void setIdadeAtual(int idadeAtual) {
        this.idadeAtual = idadeAtual;
    }

    public char getGenero() {
        return genero;
    }

    //recebendo String como parâmetro
    public void setGenero(String genero) {
        // Verifica se a string não está vazia para evitar uma exceção de índice inválido
        if (genero != null && genero.length() > 0) {
            // Obtém o primeiro caractere da string e atribui ao atributo char genero
            this.genero = genero.charAt(0);
        } else {
            // Atribua um valor padrão ao atributo char genero se a string estiver vazia
            this.genero = ' ';
        }
    }

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }

    public List<Contato> getContatos() {
        return contatos;
    }

    public void setContatos(List<Contato> contatos) {
        this.contatos = contatos;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    // Construtor vazio para inicializar as listas de contato, endereco e pedido
    public Cliente() {
        this.contatos = new ArrayList<>();
        this.enderecos = new ArrayList<>();
        this.pedidos = new ArrayList<>();
    }

    // Método para adicionar um contato à lista de contatos do cliente
    public void addContato(Contato contato) {
        contatos.add(contato);
    }

    // Método para adicionar um contato à lista de contatos do cliente
    public void addEndereco(Endereco endereco) {
        enderecos.add(endereco);
    }

    // Método para adicionar um pedido à lista de pedidos do cliente
    public void addPedido(Pedido pedido) { pedidos.add(pedido);}

}
