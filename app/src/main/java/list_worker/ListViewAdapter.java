package list_worker;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.vitaly.fintech.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import newtwork_working.HttpConnectionTask;
import newtwork_working.TypeRequest;

/**
 * Created by vitaly on 29.06.17.
 */

public class ListViewAdapter extends BaseAdapter {


    Context mContext;
    LayoutInflater inflater;
    private List<PeopleDatas> peopleDatasList = null;
    private ArrayList<PeopleDatas> arraylist;

    public ListViewAdapter(Context context, List<PeopleDatas> peopleDatas) {
        mContext = context;
        this.peopleDatasList = peopleDatas;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<PeopleDatas>();
        this.arraylist.addAll(peopleDatas);
    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        return peopleDatasList.size();
    }

    @Override
    public PeopleDatas getItem(int position) {
        return peopleDatasList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_view_items, null);
            holder.name = (TextView) view.findViewById(R.id.name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.name.setText(peopleDatasList.get(position).getName());
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        String jsonArray = null;
        peopleDatasList.clear();

        if (charText.length() == 0) {
            peopleDatasList.addAll(arraylist);
        }
        else {
            try {
                String sendedString = URLEncoder.encode(charText, "UTF-8").replaceAll("\\+", "%20");
                AsyncTask httpConnectionTask = new HttpConnectionTask(TypeRequest.FindUser).execute(sendedString);
                jsonArray = httpConnectionTask.get().toString();
                JSONArray json = new JSONArray(jsonArray);
                for (int i = 0; i < json.length(); i++) {
                    JSONObject object = json.getJSONObject(i);
                    String name = object.get("Name").toString();
                    for (int j = 1; j < name.length(); j++) {
                        if (Character.isUpperCase(name.charAt(j))) {
                            name = name.substring(0,j) + " " + name.substring(j);
                            break;
                        }
                    }
                    PeopleDatas peopleDatas = new PeopleDatas(name);
                    peopleDatasList.add(peopleDatas);
                }
            } catch (Exception e) {
            }
        }

        notifyDataSetChanged();
    }



}
