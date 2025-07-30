package Model;

public class PlanPostPagoMinutos extends Plan {

    private int minutosNacionales;
    private double costoMinutoNacional;
    private int minutosInternacionales;
    private double costoMinutoInternacional;

    public PlanPostPagoMinutos() {
    }

    public PlanPostPagoMinutos(int id, String cedulaCliente, String numeroCelular, String marcaCelular,
            String modeloCelular, int minutosNacionales, double costoMinutoNacional,
            int minutosInternacionales, double costoMinutoInternacional) {
        super(id, cedulaCliente, numeroCelular, marcaCelular, modeloCelular);
        this.minutosNacionales = minutosNacionales;
        this.costoMinutoNacional = costoMinutoNacional;
        this.minutosInternacionales = minutosInternacionales;
        this.costoMinutoInternacional = costoMinutoInternacional;
    }

    @Override
    public String getTipoPlan() {
        return "PostPagoMinutos";
    }

    @Override
    public double calcularPagoMensual() {
        return (minutosNacionales * costoMinutoNacional) + (minutosInternacionales * costoMinutoInternacional);
    }

    public int getMinutosNacionales() {
        return minutosNacionales;
    }

    public void setMinutosNacionales(int minutosNacionales) {
        this.minutosNacionales = minutosNacionales;
    }

    public double getCostoMinutoNacional() {
        return costoMinutoNacional;
    }

    public void setCostoMinutoNacional(double costoMinutoNacional) {
        this.costoMinutoNacional = costoMinutoNacional;
    }

    public int getMinutosInternacionales() {
        return minutosInternacionales;
    }

    public void setMinutosInternacionales(int minutosInternacionales) {
        this.minutosInternacionales = minutosInternacionales;
    }

    public double getCostoMinutoInternacional() {
        return costoMinutoInternacional;
    }

    public void setCostoMinutoInternacional(double costoMinutoInternacional) {
        this.costoMinutoInternacional = costoMinutoInternacional;
    }

    @Override
    public String toString() {
        return """
           ┌───────────────── Plan PostPago Minutos ─────────────────┐
           │ ID                      : %d
           │ Cliente                 : %s
           │ Número                  : %s
           │ Marca                   : %s
           │ Modelo                  : %s
           │ Minutos Nacionales      : %d
           │ Costo Minuto Nacional   : $%.2f
           │ Minutos Internacionales : %d
           │ Costo Minuto Internacional : $%.2f
           └─────────────────────────────────────────────────────────┘
           """.formatted(id, cedulaCliente, numeroCelular, marcaCelular, modeloCelular,
                minutosNacionales, costoMinutoNacional, minutosInternacionales, costoMinutoInternacional);
    }

}
