package com.example.myjavaapps.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myjavaapps.R;

public class Signup extends AppCompatActivity {

    Button signup, reset;
    EditText email, password, confirm_password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        signup = findViewById(R.id.signup_btn);
        reset = findViewById(R.id.reset_btn);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirm_password = findViewById(R.id.confirm_password);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_address =  email.getText().toString();
                if(email_address.matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]{2,5}+)\\.([a-zA-Z]{2,5})$")){
                    Toast.makeText(Signup.this, "Valid Email: "+email_address, Toast.LENGTH_SHORT).show();
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
