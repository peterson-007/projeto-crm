package service;

import domain.Cliente;
import domain.Endereco;
import repository.ClienteRepository;
import repository.ContatoRepository;
import repository.EnderecoRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EnderecoServiceImpl implements EnderecoService {

    //Injetando VARIAVEIS
    private ClienteRepository clienteRepository;
    private EnderecoRepository enderecoRepository;

    // Injetando CONSTRUTORES
    public EnderecoServiceImpl(ClienteRepository clienteRepository, EnderecoRepository enderecoRepository) {
        this.clienteRepository = clienteRepository;
        this.enderecoRepository = enderecoRepository;
    }

    Scanner sc = new Scanner(System.in);


    public boolean cadastrarEndereco(int idCliente) throws SQLException{

        Endereco endereco = new Endereco();

        endereco.setIdClient(idCliente);

        System.out.println("CADASTRAR ENDEREÇO\n Digite a rua:");
        String rua = sc.nextLine();
        endereco.setRua(rua);
        System.out.println("Digite o número da residência: ");
        String numero = sc.nextLine();
        endereco.setNumero(numero);
        System.out.println("Digite o complemento: ");
        String complemento = sc.nextLine();
        endereco.setComplemento(complemento);
        System.out.println("Digite o bairro: ");
        String bairro = sc.nextLine();
        endereco.setBairro(bairro);
        System.out.println("Digite a cidade: ");
        String cidade = sc.nextLine();
        endereco.setCidade(cidade);
        System.out.println("Digite o CEP: ");
        String cep = sc.nextLine();
        endereco.setCep(cep);

        boolean enderecoCadastrado = enderecoRepository.insertEndereco(endereco);
        return enderecoCadastrado;
    }

    public void adicionarEnderecosAoCliente(Cliente cliente) throws SQLException{
        List<Endereco> enderecos = new ArrayList<>();

        char resposta;
        do{
            Endereco endereco = new Endereco();

            endereco.setIdClient(cliente.getId());

            System.out.println("CADASTRAR ENDEREÇO\n Digite a rua:");
            String rua = sc.nextLine();
            endereco.setRua(rua);
            System.out.println("Digite o número da residência: ");
            String numero = sc.nextLine();
            endereco.setNumero(numero);
            System.out.println("Digite o complemento: ");
            String complemento = sc.nextLine();
            endereco.setComplemento(complemento);
            System.out.println("Digite o bairro: ");
            String bairro = sc.nextLine();
            endereco.setBairro(bairro);
            System.out.println("Digite a cidade: ");
            String cidade = sc.nextLine();
            endereco.setCidade(cidade);
            System.out.println("Digite o CEP: ");
            String cep = sc.nextLine();
            endereco.setCep(cep);

            System.out.println("Quer adicionar mais um contato? (s/n): ");
            resposta = sc.next().charAt(0);
            sc.nextLine(); // Consumir a quebra de linha após a leitura do char

        } while(resposta == 's');
    }
}
