package Viewer;

import Model.Conexiones;
import Model.Menu;

public class Main {

    public static void main(String[] args) {
        Conexiones.crearTablas();
        Menu.mostrarMenu();
    }
}
