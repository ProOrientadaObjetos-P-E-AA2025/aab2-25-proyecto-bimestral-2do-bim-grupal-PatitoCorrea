package Model;

public class Cliente {

    private String cedula;
    private String nombre;
    private String apellido;
    private String ciudad;
    private String barrio;
    private String calle;
    private String correo;

    public Cliente() {
    }

    public Cliente(String cedula, String nombre, String apellido, String ciudad, String barrio, String calle, String correo) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.ciudad = ciudad;
        this.barrio = barrio;
        this.calle = calle;
        this.correo = correo;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public String toString() {
        return """
           ┌─────────────── Cliente ───────────────┐
           │ Cédula : %s
           │ Nombre : %s %s
           │ Ciudad : %s
           │ Barrio : %s
           │ Calle  : %s
           │ Correo : %s
           └───────────────────────────────────────┘
           """.formatted(cedula, nombre, apellido, ciudad, barrio, calle, correo);
    }

}
