package service;

import domain.Cliente;
import domain.Contato;
import domain.Endereco;
import repository.ClienteRepository;
import repository.ContatoRepository;
import repository.EnderecoRepository;

import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClienteServiceImpl implements ClienteService{

    // Injetando VARIÁVEIS
    private ClienteRepository clienteRepository;
    private ContatoRepository contatoRepository;
    private EnderecoRepository enderecoRepository;

    private ContatoService contatoService;
    private EnderecoService enderecoService;

    // Injetando CONSTRUTOR
    public ClienteServiceImpl(ClienteRepository clienteRepository, ContatoRepository contatoRepository, EnderecoRepository enderecoRepository) {
        this.clienteRepository = clienteRepository;
        this.contatoRepository = contatoRepository;
        this.enderecoRepository = enderecoRepository;

        // Criação das instâncias de ContatoServiceImpl e EnderecoServiceImpl após a inicialização dos repositórios
        this.contatoService = new ContatoServiceImpl(clienteRepository, contatoRepository);
        this.enderecoService = new EnderecoServiceImpl(clienteRepository, enderecoRepository);

    }

    Scanner sc = new Scanner(System.in);


    public void cadastrarCliente() {

        try {
        // Lógica para cadastrar o cliente
        Cliente cliente = obterDetalhesCliente();

        boolean clienteCadastrado = clienteRepository.insertCliente(cliente);

        //puxar cliente pelo CPF para obter ID gerado pelo banco
        cliente = clienteRepository.findByCpf(cliente.getCpf());

        //Cadastrar CONTATO do cliente
        boolean contatoCadastrado = contatoService.cadastrarContato(cliente.getId());

        //Cadastrar ENDERECO do cliente
        boolean enderecoCadastrado = enderecoService.cadastrarEndereco(cliente.getId());

       if (clienteCadastrado && contatoCadastrado && enderecoCadastrado){
           System.out.println("Cliente cadastrado com sucesso!");
       } else {
           System.out.println("Falha no cadastro!");
       }

        } catch (SQLException e) {
            e.printStackTrace(); // Ou tratamento apropriado
        }
    }

    private Cliente obterDetalhesCliente() throws SQLException {
        Cliente cliente = new Cliente();

        System.out.println("Digite o nome do cliente:");
        String nome = sc.nextLine();
        cliente.setNome(nome);

        // Cadastro e validação do CPF
        boolean cpfValidado = false;
        while (!cpfValidado) {
            System.out.println("Digite o cpf do cliente:");
            String cpf = sc.nextLine();
            if (clienteRepository.verificarCpf(cpf)) {
                System.out.println("Já existe um cliente com o mesmo CPF.\nDigite novamente:");
            } else {
                cliente.setCpf(cpf);
                cpfValidado = true;
            }
        }

        System.out.println("Digite a data de nascimento do cliente:");
        String dataNascimento = sc.nextLine();
        cliente.setDataDeNascimento(dataNascimento);
        System.out.println("Digite a idade atual do cliente:");
        int idadeAtual = sc.nextInt();
        cliente.setIdadeAtual(idadeAtual);
        System.out.println("Digite o genero do cliente: ");
        String genero = String.valueOf(sc.next().charAt(0));
        cliente.setGenero(genero);

        return cliente;
    }

    @Override
    public Cliente buscarClientePorId(int id) throws SQLException {
        Cliente cliente = clienteRepository.findById(id);
        if (cliente == null) {
            System.out.println("Cliente não encontrado com o ID: " + id);
        }
        return cliente;
    }

    public void listarClientesCadastrados() throws SQLException {

        List<Cliente> clientes = clienteRepository.findAll();

        for(Cliente cliente: clientes){
            System.out.println("\nNOME DO CLIENTE: "+cliente.getNome()+
                    "\nID: "+cliente.getId()+
                    "\nCPF: "+cliente.getCpf()+
                    "\nData de nascimento: "+cliente.getDataDeNascimento()+
                    "\nIdade: "+cliente.getIdadeAtual()+
                    "\nGênero: "+cliente.getGenero());

            /*
            Contato contato = contatoRepository.findByIdCliente(cliente.getId());
            System.out.println("CONTATO:" +
                    "\nEmail: "+contato.getEmail()+
                    "\nTelefone: "+contato.getTelefone());

            Endereco endereco = enderecoRepository.findByIdCliente(cliente.getId());
            System.out.println("ENDEREÇO: "+
                    "\nRua: "+endereco.getRua()+
                    "\nNúmero: "+endereco.getNumero()+
                    "\nComplemento: "+endereco.getComplemento()+
                    "\nBairro: "+endereco.getBairro()+
                    "\nCidade: "+endereco.getCidade()+
                    "\nCEP: "+endereco.getCep()); */
        }
    }

    public void atualizarCliente() throws SQLException{

        System.out.println("ATUALIZAR DADOS DO CLIENTE");
        listarClientesCadastrados();

        System.out.println("\nDigite o CPF do cliente que deseja atualizar os dados: ");
        String cpf = sc.nextLine();
        Cliente cliente = clienteRepository.findByCpf(cpf);

        System.out.println("Digite o nome: ");
        String nome = sc.nextLine();
        cliente.setNome(nome);

        System.out.println("Digite a data de nascimento: ");
        String dataNascimento = sc.nextLine();
        cliente.setDataDeNascimento(dataNascimento);

        System.out.println("Digite a idade atual: ");
        int idadeAtual = sc.nextInt();
        cliente.setIdadeAtual(idadeAtual);

        boolean clienteAtualizado = clienteRepository.update(cliente);

        if (clienteAtualizado){
            System.out.println("Cadastro atualizado");
        } else{
            System.out.println("Falha na atualização do cadastro");
        }

        // Adicionar Contato
        adicionarContato(cliente);

        //Adicionar Endereco
        adicionarEndereco(cliente);

    }

    private void adicionarContato(Cliente cliente) throws SQLException {

        System.out.println("Deseja adicionar contatos ao cliente? (s/n): ");
        char adicionarContato = sc.next().charAt(0);
        sc.nextLine(); // Consumir a quebra de linha após a leitura do char

        if (adicionarContato == 's') {
            contatoService.adicionarContatosAoCliente(cliente);
        }
    }

    private void adicionarEndereco(Cliente cliente) throws SQLException {

        System.out.println("Deseja adicionar endereços ao cliente? (s/n):");
        char adicionarEndereco = sc.next().charAt(0);
        sc.nextLine(); // Consumir a quebra de linha após a leitura do char

        if (adicionarEndereco == 's') {
            enderecoService.adicionarEnderecosAoCliente(cliente);
        }
    }

    public void deletarCliente() throws SQLException{

        listarClientesCadastrados();

        System.out.println("Digite o ID do cliente que deseja excluir: ");
        int idCliente = sc.nextInt();
        boolean deleted = clienteRepository.delete(idCliente);

        if(deleted){
            System.out.println("Cliente deletado com sucesso");
        } else {
            System.out.println("Erro na exclusão do cliente");
        }
    }
}
