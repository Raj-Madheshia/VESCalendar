package com.example.vescalendar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
    EditText emailid, password;
    TextView error;
    Button login;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        final SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();

        String id = sharedPref.getString("user_id",null);
        if(id!=null){
            Log.d("Testing Login page","id not null");
            Intent i = new Intent(Login.this, MainActivity.class);
            startActivity(i);
        }


        emailid = (EditText) findViewById(R.id.login_emailid);
        password =(EditText) findViewById(R.id.login_pass);
        error = (TextView) findViewById(R.id.errortext);
        login =(Button) findViewById(R.id.logIntoAccount);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_id="1";
                editor.putString("user_id",user_id);
                editor.commit();
                Intent i = new Intent(Login.this, MainActivity.class);
                startActivity(i);
            }
        });

    }
}
