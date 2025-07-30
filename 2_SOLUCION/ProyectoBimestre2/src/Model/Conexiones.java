package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexiones {

    private static final String URL = "jdbc:sqlite:mov_utpl.db";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void crearTablas() {
        String sqlClientes = "CREATE TABLE IF NOT EXISTS clientes ("
                + "cedula TEXT PRIMARY KEY,"
                + "nombre TEXT NOT NULL,"
                + "apellido TEXT NOT NULL,"
                + "ciudad TEXT,"
                + "barrio TEXT,"
                + "calle TEXT,"
                + "correo TEXT"
                + ");";

        String sqlPlanes = "CREATE TABLE IF NOT EXISTS planes ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "cedulaCliente TEXT,"
                + "tipo TEXT,"
                + "numeroCelular TEXT,"
                + "marcaCelular TEXT,"
                + "modeloCelular TEXT,"
                + "minutos INTEGER,"
                + "costoMinuto REAL,"
                + "minutosNacionales INTEGER,"
                + "costoMinutoNacional REAL,"
                + "minutosInternacionales INTEGER,"
                + "costoMinutoInternacional REAL,"
                + "megas INTEGER,"
                + "costoGiga REAL,"
                + "descuento REAL,"
                + "tarifaBase REAL,"
                + "FOREIGN KEY (cedulaCliente) REFERENCES clientes(cedula)"
                + ");";

        String sqlFacturas = "CREATE TABLE IF NOT EXISTS facturas ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "numeroFactura TEXT UNIQUE NOT NULL,"
                + "cedulaCliente TEXT NOT NULL,"
                + "fecha TEXT NOT NULL,"
                + "total REAL DEFAULT 0,"
                + "FOREIGN KEY (cedulaCliente) REFERENCES clientes(cedula)"
                + ");";

        String sqlDetalleFacturas = "CREATE TABLE IF NOT EXISTS detalle_facturas ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "numeroFactura TEXT NOT NULL,"
                + "idPlan INTEGER NOT NULL,"
                + "pagoPlan REAL,"
                + "FOREIGN KEY (numeroFactura) REFERENCES facturas(numeroFactura),"
                + "FOREIGN KEY (idPlan) REFERENCES planes(id)"
                + ");";

        try (Connection conn = conectar(); Statement st = conn.createStatement()) {
            st.execute(sqlClientes);
            st.execute(sqlPlanes);
            st.execute(sqlFacturas);
            st.execute(sqlDetalleFacturas);
            System.out.println("[INFO] Tablas creadas o verificadas con éxito.");
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al crear tablas: " + e.getMessage());
        }

        agregarColumnaTotalSiNoExiste();
    }

    public static void agregarColumnaTotalSiNoExiste() {
        String sqlAlter = "ALTER TABLE facturas ADD COLUMN total REAL DEFAULT 0";
        try (Connection conn = conectar(); Statement st = conn.createStatement()) {
            st.execute(sqlAlter);
            System.out.println("[INFO] Columna 'total' agregada a la tabla 'facturas'.");
        } catch (SQLException e) {
            if (e.getMessage().contains("duplicate column name") || e.getMessage().contains("column total already exists")) {
                System.out.println("[INFO] La columna 'total' ya existe en la tabla 'facturas'.");
            } else {
                System.err.println("[ERROR] Error al agregar columna 'total': " + e.getMessage());
            }
        }
    }

}
