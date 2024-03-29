package com.example.vescalendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class FragmentCalendar extends Fragment {
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    ListView listView;
    String dataFetch =null;
    ProgressBar pb;

    String p_Title[];
    String p_Desp[];
    String p_Time[];
    String p_Date[];
    String p_Id[];
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Calendar");

        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        pb = (ProgressBar) view.findViewById(R.id.progress_calendar);
        listView = view.findViewById(R.id.listview);
        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.pullToRefresh);
        final SharedPreferences sharedPref = getActivity().getSharedPreferences("Login",0);
        final String id = sharedPref.getString("user_id", "");
        pb.setVisibility(View.VISIBLE);
        getData(id);
        SqliteDatabaseHelper sqliteDatabaseHelper = new SqliteDatabaseHelper(getActivity());
        String c_time = sdf.format(new Date());
        Date todayDate = Calendar.getInstance().getTime();
        String c_date = format.format(todayDate);
        try{
            Cursor res = sqliteDatabaseHelper.getDataForNotification(c_date,c_time);
            String e_id="";
            String e_title="";
            String e_desp="";
            String e_date="";
            String e_time="";
            while(res.moveToNext()) {
                e_id = res.getString(0);
                e_title = res.getString(1);
                e_desp = res.getString(2);
                e_date = res.getString(3);
                e_time = res.getString(4);
            }
            Calendar c= Calendar.getInstance();
            String month = e_date.substring(5,7);
            String months[] ={"Jan","Feb","Mar","Apr", "May", "Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
            int monthint = Integer.parseInt(month) -1;
            String da = e_date.substring(8,10);
            c.set(Calendar.MONTH, monthint);
            c.set(Calendar.DAY_OF_MONTH, Integer.valueOf(da));
            String hr = e_time.substring(0,2);
            String mi = e_time.substring(3,e_time.length()-1);
            c.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hr));
            c.set(Calendar.MINUTE, Integer.valueOf(mi));


            Intent notifyIntent = new Intent(getContext(),Notificationreceiver.class);
            Bundle b = new Bundle();
            b.putString("title",e_title);
            b.putString("desp",e_desp);
            notifyIntent.putExtras(b);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),100 , notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,  c.getTimeInMillis(),
                    1000 * 60 * 60 * 24, pendingIntent);

        }catch (Exception e){

        }

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(id);
                pullToRefresh.setRefreshing(false);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String data=(String)adapterView.getItemAtPosition(i);
                int index  = getID(data);
                String title = getTitle(index);
                String desp = getDesp(index);
                String date = getDate(index);
                String time = getTime(index);

                Intent intent = new Intent(getActivity(), DisplayEachCalendar.class);
                Bundle dataBundle = new Bundle();
                dataBundle.putString("id",data);
                dataBundle.putString("title",title);
                dataBundle.putString("desp",desp);
                dataBundle.putString("date",date);
                dataBundle.putString("time",time);
                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });

        return view;
    }
    public int getID(String v){
        return  Arrays.asList(p_Id).indexOf(v);
    }
    public String getTitle(int v){
        return  p_Title[v];
    }
    public String getDesp(int v){
        return  p_Desp[v];
    }
    public String getDate(int v){
        return  p_Date[v];
    }
    public String getTime(int v){
        return  p_Time[v];
    }


    public void getData(String id){
        if(id!=null){
            new Background().execute(id);
        }
        else{
            Toast.makeText(getActivity(),"Unable to fetch data in android",Toast.LENGTH_SHORT).show();
        }
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
            String connect = String.format("https://calendar2016.000webhostapp.com/fetchdata.php?id=%s", id);
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
            pb.setVisibility(View.GONE);
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
                MyAdapter adapter = new MyAdapter(getActivity(), Eid, Title,Desp ,Time, Date);
                listView.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    class MyAdapter extends ArrayAdapter<String> {
        Context c;
        String mTitle[];
        String mDesp[];
        String mtime[];
        String mdate[];
        String Id[];

        MyAdapter(Context c, String id[], String title[],String desp[], String time[], String date[]) {
            super(c, R.layout.eventlayout, R.id.eventTitle, id);
            this.c = c;
            this.mTitle = title;
            this.mDesp = desp;
            this.mdate = date;
            this.mtime = time;
            this.Id = id;

            p_Title = this.mTitle;
            p_Desp = this.mDesp;
            p_Time = this.mtime;
            p_Date = this.mdate;
            p_Id = this.Id;
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
            eventTime.setText(mtime[position]);

            return event;
        }
    }
}
