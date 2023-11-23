package service;

import domain.Cliente;
import domain.Contato;
import repository.ClienteRepository;
import repository.ContatoRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ContatoServiceImpl implements ContatoService {

    //Injetando VARIAVEIS
    private ClienteRepository clienteRepository;
    private ContatoRepository contatoRepository;

    // Injetando CONSTRUTORES
    public ContatoServiceImpl(ClienteRepository clienteRepository, ContatoRepository contatoRepository) {
        this.clienteRepository = clienteRepository;
        this.contatoRepository = contatoRepository;
    }

    Scanner sc = new Scanner(System.in);


    public boolean cadastrarContato(int idCliente) throws SQLException {
        //Cadastrar CONTATO do cliente
        Contato contato = new Contato();

        contato.setIdCliente(idCliente);

        //cadastro e validação do EMAIL
        boolean emailValidado = false;

        Scanner sc = new Scanner(System.in);

        while (!emailValidado) {
            System.out.println("CADASTRAR CONTATO\n Digite o email do cliente:");
            String email = sc.nextLine();
            if (contatoRepository.verificarEmail(email)) {
                System.out.println("Já existe um cliente com o mesmo EMAIL.\nDigite novamente:");
            } else {
                contato.setEmail(email);
                emailValidado = true;
            }
        }

        System.out.println("Digite o telefone do cliente");
        String telefone = sc.nextLine();
        contato.setTelefone(telefone);
        boolean contatoCadastrado = contatoRepository.insertContato(contato);
        return contatoCadastrado;
    }

    public void adicionarContatosAoCliente(Cliente cliente) throws SQLException {
        List<Contato> contatos = new ArrayList<>();

        char resposta;
        do {
            System.out.println("Digite o telefone: ");
            String telefone = sc.nextLine();

            System.out.println("Digite o email: ");
            String email = sc.nextLine();

            Contato contato = new Contato();
            contato.setTelefone(telefone);
            contato.setEmail(email);
            contato.setIdCliente(cliente.getId());
            contatos.add(contato);

            System.out.println("Quer adicionar mais um contato? (s/n): ");
            resposta = sc.next().charAt(0);
            sc.nextLine(); // Consumir a quebra de linha após a leitura do char

        } while (resposta == 's');

        contatoRepository.insertContatos(contatos);
    }
}
