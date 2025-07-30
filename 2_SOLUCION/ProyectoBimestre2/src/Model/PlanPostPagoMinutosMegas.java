package Model;

public class PlanPostPagoMinutosMegas extends Plan {

    private int minutos;
    private double costoMinuto;
    private int megas;
    private double costoGiga;

    public PlanPostPagoMinutosMegas() {
    }

    public PlanPostPagoMinutosMegas(int id, String cedulaCliente, String numeroCelular, String marcaCelular,
            String modeloCelular, int minutos, double costoMinuto,
            int megas, double costoGiga) {
        super(id, cedulaCliente, numeroCelular, marcaCelular, modeloCelular);
        this.minutos = minutos;
        this.costoMinuto = costoMinuto;
        this.megas = megas;
        this.costoGiga = costoGiga;
    }

    @Override
    public String getTipoPlan() {
        return "PostPagoMinutosMegas";
    }

    @Override
    public double calcularPagoMensual() {
        return (minutos * costoMinuto) + (megas * costoGiga);
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

    @Override
    public String toString() {
        return """
           ┌─────────────── Plan PostPago Minutos Megas ────────────────┐
           │ ID           : %d
           │ Cliente      : %s
           │ Número       : %s
           │ Marca        : %s
           │ Modelo       : %s
           │ Minutos      : %d
           │ Costo Minuto : $%.2f
           │ Megas (GB)   : %d
           │ Costo Giga   : $%.2f
           └────────────────────────────────────────────────────────────┘
           """.formatted(id, cedulaCliente, numeroCelular, marcaCelular, modeloCelular,
                minutos, costoMinuto, megas, costoGiga);
    }

}
