package Model;

public class DetalleFactura {

    private int id;                 
    private String numeroFactura;   
    private Plan plan;              
    private double pagoPlan;        
    public DetalleFactura() {
    }

    public DetalleFactura(int id, String numeroFactura, Plan plan, double pagoPlan) {
        this.id = id; 
        this.numeroFactura = numeroFactura;
        this.plan = plan;
        this.pagoPlan = pagoPlan;
    }

    public DetalleFactura(String numeroFactura, Plan plan, double pagoPlan) {
        this.numeroFactura = numeroFactura;
        this.plan = plan;
        this.pagoPlan = pagoPlan;
    }

    public int getId() {
        return id;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public Plan getPlan() {
        return plan;
    }

    public double getPagoPlan() {
        return pagoPlan;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public void setPagoPlan(double pagoPlan) {
        this.pagoPlan = pagoPlan;
    }

    @Override
    public String toString() {
        return String.format("DetalleFactura[id=%d, factura=%s, planID=%d, pago=%.2f]",
                id, numeroFactura, plan.getId(), pagoPlan);
    }
}