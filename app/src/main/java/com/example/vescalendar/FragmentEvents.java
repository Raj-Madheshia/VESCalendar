package com.example.vescalendar;

import android.content.Context;
import android.content.Intent;
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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;

public class FragmentEvents extends Fragment {


    String titles[] = {"Event1","Event2", "Event3","Event4","Event5","Event6"};
    String descriptions[] = {"Description 1", "Description 2","Description 3","Description 4","Description 5","Description 6"};
    String times[] = {"123","123","123","123","123","123"};
    String dates[] = {"123","123","123","123","123","123"};

    ListView listView;
    FloatingActionButton floatingActionButton;

    SqliteDatabaseHelper sqliteDatabaseHelper;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        getActivity().setTitle("My Events");
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        listView = view.findViewById(R.id.listview);

        sqliteDatabaseHelper = new SqliteDatabaseHelper(view.getContext());


        MyAdapter adapter = new MyAdapter(view.getContext(), titles,descriptions, times, dates);
        listView.setAdapter(adapter);

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
        String mDesp[];
        String mtime[];
        String mdate[];

        MyAdapter(Context c, String title[], String descp[],String time[], String date[]){
            super(c, R.layout.eventlayout,R.id.eventTitle,title);
            this.c = c;
            this.mTitle = title;
            this.mDesp = descp;
            this.mdate = date;
            this.mtime = time;
            Log.d("To Test This", Arrays.toString(title)+" || "+Arrays.toString(descp)+"||"+Arrays.toString(date)+"||");
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View event = layoutInflater.inflate(R.layout.eventlayout,parent,false);

            TextView eventTitle = event.findViewById(R.id.eventTitle);
            TextView eventDesp = event.findViewById(R.id.eventdescp);
            TextView eventDate = event.findViewById(R.id.eventdate);
            TextView eventTime = event.findViewById(R.id.eventtime);


            eventTitle.setText(mTitle[position]);
            eventDesp.setText(mDesp[position]);
            eventDate.setText(mdate[position]);
            eventTime.setText(mtime[position]);

            return event;
        }
    }
}

