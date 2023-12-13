package service;

import domain.Cliente;
import domain.Entrega;
import domain.Pedido;
import domain.Produto;
import repository.ClienteRepository;
import repository.EntregaRepository;
import repository.PedidoRepository;
import repository.ProdutoRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class PedidoServiceImpl implements PedidoService {

    //Injetando VARIÁVEIS
    private ClienteRepository clienteRepository;
    private PedidoRepository pedidoRepository;
    private ProdutoRepository produtoRepository;
    private EntregaRepository entregaRepository;
    private ClienteServiceImpl clienteService;

    // Injetando CONSTRUTORES
    public PedidoServiceImpl(ClienteRepository clienteRepository, PedidoRepository pedidoRepository, ProdutoRepository produtoRepository, EntregaRepository entregaRepository, ClienteServiceImpl clienteService) {
        this.clienteRepository = clienteRepository;
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
        this.entregaRepository = entregaRepository;
        this.clienteService = clienteService;
    }

    Scanner sc = new Scanner(System.in);

    @Override
    public void cadastrarPedido() throws SQLException {

        clienteService.listarClientesCadastrados();
        System.out.println("\nDigite o CPF do cliente:");
        String cpf = sc.nextLine();

        //Associa o cliente ao pedido
        Cliente cliente = (clienteRepository.findByCpf(cpf));

        //CRIAR PEDIDO
        Pedido pedido = criarPedido();

        //Associar pedido ao cliente
        pedido.setIdCliente(cliente.getId());

        //Retorna produtos escolhidos
        List<Produto> produtos = adicionarProdutos(pedido);

        //Calcular valor total do pedido
        pedido.setValorPedido(calcularValorPedido(produtos));

        //status do pedido
        pedido.setStatus(Pedido.StatusPedido.EM_PROCESSAMENTO);

        //Adiciona pedido no banco e RETORNA O ID GERADO
        int idPedido = pedidoRepository.insertPedido(pedido);

        //Associa os produtos escolhidos ao pedido no banco de dados
        pedidoRepository.associarProdutosAoPedido(idPedido, produtos);

        //CRIAR ENTREGA
        Entrega entrega = criarEntrega(idPedido);
        //Associa com a instância atual do cliente
        entregaRepository.insertEntrega(entrega);
    }

    private Pedido criarPedido() {
        Pedido pedido = new Pedido();

        LocalDateTime dataHoraAtual = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dataHoraFormatada = dataHoraAtual.format(formatter);

        pedido.setDatahoraCriacao(dataHoraFormatada);

        return pedido;
    }

    private Entrega criarEntrega(int idPedido) {
        Entrega entrega = new Entrega();
        entrega.setEntregador("Zé");
        entrega.setStatus(Entrega.StatusEntrega.PENDENTE);
        entrega.setIdPedido(idPedido);
        return entrega;
    }

    @Override
    public Pedido buscarPedidoPorId(int id) throws SQLException {
        return null;
    }

    @Override
    public List<Produto> adicionarProdutos(Pedido pedido) throws SQLException {

        List<Produto> produtos = produtoRepository.findAllProdutos();
        List<Produto> produtosEscolhidos = new ArrayList<>();
        boolean carrinhoDeCompras = true;

        while (carrinhoDeCompras) {
            System.out.println("Lista de Produtos Disponíveis:");
            int contador = 1;

            for (Produto produto : produtos) {
                System.out.println(contador + ". " + produto.getNome() + " - R$" + produto.getPreco());
                contador++;
            }

            System.out.println("Digite o número do produto que deseja adicionar ao carrinho (ou 0 para sair): ");
            int escolha = sc.nextInt();

            if (escolha >= 1 && escolha <= produtos.size()) {
                Produto produtoEscolhido = produtos.get(escolha - 1);
                //adicionar o produto escolhido à LISTA de produtos do objeto pedido
                produtosEscolhidos.add(produtoEscolhido);
                System.out.println(produtoEscolhido.getNome() + " foi adicionado ao seu pedido.");
            } else if (escolha == 0) {
                carrinhoDeCompras = false;
                System.out.println("Pedido cadastrado com sucesso!");
                return produtosEscolhidos;
            } else {
                System.out.println("Opção inválida. Por favor, escolha um número válido.");
            }
        }
        return produtosEscolhidos;
    }



        public double calcularValorPedido(List<Produto> produtos) {
        //List<Produto> produtos = pedido.getProdutos(); Recuperar produtos pelo repository
        double valorTotal = 0.0;

        for (Produto produto : produtos) {
            valorTotal += produto.getPreco();
        }

        return valorTotal;
    }

    @Override
    public void efetuarEntrega() throws SQLException {

        clienteService.listarClientesCadastrados();

        System.out.println("EFETUAR ENTREGA DE PEDIDO\n Digite o CPF do cliente: ");
        String cpf = sc.nextLine();

        List<Pedido> pedidos = pedidoRepository.findPedidosPendentesByCpf(cpf);
        //Exibir pedidos
        boolean haPedidos = exibirPedidosParaEntrega(pedidos);

        if(haPedidos) {
            //Obter ID do pedido
            int idPedido = obterIdPedido();

            Pedido pedido = pedidoRepository.findByIdPedido(idPedido);
            Entrega entrega = entregaRepository.findEntregaByPedido(idPedido);

            processarOpcaoEntrega(pedido, entrega);
        } else {
            System.exit(0);
        }

    }

    private boolean exibirPedidosParaEntrega(List<Pedido> pedidos) {

        if (pedidos.isEmpty()) {
            System.out.println("Não há produtos a serem entregues.");
            return false;
        } else {
            System.out.println("Pedidos a serem entregues:");

            for (Pedido pedido : pedidos) {
                System.out.println(pedido.getStatus() +
                        " - Número do pedido:" + pedido.getId() + "  Valor do pedido:" + pedido.getValorPedido());
            }
            return true;
        }
    }

    private int obterIdPedido() {
        int idPedido;
        while (true) {
            try {
                System.out.println("Digite o número do pedido a ser entregue: ");
                idPedido = sc.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Por favor, insira um número válido.");
                sc.nextLine(); // Limpa o buffer do Scanner
            }
        }
        return idPedido;
    }

    private void processarOpcaoEntrega(Pedido pedido, Entrega entrega) throws SQLException {

        int opcao = obterOpcaoEntrega();

        switch (opcao) {
            case 1:
                processarEntregaEfetuada(pedido, entrega);
                break;
            case 2:
                processarNaoAtendido(pedido, entrega);
                break;
            case 3:
                processarEntregaDevolvida(pedido, entrega);
                break;
            default:
                System.out.println("Opção inválida.");
                break;
        }
    }

    private int obterOpcaoEntrega() {
        int opcao;
        while (true) {
            try {
                System.out.println("Digite uma opção referente à entrega: \n"+
                        "1 - Entrega efetuada\n"+
                        "2 - Não atendido\n"+
                        "3 - Entrega devolvida\n");
                opcao = sc.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Por favor, insira um número válido.");
                sc.nextLine(); // Limpa o buffer do Scanner
            }
        }
        return opcao;
    }

    private void processarEntregaEfetuada(Pedido pedido, Entrega entrega) throws SQLException {
        sc.nextLine(); // Limpa o buffer do Scanner
        //Nome do recebedor:
        System.out.println("Digite o nome do recebedor: ");
        String recebedor = sc.nextLine();
        System.out.println("Pedido entregue!");

        entrega.setRecebedor(recebedor);
        //Qt Tentativas de entrega:
        int qtTentativasEntrega = (entrega.getQtTentativasEntrega() + 1);
        entrega.setQtTentativasEntrega(qtTentativasEntrega);
        //Data da entrega:
        //data e hora atual
        LocalDateTime dataHoraAtual = LocalDateTime.now();
        // Formata a data e hora atual para o formato "yyyy-MM-dd HH:mm:ss"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dataHoraFormatada = dataHoraAtual.format(formatter);
        pedido.setDatahoraEntrega(dataHoraFormatada);//setar datahoraFormatada na datahoraEntrega
        entrega.setDataEntrega(dataHoraFormatada);//setar datahoraFormata na ENTREGA
        pedido.setDatahoraEntrega(dataHoraFormatada);//setar datahoraFormata no PEDIDO

        //atualiza o status da ENTREGA NA INSTÂNCIA do pedido
        entrega.setStatus(Entrega.StatusEntrega.ENTREGUE);

        //atualiza a entrega NO BANCO DE DADOS
        entregaRepository.efetuarEntrega(entrega);

        //atualiza também o status do PEDIDO no banco de dados
        pedido.setStatus(Pedido.StatusPedido.FINALIZADO);//Atualiza a instância
        pedidoRepository.atualizarStatus(pedido);//atualiza STATUS no banco
        pedidoRepository.adicionarDataHoraEntrega(pedido);// atualiza DataHoraEntrega do PEDIDO no banco
    }

    private void processarNaoAtendido(Pedido pedido, Entrega entrega) throws SQLException {

        entrega.setStatus(Entrega.StatusEntrega.PENDENTE);
        //atualiza o status da entrega no banco de dados
        entregaRepository.atualizarStatus(entrega);

        //atualizar qtTentativasEntrega
        int qtTentativasEntrega = (entrega.getQtTentativasEntrega() + 1);
        entrega.setQtTentativasEntrega(qtTentativasEntrega);
        entregaRepository.atualizarQtTentativasEntrega(entrega);

        //atualiza também o status do PEDIDO no banco de dados
        pedido.setStatus(Pedido.StatusPedido.ENVIADO);//Atualiza a instância
        pedidoRepository.atualizarStatus(pedido);//Atualiza no banco
        System.out.println("Destinatário ausente\n Entrega Pendente");
    }

    private void processarEntregaDevolvida(Pedido pedido, Entrega entrega) throws SQLException {
        entrega.setStatus(Entrega.StatusEntrega.DEVOLVIDA);
        //atualiza o status da entrega no banco de dados
        entregaRepository.atualizarStatus(entrega);
        //atualiza também o status do PEDIDO no banco de dados
        pedido.setStatus(Pedido.StatusPedido.CANCELADO);//Atualiza a instância
        pedidoRepository.atualizarStatus(pedido);//Atualiza no banco
        System.out.println("Entrega Devolvida");
    }

    private void atualizarStatusPedido(Pedido pedido) throws SQLException {
        pedidoRepository.atualizarStatus(pedido);
    }

    private void atualizarStatusEntrega(Entrega entrega) throws SQLException {
        entregaRepository.atualizarStatus(entrega);
    }

    private void atualizarDatasEStatusEntrega(Pedido pedido, Entrega entrega, Entrega.StatusEntrega novoStatus) throws SQLException {
        LocalDateTime dataHoraAtual = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dataHoraFormatada = dataHoraAtual.format(formatter);

        pedido.setDatahoraEntrega(dataHoraFormatada);
        entrega.setDataEntrega(dataHoraFormatada);
        entrega.setStatus(novoStatus);

        atualizarStatusPedido(pedido);
        atualizarStatusEntrega(entrega);
    }

}
