package com.example.vescalendar;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;


public class UpdateEvent extends AppCompatActivity {
    String id;
    String title;
    String desp;
    String date;
    String time;

    ImageView img;
    EditText Title;
    EditText Desp;
    EditText timePicker;
    Button update;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_data);
        final SqliteDatabaseHelper sqliteDatabaseHelper;
        sqliteDatabaseHelper = new SqliteDatabaseHelper(this);

        Bundle extras = getIntent().getExtras();
        id = extras.getString("id").toString();
        title = extras.getString("title").toString();
        desp = extras.getString("desp").toString();
        date = extras.getString("date").toString();
        time = extras.getString("time").toString();

        Title = (EditText)findViewById(R.id.updateTitle);
        Desp = (EditText) findViewById(R.id.updateDesp);
        timePicker =(EditText) findViewById(R.id.updateTime);
        img = (ImageView)findViewById(R.id.backtodisplayeach);
        update =(Button) findViewById(R.id.updateEvent) ;
        timePicker.setText(time);
        Title.setText(title);
        Desp.setText(desp);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        timePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(UpdateEvent.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker tp, int selectedHour, int selectedMinute) {
                        String s,m;
                        if (selectedHour<10){
                            s = 0+ String.valueOf(selectedHour);
                        }
                        else{
                            s = String.valueOf(selectedHour);
                        }
                        if (selectedMinute<10){
                            m = 0+ String.valueOf(selectedMinute);
                        }
                        else{
                            m = String.valueOf(selectedMinute);
                        }
                        timePicker.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = Title.getText().toString();
                String desp = Desp.getText().toString();
                String time = timePicker.getText().toString();

                if(title.matches("") || desp.matches("") || date.matches("") ||time.matches("") ){
                    Toast.makeText(UpdateEvent.this, "All Fields are Compulsory", Toast.LENGTH_SHORT).show();
                }
                else{
                    boolean inserted = sqliteDatabaseHelper.updateData(id,title, desp, date, time);
                    if(!inserted){
                        Toast.makeText(UpdateEvent.this, "Unable to insert data", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(UpdateEvent.this, "Data Updated", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(UpdateEvent.this, MainActivity.class);
                        Bundle b = new Bundle();
                        b.putString("toOpen","FragmentEvents");
                        i.putExtras(b);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);

                    }
                }

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
