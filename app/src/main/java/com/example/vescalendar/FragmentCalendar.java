package com.example.vescalendar;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class FragmentCalendar extends Fragment {
    ListView listView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Calendar");

        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        listView = view.findViewById(R.id.listview);
        final SharedPreferences sharedPref = getActivity().getSharedPreferences("Login",0);
        String id = sharedPref.getString("user_id", "");


        if(id!=null){
            Log.d("Working",id);
            Background b = new Background();

            String dataFetch = null;
            try {
                dataFetch = b.execute(id).get();
                Log.d("This fetched data",dataFetch);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else{
            Log.d("Not working",id);
        }


        return view;
    }


    public class Background extends AsyncTask<String, Void, String> {
        public static final int CONNECTION_TIMEOUT = 10000;
        public static final int READ_TIMEOUT = 15000;

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder result=null;
            String id = strings[0];
            Log.d("Check",id);
            Log.d("wch", Arrays.toString(strings));

            String error="";
            id = id.replace("\'","");
            String connect = String.format("http://192.168.0.106/fetchdata.php?id=%s", id);
            Log.d("URL", connect);
            try {
                URL url = new URL(connect);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setReadTimeout(READ_TIMEOUT);
                http.setConnectTimeout(CONNECTION_TIMEOUT);
                http.setRequestMethod("GET");
                http.setDoOutput(true);


                InputStream ips = http.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(ips));

                result = new StringBuilder();

                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();
                ips.close();
                http.disconnect();
                return (result.toString());

            } catch (MalformedURLException e) {
                error = "Mal";
            } catch (IOException e) {
                error="IO";
            }
            return error;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String Eid[];
            String Title[];
            String Desp[];
            String Date[];
            String Time[];
            try {
                JSONArray jsonArray = new JSONArray(s);
                JSONObject jsonObject =null;
                Eid =  new String[jsonArray.length()];
                Title =  new String[jsonArray.length()];
                Desp =  new String[jsonArray.length()];
                Date =  new String[jsonArray.length()];
                Time =  new String[jsonArray.length()];

                for(int i=0;i<jsonArray.length();i++){
                    jsonObject = jsonArray.getJSONObject(i);
                    Eid[i] = jsonObject.getString("eid");
                    Title[i] = jsonObject.getString("title");
                    Desp[i] = jsonObject.getString("desp");
                    Date[i] = jsonObject.getString("date");
                    Time[i] = jsonObject.getString("time");
                }
                MyAdapter adapter = new MyAdapter(getActivity(), Eid, Title, Time, Date);
                listView.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    class MyAdapter extends ArrayAdapter<String> {
        Context c;
        String mTitle[];
        //String mDesp[];
        String mtime[];
        String mdate[];
        String Id[];

        MyAdapter(Context c, String Id[], String title[], String time[], String date[]) {
            super(c, R.layout.eventlayout, R.id.eventTitle, Id);
            this.c = c;
            this.mTitle = title;
            //this.mDesp = descp;
            this.mdate = date;
            this.mtime = time;
            this.Id = Id;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View event = layoutInflater.inflate(R.layout.eventlayout, parent, false);

            TextView eventTitle = event.findViewById(R.id.eventTitle);
            TextView eventDate = event.findViewById(R.id.eventdate);
            TextView eventTime = event.findViewById(R.id.eventtime);


            eventTitle.setText(mTitle[position]);
            //eventDesp.setText(mDesp[position]);
            eventDate.setText("Date: "+mdate[position]);
            eventTime.setText("Time: "+mtime[position]);

            return event;
        }
    }
}
