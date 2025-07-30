package Controller;

import Model.Conexiones;
import Model.Factura;
import Model.Plan;
import Model.Cliente;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FacturaControlador {

    public boolean crearFactura(Factura factura) {
        String sqlFactura = "INSERT INTO facturas (numeroFactura, cedulaCliente, fecha, total) VALUES (?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_facturas (numeroFactura, idPlan, pagoPlan) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement psFactura = null;
        PreparedStatement psDetalle = null;

        try {
            conn = Conexiones.conectar();
            conn.setAutoCommit(false);

            psFactura = conn.prepareStatement(sqlFactura);
            psFactura.setString(1, factura.getNumeroFactura());
            psFactura.setString(2, factura.getCliente().getCedula());
            psFactura.setString(3, factura.getFecha().toString());
            psFactura.setDouble(4, factura.calcularTotal());
            psFactura.executeUpdate();

            psDetalle = conn.prepareStatement(sqlDetalle);
            for (Plan p : factura.getPlanes()) {
                psDetalle.setString(1, factura.getNumeroFactura());
                psDetalle.setInt(2, p.getId());
                psDetalle.setDouble(3, p.calcularPagoMensual());
                psDetalle.addBatch();
            }
            psDetalle.executeBatch();

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("[ERROR] Error al crear factura: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("[ERROR] Rollback fallido: " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                if (psFactura != null) {
                    psFactura.close();
                }
                if (psDetalle != null) {
                    psDetalle.close();
                }
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("[ERROR] Error al cerrar conexiones: " + e.getMessage());
            }
        }
    }

    public List<Factura> listarFacturas() {
        List<Factura> facturas = new ArrayList<>();
        String sql = "SELECT f.numeroFactura, f.cedulaCliente, f.fecha, f.total, "
                + "c.nombre, c.apellido, c.ciudad, c.barrio, c.calle, c.correo "
                + "FROM facturas f JOIN clientes c ON f.cedulaCliente = c.cedula";

        try (Connection conn = Conexiones.conectar(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String numeroFactura = rs.getString("numeroFactura");
                String cedula = rs.getString("cedulaCliente");
                LocalDate fecha = LocalDate.parse(rs.getString("fecha"));
                double total = rs.getDouble("total");  // Obtén total

                Cliente cliente = new Cliente(
                        cedula,
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("ciudad"),
                        rs.getString("barrio"),
                        rs.getString("calle"),
                        rs.getString("correo")
                );

                List<Plan> planes = obtenerPlanesPorFactura(numeroFactura);

                Factura factura = new Factura(numeroFactura, cliente, planes);
                factura.setTotal(total); 
                factura.setFecha(fecha); 

                facturas.add(factura);
            }

        } catch (SQLException e) {
            System.err.println("[ERROR] Error al listar facturas: " + e.getMessage());
        }

        return facturas;
    }

    private List<Plan> obtenerPlanesPorFactura(String numeroFactura) {
        List<Plan> planes = new ArrayList<>();
        String sql = "SELECT p.* FROM detalle_facturas df JOIN planes p ON df.idPlan = p.id WHERE df.numeroFactura = ?";

        try (Connection conn = Conexiones.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, numeroFactura);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String tipo = rs.getString("tipo");
                    Plan plan = null;
                    switch (tipo) {
                        case "PostPagoMinutosMegasEconomico" ->
                            plan = new Model.PlanPostPagoMinutosMegasEconomico(
                                    rs.getInt("id"),
                                    rs.getString("cedulaCliente"),
                                    rs.getString("numeroCelular"),
                                    rs.getString("marcaCelular"),
                                    rs.getString("modeloCelular"),
                                    rs.getInt("minutos"),
                                    rs.getDouble("costoMinuto"),
                                    rs.getInt("megas"),
                                    rs.getDouble("costoGiga"),
                                    rs.getDouble("descuento")
                            );
                        case "PostPagoMinutos" ->
                            plan = new Model.PlanPostPagoMinutos(
                                    rs.getInt("id"),
                                    rs.getString("cedulaCliente"),
                                    rs.getString("numeroCelular"),
                                    rs.getString("marcaCelular"),
                                    rs.getString("modeloCelular"),
                                    rs.getInt("minutosNacionales"),
                                    rs.getDouble("costoMinutoNacional"),
                                    rs.getInt("minutosInternacionales"),
                                    rs.getDouble("costoMinutoInternacional")
                            );
                        case "PostPagoMegas" ->
                            plan = new Model.PlanPostPagoMegas(
                                    rs.getInt("id"),
                                    rs.getString("cedulaCliente"),
                                    rs.getString("numeroCelular"),
                                    rs.getString("marcaCelular"),
                                    rs.getString("modeloCelular"),
                                    rs.getInt("megas"),
                                    rs.getDouble("costoGiga"),
                                    rs.getDouble("tarifaBase")
                            );
                        case "PostPagoMinutosMegas" ->
                            plan = new Model.PlanPostPagoMinutosMegas(
                                    rs.getInt("id"),
                                    rs.getString("cedulaCliente"),
                                    rs.getString("numeroCelular"),
                                    rs.getString("marcaCelular"),
                                    rs.getString("modeloCelular"),
                                    rs.getInt("minutos"),
                                    rs.getDouble("costoMinuto"),
                                    rs.getInt("megas"),
                                    rs.getDouble("costoGiga")
                            );
                        default -> {
                            System.err.println("[WARN] Tipo de plan desconocido: " + tipo);
                        }
                    }
                    if (plan != null) {
                        planes.add(plan);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al obtener planes por factura: " + e.getMessage());
        }
        return planes;
    }

    public String generarNumeroFactura() {
        return "FAC-" + System.currentTimeMillis();
    }
}