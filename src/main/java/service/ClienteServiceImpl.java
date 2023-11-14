package service;

import domain.Cliente;
import domain.Contato;
import domain.Endereco;
import repository.ClienteRepository;
import repository.ContatoRepository;
import repository.EnderecoRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ClienteServiceImpl implements ClienteService{

    //Injetando VARIAVEIS
    private ClienteRepository clienteRepository;
    private ContatoRepository contatoRepository;
    private EnderecoRepository enderecoRepository;

    // Injetando CONSTRUTORES
    public ClienteServiceImpl(ClienteRepository clienteRepository, ContatoRepository contatoRepository, EnderecoRepository enderecoRepository) {
        this.clienteRepository = clienteRepository;
        this.contatoRepository = contatoRepository;
        this.enderecoRepository = enderecoRepository;
    }

    Scanner sc = new Scanner(System.in);

    public void cadastrarCliente() throws SQLException {
        // Lógica para cadastrar o cliente

        Cliente cliente = new Cliente();

        System.out.println("Digite o nome do cliente:");
        String nome = sc.nextLine();
        cliente.setNome(nome);

        //cadastro e validação do CPF
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

        boolean clienteCadastrado = clienteRepository.insertCliente(cliente);

        //Cadastrar CONTATO do cliente
        Contato contato = new Contato();
        //Associa a instância atual do cliente com o contato
        //Precisa bucar o cliente no banco para obter o ID gerado
        cliente = clienteRepository.findByCpf(cliente.getCpf());
        contato.setCliente(cliente);

        //cadastro e validação do EMAIL
        boolean emailValidado = false;
        sc.nextLine(); // Consumir o caractere de nova linha pendente(Passando direto pelo sc.nextLine())
        while (!emailValidado) {
            System.out.println("Digite o email do cliente:");
            String email = sc.nextLine();
            if (clienteRepository.verificarEmail(email)) {
                System.out.println("Já existe um cliente com o mesmo EMAIL.\nDigite novamente:");
            } else {
                contato.setEmail(email);
                emailValidado = true;
            }
        }

        System.out.println("Digite o telefone do cliente");
        String telefone = sc.nextLine();
        contato.setTelefone(telefone);
        cliente.addContato(contato);//DÚVIDA quanto à necessidade de usar esse método !_!_!_!_!_!_!
        boolean contatoCadastrado = contatoRepository.insertContato(contato);

        //Cadastrar ENDERECO do cliente
        Endereco endereco = new Endereco();
        //Associa a instância atual do cliente com o endereco
        endereco.setCliente(cliente);
        System.out.println("CADASTRO DO ENDEREÇO\n Digite a rua:");
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
        cliente.addEndereco(endereco);//DÚVIDA quanto à necessidade de usar esse método !_!_!_!_!_!_!
        boolean enderecoCadastrado = enderecoRepository.insertEndereco(endereco);

       if (clienteCadastrado && contatoCadastrado && enderecoCadastrado){
           System.out.println("Cliente cadastrado com sucesso!");
       } else {
           System.out.println("Falha no cadastro!");
       }

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
            cliente.addContato(contato);// -!_!_!_!_!
            System.out.println("CONTATO:" +
                    "\nEmail: "+contato.getEmail()+
                    "\nTelefone: "+contato.getTelefone());

            Endereco endereco = enderecoRepository.findByIdCliente(cliente.getId());
            cliente.addEndereco(endereco);//!_!_!_!_!_!
            System.out.println("ENDEREÇO: "+
                    "\nRua: "+endereco.getRua()+
                    "\nNúmero: "+endereco.getNumero()+
                    "\nComplemento: "+endereco.getComplemento()+
                    "\nBairro: "+endereco.getBairro()+
                    "\nCidade: "+endereco.getCidade()+
                    "\nCEP: "+endereco.getCep()); */
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
