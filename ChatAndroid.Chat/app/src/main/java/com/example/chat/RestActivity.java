package com.example.chat;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONObject;


public abstract class RestActivity extends AppCompatActivity {

    protected GlobalState gs;

    // Une classe capable de faire des requêtes simplement
    // Si elle doit faire plusieurs requetes,
    // comment faire pour controler quelle requete se termine ?
    // on passe une seconde chaine à l'appel asynchrone

    public void envoiRequete(String qs, int method, JSONObject request, Response.Listener<JSONObject> onResponse, Response.ErrorListener onErrorResponse) {
        String url = gs.getUrl(qs);

        gs.alerter(url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (method, url, request, onResponse, onErrorResponse)
        {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<>();
                params.put("Authorization", "Bearer " + gs.getToken());

                return params;
            }
        };;

        // Access the RequestQueue through your singleton class.
        RequestQueueSingleton.getInstance(this.getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    public void envoiRequete(String qs, int method, JSONObject request, Response.Listener<JSONObject> onResponse) {
        envoiRequete(qs, method, request, onResponse, printError());
    }

    public String urlPeriodique() {
        // devrait être abstraite, mais dans ce cas doit être obligatoirement implémentée...
        // On pourrait utiliser une interface ?
        return "";
    }

    // http://androidtrainningcenter.blogspot.fr/2013/12/handler-vs-timer-fixed-period-execution.html
    // Try AlarmManager running Service
    // http://rmdiscala.developpez.com/cours/LesChapitres.html/Java/Cours3/Chap3.1.htm
    // La requete elle-même sera récupérée grace à l'action demandée dans la méthode urlPeriodique
    public void requetePeriodique(int periode, final int method, final JSONObject request, final Response.Listener<JSONObject> onResponse, final Response.ErrorListener onErrorResponse) {

        TimerTask doAsynchronousTask;
        final Handler handler = new Handler();
        Timer timer = new Timer();

        doAsynchronousTask = new TimerTask() {

            @Override
            public void run() {

                handler.post(new Runnable() {
                    public void run() {
                        envoiRequete(urlPeriodique(), method, request, onResponse, onErrorResponse);
                    }
                });

            }

        };

        timer.schedule(doAsynchronousTask, 0, 1000 * periode);

    }

    public void requetePeriodique(int periode, final int method, final JSONObject request, final Response.Listener<JSONObject> onResponse) {
        requetePeriodique(periode, method, request, onResponse, printError());
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

    private Response.ErrorListener printError() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                gs.alerter("Error");

            }
        };
    }

}