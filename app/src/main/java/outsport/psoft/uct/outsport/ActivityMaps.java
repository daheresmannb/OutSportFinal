package outsport.psoft.uct.outsport;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import outsport.psoft.uct.outsport.BD.Constantes;
import outsport.psoft.uct.outsport.BD.CustomVolleyRequestQueue;
import outsport.psoft.uct.outsport.BD.Peticiones;
import outsport.psoft.uct.outsport.entidades.Eventos;
import outsport.psoft.uct.outsport.util.Internet;
import outsport.psoft.uct.outsport.util.TrasladorDeObjetos;

/**
 * Created by Daniel on 29-04-2016.
 */
public class ActivityMaps extends FragmentActivity implements View.OnClickListener, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, Response.ErrorListener, Response.Listener<JSONObject> {
    private GoogleMap mapa;
    private RequestQueue mQueue;
    private Peticiones pet;
    private Button boton;
    private Intent i;

    public void marca_punto(LatLng punto, String titulo, String desc) {
        this.mapa.addMarker( /// agregar letrero con icono en el mapa
                new MarkerOptions()
                        .position(punto)
                        .title(titulo)
                        .snippet(desc)
                        .icon(
                                BitmapDescriptorFactory
                                        .fromResource(
                                                R.mipmap.ic_launcher
                                        )
                        )
                        .anchor(1f, 1f)
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_map);

        pet = new Peticiones(this);
        mQueue = CustomVolleyRequestQueue.getInstance(
                this.getApplicationContext()
        ).getRequestQueue();

        boton = (Button) findViewById(R.id.button4);
        boton.setOnClickListener(this);
        mapa = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

        mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        mapa.getUiSettings().setZoomControlsEnabled(false);
        mapa.getUiSettings().setCompassEnabled(true);
        mapa.setOnMapClickListener(this);
        mapa.setOnMarkerClickListener(this);

/*
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mapa.setMyLocationEnabled(true);
        }
        */
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    public void animateCamera(View view) {
        if (mapa.getMyLocation() != null)
            mapa.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                            new LatLng(
                                    mapa.getMyLocation().getLatitude(),
                                    mapa.getMyLocation().getLongitude()
                            ),
                            15
                    )
            );
    }

    public void addMarker(View view) {
        mapa.addMarker(new MarkerOptions().position(
                mapa.getCameraPosition().target));
    }

    @Override
    public void onMapClick(LatLng puntoPulsado) {
        mapa.addMarker(
                new MarkerOptions().position(puntoPulsado).icon(
                        BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)
                )
        );
    }

    @Override
    public void onBackPressed() {
        i = new Intent(
                this,
                PerfilActivity.class
        );
        startActivity(i);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button4:
                if (Internet.isOnline(this)) {
                    pet.ProcessDialog();
                    /*      System.out.println(Constantes.Obtener_eventos_cercanos +
                            "?lat=" + -38.7289346 +
                            "&lng=" + -72.6729331 +
                            "&distance=" + 100);
                            */
                    pet.Obtener(
                            this,
                            this,
                            (
                                    Constantes.Obtener_eventos_cercanos +
                                            "?lat=" + -38.7289346 +
                                            "&lng=" + -72.6729331 +
                                            "&distance=" + 100
                    ).trim());
                    mQueue.add(pet.getJsonRequest());
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            "Compruebe su conexion a Internet",
                            Toast.LENGTH_LONG
                    ).show();
                }
                break;
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Eventos ev = new Eventos(
                Integer.parseInt(marker.getSnippet().trim()),
                marker.getSnippet(),
                marker.getTitle(),
                marker.getPosition()
        );
        TrasladorDeObjetos.setEvent(ev);
        i = new Intent(
                this,
                PerfilEventosActivity.class
        );
        startActivity(i);
        //finish();
        return false;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        pet.DetenerProgressDialog(false);
        Log.d("Error: ", error.getMessage());
    }

    @Override
    public void onResponse(JSONObject response) {
        pet.DetenerProgressDialog(true);

        if (pet.isFlag() == true) {
            try {
                String estado = response.getString("estado");

                switch (estado) {
                    case "1": // EXITO
                        JSONArray jArray = response.getJSONArray("ubicaciones_eventos");

                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject json_data = jArray.getJSONObject(i);

                            marca_punto(
                                    new LatLng(
                                            json_data.getDouble("X"),
                                            json_data.getDouble("Y")
                                    ),
                                    json_data.getString("nombre"),
                                    "" + json_data.getString("id_evento")
                            );

                            mapa.moveCamera(
                                    CameraUpdateFactory.newLatLngZoom(
                                            new LatLng(
                                                    -38.7289346,
                                                    -72.6729331
                                            ),
                                            8
                                    )
                            );
                        }
                        break;

                    case "2": // FALLIDO
                        String mensaje2 = pet.getJsob().getString("mensaje");
                        Log.d(mensaje2, "");
                        break;
                }
            } catch (JSONException e) {
                System.out.println(e);
            }
        } else {
        }
    }
}