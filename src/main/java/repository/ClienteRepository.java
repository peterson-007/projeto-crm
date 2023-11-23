package repository;

import domain.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepository {

    private DataBaseConnection connection;

    public ClienteRepository() {
        this.connection = DataBaseConnection.getInstance();
    }

    public List<Cliente> findAll() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();

        // Consulta SQL para recuperar todos os clientes
        String sql = "SELECT * FROM Cliente WHERE deleted = false";

        try (PreparedStatement preparedStatement = this.connection
                .getConnection()
                .prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(resultSet.getInt("id"));
                cliente.setNome(resultSet.getString("nome"));
                cliente.setCpf(resultSet.getString("cpf"));
                cliente.setDataDeNascimento(resultSet.getString("data_nascimento"));
                cliente.setIdadeAtual(resultSet.getInt("idade_atual"));
                cliente.setGenero(resultSet.getString("genero"));

                clientes.add(cliente);
            }
        }

        return clientes;
    }

    //MÉTODO findById
    public Cliente findById(int id) throws SQLException {
        Cliente cliente = null;

        //Monta uma consulta SQL a ser realizada no banco
        PreparedStatement preparedStatement = this.connection
                .getConnection()
                .prepareStatement("SELECT * FROM Cliente WHERE id = ?");
        preparedStatement.setInt(1, id);//quantos e qual parâmetro a ser substituído(?)
        //Armazena o resultada da consulta
        ResultSet resultSet = preparedStatement.executeQuery();

        //Verifica se existe registro com o id informado
        if (resultSet.next()) {
            cliente = new Cliente();
            cliente.setId(resultSet.getInt("id"));
            cliente.setNome(resultSet.getString("nome"));
            cliente.setCpf(resultSet.getString("cpf"));
            cliente.setDataDeNascimento(resultSet.getString("data_nascimento"));
            cliente.setIdadeAtual(resultSet.getInt("idade_atual"));
            cliente.setGenero(resultSet.getString("genero"));
        }
        //caso exista, retorna a instância
        //caso não exista, retorna null
        return cliente;
    }

    //MÉTODO findByCpf
    public Cliente findByCpf(String cpf) throws SQLException {
        Cliente cliente = null;

        //Monta uma consulta SQL a ser realizada no banco
        PreparedStatement preparedStatement = this.connection
                .getConnection()
                .prepareStatement("SELECT * FROM Cliente WHERE cpf = ?");
        preparedStatement.setString(1, cpf);//quantos e qual parâmetro a ser substituído(?)
        //Armazena o resultada da consulta
        ResultSet resultSet = preparedStatement.executeQuery();

        //Verifica se existe registro com o id informado
        if (resultSet.next()) {
            cliente = new Cliente();
            cliente.setId(resultSet.getInt("id"));
            cliente.setNome(resultSet.getString("nome"));
            cliente.setCpf(resultSet.getString("cpf"));
            cliente.setDataDeNascimento(resultSet.getString("data_nascimento"));
            cliente.setIdadeAtual(resultSet.getInt("idade_atual"));
            cliente.setGenero(resultSet.getString("genero"));

            /*
            // Recuperando os dados do Contato
            String telefone = resultSet.getString("telefone");
            String email = resultSet.getString("email");
            // Cria um objeto de contato e associa ao cliente
            Contato contato = new Contato();
            contato.setTelefone(telefone);
            contato.setEmail(email);
            // Adiciona o objeto de contato à lista de contatos do cliente
            cliente.addContato(contato);

            // Recuperando os dados do Endereço
            String rua = resultSet.getString("rua");
            String numero = resultSet.getString("numero");
            String complemento = resultSet.getString("complemento");
            String bairro = resultSet.getString("bairro");
            String cidade = resultSet.getString("cidade");
            String cep = resultSet.getString("cep");
            //Cria um objeto Endereco e associa ao cliente
            Endereco endereco = new Endereco();
            endereco.setRua(rua);
            endereco.setNumero(numero);
            endereco.setComplemento(complemento);
            endereco.setBairro(bairro);
            endereco.setCidade(cidade);
            endereco.setCep(cep);
            // Adiciona o objeto de endereco à lista de enderecos do cliente
            cliente.addEndereco(endereco);*/

        }
        //caso exista, retorna a instância
        //caso não exista, retorna null
        return cliente;
    }


    public boolean insertCliente(Cliente cliente) throws SQLException {
        try {
            String insertSql = "INSERT INTO Cliente (nome, cpf, data_nascimento, idade_atual, genero)" +
                    " VALUES (?,?,?,?,?)";

            PreparedStatement preparedStatement = this.connection
                    .getConnection()
                    .prepareStatement(insertSql);

            preparedStatement.setString(1, cliente.getNome());
            preparedStatement.setString(2, cliente.getCpf());
            preparedStatement.setString(3, cliente.getDataDeNascimento());
            preparedStatement.setInt(4, cliente.getIdadeAtual());
            preparedStatement.setString(5, String.valueOf(cliente.getGenero()));

            preparedStatement.execute(); // Executa a inserção

            return true; // Se chegou até aqui sem lançar exceção, a inserção foi bem-sucedida
        } catch (SQLException e) {
            // Tratar a exceção de alguma forma
            throw e;
        }

    }



    public boolean verificarCpf(String cpf) throws SQLException {
        String query = "SELECT COUNT(*) FROM Cliente WHERE cpf = ?";
        PreparedStatement preparedStatement = this.connection
                .getConnection()
                .prepareStatement(query);
        preparedStatement.setString(1, cpf);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0; // Retorna true se encontrou algum cliente com o mesmo CPF
            }
        }
        return false; // Retorna false se ocorrer algum erro ou não encontrar cliente com o mesmo CPF
    }

    public boolean update(Cliente cliente) throws SQLException {
        boolean updated = false;

        // Cliente nulo ou id <= 0, retorna false
        if (cliente == null || cliente.getId() <= 0) {
            return false;
        }

        // Cuidado com os espaços na query
        String updateSql = "UPDATE Cliente " +
                "SET nome = ?," +
                "data_nascimento = ?," +
                "idade_atual = ? " + // Adicione um espaço após a vírgula
                "WHERE id = ?";

        try (PreparedStatement preparedStatement = this.connection
                .getConnection()
                .prepareStatement(updateSql)) {

            preparedStatement.setString(1, cliente.getNome());
            preparedStatement.setString(2, cliente.getDataDeNascimento());
            preparedStatement.setInt(3, cliente.getIdadeAtual());
            preparedStatement.setInt(4, cliente.getId());

            int rowsAffected = preparedStatement.executeUpdate();

            updated = rowsAffected > 0;
        }

        return updated;
    }

    //delete lógico
    public boolean delete(int id) throws SQLException {
        boolean isDeleted = false;

        try (PreparedStatement preparedStatement = this.connection
                .getConnection()
                .prepareStatement("UPDATE Cliente SET deleted = true WHERE id = ?")) {

            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();

            // Se a consulta afetou alguma linha, consideramos que o delete lógico foi bem-sucedido
            if (rowsAffected > 0) {
                isDeleted = true;
            }
        }

        return isDeleted;
    }

}
