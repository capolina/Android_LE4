package com.example.chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends RestActivity implements View.OnClickListener {

    EditText champLogin;
    EditText champPass;
    CheckBox champRemember;
    GlobalState gs;
    Button btnOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gs = (GlobalState) getApplication();

        Log.i(gs.CAT,"onCreate");

        setContentView(R.layout.activity_login);

        champLogin = findViewById(R.id.login_edtLogin);
        champPass = findViewById(R.id.login_edtPasse);
        champRemember = findViewById(R.id.login_cbRemember);
        btnOK = findViewById(R.id.login_btnOK);

        champRemember.setOnClickListener(this);
        btnOK.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i(gs.CAT,"onResume");

        // Verif Réseau, si dispo, on active le bouton
        btnOK.setEnabled(gs.verifReseau());
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.i(gs.CAT,"onStart");

        // Récupérer les préférences, si la case 'remember' est cochée, on complète le formulaire
        // autres champs des préférences : urlData, login, passe

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (prefs.getBoolean("remember",true)) {
            String login = prefs.getString("login", "");
            String passe = prefs.getString("passe", "");
            champLogin.setText(login);
            champPass.setText(passe);
            champRemember.setChecked(true);
        }

    }

    private void sendLoginRequest() {
        String login = champLogin.getText().toString();
        String passe = champPass.getText().toString();

        String qs = "action=connexion&login=" + login + "&passe=" +passe;

        // On se sert des services offerts par RestActivity,
        // qui propose des méthodes d'envoi de requetes asynchrones
        envoiRequete(qs, login());
    }

    private Response.Listener<JSONObject> login() {
        return new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject result) {
                Log.i("L4-SI-Logs", result.toString());
                //LoginActivity.this.gs.alerter(result.toString());

                // TODO: Vérifier la connexion ("connecte":true)
                try {
                    if (result.getBoolean("connecte")) {
                        LoginActivity.this.gs.alerter("Connexion réussie");
                        LoginActivity.this.savePrefs();
                        // TODO: Changer d'activité vers choixConversation
                        Intent toChoixConv = new Intent(LoginActivity.this,ChoixConvActivity.class);
                        startActivity(toChoixConv);

                    }
                    else {
                        LoginActivity.this.gs.alerter("Connexion échouée");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void savePrefs() {
        SharedPreferences settings =
                PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();

        if (champRemember.isChecked()) {
            editor.putBoolean("remember", true);
            editor.putString("login", champLogin.getText().toString());
            editor.putString("passe", champPass.getText().toString());
        } else {
            editor.putBoolean("remember", false);
            editor.putString("login", "");
            editor.putString("passe", "");
        }
        editor.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_cbRemember : // Clic sur case à cocher
                savePrefs();
                break;
            case R.id.login_btnOK :
                sendLoginRequest();
                break;

        }

    }
}
