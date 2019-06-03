package com.example.chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.example.chat.Model.Login;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends RestActivity implements View.OnClickListener {

    private TextInputLayout champLogin;
    private TextInputLayout champPass;
    private Switch champRemember;
    private GlobalState gs;
    private Button btnOK;

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
            champLogin.getEditText().setText(login);
            champPass.getEditText().setText(passe);
            champRemember.setChecked(true);
        }

    }

    private void sendLoginRequest() {
        String login = champLogin.getEditText().getText().toString();
        String pass = champPass.getEditText().getText().toString();

        JSONObject request = new JSONObject();
        try {
            request.put("username", login);
            request.put("password", pass);
        } catch(JSONException e) {
            gs.alerter("Error while logging in");
        }

        String qs = "user/login";

        // On se sert des services offerts par RestActivity,
        // qui propose des méthodes d'envoi de requetes asynchrones
        envoiRequete(qs, "login", Request.Method.POST, request);
    }

    @Override
    public void successCallBack(JSONObject result, String action)
    {
        if(action.contentEquals("login"))
        {
            login(result);
        }
    }

    @Override
    public void errorCallBack(VolleyError error, String action)
    {
        if(action.contentEquals("login"))
        {
            Log.e(gs.CAT, String.valueOf(error.networkResponse.statusCode));
            switch( error.networkResponse.statusCode) {
                case 400:
                    gs.alerter("Bad username or password");
                    break;
                default:
                    gs.alerter("Unknown error");
            }
        }
    }

    private void login(JSONObject result) {
        Log.i("L4-SI-Logs", result.toString());
        //LoginActivity.this.gs.alerter(result.toString());

        Gson mGson = new Gson();

        Login login = mGson.fromJson(result.toString(), Login.class);

        LoginActivity.this.gs.alerter("Connexion réussie");
        LoginActivity.this.gs.setToken(login.getToken());

        Intent toChoixConv = new Intent(LoginActivity.this,ChoixConvActivity.class);
        startActivity(toChoixConv);
    }

    private void savePrefs() {
        SharedPreferences settings =
                PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();

        if (champRemember.isChecked()) {
            editor.putBoolean("remember", true);
            editor.putString("login", champLogin.getEditText().getText().toString());
            editor.putString("passe", champPass.getEditText().getText().toString());
        } else {
            editor.putBoolean("remember", false);
            editor.putString("login", "");
            editor.putString("passe", "");
        }
        // editor.putString("urlData", settings.getString("urlData", "http://10.0.2.2/android_chat/data.php"));
        editor.apply();
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
