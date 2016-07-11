package outsport.psoft.uct.outsport.entidades;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by Daniel on 25-05-2016.
 */
@SuppressWarnings("serial")
public class Usuarios {

    private String nombre;
    private String apellido;
    private String correo;
    private String password;
    private int id_nivel;
    private int id_foto;
    static Drawable fondo;


    public Usuarios(String Anombre, String Bapellido, String correo, String password, int id_nivel, int id_foto) {
        this.nombre = Anombre;
        this.apellido = Bapellido;
        this.correo = correo;
        this.password = password;
        this.id_nivel = id_nivel;
        this.id_foto = id_foto;
    }

    public String getAnombre() {
        return nombre;
    }

    public String getBapellido() {
        return apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public String getPassword() {
        return password;
    }

    public int getId_nivel() {
        return id_nivel;
    }

    public int getId_foto() {
        return id_foto;
    }

    public static Drawable getFondo() {
        return fondo;
    }

    public void setAnombre(String anombre) {
        nombre = anombre;
    }

    public void setBapellido(String bapellido) {
        apellido = bapellido;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId_nivel(int id_nivel) {
        this.id_nivel = id_nivel;
    }

    public void setId_foto(int id_foto) {
        this.id_foto = id_foto;
    }

    public static void setFondo(Drawable fondo) {
        Usuarios.fondo = fondo;
    }
}