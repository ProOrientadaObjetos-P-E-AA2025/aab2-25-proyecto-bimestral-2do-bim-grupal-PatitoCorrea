package Viewer;

import Controller.ClienteControlador;
import Controller.PlanControlador;
import Model.Cliente;
import Model.Plan;
import Model.PlanPostPagoMegas;
import Model.Factura;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class InterfazMovUTPL extends JFrame {

    private ClienteControlador clienteControl = new ClienteControlador();
    private PlanControlador planControl = new PlanControlador();

    private DefaultTableModel modeloClientes;
    private JTable tablaClientes;

    private DefaultTableModel modeloPlanes;
    private JTable tablaPlanes;

    private DefaultTableModel modeloFacturas;
    private JTable tablaFacturas;

    public InterfazMovUTPL() {
        setTitle("Sistema Mov-UTPL");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initComponentes();
        cargarDatosClientes();
        cargarDatosPlanes();
        cargarDatosFacturas();
    }

    private void initComponentes() {
        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel panelClientes = new JPanel(new BorderLayout());
        JPanel panelBtnsClientes = new JPanel();
        JButton btnAgregarCliente = new JButton("Agregar Cliente");
        JButton btnEditarCliente = new JButton("Editar Cliente");
        JButton btnEliminarCliente = new JButton("Eliminar Cliente");
        JButton btnRefrescarClientes = new JButton("Refrescar Lista");
        panelBtnsClientes.add(btnAgregarCliente);
        panelBtnsClientes.add(btnEditarCliente);
        panelBtnsClientes.add(btnEliminarCliente);
        panelBtnsClientes.add(btnRefrescarClientes);

        modeloClientes = new DefaultTableModel(new String[]{"Cédula", "Nombre", "Apellido", "Ciudad", "Barrio", "Calle", "Correo"}, 0);
        tablaClientes = new JTable(modeloClientes);
        JScrollPane scrollClientes = new JScrollPane(tablaClientes);

        panelClientes.add(panelBtnsClientes, BorderLayout.NORTH);
        panelClientes.add(scrollClientes, BorderLayout.CENTER);

        JPanel panelPlanes = new JPanel(new BorderLayout());
        JPanel panelBtnsPlanes = new JPanel();
        JButton btnAgregarPlan = new JButton("Agregar Plan");
        JButton btnEditarPlan = new JButton("Editar Plan");
        JButton btnEliminarPlan = new JButton("Eliminar Plan");
        JButton btnRefrescarPlanes = new JButton("Refrescar Lista");
        JButton btnAsignarPlan = new JButton("Asignar Plan a Cliente");
        panelBtnsPlanes.add(btnAgregarPlan);
        panelBtnsPlanes.add(btnEditarPlan);
        panelBtnsPlanes.add(btnEliminarPlan);
        panelBtnsPlanes.add(btnAsignarPlan);
        panelBtnsPlanes.add(btnRefrescarPlanes);

        modeloPlanes = new DefaultTableModel(new String[]{"ID", "Cédula Cliente", "Número Celular", "Marca", "Modelo", "Tipo"}, 0);
        tablaPlanes = new JTable(modeloPlanes);
        JScrollPane scrollPlanes = new JScrollPane(tablaPlanes);

        panelPlanes.add(panelBtnsPlanes, BorderLayout.NORTH);
        panelPlanes.add(scrollPlanes, BorderLayout.CENTER);

        JPanel panelFacturas = new JPanel(new BorderLayout());
        modeloFacturas = new DefaultTableModel(new String[]{"Número Factura", "Cédula Cliente", "Fecha", "Total"}, 0);
        tablaFacturas = new JTable(modeloFacturas);
        JScrollPane scrollFacturas = new JScrollPane(tablaFacturas);

        JButton btnGenerarFactura = new JButton("Generar Factura");
        JPanel panelBtnsFacturas = new JPanel();
        panelBtnsFacturas.add(btnGenerarFactura);

        panelFacturas.add(panelBtnsFacturas, BorderLayout.NORTH);
        panelFacturas.add(scrollFacturas, BorderLayout.CENTER);

        tabbedPane.addTab("Clientes", panelClientes);
        tabbedPane.addTab("Planes", panelPlanes);
        tabbedPane.addTab("Facturas", panelFacturas);

        add(tabbedPane);

        btnAgregarCliente.addActionListener(e -> dialogAgregarCliente());
        btnEditarCliente.addActionListener(e -> dialogEditarCliente());
        btnEliminarCliente.addActionListener(e -> eliminarCliente());
        btnRefrescarClientes.addActionListener(e -> cargarDatosClientes());

        btnAgregarPlan.addActionListener(e -> dialogAgregarPlan());
        btnEditarPlan.addActionListener(e -> dialogEditarPlan());
        btnEliminarPlan.addActionListener(e -> eliminarPlan());
        btnRefrescarPlanes.addActionListener(e -> cargarDatosPlanes());
        btnAsignarPlan.addActionListener(e -> asignarPlanACliente());

        btnGenerarFactura.addActionListener(e -> dialogGenerarFactura());
    }

    private void cargarDatosClientes() {
        modeloClientes.setRowCount(0);
        List<Cliente> lista = clienteControl.listarClientes();
        for (Cliente c : lista) {
            modeloClientes.addRow(new Object[]{
                    c.getCedula(), c.getNombre(), c.getApellido(), c.getCiudad(), c.getBarrio(), c.getCalle(), c.getCorreo()
            });
        }
    }

    private void dialogAgregarCliente() {
        JTextField cedula = new JTextField();
        JTextField nombre = new JTextField();
        JTextField apellido = new JTextField();
        JTextField ciudad = new JTextField();
        JTextField barrio = new JTextField();
        JTextField calle = new JTextField();
        JTextField correo = new JTextField();

        Object[] message = {
                "Cédula:", cedula,
                "Nombre:", nombre,
                "Apellido:", apellido,
                "Ciudad:", ciudad,
                "Barrio:", barrio,
                "Calle:", calle,
                "Correo:", correo
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Agregar Cliente", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            Cliente c = new Cliente(
                    cedula.getText().trim(),
                    nombre.getText().trim(),
                    apellido.getText().trim(),
                    ciudad.getText().trim(),
                    barrio.getText().trim(),
                    calle.getText().trim(),
                    correo.getText().trim()
            );
            if (clienteControl.agregarCliente(c)) {
                JOptionPane.showMessageDialog(this, "Cliente agregado con éxito.");
                cargarDatosClientes();
            } else {
                JOptionPane.showMessageDialog(this, "Error al agregar cliente.");
            }
        }
    }

    private void dialogEditarCliente() {
        int fila = tablaClientes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente para editar.");
            return;
        }
        String cedula = (String) modeloClientes.getValueAt(fila, 0);
        Cliente c = clienteControl.buscarCliente(cedula);
        if (c == null) {
            JOptionPane.showMessageDialog(this, "Cliente no encontrado.");
            return;
        }

        JTextField nombre = new JTextField(c.getNombre());
        JTextField apellido = new JTextField(c.getApellido());
        JTextField ciudad = new JTextField(c.getCiudad());
        JTextField barrio = new JTextField(c.getBarrio());
        JTextField calle = new JTextField(c.getCalle());
        JTextField correo = new JTextField(c.getCorreo());

        Object[] message = {
                "Nombre:", nombre,
                "Apellido:", apellido,
                "Ciudad:", ciudad,
                "Barrio:", barrio,
                "Calle:", calle,
                "Correo:", correo
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Editar Cliente: " + cedula, JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            c.setNombre(nombre.getText().trim());
            c.setApellido(apellido.getText().trim());
            c.setCiudad(ciudad.getText().trim());
            c.setBarrio(barrio.getText().trim());
            c.setCalle(calle.getText().trim());
            c.setCorreo(correo.getText().trim());

            if (clienteControl.actualizarCliente(c)) {
                JOptionPane.showMessageDialog(this, "Cliente actualizado.");
                cargarDatosClientes();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar cliente.");
            }
        }
    }

    private void eliminarCliente() {
        int fila = tablaClientes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente para eliminar.");
            return;
        }
        String cedula = (String) modeloClientes.getValueAt(fila, 0);
        int option = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar al cliente con cédula " + cedula + "?");
        if (option == JOptionPane.OK_OPTION) {
            if (clienteControl.eliminarCliente(cedula)) {
                JOptionPane.showMessageDialog(this, "Cliente eliminado.");
                cargarDatosClientes();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar cliente.");
            }
        }
    }

    private void cargarDatosPlanes() {
        modeloPlanes.setRowCount(0);
        List<Plan> lista = planControl.listarPlanes();
        for (Plan p : lista) {
            modeloPlanes.addRow(new Object[]{
                    p.getId(), p.getCedulaCliente(), p.getNumeroCelular(), p.getMarcaCelular(), p.getModeloCelular(), p.getTipoPlan()
            });
        }
    }

    private void dialogAgregarPlan() {
        JTextField cedulaCliente = new JTextField();
        JTextField numeroCelular = new JTextField();
        JTextField marcaCelular = new JTextField();
        JTextField modeloCelular = new JTextField();
        JTextField megas = new JTextField();
        JTextField costoGiga = new JTextField();
        JTextField tarifaBase = new JTextField();

        Object[] message = {
                "Cédula Cliente:", cedulaCliente,
                "Número Celular:", numeroCelular,
                "Marca Celular:", marcaCelular,
                "Modelo Celular:", modeloCelular,
                "Megas:", megas,
                "Costo por Giga:", costoGiga,
                "Tarifa Base:", tarifaBase
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Agregar Plan PostPagoMegas", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                PlanPostPagoMegas plan = new PlanPostPagoMegas(
                        0,
                        cedulaCliente.getText().trim(),
                        numeroCelular.getText().trim(),
                        marcaCelular.getText().trim(),
                        modeloCelular.getText().trim(),
                        Integer.parseInt(megas.getText().trim()),
                        Double.parseDouble(costoGiga.getText().trim()),
                        Double.parseDouble(tarifaBase.getText().trim())
                );
                if (planControl.agregarPlan(plan)) {
                    JOptionPane.showMessageDialog(this, "Plan agregado con éxito.");
                    cargarDatosPlanes();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al agregar plan.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error: campos numéricos inválidos.");
            }
        }
    }

    private void dialogEditarPlan() {
        int fila = tablaPlanes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un plan para editar.");
            return;
        }
        int id = (int) modeloPlanes.getValueAt(fila, 0);
        Plan plan = planControl.buscarPlanPorId(id);
        if (plan == null) {
            JOptionPane.showMessageDialog(this, "Plan no encontrado.");
            return;
        }

        JTextField cedulaCliente = new JTextField(plan.getCedulaCliente());
        JTextField numeroCelular = new JTextField(plan.getNumeroCelular());
        JTextField marcaCelular = new JTextField(plan.getMarcaCelular());
        JTextField modeloCelular = new JTextField(plan.getModeloCelular());

        JTextField megas = new JTextField("0");
        JTextField costoGiga = new JTextField("0");
        JTextField tarifaBase = new JTextField("0");

        if (plan instanceof PlanPostPagoMegas ppm) {
            megas.setText(String.valueOf(ppm.getMegas()));
            costoGiga.setText(String.valueOf(ppm.getCostoGiga()));
            tarifaBase.setText(String.valueOf(ppm.getTarifaBase()));
        }

        Object[] message = {
                "Cédula Cliente:", cedulaCliente,
                "Número Celular:", numeroCelular,
                "Marca Celular:", marcaCelular,
                "Modelo Celular:", modeloCelular,
                "Megas:", megas,
                "Costo por Giga:", costoGiga,
                "Tarifa Base:", tarifaBase
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Editar Plan ID " + id, JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                plan.setCedulaCliente(cedulaCliente.getText().trim());
                plan.setNumeroCelular(numeroCelular.getText().trim());
                plan.setMarcaCelular(marcaCelular.getText().trim());
                plan.setModeloCelular(modeloCelular.getText().trim());
                if (plan instanceof PlanPostPagoMegas ppm) {
                    ppm.setMegas(Integer.parseInt(megas.getText().trim()));
                    ppm.setCostoGiga(Double.parseDouble(costoGiga.getText().trim()));
                    ppm.setTarifaBase(Double.parseDouble(tarifaBase.getText().trim()));
                }
                if (planControl.editarPlan(plan)) {
                    JOptionPane.showMessageDialog(this, "Plan actualizado.");
                    cargarDatosPlanes();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar plan.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error: campos numéricos inválidos.");
            }
        }
    }

    private void eliminarPlan() {
        int fila = tablaPlanes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un plan para eliminar.");
            return;
        }
        int id = (int) modeloPlanes.getValueAt(fila, 0);
        int option = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar el plan con ID " + id + "?");
        if (option == JOptionPane.OK_OPTION) {
            if (planControl.eliminarPlan(id)) {
                JOptionPane.showMessageDialog(this, "Plan eliminado.");
                cargarDatosPlanes();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar plan.");
            }
        }
    }

    private void asignarPlanACliente() {
        JOptionPane.showMessageDialog(this, "Asignación de plan a cliente se hace asignando la cédula del cliente en el plan.");
    }

    private void cargarDatosFacturas() {
        modeloFacturas.setRowCount(0);
    }

    private void dialogGenerarFactura() {
        String cedula = JOptionPane.showInputDialog(this, "Ingrese cédula del cliente para generar factura:");
        if (cedula == null || cedula.trim().isEmpty()) {
            return;
        }
        Cliente cliente = clienteControl.buscarCliente(cedula.trim());
        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "Cliente no encontrado.");
            return;
        }

        List<Plan> planesCliente = planControl.listarPlanesPorCliente(cedula.trim());
        if (planesCliente.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El cliente no tiene planes asignados.");
            return;
        }

        Factura factura = new Factura("FAC-" + System.currentTimeMillis(), cliente, planesCliente);
        JOptionPane.showMessageDialog(this, factura.toString(), "Factura Generada", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        Model.Conexiones.crearTablas();

        SwingUtilities.invokeLater(() -> {
            new InterfazMovUTPL().setVisible(true);
        });
    }
}