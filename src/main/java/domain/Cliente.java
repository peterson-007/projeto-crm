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

}
