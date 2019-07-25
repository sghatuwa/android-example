package com.example.myjavaapps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myjavaapps.activity.DashBoard;
import com.example.myjavaapps.activity.Signup;

public class MainActivity extends AppCompatActivity {

    TextView forgotpwd, signup, err_msg;
    EditText uname, password;
    Button login_btn, reset_btn;
    final String loggedIn = "loggedIn";
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPrefs();

        if(sharedPreferences.getBoolean(loggedIn, false)){
            startDashBoard();
        }
        setContentView(R.layout.login_activity);
        initUI();
        buttonClickListener();
    }

    private void initPrefs() {
//        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        sharedPreferences = getSharedPreferences("user",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    private void buttonClickListener() {
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = uname.getText().toString();
                String pwd = password.getText().toString();

                if(username.equals("admin") && pwd.equals("admin")){
                    Toast.makeText(MainActivity.this, "Congratulation", Toast.LENGTH_LONG).show();
                    editor.putBoolean(loggedIn, true);
                    editor.putString("username", username);
                    editor.apply();
                    startDashBoard();
                } else{
                    err_msg.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this, "Opps!!! Sorry", Toast.LENGTH_LONG).show();
                }
            }
        });

        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uname.setText("");
                password.setText("");
                err_msg.setVisibility(View.GONE);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Signup.class));
            }
        });
    }

    private void startDashBoard() {
        Intent dashBoardIntent = new Intent(MainActivity.this, DashBoard.class);
        startActivity(dashBoardIntent);
        finish();
    }

    private void initUI() {
        uname = findViewById(R.id.uname);
        password = findViewById(R.id.password);
        err_msg = findViewById(R.id.err_msg);
        login_btn = findViewById(R.id.login_btn);
        reset_btn = findViewById(R.id.reset_btn);
        forgotpwd = findViewById(R.id.forgotpwd);
        signup = findViewById(R.id.signup);
    }
}
