package repository;

import domain.Contato;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContatoRepository {

    private DataBaseConnection connection;
    public ContatoRepository() {
        this.connection = DataBaseConnection.getInstance();
    }

    public boolean insertContato(Contato contato) throws SQLException {
        try {

            String insertSql = "INSERT INTO Contato (id_cliente, telefone, email)" +
                    " VALUES (?,?,?)";

            PreparedStatement preparedStatement = this.connection
                    .getConnection()
                    .prepareStatement(insertSql);

            preparedStatement.setInt(1, contato.getCliente().getId());// precisa do id do CLIENTE
            preparedStatement.setString(2, contato.getTelefone());
            preparedStatement.setString(3, contato.getEmail());

            preparedStatement.execute(); // Executa a inserção

            return true; // Se chegou até aqui sem lançar exceção, a inserção foi bem-sucedida
        } catch (SQLException e) {
            // Trate a exceção de alguma forma (log, relance, etc.)
            throw e;
        }
    }

    public Contato findByIdCliente(int idCliente) throws SQLException {
        Contato contato = null;

        // Consulta SQL para recuperar o contato associado a um cliente pelo ID do cliente
        String sql = "SELECT * FROM Contato WHERE id_cliente = ?";

        try (PreparedStatement preparedStatement = this.connection
                .getConnection()
                .prepareStatement(sql)) {
            preparedStatement.setInt(1, idCliente);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    contato = new Contato();
                    contato.setId(resultSet.getInt("id"));
                    contato.setTelefone(resultSet.getString("telefone"));
                    contato.setEmail(resultSet.getString("email"));
                }
            }
        }

        return contato;
    }
}
