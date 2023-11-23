import repository.*;
import service.*;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    private static ProdutoService produtoService;
    private static EntregaService entregaService;
    private static PedidoService pedidoService;
    private static ClienteService clienteService;

    public static void main(String[] args) throws SQLException {
        // Criar uma instância dos Repository (ou injetá-lo, dependendo do seu caso)
        ClienteRepository clienteRepository = new ClienteRepository();
        ContatoRepository contatoRepository = new ContatoRepository();
        EnderecoRepository enderecoRepository = new EnderecoRepository();
        PedidoRepository pedidoRepository = new PedidoRepository();
        ProdutoRepository produtoRepository = new ProdutoRepository();
        EntregaRepository entregaRepository = new EntregaRepository();

        // Passar o Repository para o construtor de ClienteServiceImpl
        clienteService = new ClienteServiceImpl(clienteRepository, contatoRepository, enderecoRepository);
        pedidoService = new PedidoServiceImpl(clienteRepository, pedidoRepository, produtoRepository, entregaRepository, (ClienteServiceImpl) clienteService);
        produtoService = new ProdutoServiceImpl(produtoRepository);
        entregaService = new EntregaServiceImpl(entregaRepository, clienteRepository);

        Scanner scanner = new Scanner(System.in);

        //MENU ADMIN
        //exibirMenuAdmin(scanner);

        //MENU USUÁRIO
        exibirMenuUsuario(scanner);

        scanner.close();
    }

    private static void exibirMenuAdmin(Scanner scanner) throws SQLException {
        int opcao;

        do {
            System.out.println("----- Menu Admin -----");
            System.out.println("1. Cadastrar Produto");
            System.out.println("2. Entregas Concluídas");
            System.out.println("3. Efetuar Entrega");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer do scanner

            switch (opcao) {
                case 1:
                    produtoService.cadastrarProduto();
                    break;
                case 2:
                    entregaService.entregasConcluidas();
                    break;
                case 3:
                    pedidoService.efetuarEntrega();
                    break;
                case 0:
                    System.out.println("Saindo do Menu Admin.");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }

        } while (opcao != 0);
    }

    private static void exibirMenuUsuario(Scanner scanner) throws SQLException {
        int opcao;

        do {
            System.out.println("----- Menu Usuário -----");
            System.out.println("1. Realizar cadastro");
            System.out.println("2. Realizar Pedido");
            System.out.println("3. Excluir Cadastro");
            System.out.println("4. Atualizar Cadastro");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer do scanner

            switch (opcao) {
                case 1:
                    clienteService.cadastrarCliente();
                    break;
                case 2:
                    pedidoService.cadastrarPedido();
                    break;
                case 3:
                    clienteService.deletarCliente();
                    break;
                case 4:
                    clienteService.atualizarCliente();
                    break;
                case 0:
                    System.out.println("Saindo do Menu Usuário.");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }

        } while (opcao != 0);
    }

}
