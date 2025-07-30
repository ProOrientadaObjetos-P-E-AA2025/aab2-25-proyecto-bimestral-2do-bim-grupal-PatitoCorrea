package Model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Factura {

    private String numeroFactura;
    private Cliente cliente;
    private List<Plan> planes;
    private LocalDate fecha;
    private double total; 

    public Factura(String numeroFactura, Cliente cliente, List<Plan> planes) {
        this.numeroFactura = numeroFactura;
        this.cliente = cliente;
        this.planes = planes;
        this.fecha = LocalDate.now();
        this.total = calcularTotal();
    }

    public Factura(String numeroFactura, Cliente cliente, List<Plan> planes, double total) {
        this.numeroFactura = numeroFactura;
        this.cliente = cliente;
        this.planes = planes;
        this.fecha = LocalDate.now();
        this.total = total;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
    
    public void setFecha(LocalDate fecha) {
    this.fecha = fecha;
}

    public double calcularTotal() {
        if (total > 0) {
            return total;
        }
        return planes.stream().mapToDouble(Plan::calcularPagoMensual).sum();
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<Plan> getPlanes() {
        return planes;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("┌───────────────────────────── FACTURA Mov-UTPL ─────────────────────────────┐\n");
        sb.append(String.format("│ Número de Factura : %-50s │\n", numeroFactura));
        sb.append(String.format("│ Fecha            : %-50s │\n", fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        sb.append("├────────────────────────────── Cliente ────────────────────────────────┤\n");

        String[] clienteLines = cliente.toString().split("\n");
        for (String line : clienteLines) {
            sb.append(String.format("│ %-70s │\n", line));
        }

        sb.append("├──────────────────────────── Detalle de Planes ──────────────────────────┤\n");

        for (Plan p : planes) {
            String[] planLines = p.toString().split("\n");
            for (String line : planLines) {
                sb.append(String.format("│ %-70s │\n", line));
            }
            sb.append(String.format("│ %-30s : $%10.2f %-27s │\n", "Pago mensual", p.calcularPagoMensual(), ""));
            sb.append("├───────────────────────────────────────────────────────────────────────────┤\n");
        }

        sb.append(String.format("│ %-30s : $%10.2f %-27s │\n", "TOTAL A PAGAR", calcularTotal(), ""));
        sb.append("└───────────────────────────────────────────────────────────────────────────┘\n");
        return sb.toString();
    }

    private static String generarNumeroFactura() {
        return "FAC-" + System.currentTimeMillis();
    }
}