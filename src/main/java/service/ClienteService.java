package service;

import domain.Cliente;

import java.sql.SQLException;

public interface ClienteService {

    void cadastrarCliente() throws SQLException;
    Cliente buscarClientePorId(int id) throws SQLException;
    void listarClientesCadastrados() throws SQLException;
    void deletarCliente() throws SQLException;
}
