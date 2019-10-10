package com.example.vescalendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddNewEvent extends AppCompatActivity {

    SqliteDatabaseHelper sqliteDatabaseHelper;

    private EditText editText1;
    private EditText editText2;
    private EditText datePicker;
    private EditText timePicker;
    private Button btn;
    private ImageView img;
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_event);
        editText1 = (EditText) findViewById(R.id.getTitle);
        editText2 = (EditText) findViewById(R.id.getDesp);
        datePicker = (EditText) findViewById(R.id.getDate);
        timePicker= (EditText) findViewById(R.id.getTime);
        btn = (Button)findViewById(R.id.addnewevent);
        img = (ImageView)findViewById(R.id.backtofragment);

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddNewEvent.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

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
                        timePicker.setText( selectedHour + ":" + selectedMinute);
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
                String title = editText1.getText().toString();
                String desp = editText2.getText().toString();
                String date = datePicker.getText().toString();
                String time = timePicker.getText().toString();
                if(title.matches("") || desp.matches("") || date.matches("") ||time.matches("") ){
                    Toast.makeText(AddNewEvent.this, "All Fields are Compulsory", Toast.LENGTH_SHORT).show();
                }
                else{
                    boolean inserted = sqliteDatabaseHelper.insertDate(title, desp, date, time);
                    if(!inserted){
                        Toast.makeText(AddNewEvent.this, "Unable to insert data", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(AddNewEvent.this, "Data Inserted", Toast.LENGTH_SHORT).show();
                        onBackPressed();
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
    public void onBackPressed() {
        super.onBackPressed();
    }
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };
    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        datePicker.setText(sdf.format(myCalendar.getTime()));
    }

}
