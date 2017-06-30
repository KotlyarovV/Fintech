package com.example.vitaly.fintech;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import list_worker.PeopleDatas;
import list_worker.ListViewAdapter;
import newtwork_working.HttpConnectionTask;
import newtwork_working.TypeRequest;

import static com.example.vitaly.fintech.R.id.parent;
import static com.example.vitaly.fintech.R.styleable.View;

/**
 * Created by vitaly on 29.06.17.
 */

public class FindChatActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    String json ;
    String name;
    FindChatActivity findChatActivity;

    ListView list;
    ListViewAdapter adapter;
    SearchView editsearch;
    ArrayList<PeopleDatas> arraylist = new ArrayList<PeopleDatas>();
    ArrayList<String> names = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_chat);
        final FindChatActivity findChatActivity = this;

        Intent intent = getIntent();
        json = intent.getStringExtra("json");
        name = intent.getStringExtra("name");

        try {
            JSONObject jsonObject = new JSONObject(json);
            String phone = jsonObject.getString("phone");
            String accessToken = jsonObject.getString("access_token");

            AsyncTask getName = new HttpConnectionTask(TypeRequest.GetUsers).execute(phone, accessToken);
            String jsonString = getName.get().toString();

            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);

                String name = object.get("ReceiverName").toString();
                String balanse = object.get("Balance").toString();
                String phoneReciever = object.get("Receiver").toString();


                for (int j = 1; j < name.length(); j++) {
                    if (Character.isUpperCase(name.charAt(j))) {
                        name = name.substring(0,j) + " " + name.substring(j);
                        break;
                    }
                }

                PeopleDatas peopleDatas = new PeopleDatas(name, balanse, phoneReciever);
                arraylist.add(peopleDatas);
            }

        } catch (Exception e) {}

        list = (ListView) findViewById(R.id.listview);
        adapter = new ListViewAdapter(this, arraylist);

        // Binds the Adapter to the ListView
        list.setAdapter(adapter);

        // Locate the EditText in listview_main.xml
        editsearch = (SearchView) findViewById(R.id.search);
        editsearch.setOnQueryTextListener(this);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, android.view.View itemClicked, int position,
                                    long id) {

                PeopleDatas reciever = arraylist.get(position);
                Intent intent = new Intent(findChatActivity, ChatActivity.class);
                intent.putExtra("json", json);
                intent.putExtra("name", name);
                intent.putExtra("recieverName", reciever.getName());
                intent.putExtra("money", reciever.getMoney());
                intent.putExtra("recieverPhone", reciever.getPhone());
                startActivity(intent);
            }
        });

    }




    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        adapter.filter(text);
        return false;
    }


}
