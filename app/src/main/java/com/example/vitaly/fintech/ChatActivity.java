package com.example.vitaly.fintech;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import newtwork_working.HttpConnectionTask;
import newtwork_working.TypeRequest;

/**
 * Created by vitaly on 30.06.17.
 */

public class ChatActivity extends AppCompatActivity {

    private String phone;
    private String access_token;
    private String recieverName;
    private String recieverPhone;
    private String json;

    public void setBalance (String balance) {
        ((TextView) findViewById(R.id.balance)).setText("Баланс " + balance + "рублей");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);



        Intent intent = getIntent();
        json = intent.getStringExtra("json");
        recieverName = intent.getStringExtra("recieverName");
        recieverPhone = intent.getStringExtra("recieverPhone");
        ((TextView) findViewById(R.id.name)).setText(recieverName);
        setBalance(intent.getStringExtra("money"));

        try {
            JSONObject jsonObject = new JSONObject(json);
            String phone = jsonObject.getString("phone");
            String accessToken = jsonObject.getString("access_token");
        } catch (Exception e) {}

    }

    public void sendMoney(View view) {
        String number = ((TextView) findViewById(R.id.phone)).getText().toString();

        if (number.length() == 0 ) {
            return;
        }

        AsyncTask httpConnectionTask = new HttpConnectionTask(TypeRequest.Pay)
                    .execute(phone, access_token, recieverPhone, number);
        ((TextView) findViewById(R.id.phone)).setText("");
    }
}
