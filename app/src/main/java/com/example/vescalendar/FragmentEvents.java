package com.example.vescalendar;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentEvents extends Fragment {


//    String titles[] = {"Event1","Event2", "Event3","Event4","Event5","Event6"};
//    //String descriptions[] = {"Description 1", "Description 2","Description 3","Description 4","Description 5","Description 6"};
//    String times[] = {"123","123","123","123","123","123"};
//    String dates[] = {"123","123","123","123","123","123"};

    ListView listView;
    private Toolbar toolbar;
    FloatingActionButton floatingActionButton;

    SqliteDatabaseHelper sqliteDatabaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        getActivity().setTitle("My Events");
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        listView = view.findViewById(R.id.listview);
        listView.setClickable(true);
        sqliteDatabaseHelper = new SqliteDatabaseHelper(view.getContext());
        String ID[];
        String TITLE[];
        String TIME[];
        String DATE[];

        Cursor res = sqliteDatabaseHelper.getAllData();
        if(res.getCount()!=0){
            int size = res.getCount();
            ID = new String[size];
            TITLE = new String[size];
            DATE = new String[size];
            TIME= new String[size];
            int i =0;
            while(res.moveToNext()){
                ID[i] = res.getString(0);
                TITLE[i] = res.getString(1);
                DATE[i] = res.getString(3);
                TIME[i] = res.getString(4);;
                i+=1;
            }
        }
        else{
            int size = 0;
            ID = new String[size];
            TITLE = new String[size];
            DATE = new String[size];
            TIME= new String[size];
        }

        MyAdapter adapter = new MyAdapter(view.getContext(), ID,TITLE, TIME, DATE);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String data=(String)adapterView.getItemAtPosition(position);
                Log.w("Clicked", data );
                Cursor res = sqliteDatabaseHelper.getValById(data);
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
                    Log.w("printing data", e_id + " " + e_title + " " + e_desp + " " + e_date + " " + e_time);
                    //getFragmentManager().beginTransaction().replace(R.id.fragment_container, new DisplayEachMyEvents()).addToBackStack("FragementEvents").commit();

                }
                Intent intent = new Intent(getActivity(), DisplayEachMyEvents.class);
                Bundle dataBundle = new Bundle();
                dataBundle.putString("id",e_id);
                dataBundle.putString("title",e_title);
                dataBundle.putString("desp",e_desp);
                dataBundle.putString("date",e_date);
                dataBundle.putString("time",e_time);
                intent.putExtras(dataBundle);
                startActivity(intent);

            }

        });

        floatingActionButton = (FloatingActionButton)view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddNewEvent.class));
            }
        });


        return view;
    }

    class MyAdapter extends ArrayAdapter<String>{
        Context c;
        String mTitle[];
        //String mDesp[];
        String mtime[];
        String mdate[];
        String Id[];

        MyAdapter(Context c, String Id[],String title[], String time[], String date[]){
            super(c, R.layout.eventlayout,R.id.eventTitle,Id);
            this.c = c;
            this.mTitle = title;
            //this.mDesp = descp;
            this.mdate = date;
            this.mtime = time;
            this.Id = Id;
            //Log.d("To Test This", Arrays.toString(title)+" || "+Arrays.toString(descp)+"||"+Arrays.toString(date)+"||");
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View event = layoutInflater.inflate(R.layout.eventlayout,parent,false);

            TextView eventTitle = event.findViewById(R.id.eventTitle);
            // TextView eventDesp = event.findViewById(R.id.eventdescp);
            TextView eventDate = event.findViewById(R.id.eventdate);
            TextView eventTime = event.findViewById(R.id.eventtime);


            eventTitle.setText(mTitle[position]);
            //eventDesp.setText(mDesp[position]);
            eventDate.setText(mdate[position]);
            eventTime.setText(mtime[position]);

            return event;
        }
    }

}

