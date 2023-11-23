package domain;

import java.time.LocalDateTime;
import java.util.List;

public class Pedido {

    private int id;
    private String datahoraCriacao;
    private String datahoraEntrega;
    private double valorPedido;
    private StatusPedido status;
    private int idCliente;

    // Enum para representar os diferentes estados de um pedido
    public enum StatusPedido {
        AGUARDANDO_PAGAMENTO,
        EM_PROCESSAMENTO,
        ENVIADO,
        FINALIZADO,
        CANCELADO;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDatahoraCriacao() {
        return datahoraCriacao;
    }

    public void setDatahoraCriacao(String datahoraCriacao) {
        this.datahoraCriacao = datahoraCriacao;
    }

    public String getDatahoraEntrega() {
        return datahoraEntrega;
    }

    public void setDatahoraEntrega(String datahoraEntrega) {
        this.datahoraEntrega = datahoraEntrega;
    }

    public double getValorPedido() {
        return valorPedido;
    }

    public void setValorPedido(double valorPedido) {
        this.valorPedido = valorPedido;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }
}
