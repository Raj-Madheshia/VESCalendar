package com.example.vescalendar;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FragmentEvents extends Fragment {
    CompactCalendarView compactCalendarView;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
    ListView listView;
    TextView yearmonth;
    private ImageView left, right;
    FloatingActionButton floatingActionButton;

    SqliteDatabaseHelper sqliteDatabaseHelper;
    private String clickeddate;
    Date todayDate = Calendar.getInstance().getTime();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        getActivity().setTitle("My Events");
        final View view = inflater.inflate(R.layout.fragment_events, container, false);

        compactCalendarView = (CompactCalendarView) view.findViewById(R.id.compactcalendar_view);
        listView = view.findViewById(R.id.listview);
        yearmonth = (TextView)view.findViewById(R.id.month);

        compactCalendarView.setUseThreeLetterAbbreviation(true);
        Event ev;
        sqliteDatabaseHelper = new SqliteDatabaseHelper(view.getContext());
        clickeddate= formatter.format(todayDate);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
        Date gmt = null;



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
                String date = DATE[i];
                String title = TITLE[i];
                String time = TIME[i];
                String id = ID[i];

                try {
                    gmt = formatter.parse(date +'T'+time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long millisecondsSinceEpoch0 = gmt.getTime();
                //String asString = formatter.format(gmt);

                ev = new Event(Color.GREEN, millisecondsSinceEpoch0, id);
                compactCalendarView.addEvent(ev);
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

        left = (ImageView) view.findViewById(R.id.left);
        right = (ImageView) view.findViewById(R.id.right);
//        left.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ompactCalendarView.showNextMonth();
//            }
//        });
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            public void onDayClick(Date dateClicked) {
                List<Event> events = compactCalendarView.getEvents(dateClicked);

                long full = dateClicked.getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
                String asString = formatter.format(full);
                clickeddate = asString.substring(0,10);
                String ID[];
                String TITLE[];
                String TIME[];
                String DATE[];
                String year;
                String month;
                String date;
                String testdate;
                int i =0;
                int sizeoflist =0;
                if(events.size()>0) {
                    for (Event item : events) {
                        String id = (String) item.getData();
                        Cursor res = sqliteDatabaseHelper.getValById(id);
                        sizeoflist+=res.getCount();
                    }
                }
                ID= new String[sizeoflist];
                TITLE = new String[sizeoflist];
                DATE = new String[sizeoflist];
                TIME = new String[sizeoflist];

                for (Event item : events) {
                    String id = (String) item.getData();
                    Cursor res = sqliteDatabaseHelper.getValById(id);

                    String months[] ={"Jan","Feb","Mar","Apr", "May", "Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

                    if (res.getCount() != 0) {
                        while (res.moveToNext()) {
                            ID[i] = res.getString(0);
                            TITLE[i] = res.getString(1);
                            testdate= res.getString(3);
                            year = testdate.substring(0,4);
                            month= testdate.substring(5,7);
                            date = testdate.substring(8,10);
                            int monthint = Integer.parseInt(month) -1;
                            DATE[i] = months[monthint]+" "+date+", "+year;
                            TIME[i] = res.getString(4);
                            i += 1;
                        }
                    } else {
                        int size = 0;
                        ID = new String[size];
                        TITLE = new String[size];
                        DATE = new String[size];
                        TIME = new String[size];
                    }
                }
                System.out.println(DATE);
                MyAdapter adapter = new MyAdapter(view.getContext(), ID, TITLE, TIME, DATE);
                listView.setAdapter(adapter);


            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                long full = firstDayOfNewMonth.getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
                String asString = formatter.format(full);
                String Year= asString.substring(0,4);
                String month = asString.substring(5,7);
                String months[] ={"Jan","Feb","Mar","Apr", "May", "Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
                int monthint = Integer.parseInt(month) -1;
                yearmonth.setText(months[monthint]+", "+Year);
                Log.d("Month", "Month was scrolled to: " + firstDayOfNewMonth);
            }
        });

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
                Intent intent = new Intent(getActivity(), AddNewEvent.class);
                intent.putExtra("date", clickeddate);
                startActivity(intent);
            }
        });

        return view;
    }
    class MyAdapter extends ArrayAdapter<String> {
        Context c;
        String mTitle[];
        //String mDesp[];
        String mtime[];
        String mdate[];
        String Id[];
        int i =0;

        MyAdapter(Context c, String Id[], String title[], String time[], String date[]) {
            super(c, R.layout.eventlayout, R.id.eventTitle, Id);
            this.c = c;
            this.mTitle = title;
            //this.mDesp = descp;
            this.mdate = date;
            this.mtime = time;
            this.Id = Id;

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
            eventDate.setText(mdate[position]);
            eventTime.setText(mtime[position]);

            return event;
        }
    }

}




