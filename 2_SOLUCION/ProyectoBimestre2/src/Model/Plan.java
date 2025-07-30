package Model;

public abstract class Plan {
    protected int id;
    protected String cedulaCliente;
    protected String numeroCelular;
    protected String marcaCelular;
    protected String modeloCelular;

    public Plan() {}

    public Plan(int id, String cedulaCliente, String numeroCelular, String marcaCelular, String modeloCelular) {
        this.id = id;
        this.cedulaCliente = cedulaCliente;
        this.numeroCelular = numeroCelular;
        this.marcaCelular = marcaCelular;
        this.modeloCelular = modeloCelular;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCedulaCliente() { return cedulaCliente; }
    public void setCedulaCliente(String cedulaCliente) { this.cedulaCliente = cedulaCliente; }
    public String getNumeroCelular() { return numeroCelular; }
    public void setNumeroCelular(String numeroCelular) { this.numeroCelular = numeroCelular; }
    public String getMarcaCelular() { return marcaCelular; }
    public void setMarcaCelular(String marcaCelular) { this.marcaCelular = marcaCelular; }
    public String getModeloCelular() { return modeloCelular; }
    public void setModeloCelular(String modeloCelular) { this.modeloCelular = modeloCelular; }

    public abstract String getTipoPlan();

    public abstract double calcularPagoMensual();

    @Override
    public String toString() {
        return "ID: " + id + ", Cliente: " + cedulaCliente + ", Celular: " + numeroCelular +
               ", Marca: " + marcaCelular + ", Modelo: " + modeloCelular;
    }
}
