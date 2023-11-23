package service;

import domain.Cliente;

import java.sql.SQLException;

public interface ContatoService {
    boolean cadastrarContato(int idCliente) throws SQLException;
    void adicionarContatosAoCliente(Cliente cliente) throws SQLException;
}
