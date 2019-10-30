package com.example.vescalendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DisplayEachCalendar  extends AppCompatActivity {
    ImageView img;
    TextView Title;
    TextView Desp;
    TextView Date;
    TextView Time;

    Button delete;
    String id;
    String desp;
    String date;
    String time;
    String title;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_each_calandar);

        Title = (TextView)findViewById(R.id.event_detail_eventTitle);
        Desp = (TextView)findViewById(R.id.event_detail_eventDesp);
        Date = (TextView)findViewById(R.id.event_detail_eventDate);
        Time = (TextView)findViewById(R.id.event_detail_eventTime);

        delete  =(Button) findViewById(R.id.delete);

        Bundle extras = getIntent().getExtras();
        id = extras.getString("id");
        title= extras.getString("title");;
        desp= extras.getString("desp");;
        date= extras.getString("date");;
        time= extras.getString("time");;

        Title.setText("Title: "+title);
        Desp.setText("Desp: "+desp);
        Date.setText("Date: "+date);
        Time.setText("Time: "+time);

        img = (ImageView) findViewById(R.id.moveBacktoAllEvents);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
