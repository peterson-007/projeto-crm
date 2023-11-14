package service;



import domain.Produto;

import java.sql.SQLException;

public interface ProdutoService {

    void cadastrarProduto() throws SQLException;
    Produto buscarProdutoPorId(int id) throws SQLException;
}
