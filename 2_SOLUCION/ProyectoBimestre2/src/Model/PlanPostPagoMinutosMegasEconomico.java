package Model;

public class PlanPostPagoMinutosMegasEconomico extends Plan {

    private int minutos;
    private double costoMinuto;
    private int megas; // en GB
    private double costoGiga;
    private double porcentajeDescuento;

    public PlanPostPagoMinutosMegasEconomico() {
    }

    public PlanPostPagoMinutosMegasEconomico(int id, String cedulaCliente, String numeroCelular, String marcaCelular,
            String modeloCelular, int minutos, double costoMinuto,
            int megas, double costoGiga, double porcentajeDescuento) {
        super(id, cedulaCliente, numeroCelular, marcaCelular, modeloCelular);
        this.minutos = minutos;
        this.costoMinuto = costoMinuto;
        this.megas = megas;
        this.costoGiga = costoGiga;
        this.porcentajeDescuento = porcentajeDescuento;
    }

    @Override
    public String getTipoPlan() {
        return "PostPagoMinutosMegasEconomico";
    }

    @Override
    public double calcularPagoMensual() {
        double pago = (minutos * costoMinuto) + (megas * costoGiga);
        double descuento = pago * (porcentajeDescuento / 100.0);
        return pago - descuento;
    }

    public int getMinutos() {
        return minutos;
    }

    public void setMinutos(int minutos) {
        this.minutos = minutos;
    }

    public double getCostoMinuto() {
        return costoMinuto;
    }

    public void setCostoMinuto(double costoMinuto) {
        this.costoMinuto = costoMinuto;
    }

    public int getMegas() {
        return megas;
    }

    public void setMegas(int megas) {
        this.megas = megas;
    }

    public double getCostoGiga() {
        return costoGiga;
    }

    public void setCostoGiga(double costoGiga) {
        this.costoGiga = costoGiga;
    }

    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(double porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    @Override
    public String toString() {
        return "┌───────────── Plan PostPago Minutos Megas Económico ─────────────┐\n"
                + "│ ID            : " + id + "\n"
                + "│ Cliente       : " + cedulaCliente + "\n"
                + "│ Número        : " + numeroCelular + "\n"
                + "│ Marca         : " + marcaCelular + "\n"
                + "│ Modelo        : " + modeloCelular + "\n"
                + "│ Minutos       : " + minutos + "\n"
                + "│ Costo Minuto  : $" + String.format("%.2f", costoMinuto) + "\n"
                + "│ Megas (GB)    : " + megas + "\n"
                + "│ Costo Giga    : $" + String.format("%.2f", costoGiga) + "\n"
                + "│ Descuento (%) : " + String.format("%.2f", porcentajeDescuento) + "%\n"
                + "└───────────────────────────────────────────────────────────────┘\n";
    }

}
