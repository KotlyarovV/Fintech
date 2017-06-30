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
 * Created by vitaly on 25.06.17.
 */

public class GetMessageActivity extends AppCompatActivity {

    String key;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_message);

        Intent intent = getIntent();
        key = intent.getStringExtra("code");
    }

    public void onBackPressed(View view) {
        super.onBackPressed();
    }

    private boolean isCode (String number) {
        try {
            long n = Long.parseLong(number);
        }
        catch (NumberFormatException nfe){
            return false;
        }
        return true;
    }

    public void sendCode(View view) throws Exception{
        String number = ((EditText) findViewById(R.id.code)).getText().toString();

        if (!isCode(number) ) {
            ((TextView) findViewById(R.id.reaction)).setText("Введен неверный код");
            return;
        }

        AsyncTask httpConnectionTask = new HttpConnectionTask(TypeRequest.GetToken).execute(key, number);
        String keyJson = httpConnectionTask.get().toString();

        if (keyJson == null) {
            ((TextView) findViewById(R.id.reaction)).setText("Введен неверный код");
        } else {

            JSONObject jsonObject = new JSONObject(keyJson);
            String phone = jsonObject.get("phone").toString();
            String accessToken = jsonObject.get("access_token").toString();
            AsyncTask getName = new HttpConnectionTask(TypeRequest.GetName).execute(phone, accessToken);

            String jsonNameString = getName.get().toString();
            JSONObject jsonName = new JSONObject(jsonNameString);
            String name = jsonName.get("name").toString();

            if (name.equals("none")) {
                Intent intent = new Intent(this, SendNameActivity.class);
                intent.putExtra("json", keyJson);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, FindChatActivity.class);
                intent.putExtra("json", keyJson);
                intent.putExtra("name", name);
                startActivity(intent);
            }

        }
    }
}
