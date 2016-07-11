package outsport.psoft.uct.outsport;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import outsport.psoft.uct.outsport.BD.Constantes;
import outsport.psoft.uct.outsport.BD.CustomVolleyRequestQueue;
import outsport.psoft.uct.outsport.BD.Peticiones;
import outsport.psoft.uct.outsport.componentes.Colors;
import outsport.psoft.uct.outsport.componentes.DinamicBar;
import outsport.psoft.uct.outsport.componentes.LinearLayout;
import outsport.psoft.uct.outsport.componentes.TextView;
import outsport.psoft.uct.outsport.entidades.Eventos;
import outsport.psoft.uct.outsport.util.Internet;
import outsport.psoft.uct.outsport.util.TrasladorDeObjetos;

public class PerfilEventosActivity extends AppCompatActivity implements View.OnClickListener, RequestQueue.RequestFinishedListener<Object>, Response.ErrorListener, Response.Listener<JSONObject> {
    private ActionBar actionBar;
    private DinamicBar db;
    private Intent i;
    private DisplayMetrics met;
    private LinearLayout layout_buttons;
    private Eventos ev;
    private RequestQueue mQueue;
    private Peticiones pet;
    private TextView txt;

    @Override
    protected void onStart() {
        super.onStart();

        if (Internet.isOnline(this)) {
            pet.ProcessDialog();
            pet.Obtener(
                    this,
                    this,
                    Constantes.obtener_eventos_id + ev.getId()
            );
            mQueue.add(pet.getJsonRequest());
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "Compruebe su conexion a Internet",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setTheme(android.R.style.Theme_Holo_NoActionBar_TranslucentDecor);

        pet = new Peticiones(this);
        mQueue = CustomVolleyRequestQueue.getInstance(
                this.getApplicationContext()
        ).getRequestQueue();
        mQueue.addRequestFinishedListener(this);

        obtener_event();
        met = getResources().getDisplayMetrics();
        db = new DinamicBar(
                this,
                met
        );
        db.getImg().setBackgroundDrawable(
                getResources().getDrawable(
                        R.mipmap.ic_launcher
                )
        );

        layout_buttons = new LinearLayout(
                this,
                LinearLayout.VERTICAL,
                Gravity.BOTTOM,
                Color.TRANSPARENT,
                new int[]{25, 0, 25, met.widthPixels * 20 / 100}, // padding
                met
        );

        dynamicToolbarColor();
        toolbarTextAppernce();

        db.getLinearLayout().addView(
                layout_buttons,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        db.getCollapsingToolbarLayout().setTitle(
                ev.getTitulo()
        );
        txt = new TextView(this);

        db.getLinearLayout().addView(
                txt
        );

        setContentView(db);
        setSupportActionBar(db.getToolbar());
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void obtener_event() {
        if (TrasladorDeObjetos.getEvent() != null)
            ev = (Eventos) TrasladorDeObjetos.getEvent();
    }

    private void dynamicToolbarColor() {
        Bitmap bitmap = BitmapFactory.decodeResource(
                getResources(),
                R.mipmap.ic_launcher
        );

        Palette.from(bitmap).generate(
                new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        db.getCollapsingToolbarLayout().setContentScrimColor(
                                palette.getMutedColor(
                                        Color.DKGRAY
                                )
                        );
                        db.getCollapsingToolbarLayout().setStatusBarScrimColor(
                                palette.getMutedColor(
                                        Color.BLUE
                                )
                        );
                    }
                }
        );
    }

    private void toolbarTextAppernce() {
        db.getCollapsingToolbarLayout().setCollapsedTitleTextAppearance(
                R.style.collapsedappbar
        );
        db.getCollapsingToolbarLayout().setExpandedTitleTextAppearance(
                R.style.expandedappbar
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 0, 0, "Asistir a evento").setShortcut('3', 'c');
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                // code for option1
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        i = new Intent(
                this,
                ActivityMaps.class
        );
        startActivity(i);
        finish();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onRequestFinished(Request<Object> request) {
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        pet.DetenerProgressDialog(false);
        Log.d("Error: ", error.getMessage());
    }

    @Override
    public void onResponse(JSONObject response) {
        pet.DetenerProgressDialog(true);

        try {
            String estado = response.getString("estado");

            switch (estado) {
                case "1": // EXITO
                    JSONObject info = response.getJSONObject("evento");

                    db.getCollapsingToolbarLayout().setTitle(
                            info.getString("descripcion")
                    );
                    txt.setTextSize(25);
                    txt.setText(
                           "Descripcion  : " + info.getString("descripcion") + "\n\n" +
                           "Fecha Evento : " + info.getString("fecha") + "\n" +
                           "Hora Evento   : " + info.getString("hora") + "\n" +
                           "Costo Evento : " + info.getString("precio")
                    );
                    break;

                case "2": // FALLIDO
                    String mensaje2 = response.getString("mensaje");
                    Log.d(mensaje2, "");
                    break;
            }
        } catch (JSONException e) {
            System.out.println(e);
        }
    }
}
