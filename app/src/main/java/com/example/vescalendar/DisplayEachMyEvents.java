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


public class DisplayEachMyEvents extends AppCompatActivity {
    ImageView img;
    TextView Title;
    TextView Desp;
    TextView Date;
    TextView Time;

    Button update;
    Button delete;
    String id;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_each_myevent);
        final SqliteDatabaseHelper sqliteDatabaseHelper;
        sqliteDatabaseHelper = new SqliteDatabaseHelper(this);

        Title = (TextView)findViewById(R.id.event_detail_eventTitle);
        Desp = (TextView)findViewById(R.id.event_detail_eventDesp);
        Date = (TextView)findViewById(R.id.event_detail_eventDate);
        Time = (TextView)findViewById(R.id.event_detail_eventTime);
        update = (Button)findViewById(R.id.update);
        delete  =(Button) findViewById(R.id.delete);

        Bundle extras = getIntent().getExtras();
        id = extras.getString("id");
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


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer deleteRow =  sqliteDatabaseHelper.deleteEvent(id);
                if(deleteRow <=0){
                    Toast.makeText(DisplayEachMyEvents.this, "Unable to Delete data", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(DisplayEachMyEvents.this, "Data Inserted", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(DisplayEachMyEvents.this, MainActivity.class);
                    i.putExtra("toOpen", "FragmentEvents");
                    startActivity(i);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
