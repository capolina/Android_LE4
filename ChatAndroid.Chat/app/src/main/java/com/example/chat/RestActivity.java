package com.example.chat;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;


public abstract class RestActivity extends AppCompatActivity {

    protected GlobalState gs;

    protected Timer timer = new Timer();

    // Une classe capable de faire des requêtes simplement
    // Si elle doit faire plusieurs requetes,
    // comment faire pour controler quelle requete se termine ?
    // on passe une seconde chaine à l'appel asynchrone

    public void envoiRequete(String qs, String action, int method, JSONObject request) {
        String url = gs.getUrl(qs);

        //gs.alerter(url);
        Log.i(gs.CAT, "Request sent to " + url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (method, url, request, handleSuccess(action), handleError(action))
        {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<>();
                params.put("Authorization", "Bearer " + gs.getToken());

                return params;
            }
        };

        // Access the RequestQueue through your singleton class.
        RequestQueueSingleton.getInstance(this.getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    public String urlPeriodique(String action) {
        // devrait être abstraite, mais dans ce cas doit être obligatoirement implémentée...
        // On pourrait utiliser une interface ?
        return "";
    }

    // http://androidtrainningcenter.blogspot.fr/2013/12/handler-vs-timer-fixed-period-execution.html
    // Try AlarmManager running Service
    // http://rmdiscala.developpez.com/cours/LesChapitres.html/Java/Cours3/Chap3.1.htm
    // La requete elle-même sera récupérée grace à l'action demandée dans la méthode urlPeriodique
    public void requetePeriodique(int periode, final String action, final int method, final JSONObject request) {

        TimerTask doAsynchronousTask;
        final Handler handler = new Handler();

        doAsynchronousTask = new TimerTask() {

            @Override
            public void run() {

                handler.post(new Runnable() {
                    public void run() {
                        envoiRequete(urlPeriodique(action), action, method, request);
                    }
                });

            }

        };

        timer.schedule(doAsynchronousTask, 0, 1000 * periode);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gs = (GlobalState) getApplication();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.action_account : gs.alerter("Non implementé"); break;
            case R.id.action_settings :
                Intent versSettings = new Intent(this, SettingsActivity.class);
                startActivity(versSettings);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        timer.cancel();
    }

    public abstract void successCallBack(JSONObject result, String action);

    public void errorCallBack(VolleyError result, String action)
    {
        gs.alerter("Error");
    }

    private Response.Listener<JSONObject> handleSuccess(final String action) {
        return new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject result) {
                successCallBack(result, action);
            }
        };
    }

    private Response.ErrorListener handleError(final String action) {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                errorCallBack(error, action);
            }
        };
    }

}