package repository;

import domain.Produto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoRepository {

    private DataBaseConnection connection;

    public ProdutoRepository() {
        this.connection = DataBaseConnection.getInstance();
    }

    public boolean insertProduto(Produto produto) throws SQLException {
      try {
        String insertSql = "INSERT INTO Produto (nome, preco)"+
                " VALUES (?,?)";

        PreparedStatement preparedStatement = this.connection
                .getConnection()
                .prepareStatement(insertSql);

        preparedStatement.setString(1, produto.getNome());
        preparedStatement.setDouble(2, produto.getPreco());

        preparedStatement.execute();
        /*O método execute() da interface PreparedStatement em Java retorna um booleano
        * indicando se o resultado é um conjunto de resultados (true) ou se é um update,
        * delete ou insert (false)*/


        return true; // Se chegou até aqui sem lançar exceção, a inserção foi bem-sucedida
    } catch (SQLException e) {
        // Trate a exceção de alguma forma (log, relance, etc.)
        throw e;
    }

    }

    public List<Produto> findAllProdutos() throws SQLException {
        ArrayList<Produto> produtos = new ArrayList<>();

        // Monta uma consulta SQL a ser realizada no banco
        PreparedStatement preparedStatement = this.connection
                .getConnection()
                .prepareStatement("SELECT * FROM Produto");

        // Armazena o resultado da consulta
        ResultSet resultSet = preparedStatement.executeQuery();

        // Percorrer o ResultSet e pegar os resultados
        while (resultSet.next()) {
            Produto produto = new Produto();
            produto.setNome(resultSet.getString("nome"));
            produto.setPreco(resultSet.getDouble("preco"));

            produtos.add(produto);
        }

        return produtos;
    }

    public boolean verificarNomeProduto(String nome) throws SQLException {
        String query = "SELECT COUNT(*) FROM Produto WHERE nome = ?";
        PreparedStatement preparedStatement = this.connection
                .getConnection()
                .prepareStatement(query);
        preparedStatement.setString(1, nome);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0; // Retorna true se encontrou algum produto com o mesmo nome
            }
        }
        return false; // Retorna false se ocorrer algum erro ou não encontrar produto com mesmo nome
    }

}
