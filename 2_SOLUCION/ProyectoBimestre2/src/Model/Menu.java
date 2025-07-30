package Model;

import Controller.ClienteControlador;
import Controller.PlanControlador;
import Controller.FacturaControlador;
import Controller.DetalleFacturaControlador;
import java.util.ArrayList;
import java.util.HashSet;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Menu {

    private static Scanner tcl = new Scanner(System.in);
    private static ClienteControlador clienteController = new ClienteControlador();
    private static PlanControlador planController = new PlanControlador();
    private static FacturaControlador facturaController = new FacturaControlador();
    private static DetalleFacturaControlador detalleFacturaController = new DetalleFacturaControlador();

    public static void main(String[] args) {
        mostrarMenu();
    }

    public static void mostrarMenu() {
        int opcion = -1;
        do {
            System.out.println("\n=== Sistema Mov-UTPL ===");
            System.out.println("1. Gestionar Clientes");
            System.out.println("2. Gestionar Planes");
            System.out.println("3. Asignar Plan a Cliente");
            System.out.println("4. Generar Factura");
            System.out.println("5. Listar Facturas");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(tcl.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingrese un número válido.");
                continue;
            }

            switch (opcion) {
                case 1 ->
                    gestionarClientes();
                case 2 ->
                    gestionarPlanes();
                case 3 ->
                    asignarPlanACliente();
                case 4 ->
                    generarFactura();
                case 5 ->
                    listarFacturas();
                case 0 ->
                    System.out.println("Gracias por usar nuestro sistema :D!! *Saliendo del sistema*");
                default ->
                    System.out.println("Opción no válida, intente de nuevo.");
            }
        } while (opcion != 0);
    }

    private static void gestionarClientes() {
        int opcion;
        do {
            System.out.println("\n--- Gestión de Clientes ---");
            System.out.println("1. Crear cliente");
            System.out.println("2. Listar clientes");
            System.out.println("3. Editar cliente");
            System.out.println("4. Eliminar cliente");
            System.out.println("0. Volver al menú principal");
            opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1 ->
                    crearCliente();
                case 2 ->
                    listarClientes();
                case 3 ->
                    clienteController.editarCliente();
                case 4 ->
                    eliminarCliente();
                case 0 ->
                    System.out.println("*Regresando al menú principal*");
                default ->
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    private static void eliminarCliente() {
        System.out.println("\n--- Eliminar Cliente ---");
        String cedula = leerTexto("Ingrese la cédula del cliente a eliminar: ");
        Cliente c = clienteController.buscarCliente(cedula);
        if (c == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }
        System.out.print("¿Está seguro que desea eliminar al cliente " + c.getNombre() + " (S/N)? ");
        String confirma = tcl.nextLine().trim().toUpperCase();
        if (confirma.equals("S")) {
            if (clienteController.eliminarCliente(cedula)) {
                System.out.println("Cliente eliminado correctamente.");
            } else {
                System.out.println("Error al eliminar cliente.");
            }
        } else {
            System.out.println("Eliminación cancelada.");
        }
    }

    private static void crearCliente() {
        System.out.println("\nCrear nuevo cliente");
        String cedula = leerTexto("Cédula: ");
        if (clienteController.buscarCliente(cedula) != null) {
            System.out.println("[ERROR] Cliente ya existe.");
            return;
        }
        String nombre = leerTexto("Nombre: ");
        String apellido = leerTexto("Apellido: ");
        String ciudad = leerTexto("Ciudad: ");
        String barrio = leerTexto("Barrio: ");
        String calle = leerTexto("Calle: ");
        String correo = leerTexto("Correo: ");

        Cliente cliente = new Cliente(cedula, nombre, apellido, ciudad, barrio, calle, correo);
        clienteController.agregarCliente(cliente);
        System.out.println("Cliente creado exitosamente.");
    }

    private static void listarClientes() {
        System.out.println("\nListado de clientes:");
        List<Cliente> clientes = clienteController.listarClientes();
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
        } else {
            clientes.forEach(System.out::println);
        }
    }

    private static void gestionarPlanes() {
        int opcion;
        do {
            System.out.println("\n--- Gestión de Planes ---");
            System.out.println("1. Crear plan");
            System.out.println("2. Listar planes");
            System.out.println("3. Editar plan");
            System.out.println("4. Eliminar plan");
            System.out.println("0. Volver al menú principal");
            opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1 ->
                    crearPlan();
                case 2 ->
                    listarPlanes();
                case 3 ->
                    editarPlan();
                case 4 ->
                    eliminarPlan();
                case 0 ->
                    System.out.println("*Regresando al menu*");
                default ->
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    private static void editarPlan() {
        System.out.println("\n--- Editar Plan ---");
        int id = leerEntero("Ingrese ID del plan a editar: ");
        Plan plan = planController.buscarPlanPorId(id);
        if (plan == null) {
            System.out.println("Plan no encontrado.");
            return;
        }

        System.out.println("Plan encontrado:");
        System.out.println(plan);

        System.out.println("Ingrese los nuevos datos (dejar vacío para no cambiar):");

        String cedulaCliente = leerTexto("Cédula cliente [" + plan.getCedulaCliente() + "]: ");
        if (!cedulaCliente.isEmpty()) {
            plan.setCedulaCliente(cedulaCliente);
        }

        String numeroCelular = leerTexto("Número celular [" + plan.getNumeroCelular() + "]: ");
        if (!numeroCelular.isEmpty()) {
            plan.setNumeroCelular(numeroCelular);
        }

        String marcaCelular = leerTexto("Marca celular [" + plan.getMarcaCelular() + "]: ");
        if (!marcaCelular.isEmpty()) {
            plan.setMarcaCelular(marcaCelular);
        }

        String modeloCelular = leerTexto("Modelo celular [" + plan.getModeloCelular() + "]: ");
        if (!modeloCelular.isEmpty()) {
            plan.setModeloCelular(modeloCelular);
        }

        if (plan instanceof PlanPostPagoMinutosMegasEconomico p) {
            String s = leerTexto("Minutos [" + p.getMinutos() + "]: ");
            if (!s.isEmpty()) {
                p.setMinutos(Integer.parseInt(s));
            }

            s = leerTexto("Costo por minuto [" + p.getCostoMinuto() + "]: ");
            if (!s.isEmpty()) {
                p.setCostoMinuto(Double.parseDouble(s));
            }

            s = leerTexto("Megas (GB) [" + p.getMegas() + "]: ");
            if (!s.isEmpty()) {
                p.setMegas(Integer.parseInt(s));
            }

            s = leerTexto("Costo por giga [" + p.getCostoGiga() + "]: ");
            if (!s.isEmpty()) {
                p.setCostoGiga(Double.parseDouble(s));
            }

            s = leerTexto("Porcentaje de descuento [" + p.getPorcentajeDescuento() + "]: ");
            if (!s.isEmpty()) {
                p.setPorcentajeDescuento(Double.parseDouble(s));
            }

        } else if (plan instanceof PlanPostPagoMinutos p) {
            String s = leerTexto("Minutos nacionales [" + p.getMinutosNacionales() + "]: ");
            if (!s.isEmpty()) {
                p.setMinutosNacionales(Integer.parseInt(s));
            }

            s = leerTexto("Costo minuto nacional [" + p.getCostoMinutoNacional() + "]: ");
            if (!s.isEmpty()) {
                p.setCostoMinutoNacional(Double.parseDouble(s));
            }

            s = leerTexto("Minutos internacionales [" + p.getMinutosInternacionales() + "]: ");
            if (!s.isEmpty()) {
                p.setMinutosInternacionales(Integer.parseInt(s));
            }

            s = leerTexto("Costo minuto internacional [" + p.getCostoMinutoInternacional() + "]: ");
            if (!s.isEmpty()) {
                p.setCostoMinutoInternacional(Double.parseDouble(s));
            }

        } else if (plan instanceof PlanPostPagoMegas p) {
            String s = leerTexto("Megas (GB) [" + p.getMegas() + "]: ");
            if (!s.isEmpty()) {
                p.setMegas(Integer.parseInt(s));
            }

            s = leerTexto("Costo por giga [" + p.getCostoGiga() + "]: ");
            if (!s.isEmpty()) {
                p.setCostoGiga(Double.parseDouble(s));
            }

            s = leerTexto("Tarifa base [" + p.getTarifaBase() + "]: ");
            if (!s.isEmpty()) {
                p.setTarifaBase(Double.parseDouble(s));
            }

        } else if (plan instanceof PlanPostPagoMinutosMegas p) {
            String s = leerTexto("Minutos [" + p.getMinutos() + "]: ");
            if (!s.isEmpty()) {
                p.setMinutos(Integer.parseInt(s));
            }

            s = leerTexto("Costo por minuto [" + p.getCostoMinuto() + "]: ");
            if (!s.isEmpty()) {
                p.setCostoMinuto(Double.parseDouble(s));
            }

            s = leerTexto("Megas (GB) [" + p.getMegas() + "]: ");
            if (!s.isEmpty()) {
                p.setMegas(Integer.parseInt(s));
            }

            s = leerTexto("Costo por giga [" + p.getCostoGiga() + "]: ");
            if (!s.isEmpty()) {
                p.setCostoGiga(Double.parseDouble(s));
            }
        }

        if (planController.editarPlan(plan)) {
            System.out.println("Plan actualizado correctamente.");
        } else {
            System.out.println("Error al actualizar el plan.");
        }
    }

    private static void eliminarPlan() {
        System.out.println("\n--- Eliminar Plan ---");
        int id = leerEntero("Ingrese ID del plan a eliminar: ");
        Plan plan = planController.buscarPlanPorId(id);
        if (plan == null) {
            System.out.println("Plan no encontrado.");
            return;
        }

        System.out.print("¿Está seguro que desea eliminar el plan ID " + id + " (S/N)? ");
        String confirma = tcl.nextLine().trim().toUpperCase();
        if (confirma.equals("S")) {
            if (planController.eliminarPlan(id)) {
                System.out.println("Plan eliminado correctamente.");
            } else {
                System.out.println("Error al eliminar el plan.");
            }
        } else {
            System.out.println("Eliminación cancelada.");
        }
    }

    private static void crearPlan() {
        System.out.println("\nCrear nuevo plan");
        System.out.println("Tipos de plan:");
        System.out.println("1. PostPago Minutos Megas Económico");
        System.out.println("2. PostPago Minutos");
        System.out.println("3. PostPago Megas");
        System.out.println("4. PostPago Minutos Megas");
        int tipo = leerEntero("Seleccione tipo de plan: ");

        String cedulaCliente = leerTexto("Cédula del cliente para asignar (dejar vacío si no asignar aún): ");
        if (!cedulaCliente.isEmpty() && clienteController.buscarCliente(cedulaCliente) == null) {
            System.out.println("[ERROR] Cliente no encontrado.");
            return;
        }

        String numeroCelular = leerTexto("Número celular: ");
        String marcaCelular = leerTexto("Marca celular: ");
        String modeloCelular = leerTexto("Modelo celular: ");

        Plan plan = null;
        int id = planController.generarIdNuevoPlan();

        try {
            switch (tipo) {
                case 1 -> {
                    int minutos = leerEntero("Minutos: ");
                    double costoMinuto = Double.parseDouble(leerTexto("Costo por minuto: "));
                    int megas = leerEntero("Megas (GB): ");
                    double costoGiga = Double.parseDouble(leerTexto("Costo por giga: "));
                    double descuento = Double.parseDouble(leerTexto("Porcentaje de descuento: "));
                    plan = new PlanPostPagoMinutosMegasEconomico(id, cedulaCliente, numeroCelular, marcaCelular, modeloCelular,
                            minutos, costoMinuto, megas, costoGiga, descuento);
                }
                case 2 -> {
                    int minutosNac = leerEntero("Minutos nacionales: ");
                    double costoMinNac = Double.parseDouble(leerTexto("Costo minuto nacional: "));
                    int minutosInt = leerEntero("Minutos internacionales: ");
                    double costoMinInt = Double.parseDouble(leerTexto("Costo minuto internacional: "));
                    plan = new PlanPostPagoMinutos(id, cedulaCliente, numeroCelular, marcaCelular, modeloCelular,
                            minutosNac, costoMinNac, minutosInt, costoMinInt);
                }
                case 3 -> {
                    int megas = leerEntero("Megas (GB): ");
                    double costoGiga = Double.parseDouble(leerTexto("Costo por giga: "));
                    double tarifaBase = Double.parseDouble(leerTexto("Tarifa base: "));
                    plan = new PlanPostPagoMegas(id, cedulaCliente, numeroCelular, marcaCelular, modeloCelular,
                            megas, costoGiga, tarifaBase);
                }
                case 4 -> {
                    int minutos = leerEntero("Minutos: ");
                    double costoMinuto = Double.parseDouble(leerTexto("Costo por minuto: "));
                    int megas = leerEntero("Megas (GB): ");
                    double costoGiga = Double.parseDouble(leerTexto("Costo por giga: "));
                    plan = new PlanPostPagoMinutosMegas(id, cedulaCliente, numeroCelular, marcaCelular, modeloCelular,
                            minutos, costoMinuto, megas, costoGiga);
                }
                default -> {
                    System.out.println("Tipo de plan inválido.");
                    return;
                }
            }

            planController.agregarPlan(plan);
            System.out.println("Plan creado exitosamente.");

        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Entrada numérica inválida.");
        }
    }

    private static void listarPlanes() {
        System.out.println("\nListado de planes:");
        List<Plan> planes = planController.listarPlanes();
        if (planes.isEmpty()) {
            System.out.println("No hay planes registrados.");
        } else {
            planes.forEach(System.out::println);
        }
    }

    private static void asignarPlanACliente() {
        System.out.println("\nAsignar plan a cliente");
        String cedula = leerTexto("Ingrese cédula del cliente: ");
        var cliente = clienteController.buscarCliente(cedula);
        if (cliente == null) {
            System.out.println("[ERROR] Cliente no encontrado.");
            return;
        }

        List<Plan> planesCliente = planController.listarPlanes().stream()
                .filter(p -> p.getCedulaCliente().equals(cedula))
                .toList();

        if (planesCliente.size() >= 2) {
            System.out.println("El cliente ya tiene asignados 2 planes, no se puede agregar más.");
            return;
        }

        int idPlan = leerEntero("Ingrese ID del plan a asignar: ");
        Plan plan = planController.buscarPlanPorId(idPlan);

        if (plan == null) {
            System.out.println("[ERROR] Plan no encontrado.");
            return;
        }

        if (!plan.getCedulaCliente().isEmpty()) {
            System.out.println("[ERROR] El plan ya está asignado a otro cliente.");
            return;
        }

        if (planController.asignarPlanACliente(plan.getId(), cedula)) {
            System.out.println("Plan asignado correctamente.");
        } else {
            System.out.println("[ERROR] No se pudo asignar el plan.");
        }
    }

    private static void generarFactura() {
        System.out.println("\nGenerar factura");
        String cedula = leerTexto("Ingrese cédula del cliente: ");
        var cliente = clienteController.buscarCliente(cedula);
        if (cliente == null) {
            System.out.println("[ERROR] Cliente no encontrado.");
            return;
        }

        List<Plan> planesCliente = planController.listarPlanes().stream()
                .filter(p -> p.getCedulaCliente().equals(cedula))
                .toList();

        if (planesCliente.isEmpty()) {
            System.out.println("El cliente no tiene planes asignados.");
            return;
        }

        String numeroFactura = "FAC-" + System.currentTimeMillis();
        Factura factura = new Factura(numeroFactura, cliente, planesCliente);

        if (facturaController.crearFactura(factura)) {
            System.out.println("Factura guardada en base de datos.");

            for (Plan p : planesCliente) {
                var detalle = new Model.DetalleFactura(numeroFactura, p, p.calcularPagoMensual());
                detalleFacturaController.agregarDetalleFactura(detalle);
            }
        } else {
            System.out.println("[ERROR] No se pudo guardar la factura en base de datos.");
            return;
        }

        System.out.println(factura);

        guardarFacturaArchivo(factura);
    }

    private static void guardarFacturaArchivo(Factura factura) {
        String nombreArchivo = factura.getNumeroFactura() + ".txt";
        try (java.io.FileWriter writer = new java.io.FileWriter(nombreArchivo)) {
            writer.write("Factura Número: " + factura.getNumeroFactura() + "\n");
            writer.write("Cliente: " + factura.getCliente().getNombre() + " " + factura.getCliente().getApellido() + "\n");
            writer.write("Cédula: " + factura.getCliente().getCedula() + "\n");
            writer.write("Fecha: " + factura.getFecha() + "\n\n");
            writer.write("Detalles de Planes:\n");

            List<DetalleFactura> detalles = detalleFacturaController.listarDetallesPorFactura(factura.getNumeroFactura());
            for (DetalleFactura d : detalles) {
                writer.write(String.format(" - Plan ID: %d\n", d.getPlan().getId()));
                writer.write(String.format("   Tipo: %s\n", d.getPlan().getClass().getSimpleName()));
                writer.write(String.format("   Número Celular: %s\n", d.getPlan().getNumeroCelular()));
                writer.write(String.format("   Pago Mensual: $%.2f\n\n", d.getPagoPlan()));
            }

            writer.write(String.format("TOTAL A PAGAR: $%.2f%n", factura.calcularTotal()));

            System.out.println("Factura guardada en archivo: " + nombreArchivo);
        } catch (java.io.IOException e) {
            System.err.println("[ERROR] No se pudo guardar la factura: " + e.getMessage());
        }
    }

    private static void listarFacturas() {
        System.out.println("\nListado de facturas:");
        List<Factura> facturas = facturaController.listarFacturas();
        if (facturas.isEmpty()) {
            System.out.println("No hay facturas registradas.");
        } else {
            for (Factura f : facturas) {
                System.out.println(f);
                List<DetalleFactura> detalles = detalleFacturaController.listarDetallesPorFactura(f.getNumeroFactura());
                if (detalles.isEmpty()) {
                    System.out.println("  No hay detalles para esta factura.");
                } else {
                    System.out.println("  Detalles de la factura:");
                    for (DetalleFactura d : detalles) {
                        System.out.printf("    Plan ID: %d, Pago: $%.2f, Tipo: %s, Número Celular: %s\n",
                                d.getPlan().getId(),
                                d.getPagoPlan(),
                                d.getPlan().getClass().getSimpleName(),
                                d.getPlan().getNumeroCelular()
                        );
                    }
                }
                System.out.println("──────────────────────────────────────────────");
            }
        }
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Integer.parseInt(tcl.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor ingrese un número entero.");
            }
        }
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return tcl.nextLine().trim();
    }

}
