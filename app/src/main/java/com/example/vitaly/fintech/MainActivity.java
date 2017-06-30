package com.example.vitaly.fintech;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import newtwork_working.HttpConnectionTask;
import newtwork_working.TypeRequest;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private boolean isNumber (String number) {
        if (number.length() !=  10) return false;
        try {
            long n = Long.parseLong(number);
        }
        catch (NumberFormatException nfe){
            return false;
        }
        return true;
    }

    public void sendNumber(View view) throws Exception{

        String number = ((EditText) findViewById(R.id.phone)).getText().toString();

        if (!isNumber(number) ) {
            ((TextView) findViewById(R.id.reaction)).setText("Введен неверный номер");
            return;
        }

        AsyncTask httpConnectionTask = new HttpConnectionTask(TypeRequest.GetSms).execute(number);
        Object keyJsonObject = httpConnectionTask.get();


        if (keyJsonObject == null) {

        } else {
            String keyJson = (String) keyJsonObject;
            JSONObject jsonObject = new JSONObject(keyJson);
            String key = jsonObject.get("code").toString();
            Intent intent = new Intent(this, GetMessageActivity.class);
            intent.putExtra("code", key);
            startActivity(intent);
        }
    }
}
