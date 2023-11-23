package service;

import domain.Pedido;
import domain.Produto;

import java.sql.SQLException;
import java.util.List;

public interface PedidoService {

    void cadastrarPedido() throws SQLException;
    Pedido buscarPedidoPorId(int id) throws SQLException;
    List<Produto> adicionarProdutos(Pedido pedido) throws SQLException;
    double calcularValorPedido(List<Produto> produtos);
    void efetuarEntrega() throws SQLException;
}
