package service;

import domain.Cliente;

import java.sql.SQLException;

public interface EnderecoService {
    boolean cadastrarEndereco(int idCliente) throws SQLException;
    void adicionarEnderecosAoCliente(Cliente cliente) throws SQLException;
}
