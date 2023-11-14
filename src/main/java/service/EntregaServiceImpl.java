package service;

import domain.Entrega;
import domain.Pedido;
import org.w3c.dom.ls.LSOutput;
import repository.EntregaRepository;
import repository.PedidoRepository;

import java.sql.SQLException;
import java.util.Scanner;

public class EntregaServiceImpl {

    private EntregaRepository entregaRepository;
    private PedidoRepository pedidoRepository;

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
