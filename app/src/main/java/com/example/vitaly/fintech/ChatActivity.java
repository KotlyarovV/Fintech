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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * Created by vitaly on 30.06.17.
 */

public class ChatActivity extends AppCompatActivity {

    private String phone;
    private String accessToken;
    private String recieverName;
    private String recieverPhone;
    private String json;
    private OkHttpClient client;


    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;


        public void getMessage (String text) {
            try {
                JSONObject object = new JSONObject(text);
                String name = object.getString("ReceiverName");
                String money = object.getString("Money");
                String phone = object.getString("Receiver");

                setBalance(money, phone);

            }  catch (Throwable e) {}
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            getMessage(text);
        }
        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            getMessage(bytes.hex());
        }
        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
        }
        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            try {

            } catch (Exception e) {}
        }
    }


    private void startSocket() {
        Request request = new Request.Builder()
                .addHeader("phone", phone)
                .addHeader("access_token", accessToken)
                .url("ws://echo.websocket.org").build();
        EchoWebSocketListener listener = new EchoWebSocketListener();
        WebSocket ws = client.newWebSocket(request, listener);
        //  client.dispatcher().executorService().shutdown();
    }

    public void setBalance (String balance) {
        if (balance == null) balance = "0";
        ((TextView) findViewById(R.id.balance)).setText("Баланс " + balance + "рублей");
    }

    public void setBalance (String balance, String phone) {

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
            phone = jsonObject.getString("phone");
            accessToken = jsonObject.getString("access_token");
        } catch (Exception e) {}

        client = new OkHttpClient();
        startSocket();
    }

    public void sendMoney(View view) {
        String number = ((TextView) findViewById(R.id.money_for_send)).getText().toString();

        if (number.length() == 0 ) {
            return;
        }

        AsyncTask httpConnectionTask = new HttpConnectionTask(TypeRequest.Pay)
                    .execute(phone, accessToken, recieverPhone, number);
    }
}
