package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {

    //Padrão de projeto Singleton
    private static DataBaseConnection instance;

    private Connection connection;

    //Método que gerencia a criação de instâncias da classe
    public static DataBaseConnection getInstance(){

        // if sendo usado como uma restrição, permitindo a criação de apenas UMA instância
        if (instance == null){
            instance = new DataBaseConnection();
        }

        //Caso já exista uma instãncia, ela será REUTILIZADA
        return instance;
    }

    private DataBaseConnection() {
        //tenta criar a conexão
        try {
            this.createConnection();
            //Caso ocorra um erro SQLException exibe a mensagem
        } catch (SQLException e){
            System.out.println("SQL error: " + e.getMessage());
        }
    }

    private void createConnection() throws SQLException {
        this.connection = DriverManager
                .getConnection("jdbc:postgresql://localhost:3306/crm",
                        "postgres",
                        "zero1");
        this.connection.setAutoCommit(true);
    }

    public Connection getConnection() {
        return this.connection;
    }

    //fechar conexão
    public void closeConnection() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }

}
