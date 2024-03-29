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
    String desp;
    String date;
    String time;

    String title;
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


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Integer deleteRow =  sqliteDatabaseHelper.updateData(id);
                Bundle b = new Bundle();
                Intent i = new Intent(DisplayEachMyEvents.this, UpdateEvent.class);

                b.putString("id", id);
                b.putString("title", title);
                b.putString("desp", desp);
                b.putString("date", date);
                b.putString("time", time);
                i.putExtras(b);

                startActivity(i);
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
                    Toast.makeText(DisplayEachMyEvents.this, "Data Deleted", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(DisplayEachMyEvents.this, MainActivity.class);
                    Bundle b = new Bundle();
                    b.putString("toOpen","FragmentEvents");
                    i.putExtras(b);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
