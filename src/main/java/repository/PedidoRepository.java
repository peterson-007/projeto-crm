package repository;

import domain.Entrega;
import domain.Pedido;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PedidoRepository {

    private DataBaseConnection connection;

    public PedidoRepository() {
        this.connection = DataBaseConnection.getInstance();
    }

    public boolean insertPedido(Pedido pedido) throws SQLException {
        boolean inserted = false;

        String insertSql = "INSERT INTO Pedido (id_cliente, datahora_criacao, valor_pedido, status_pedido)"+
                " VALUES (?,?,?,?)";

        PreparedStatement preparedStatement = this.connection
                .getConnection()
                .prepareStatement(insertSql);

        // Convertendo a String para java.sql.Timestamp
        Timestamp timestampDataCriacao = Timestamp.valueOf(pedido.getDatahoraCriacao());

        preparedStatement.setInt(1, pedido.getCliente().getId());// precisa do id do CLIENTE
        preparedStatement.setTimestamp(2, timestampDataCriacao);
        preparedStatement.setDouble(3, pedido.getValorPedido());
        // Fornecendo explicitamente o tipo SQL para o ENUM
        preparedStatement.setObject(4, pedido.getStatus(), java.sql.Types.OTHER);
        inserted = preparedStatement.execute();

        return  inserted;

    }

    public void atualizarStatus(Pedido pedido) throws SQLException{

        String updateSql = "UPDATE Pedido SET status_pedido = ? WHERE id = ?";

        PreparedStatement preparedStatement = this.connection
                .getConnection()
                .prepareStatement(updateSql);

        preparedStatement.setObject(1, pedido.getStatus(),java.sql.Types.OTHER);
        preparedStatement.setInt(2,pedido.getId());

        preparedStatement.execute();
    }

    public void adicionarDataHoraEntrega(Pedido pedido) throws SQLException{

        String updateSql = "UPDATE Pedido SET datahora_entrega = ? WHERE id = ?";

        PreparedStatement preparedStatement = this.connection
                .getConnection()
                .prepareStatement(updateSql);

        // Convertendo a String para java.sql.Timestamp
        Timestamp timestampDataCriacao = Timestamp.valueOf(pedido.getDatahoraEntrega());

        preparedStatement.setTimestamp(1, timestampDataCriacao);
        preparedStatement.setInt(2,pedido.getId());

        preparedStatement.execute();
    }

    //Retorna o ID do pedido mais recente
    public Pedido findByDatahoraCriacao() throws SQLException {
        Pedido pedido = null;

        String selectSql = "SELECT * FROM Pedido ORDER BY datahora_criacao DESC LIMIT 1";

        try (PreparedStatement preparedStatement = this.connection
                .getConnection()
                .prepareStatement(selectSql);) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    pedido = mapResultSetToPedido(resultSet);
                }
            }
        }

        return pedido;
    }

    private Pedido mapResultSetToPedido(ResultSet resultSet) throws SQLException {
        // Mapeia os campos do ResultSet para a classe Pedido
        // Certificar de ajustar isso com os campos específicos da tabela
        Pedido pedido = new Pedido();
        pedido.setId(resultSet.getInt("id"));

        return pedido;
    }

    public List<Pedido> findAllPedidosByClienteCpf(String cpf) throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();

        // Consulta SQL para recuperar todos os pedidos de um cliente específico
        String sql = "SELECT p.*, c.cpf as cliente_cpf " +
                "FROM Pedido p " +
                "JOIN Cliente c ON p.id_cliente = c.id " +
                "WHERE c.cpf = ?";

        try (PreparedStatement preparedStatement = this.connection
                .getConnection()
                .prepareStatement(sql)) {

            preparedStatement.setString(1, cpf);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Pedido pedido = new Pedido();
                    pedido.setId(resultSet.getInt("id"));
                    pedido.setDatahoraCriacao(resultSet.getString("datahora_criacao"));
                    pedido.setDatahoraEntrega(resultSet.getString("datahora_entrega"));
                    pedido.setValorPedido(resultSet.getDouble("valor_pedido"));
                    // Mapeia o StatusPedido a partir da string no banco de dados
                    pedido.setStatus(Pedido.StatusPedido.valueOf(resultSet.getString("status_pedido")));

                    // Aqui você também pode recuperar a entrega, produtos e cliente, se necessário, e configurar no objeto Pedido

                    // Adicione o pedido à lista
                    pedidos.add(pedido);
                }
            }
        }

        return pedidos;
    }

    public Pedido findByIdPedido(int idPedido) throws SQLException {
        Pedido pedido = null;

        // Consulta SQL para recuperar um pedido pelo ID
        String sql = "SELECT * FROM Pedido WHERE id = ?";

        try (PreparedStatement preparedStatement = this.connection
                .getConnection()
                .prepareStatement(sql)) {
            preparedStatement.setInt(1, idPedido);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    pedido = new Pedido();
                    pedido.setId(resultSet.getInt("id"));
                    pedido.setDatahoraCriacao(resultSet.getString("datahora_criacao"));
                    pedido.setDatahoraEntrega(resultSet.getString("datahora_entrega"));
                    pedido.setValorPedido(resultSet.getDouble("valor_pedido"));

                    // Mapeia o StatusPedido a partir da string no banco de dados
                    pedido.setStatus(Pedido.StatusPedido.valueOf(resultSet.getString("status_pedido")));

                    // Recupere a entrega, produtos e cliente, se necessário, e configure no objeto Pedido
                }
            }
        }

        return pedido;
    }

}
