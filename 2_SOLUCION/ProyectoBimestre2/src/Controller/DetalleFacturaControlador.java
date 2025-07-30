package Controller;

import Model.Conexiones;
import Model.DetalleFactura;
import Model.Plan;
import Model.PlanPostPagoMegas;
import Model.PlanPostPagoMinutos;
import Model.PlanPostPagoMinutosMegas;
import Model.PlanPostPagoMinutosMegasEconomico;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetalleFacturaControlador {

    public boolean agregarDetalleFactura(DetalleFactura detalle) {
        String sql = "INSERT INTO detalle_facturas (numeroFactura, idPlan, pagoPlan) VALUES (?, ?, ?)";
        try (Connection conn = Conexiones.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, detalle.getNumeroFactura());
            ps.setInt(2, detalle.getPlan().getId());
            ps.setDouble(3, detalle.getPagoPlan());

            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("[ERROR] No se pudo insertar detalle factura: " + e.getMessage());
            return false;
        }
    }

    public List<DetalleFactura> listarDetallesPorFactura(String numeroFactura) {
        List<DetalleFactura> lista = new ArrayList<>();
        String sql = "SELECT df.id, df.numeroFactura, df.idPlan, df.pagoPlan, p.tipo, p.cedulaCliente, p.numeroCelular, p.marcaCelular, p.modeloCelular, "
                + "p.minutos, p.costoMinuto, p.minutosNacionales, p.costoMinutoNacional, p.minutosInternacionales, p.costoMinutoInternacional, "
                + "p.megas, p.costoGiga, p.descuento, p.tarifaBase "
                + "FROM detalle_facturas df "
                + "INNER JOIN planes p ON df.idPlan = p.id "
                + "WHERE df.numeroFactura = ?";

        try (Connection conn = Conexiones.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, numeroFactura);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    String tipo = rs.getString("tipo");
                    Plan plan = null;

                    switch (tipo) {
                        case "PostPagoMinutosMegasEconomico" ->
                            plan = new PlanPostPagoMinutosMegasEconomico(
                                    rs.getInt("idPlan"),
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
                            plan = new PlanPostPagoMinutos(
                                    rs.getInt("idPlan"),
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
                            plan = new PlanPostPagoMegas(
                                    rs.getInt("idPlan"),
                                    rs.getString("cedulaCliente"),
                                    rs.getString("numeroCelular"),
                                    rs.getString("marcaCelular"),
                                    rs.getString("modeloCelular"),
                                    rs.getInt("megas"),
                                    rs.getDouble("costoGiga"),
                                    rs.getDouble("tarifaBase")
                            );
                        case "PostPagoMinutosMegas" ->
                            plan = new PlanPostPagoMinutosMegas(
                                    rs.getInt("idPlan"),
                                    rs.getString("cedulaCliente"),
                                    rs.getString("numeroCelular"),
                                    rs.getString("marcaCelular"),
                                    rs.getString("modeloCelular"),
                                    rs.getInt("minutos"),
                                    rs.getDouble("costoMinuto"),
                                    rs.getInt("megas"),
                                    rs.getDouble("costoGiga")
                            );
                        default -> System.err.println("[WARN] Tipo de plan desconocido: " + tipo);
                    }

                    if (plan != null) {
                        DetalleFactura detalle = new DetalleFactura(
                                rs.getInt("id"),
                                rs.getString("numeroFactura"),
                                plan,
                                rs.getDouble("pagoPlan")
                        );
                        lista.add(detalle);
                    }
                }

            }

        } catch (SQLException e) {
            System.err.println("[ERROR] Error al listar detalles factura: " + e.getMessage());
        }

        return lista;
    }

}