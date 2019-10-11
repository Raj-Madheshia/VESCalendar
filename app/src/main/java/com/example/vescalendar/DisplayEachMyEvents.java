package com.example.vescalendar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;


public class DisplayEachMyEvents extends AppCompatActivity {
    ImageView img;
    TextView Title;
    TextView Desp;
    TextView Date;
    TextView Time;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_each_myevent);
        Title = (TextView)findViewById(R.id.event_detail_eventTitle);
        Desp = (TextView)findViewById(R.id.event_detail_eventDesp);
        Date = (TextView)findViewById(R.id.event_detail_eventDate);
        Time = (TextView)findViewById(R.id.event_detail_eventTime);
        Bundle extras = getIntent().getExtras();
        String id = extras.getString("id");
        String title= extras.getString("title");;
        String desp= extras.getString("desp");;
        String date= extras.getString("date");;
        String time= extras.getString("time");;

        Title.setText(title);
        Desp.setText(desp);
        Date.setText(date);
        Time.setText(time);

        img = (ImageView) findViewById(R.id.moveBacktoAllEvents);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
