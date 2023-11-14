import repository.*;
import service.*;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {

        // Criar uma instância dos Repository (ou injetá-lo, dependendo do seu caso)
        ClienteRepository clienteRepository = new ClienteRepository();
        ContatoRepository contatoRepository = new ContatoRepository();
        EnderecoRepository enderecoRepository = new EnderecoRepository();

        // Passar o Repository para o construtor de ClienteServiceImpl
        ClienteService clienteService = new ClienteServiceImpl(clienteRepository, contatoRepository, enderecoRepository);


        PedidoRepository pedidoRepository = new PedidoRepository();
        ProdutoRepository produtoRepository = new ProdutoRepository();
        EntregaRepository entregaRepository = new EntregaRepository();

        PedidoService pedidoService = new PedidoServiceImpl(clienteRepository,pedidoRepository,produtoRepository,entregaRepository, (ClienteServiceImpl) clienteService);

        ProdutoService produtoService = new ProdutoServiceImpl(produtoRepository);

        //clienteService.cadastrarCliente();

        //produtoService.cadastrarProduto();

        //pedidoService.cadastrarPedido();

        //pedidoService.efetuarEntrega();

        //clienteService.deletarCliente();

    }
}
