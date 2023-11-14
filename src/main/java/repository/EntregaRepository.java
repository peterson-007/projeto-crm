package repository;

import domain.Entrega;

import java.sql.*;

public class EntregaRepository {

    private DataBaseConnection connection;
    public EntregaRepository() {
        this.connection = DataBaseConnection.getInstance();
    }

    public boolean insertEntrega(Entrega entrega) throws SQLException{
        boolean inserted = false;

        String insertSql = "INSERT INTO Entrega (id_pedido, entregador, qt_tentativas_entrega, status_entrega)"+
                            " VALUES (?,?,0,?)";

        PreparedStatement preparedStatement = this.connection
                .getConnection()
                .prepareStatement(insertSql);

        preparedStatement.setInt(1, entrega.getPedido().getId());
        preparedStatement.setString(2, entrega.getEntregador());
        // Fornecendo explicitamente o tipo SQL para o ENUM
        preparedStatement.setObject(3, entrega.getStatus(), java.sql.Types.OTHER);

        inserted = preparedStatement.execute();

        return inserted;
    }

    public void atualizarStatus(Entrega entrega) throws SQLException{

        String updateSql = "UPDATE Entrega SET status_entrega = ? WHERE id = ?";

        PreparedStatement preparedStatement = this.connection
                .getConnection()
                .prepareStatement(updateSql);

        preparedStatement.setObject(1,entrega.getStatus(), java.sql.Types.OTHER);
        preparedStatement.setInt(2,entrega.getId());

        preparedStatement.execute();
    }

    public void efetuarEntrega(Entrega entrega) throws SQLException{

        String updateSql = "UPDATE Entrega SET recebedor = ?, qt_tentativas_entrega = ?, data_entrega = ?, status_entrega = ? WHERE id = ?";

        PreparedStatement preparedStatement = this.connection
                .getConnection()
                .prepareStatement(updateSql);

        // Convertendo a String para java.sql.Timestamp
        Timestamp timestampDataEntrega = Timestamp.valueOf(entrega.getDataEntrega());


        preparedStatement.setString(1,entrega.getRecebedor());
        preparedStatement.setInt(2,entrega.getQtTentativasEntrega());
        preparedStatement.setTimestamp(3, timestampDataEntrega);
        // Fornecendo explicitamente o tipo SQL para o ENUM
        preparedStatement.setObject(4, entrega.getStatus(),java.sql.Types.OTHER);
        preparedStatement.setInt(5, entrega.getId());

        preparedStatement.execute();
    }

    public void atualizarQtTentativasEntrega(Entrega entrega) throws SQLException{

        String updateSql = "UPDATE Entrega SET qt_tentativas_entrega = ? WHERE id = ?";

        PreparedStatement preparedStatement = this.connection
                .getConnection()
                .prepareStatement(updateSql);

        preparedStatement.setInt(1, entrega.getQtTentativasEntrega());
        preparedStatement.setInt(2, entrega.getId());

        preparedStatement.execute();
    }

    public Entrega findEntregaByPedido(int idPedido) throws SQLException {
        Entrega entrega = null;

        // Consulta SQL para recuperar uma entrega pelo ID do pedido
        String sql = "SELECT * FROM Entrega WHERE id_pedido = ?";

        try (PreparedStatement preparedStatement = this.connection
                .getConnection()
                .prepareStatement(sql)) {
            preparedStatement.setInt(1, idPedido);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    entrega = new Entrega();
                    entrega.setId(resultSet.getInt("id"));
                    entrega.setEntregador(resultSet.getString("entregador"));
                    entrega.setRecebedor(resultSet.getString("recebedor"));
                    entrega.setQtTentativasEntrega(resultSet.getInt("qt_tentativas_entrega"));
                    entrega.setDataEntrega(resultSet.getString("data_entrega"));
                    entrega.setStatus(Entrega.StatusEntrega.valueOf(resultSet.getString("status_entrega")));

                }
            }
        }

        return entrega;
    }

}
