package domain;

public class Entrega {

    private int id;
    private String entregador;
    private String recebedor;
    private int qtTentativasEntrega;
    private String dataEntrega;
    private StatusEntrega status;
    private Pedido pedido;

    // Enum para representar os diferentes estados de entrega
    public enum StatusEntrega {
        PENDENTE,
        EM_TRANSITO,
        ENTREGUE,
        DEVOLVIDA;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEntregador() {
        return entregador;
    }

    public void setEntregador(String entregador) {
        this.entregador = entregador;
    }

    public String getRecebedor() {
        return recebedor;
    }

    public void setRecebedor(String recebedor) {
        this.recebedor = recebedor;
    }

    public int getQtTentativasEntrega() {
        return qtTentativasEntrega;
    }

    public void setQtTentativasEntrega(int qtTentativasEntrega) {
        this.qtTentativasEntrega = qtTentativasEntrega;
    }

    public String getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(String dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public StatusEntrega getStatus() {
        return status;
    }

    public void setStatus(StatusEntrega status) {
        this.status = status;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
}
