package com.example.vitaly.fintech;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.net.URLEncoder;

import newtwork_working.HttpConnectionTask;
import newtwork_working.TypeRequest;

/**
 * Created by vitaly on 26.06.17.
 */

public class SendNameActivity extends AppCompatActivity {

    private String phone;
    private String accessToken;
    private String keyJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_name);

        Intent intent = getIntent();
        String jsonString = intent.getStringExtra("json");
        JSONObject jsonObject;

        try {
            this.keyJson = jsonString;
            jsonObject = new JSONObject(jsonString);
            phone = jsonObject.get("phone").toString();
            accessToken = jsonObject.get("access_token").toString();
        } catch (Exception e) {
            super.onBackPressed();
        }
    }

    public void sendName (View view) throws Exception {
        String lastName = ((EditText) findViewById(R.id.last_name)).getText().toString();
        String firstName = ((EditText) findViewById(R.id.first_name)).getText().toString();

        if (lastName.length() == 0|| firstName.length() == 0) {
            ((TextView) findViewById(R.id.reaction)).setText("Заполните оба поля");
            return;
        }

        String sendedString = URLEncoder.encode(lastName + firstName, "UTF-8").replaceAll("\\+", "%20");

        AsyncTask send = new HttpConnectionTask(TypeRequest.SendName)
                .execute(phone, sendedString, accessToken);

        Intent intent = new Intent(this, FindChatActivity.class);
        intent.putExtra("json", keyJson);
        intent.putExtra("name", lastName + firstName);
        startActivity(intent);
    }
}
