package service;


import domain.Entrega;

import java.sql.SQLException;

public interface EntregaService {

    void cadastrarEntrega() throws SQLException;
    Entrega buscarEntregaPorId(int id) throws SQLException;

}
