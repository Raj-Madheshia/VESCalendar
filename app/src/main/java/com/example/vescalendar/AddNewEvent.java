package com.example.vescalendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddNewEvent extends AppCompatActivity {
    SqliteDatabaseHelper sqliteDatabaseHelper;
    private EditText editText1;
    private EditText editText2;
    private EditText timePicker;
    private Button btn;
    private ImageView img;
    private TextView heading;
    private String date;
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_event);
        editText1 = (EditText) findViewById(R.id.getTitle);
        editText2 = (EditText) findViewById(R.id.getDesp);
        timePicker= (EditText) findViewById(R.id.getTime);
        heading =(TextView) findViewById(R.id.setheader);
        btn = (Button)findViewById(R.id.addnewevent);
        img = (ImageView)findViewById(R.id.backtofragment);
        date = getIntent().getExtras().getString("date");
        heading.setText("Add Event on "+date);




        timePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddNewEvent.this, new TimePickerDialog.OnTimeSetListener() {
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
                        timePicker.setText( s + ":" + m);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        sqliteDatabaseHelper = new SqliteDatabaseHelper(this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String title = editText1.getText().toString();
                String desp = editText2.getText().toString();
                String time = timePicker.getText().toString();


                if(title.matches("") || desp.matches("") || date.matches("") ||time.matches("") ){
                    Toast.makeText(AddNewEvent.this, "All Fields are Compulsory", Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        Date d1 = sdf.parse(presentTime());
                        Date d2 = sdf.parse(time);
                        long elapsed = d2.getTime() - d1.getTime();
                        Date todayDate = Calendar.getInstance().getTime();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        String currentDate = formatter.format(todayDate);

                        Date date1 = format.parse(currentDate);
                        Date date2 = format.parse(date);

                        if(elapsed < 0 ){
                            if(date1.compareTo(date2) <0){
                                boolean inserted = sqliteDatabaseHelper.insertDate(title, desp, date, time);
                                if(!inserted){
                                    Toast.makeText(AddNewEvent.this, "Unable to insert data", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(AddNewEvent.this, "Data Inserted", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(AddNewEvent.this, MainActivity.class);
                                    Bundle b = new Bundle();
                                    b.putString("toOpen","FragmentEvents");
                                    i.putExtras(b);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                }
                            }
                            else {
                                Toast.makeText(AddNewEvent.this, "Previous time not allowed", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            boolean inserted = sqliteDatabaseHelper.insertDate(title, desp, date, time);
                            if(!inserted){
                                Toast.makeText(AddNewEvent.this, "Unable to insert data", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(AddNewEvent.this, "Data Inserted", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(AddNewEvent.this, MainActivity.class);
                                Bundle b = new Bundle();
                                b.putString("toOpen","FragmentEvents");
                                i.putExtras(b);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }
            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    String presentTime(){
        return sdf.format(new Date());
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

}
