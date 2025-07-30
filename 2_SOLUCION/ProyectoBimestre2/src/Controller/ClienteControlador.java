package Controller;

import Model.Cliente;
import Model.Conexiones;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClienteControlador {

    private Scanner sc = new Scanner(System.in);

    public boolean agregarCliente(Cliente c) {
        String sql = "INSERT INTO clientes (cedula, nombre, apellido, ciudad, barrio, calle, correo) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conexiones.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getCedula());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getApellido());
            ps.setString(4, c.getCiudad());
            ps.setString(5, c.getBarrio());
            ps.setString(6, c.getCalle());
            ps.setString(7, c.getCorreo());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("[ERROR] No se pudo insertar cliente: " + e.getMessage());
            return false;
        }
    }

    public List<Cliente> listarClientes() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes";
        try (Connection conn = Conexiones.conectar();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Cliente c = new Cliente(
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("ciudad"),
                        rs.getString("barrio"),
                        rs.getString("calle"),
                        rs.getString("correo")
                );
                lista.add(c);
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al listar clientes: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizarCliente(Cliente c) {
        String sql = "UPDATE clientes SET nombre=?, apellido=?, ciudad=?, barrio=?, calle=?, correo=? WHERE cedula=?";
        try (Connection conn = Conexiones.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getApellido());
            ps.setString(3, c.getCiudad());
            ps.setString(4, c.getBarrio());
            ps.setString(5, c.getCalle());
            ps.setString(6, c.getCorreo());
            ps.setString(7, c.getCedula());
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("[ERROR] No se pudo actualizar cliente: " + e.getMessage());
            return false;
        }
    }
    
    public void editarCliente() {
        System.out.println("\n--- Editar Cliente ---");
        System.out.print("Ingrese la cédula del cliente a editar: ");
        String cedula = sc.nextLine().trim();

        Cliente c = buscarCliente(cedula);
        if (c == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }

        System.out.println("Deje vacío para no modificar el campo.");

        System.out.print("Nombre (" + c.getNombre() + "): ");
        String nombre = sc.nextLine().trim();
        if (!nombre.isEmpty()) c.setNombre(nombre);

        System.out.print("Apellido (" + c.getApellido() + "): ");
        String apellido = sc.nextLine().trim();
        if (!apellido.isEmpty()) c.setApellido(apellido);

        System.out.print("Ciudad (" + c.getCiudad() + "): ");
        String ciudad = sc.nextLine().trim();
        if (!ciudad.isEmpty()) c.setCiudad(ciudad);

        System.out.print("Barrio (" + c.getBarrio() + "): ");
        String barrio = sc.nextLine().trim();
        if (!barrio.isEmpty()) c.setBarrio(barrio);

        System.out.print("Calle (" + c.getCalle() + "): ");
        String calle = sc.nextLine().trim();
        if (!calle.isEmpty()) c.setCalle(calle);

        System.out.print("Correo (" + c.getCorreo() + "): ");
        String correo = sc.nextLine().trim();
        if (!correo.isEmpty()) c.setCorreo(correo);

        if (actualizarCliente(c)) {
            System.out.println("Cliente actualizado correctamente.");
        } else {
            System.out.println("Error al actualizar cliente.");
        }
    }

    public boolean eliminarCliente(String cedula) {
        String sql = "DELETE FROM clientes WHERE cedula=?";
        try (Connection conn = Conexiones.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cedula);
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("[ERROR] No se pudo eliminar cliente: " + e.getMessage());
            return false;
        }
    }

    public Cliente buscarCliente(String cedula) {
        String sql = "SELECT * FROM clientes WHERE cedula=?";
        try (Connection conn = Conexiones.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cedula);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Cliente(
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("ciudad"),
                        rs.getString("barrio"),
                        rs.getString("calle"),
                        rs.getString("correo")
                );
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al buscar cliente: " + e.getMessage());
        }
        return null;
    }
}
