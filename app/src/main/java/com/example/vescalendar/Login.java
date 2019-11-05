package com.example.vescalendar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutionException;

public class Login extends AppCompatActivity {
    EditText emailid, password;
    TextView error;
    Button login;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        final SharedPreferences sharedPref =  getSharedPreferences("Login", 0);
        final SharedPreferences.Editor editor = sharedPref.edit();

        String id = sharedPref.getString("user_id",null);

        if(id!=null){
            Log.d("Testing Login page",id);
            Intent i = new Intent(Login.this, SplashActivity.class);
            startActivity(i);
        }


        emailid = (EditText) findViewById(R.id.login_emailid);
        password =(EditText) findViewById(R.id.login_pass);
        error = (TextView) findViewById(R.id.errortext);
        login =(Button) findViewById(R.id.logIntoAccount);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email =  emailid.getText().toString();
                String pass = password.getText().toString();

                Background bg = new Background();
                String result=null;
                try {
                    result = bg.execute(email, pass).get();
                    Log.d("Is this working",result);
                    if(result.equals("Login Failed")){

                        error.setVisibility(View.VISIBLE);
                    }
                    else if(result.equals("Not Connected")){
                        Toast.makeText(Login.this, "Server is unreachable at this moment", Toast.LENGTH_SHORT)
                                .show();
                    }
                    else{
                        String user_id=result;
                        editor.putString("user_id",user_id);
                        editor.commit();
                        Intent i = new Intent(Login.this, SplashActivity.class);
                        startActivity(i);
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

    }
}
