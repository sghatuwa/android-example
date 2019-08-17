package com.example.myjavaapps.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myjavaapps.R;
import com.example.myjavaapps.database.DatabaseHelper;

import java.util.Date;

public class Signup extends AppCompatActivity {

    Button signup, reset;
    EditText email, username, password, confirm_password;
    RadioButton male, female;
    DatabaseHelper databaseHelper = new DatabaseHelper(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        signup = findViewById(R.id.signup_btn);
        reset = findViewById(R.id.reset_btn);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        username = findViewById(R.id.username);
        confirm_password = findViewById(R.id.confirm_password);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_address =  email.getText().toString();
                String uname =  username.getText().toString();
                String pwd =  password.getText().toString();
                String conf_pwd =  confirm_password.getText().toString();
                String gender = male.getText().toString();
                if(female.isChecked()){
                    gender= female.getText().toString();
                }
                if(email_address.matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]{2,5}+)\\.([a-zA-Z]{2,5})$")){
                    if(pwd.equals(conf_pwd)){
                        String date = new Date().toString();
                        boolean status = databaseHelper.insertStudent(uname, pwd, email_address,
                                gender, date);
                        if(status)
                            Toast.makeText(Signup.this, "Inserted Successfully", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(Signup.this, "OOPS!!!", Toast.LENGTH_SHORT).show();
                    } else{
                        Toast.makeText(Signup.this, "Password not Matched", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    email.setError("Invalid Email");
                    Toast.makeText(Signup.this, "Invalid Email: " + email_address, Toast.LENGTH_SHORT).show();
                }
            }
        });

        confirm_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String pwd = password.getText().toString();
                if(pwd.equals(charSequence.toString())){
                    Toast.makeText(Signup.this, "Password Matched", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
