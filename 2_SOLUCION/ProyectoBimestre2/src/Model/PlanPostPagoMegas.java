package Model;

public class PlanPostPagoMegas extends Plan {

    private int megas;
    private double costoGiga;
    private double tarifaBase;

    public PlanPostPagoMegas() {
    }

    public PlanPostPagoMegas(int id, String cedulaCliente, String numeroCelular, String marcaCelular,
            String modeloCelular, int megas, double costoGiga, double tarifaBase) {
        super(id, cedulaCliente, numeroCelular, marcaCelular, modeloCelular);
        this.megas = megas;
        this.costoGiga = costoGiga;
        this.tarifaBase = tarifaBase;
    }

    @Override
    public String getTipoPlan() {
        return "PostPagoMegas";
    }

    @Override
    public double calcularPagoMensual() {
        return tarifaBase + (megas * costoGiga);
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

    public double getTarifaBase() {
        return tarifaBase;
    }

    public void setTarifaBase(double tarifaBase) {
        this.tarifaBase = tarifaBase;
    }

    @Override
    public String toString() {
        return """
           ┌───────────────── Plan PostPago Megas ──────────────────┐
           │ ID             : %d
           │ Cliente        : %s
           │ Número         : %s
           │ Marca          : %s
           │ Modelo         : %s
           │ Megas (GB)     : %d
           │ Costo Giga     : $%.2f
           │ Tarifa Base    : $%.2f
           └────────────────────────────────────────────────────────┘
           """.formatted(id, cedulaCliente, numeroCelular, marcaCelular, modeloCelular,
                megas, costoGiga, tarifaBase);
    }

}
