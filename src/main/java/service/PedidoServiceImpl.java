package service;

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
import java.util.List;
import java.util.Scanner;

public class PedidoServiceImpl implements PedidoService{

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
        System.out.println("Digite o CPF do cliente:");
        String cpf = sc.nextLine();

        Pedido pedido = new Pedido();

        //Associa o cliente ao pedido
        pedido.setCliente(clienteRepository.findByCpf(cpf));

        //data e hora atual
        LocalDateTime dataHoraAtual = LocalDateTime.now();
        // Formata a data e hora atual para o formato "yyyy-MM-dd HH:mm:ss"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dataHoraFormatada = dataHoraAtual.format(formatter);
        pedido.setDatahoraCriacao(dataHoraFormatada);//setar datahoraFormatada

        //datahoraEntrega não é criada neste momento

        //Instanciar uma lista de produtos para o pedido
        ArrayList produtos = new ArrayList<>();
        pedido.setProdutos(produtos);

        //Método para adicionar produtos
        adicionarProdutos(pedido);

        //Calcular valor total do pedido
       pedido.setValorPedido(calcularValorPedido(pedido));

        //status do pedido
        pedido.setStatus(Pedido.StatusPedido.EM_PROCESSAMENTO);

        //Fazer um UPDATE nessa instância de cliente retornada pelo findByCpf
        //adiciona pedido à lista de pedidos do cliente
        pedido.getCliente().addPedido(pedido);

        pedidoRepository.insertPedido(pedido);

        //SALVOU Pedido NO BANCO
        //MAS AGORA PRECISA RETORNAR O PEDIDO SALVO NO BANCO COM O ID GERADO
        Pedido pedidoNovo = new Pedido();
        pedidoNovo = pedidoRepository.findByDatahoraCriacao();//Retorna o ID do Pedido mais recente
        //o ID do pedido é necessário para salvar uma nova entrega

        //Cria uma nova instância de ENTREGA e associa com a instância atual do cliente
        Entrega entrega = new Entrega();
        entrega.setEntregador("Zé");
        entrega.setStatus(Entrega.StatusEntrega.PENDENTE);
        entrega.setPedido(pedidoNovo);//Associa instância do pedido a entrega
        //instertEntrega usa o método entrega.getPedido().getId()
        //Dessa forma, é preciso chamar o método quando o Pedido já estiver salvo no banco
        entregaRepository.insertEntrega(entrega);
        pedido.setEntrega(entrega);

    }

    @Override
    public Pedido buscarPedidoPorId(int id) throws SQLException {
        return null;
    }

    @Override
    public void adicionarProdutos(Pedido pedido) throws SQLException {

        List<Produto> produtos = produtoRepository.findAllProdutos();
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
                pedido.getProdutos().add(produtoEscolhido);
                System.out.println(produtoEscolhido.getNome() + " foi adicionado ao seu pedido.");
            } else if (escolha == 0) {
                carrinhoDeCompras = false;
                System.out.println("Pedido cadastrado com sucesso!");
            } else {
                System.out.println("Opção inválida. Por favor, escolha um número válido.");
            }
        }

    }

    public double calcularValorPedido(Pedido pedido) {
        List<Produto> produtos = pedido.getProdutos();
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

        List<Pedido> pedidos = pedidoRepository.findAllPedidosByClienteCpf(cpf);

        System.out.println("Pedidos para serem entregues:");
        int contador = 1;

            for (Pedido pedido : pedidos) {
                System.out.println(contador + ". " + pedido.getStatus() +
                        " - ID do pedido:" + pedido.getId()+"  Valor do pedido:"+pedido.getValorPedido());
                contador++;
            }

        System.out.println("Digite o ID do pedido a ser entregue: ");
        int idPedido = sc.nextInt();

        Pedido pedido = pedidoRepository.findByIdPedido(idPedido);
        Entrega entrega = entregaRepository.findEntregaByPedido(idPedido);
        pedido.setEntrega(entrega);

        // PENDENTE, EM_TRANSITO,ENTREGUE, DEVOLVIDA
        int opcao;
        System.out.println("Digite uma opção referente a entrega: \n"+
                "1 - Entrega efetuada\n"+
                "2 - Não atendido\n"+
                "3 - Entrega devolvida\n");
        opcao = sc.nextInt();

        int qtTentativasEntrega;
        sc.nextLine(); // Consumir o caractere de nova linha pendente(Passando direto pelo sc.nextLine())

        switch (opcao){
            case 1:
                //Nome do recebedor:
                System.out.println("Digite o nome do recebedor: ");
                String recebedor = sc.nextLine();

                pedido.getEntrega().setRecebedor(recebedor);
                //Qt Tentativas de entrega:
                qtTentativasEntrega = (pedido.getEntrega().getQtTentativasEntrega() + 1);
                pedido.getEntrega().setQtTentativasEntrega(qtTentativasEntrega);
                //Data da entrega:
                //data e hora atual
                LocalDateTime dataHoraAtual = LocalDateTime.now();
                // Formata a data e hora atual para o formato "yyyy-MM-dd HH:mm:ss"
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String dataHoraFormatada = dataHoraAtual.format(formatter);
                pedido.setDatahoraEntrega(dataHoraFormatada);//setar datahoraFormatada na datahoraEntrega
                pedido.getEntrega().setDataEntrega(dataHoraFormatada);//setar datahoraFormata na ENTREGA
                pedido.setDatahoraEntrega(dataHoraFormatada);//setar datahoraFormata no PEDIDO

                //atualiza o status da ENTREGA NA INSTÂNCIA do pedido
                pedido.getEntrega().setStatus(Entrega.StatusEntrega.ENTREGUE);

                //atualiza a entrega NO BANCO DE DADOS
                entregaRepository.efetuarEntrega(pedido.getEntrega());

                //atualiza também o status do PEDIDO no banco de dados
                pedido.setStatus(Pedido.StatusPedido.FINALIZADO);//Atualiza a instância
                pedidoRepository.atualizarStatus(pedido);//atualiza STATUS no banco
                pedidoRepository.adicionarDataHoraEntrega(pedido);// atualiza DataHoraEntrega do PEDIDO no banco
                break;
            case 2:
                pedido.getEntrega().setStatus(Entrega.StatusEntrega.PENDENTE);
                //atualiza o status da entrega no banco de dados
                entregaRepository.atualizarStatus(pedido.getEntrega());

                //atualizar qtTentativasEntrega
                qtTentativasEntrega = (pedido.getEntrega().getQtTentativasEntrega() + 1);
                pedido.getEntrega().setQtTentativasEntrega(qtTentativasEntrega);
                entregaRepository.atualizarQtTentativasEntrega(pedido.getEntrega());

                //atualiza também o status do PEDIDO no banco de dados
                pedido.setStatus(Pedido.StatusPedido.ENVIADO);//Atualiza a instância
                pedidoRepository.atualizarStatus(pedido);//Atualiza no banco
                System.out.println("Destinatário ausente\n Entrega Pendente");
                break;
            case 3:
                pedido.getEntrega().setStatus(Entrega.StatusEntrega.DEVOLVIDA);
                //atualiza o status da entrega no banco de dados
                entregaRepository.atualizarStatus(pedido.getEntrega());
                //atualiza também o status do PEDIDO no banco de dados
                pedido.setStatus(Pedido.StatusPedido.CANCELADO);//Atualiza a instância
                pedidoRepository.atualizarStatus(pedido);//Atualiza no banco
                System.out.println("Entrega Devolvida");
                break;
        }

    }

}
