package repository;

import domain.Endereco;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EnderecoRepository {

    private DataBaseConnection connection;
    public EnderecoRepository() {
        this.connection = DataBaseConnection.getInstance();
    }

    public boolean insertEndereco(Endereco endereco) throws SQLException {
       try {

        String insertSql = "INSERT INTO Endereco (id_cliente, rua, numero, complemento, bairro, cidade, cep)"+
                " VALUES (?,?,?,?,?,?,?)";

        PreparedStatement preparedStatement = this.connection
                .getConnection()
                .prepareStatement(insertSql);

        preparedStatement.setInt(1, endereco.getCliente().getId());// precisa do id do CLIENTE
        preparedStatement.setString(2, endereco.getRua());
        preparedStatement.setString(3, endereco.getNumero());
        preparedStatement.setString(4, endereco.getComplemento());
        preparedStatement.setString(5, endereco.getBairro());
        preparedStatement.setString(6, endereco.getCidade());
        preparedStatement.setString(7, endereco.getCep());


           return true; // Se chegou até aqui sem lançar exceção, a inserção foi bem-sucedida
       } catch (SQLException e) {
           // Trate a exceção de alguma forma (log, relance, etc.)
           throw e;
       }
    }

    public Endereco findByIdCliente(int idCliente) throws SQLException {
        Endereco endereco = null;

        // Consulta SQL para recuperar o endereco associado a um cliente pelo ID do cliente
        String sql = "SELECT * FROM Endereco WHERE id_cliente = ?";

        try ( PreparedStatement preparedStatement = this.connection
                .getConnection()
                .prepareStatement(sql)) {
            preparedStatement.setInt(1, idCliente);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    endereco = new Endereco();
                    endereco.setId(resultSet.getInt("id"));
                    endereco.setRua(resultSet.getString("rua"));
                    endereco.setNumero(resultSet.getString("numero"));
                    endereco.setComplemento(resultSet.getString("complemento"));
                    endereco.setBairro(resultSet.getString("bairro"));
                    endereco.setCidade(resultSet.getString("cidade"));
                    endereco.setCep(resultSet.getString("cep"));
                }
            }
        }

        return endereco;
    }
}
