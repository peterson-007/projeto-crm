package service;


import domain.Produto;
import repository.ProdutoRepository;

import java.sql.SQLException;
import java.util.Scanner;

public class ProdutoServiceImpl implements ProdutoService{

    private ProdutoRepository produtoRepository;

    public ProdutoServiceImpl(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    Scanner sc = new Scanner(System.in);

    @Override
    public void cadastrarProduto() throws SQLException {

        Produto produto = new Produto();

        //cadastro e validação do nome do produto
        boolean nomeProdutoValidado = false;
        while (!nomeProdutoValidado) {
            System.out.println("Digite o nome do produto:");
            String nomeProduto = sc.nextLine();
            if (produtoRepository.verificarNomeProduto(nomeProduto)) {
                System.out.println("Já existe um produto com o mesmo nome.\nDigite novamente:");
            } else {
                produto.setNome(nomeProduto);
                nomeProdutoValidado = true;
            }
        }

        System.out.println("Digite o preço do produto: ");
        double preco = sc.nextDouble();
        produto.setPreco(preco);

        boolean cadastrado = produtoRepository.insertProduto(produto);

        if (cadastrado){
            System.out.println("Produto cadastrado com sucesso!");
        } else {
            System.out.println("Falha no cadastro do produto!");
        }

    }


    @Override
    public Produto buscarProdutoPorId(int id) throws SQLException {
        return null;
    }
}
