package outsport.psoft.uct.outsport.BD;

import android.content.Context;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;
import outsport.psoft.uct.outsport.componentes.CustomProgressDialog;

/**
 * Created by Daniel on 27-05-2016.
 */
public class Peticiones {

    private Context context;
    private RequestQueue mQueue;
    private CustomJSONObjectRequest jsonRequest;
    private CustomProgressDialog cpd;
    private JSONObject jsob;
    private boolean flag = true;

    public Peticiones(Context context) {
        this.context = context;
    }

    public void ProcessDialog() {
        this.cpd = new CustomProgressDialog(
                this.context,
                "Cargando......."
        );
    }

    public void DetenerProgressDialog(boolean f) {
            flag = f;
            cpd.hideProgressDialog();
    }

    public void Obtener(Response.Listener<JSONObject> listener_JSONObject, Response.ErrorListener listener_error, String url){
        cpd.showProgressDialog();

        jsonRequest = new CustomJSONObjectRequest(
                Request.Method.GET,
                url,
                new JSONObject(),
                listener_JSONObject,
                listener_error
        );
        jsonRequest.setTag(
                context.getClass().getSimpleName()
        );
    }


    public void Insertar(Response.Listener<JSONObject> listener_JSONObject, Response.ErrorListener listener_error, JSONObject objeto, String url) {
        cpd.showProgressDialog();

        jsonRequest = new CustomJSONObjectRequest(
                Request.Method.POST,
                url,
                objeto,
                listener_JSONObject,
                listener_error
        );
        jsonRequest.setTag(
                context.getClass().getSimpleName()
        );
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public JSONObject getJsob() {
        return jsob;
    }

    public RequestQueue getmQueue() {
        return mQueue;
    }

    public CustomJSONObjectRequest getJsonRequest() {
        return jsonRequest;
    }

    public CustomProgressDialog getCpd() {
        return cpd;
    }
}

/*
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        flag = true;
                        jsob = response;
                        cpd.hideProgressDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        flag = false;
                        cpd.hideProgressDialog();
                        Log.d("Error: ", error.getMessage());
                    }

                }
 */
