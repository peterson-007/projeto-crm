package service;

import domain.Cliente;
import domain.Entrega;
import domain.Pedido;
import org.w3c.dom.ls.LSOutput;
import repository.ClienteRepository;
import repository.EntregaRepository;
import repository.PedidoRepository;

import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EntregaServiceImpl implements EntregaService {

    private EntregaRepository entregaRepository;
    private PedidoRepository pedidoRepository;
    private ClienteRepository clienteRepository;


    public EntregaServiceImpl(EntregaRepository entregaRepository, ClienteRepository clienteRepository) {
        this.entregaRepository = entregaRepository;
        this.clienteRepository = clienteRepository;
    }

    Scanner sc = new Scanner(System.in);

    public void cadastrarEntrega() throws SQLException {

        Entrega entrega = new Entrega();

        System.out.println("Digite o nome do entregador: ");
        String entregador = sc.nextLine();
        entrega.setEntregador(entregador);

        System.out.println("Digite o status da entrega: ");
        String status = sc.nextLine();
        entrega.setStatus(Entrega.StatusEntrega.valueOf(status));

        boolean cadastrado = entregaRepository.insertEntrega(entrega);

        if (cadastrado){
            System.out.println("Entrega cadastrada com sucesso!");
        } else {
            System.out.println("Falha no cadastro da entrega!");
        }

    }

    public void entregasConcluidas() throws SQLException {

        List<Entrega> entregasConcluidas;

        entregasConcluidas = entregaRepository.findEntregasConcluidas();

        System.out.println("ENTREGAS CONCLUÍDAS: ");

        for(Entrega entrega : entregasConcluidas){

            Cliente cliente = new Cliente();
            cliente = clienteRepository.findById(entrega.getIdPedido());

            System.out.println("Nome do cliente: "+ cliente.getNome());
            System.out.println("Entregador: "+entrega.getEntregador());
            System.out.println("Recebedor: "+entrega.getRecebedor());
            System.out.println("Tentativas de entrega: "+entrega.getQtTentativasEntrega());
            System.out.println("Data da entrega: "+entrega.getDataEntrega());
            System.out.println("Status: "+entrega.getStatus());
            System.out.println("\n");
        }
    }

    /*public void efetuarEntrega(Entrega entrega) throws SQLException {

        boolean tentativaEntrega = false;

        // PENDENTE, EM_TRANSITO,ENTREGUE, DEVOLVIDA
        int opcao;
        System.out.println("Digite uma opção referente a entrega: \n"+
                "1 - Entrega efetuada\n"+
                "2 - Não atendido\n"+
                "3 - Entrega devolvida\n");
        opcao = sc.nextInt();

        switch (opcao){
            case 1:
                entrega.setStatus(Entrega.StatusEntrega.valueOf("ENTREGUE"));
                //atualiza o status da entrega no banco de dados
                entregaRepository.atualizarStatus(entrega);
                //atualiza também o status do PEDIDO no banco de dados
                // perai pedidoRepository.atualizarStatus(entrega.);
                break;
            case 2:
                entrega.setStatus(Entrega.StatusEntrega.valueOf("PENDENTE"));
                //atualiza o status da entrega no banco de dados
                entregaRepository.atualizarStatus(entrega);
                break;
            case 3:
                entrega.setStatus(Entrega.StatusEntrega.valueOf("DEVOLVIDA"));
                //atualiza o status da entrega no banco de dados
                entregaRepository.atualizarStatus(entrega);
                break;
        }

    }*/




}
