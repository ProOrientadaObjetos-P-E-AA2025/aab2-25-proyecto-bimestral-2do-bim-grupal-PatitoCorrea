package Controller;

import Model.Conexiones;
import Model.Plan;
import Model.PlanPostPagoMegas;
import Model.PlanPostPagoMinutos;
import Model.PlanPostPagoMinutosMegas;
import Model.PlanPostPagoMinutosMegasEconomico;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlanControlador {

    public boolean agregarPlan(Plan plan) {
        String sql = "INSERT INTO planes (cedulaCliente, numeroCelular, marcaCelular, modeloCelular, minutos, costoMinuto, minutosNacionales, costoMinutoNacional, minutosInternacionales, costoMinutoInternacional, megas, costoGiga, descuento, tarifaBase, tipo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexiones.conectar(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, plan.getCedulaCliente());
            ps.setString(2, plan.getNumeroCelular());
            ps.setString(3, plan.getMarcaCelular());
            ps.setString(4, plan.getModeloCelular());

            int minutos = 0;
            double costoMinuto = 0.0;
            int minutosNacionales = 0;
            double costoMinutoNacional = 0.0;
            int minutosInternacionales = 0;
            double costoMinutoInternacional = 0.0;
            int megas = 0;
            double costoGiga = 0.0;
            double descuento = 0.0;
            double tarifaBase = 0.0;
            String tipo = "";

            if (plan instanceof PlanPostPagoMinutosMegasEconomico p) {
                minutos = p.getMinutos();
                costoMinuto = p.getCostoMinuto();
                megas = p.getMegas();
                costoGiga = p.getCostoGiga();
                descuento = p.getPorcentajeDescuento();
                tipo = "PostPagoMinutosMegasEconomico";

            } else if (plan instanceof PlanPostPagoMinutos p) {
                minutosNacionales = p.getMinutosNacionales();
                costoMinutoNacional = p.getCostoMinutoNacional();
                minutosInternacionales = p.getMinutosInternacionales();
                costoMinutoInternacional = p.getCostoMinutoInternacional();
                tipo = "PostPagoMinutos";

            } else if (plan instanceof PlanPostPagoMegas p) {
                megas = p.getMegas();
                costoGiga = p.getCostoGiga();
                tarifaBase = p.getTarifaBase();
                tipo = "PostPagoMegas";

            } else if (plan instanceof PlanPostPagoMinutosMegas p) {
                minutos = p.getMinutos();
                costoMinuto = p.getCostoMinuto();
                megas = p.getMegas();
                costoGiga = p.getCostoGiga();
                tipo = "PostPagoMinutosMegas";

            } else {
                System.err.println("[ERROR] Tipo de plan desconocido.");
                return false;
            }

            ps.setInt(5, minutos);
            ps.setDouble(6, costoMinuto);
            ps.setInt(7, minutosNacionales);
            ps.setDouble(8, costoMinutoNacional);
            ps.setInt(9, minutosInternacionales);
            ps.setDouble(10, costoMinutoInternacional);
            ps.setInt(11, megas);
            ps.setDouble(12, costoGiga);
            ps.setDouble(13, descuento);
            ps.setDouble(14, tarifaBase);
            ps.setString(15, tipo);

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                System.err.println("[ERROR] No se pudo insertar el plan.");
                return false;
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    plan.setId(generatedKeys.getInt(1));
                }
            }

            return true;

        } catch (SQLException e) {
            System.err.println("[ERROR] Al agregar plan: " + e.getMessage());
            return false;
        }
    }

    public List<Plan> listarPlanes() {
        List<Plan> lista = new ArrayList<>();
        String sql = "SELECT * FROM planes";

        try (Connection conn = Conexiones.conectar(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String tipo = rs.getString("tipo");
                Plan plan = null;
                switch (tipo) {
                    case "PostPagoMinutosMegasEconomico" ->
                        plan = new PlanPostPagoMinutosMegasEconomico(
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
                        plan = new PlanPostPagoMinutos(
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
                        plan = new PlanPostPagoMegas(
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
                        plan = new PlanPostPagoMinutosMegas(
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
                }
                if (plan != null) {
                    lista.add(plan);
                }
            }

        } catch (SQLException e) {
            System.err.println("[ERROR] Error al listar planes: " + e.getMessage());
        }

        return lista;
    }

    public boolean eliminarPlan(int id) {
        String sql = "DELETE FROM planes WHERE id=?";
        try (Connection conn = Conexiones.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("[ERROR] No se pudo eliminar plan: " + e.getMessage());
            return false;
        }
    }

    public Plan buscarPlanPorId(int id) {
        String sql = "SELECT * FROM planes WHERE id = ?";
        try (Connection conn = Conexiones.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String tipo = rs.getString("tipo");
                    Plan plan = null;
                    switch (tipo) {
                        case "PostPagoMinutosMegasEconomico" ->
                            plan = new PlanPostPagoMinutosMegasEconomico(
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
                            plan = new PlanPostPagoMinutos(
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
                            plan = new PlanPostPagoMegas(
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
                            plan = new PlanPostPagoMinutosMegas(
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
                    }
                    return plan;
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al buscar plan por ID: " + e.getMessage());
        }
        return null;
    }

    public boolean asignarPlanACliente(int idPlan, String cedulaCliente) {
        String sql = "UPDATE planes SET cedulaCliente = ? WHERE id = ?";
        try (Connection conn = Conexiones.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cedulaCliente);
            ps.setInt(2, idPlan);
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al asignar plan a cliente: " + e.getMessage());
            return false;
        }
    }

    public boolean editarPlan(Plan plan) {
        String sql = "UPDATE planes SET cedulaCliente=?, numeroCelular=?, marcaCelular=?, modeloCelular=?, minutos=?, costoMinuto=?, minutosNacionales=?, costoMinutoNacional=?, minutosInternacionales=?, costoMinutoInternacional=?, megas=?, costoGiga=?, descuento=?, tarifaBase=?, tipo=? WHERE id=?";
        try (Connection conn = Conexiones.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, plan.getCedulaCliente());
            ps.setString(2, plan.getNumeroCelular());
            ps.setString(3, plan.getMarcaCelular());
            ps.setString(4, plan.getModeloCelular());

            int minutos = 0;
            double costoMinuto = 0.0;
            int minutosNacionales = 0;
            double costoMinutoNacional = 0.0;
            int minutosInternacionales = 0;
            double costoMinutoInternacional = 0.0;
            int megas = 0;
            double costoGiga = 0.0;
            double descuento = 0.0;
            double tarifaBase = 0.0;
            String tipo = "";

            if (plan instanceof PlanPostPagoMinutosMegasEconomico p) {
                minutos = p.getMinutos();
                costoMinuto = p.getCostoMinuto();
                megas = p.getMegas();
                costoGiga = p.getCostoGiga();
                descuento = p.getPorcentajeDescuento();
                tipo = "PostPagoMinutosMegasEconomico";

            } else if (plan instanceof PlanPostPagoMinutos p) {
                minutosNacionales = p.getMinutosNacionales();
                costoMinutoNacional = p.getCostoMinutoNacional();
                minutosInternacionales = p.getMinutosInternacionales();
                costoMinutoInternacional = p.getCostoMinutoInternacional();
                tipo = "PostPagoMinutos";

            } else if (plan instanceof PlanPostPagoMegas p) {
                megas = p.getMegas();
                costoGiga = p.getCostoGiga();
                tarifaBase = p.getTarifaBase();
                tipo = "PostPagoMegas";

            } else if (plan instanceof PlanPostPagoMinutosMegas p) {
                minutos = p.getMinutos();
                costoMinuto = p.getCostoMinuto();
                megas = p.getMegas();
                costoGiga = p.getCostoGiga();
                tipo = "PostPagoMinutosMegas";

            } else {
                System.err.println("[ERROR] Tipo de plan desconocido.");
                return false;
            }

            ps.setInt(5, minutos);
            ps.setDouble(6, costoMinuto);
            ps.setInt(7, minutosNacionales);
            ps.setDouble(8, costoMinutoNacional);
            ps.setInt(9, minutosInternacionales);
            ps.setDouble(10, costoMinutoInternacional);
            ps.setInt(11, megas);
            ps.setDouble(12, costoGiga);
            ps.setDouble(13, descuento);
            ps.setDouble(14, tarifaBase);
            ps.setString(15, tipo);

            ps.setInt(16, plan.getId());

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("[ERROR] Error al editar plan: " + e.getMessage());
            return false;
        }
    }

    public int generarIdNuevoPlan() {
        int idNuevo = 1;
        String sql = "SELECT MAX(id) AS maxId FROM planes";
        try (Connection conn = Conexiones.conectar(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                idNuevo = rs.getInt("maxId") + 1;
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al generar nuevo ID para plan: " + e.getMessage());
        }
        return idNuevo;
    }

}
