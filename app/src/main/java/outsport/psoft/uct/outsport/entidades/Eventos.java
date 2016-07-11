package outsport.psoft.uct.outsport.entidades;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Daniel on 16-06-16.
 */
public class Eventos {
    private int    id;
    private String titulo;
    private String subtitulo;
    private LatLng ubicacion;

    public Eventos(int id, String subtitulo, String titulo, LatLng ubicacion) {
        this.id = id;
        this.subtitulo = subtitulo;
        this.titulo = titulo;
        this.ubicacion = ubicacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public LatLng getUbicacion() {
        return ubicacion;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }

    public void setUbicacion(LatLng ubicacion) {
        this.ubicacion = ubicacion;
    }
}
