package com.example.myjavaapps.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myjavaapps.MainActivity;
import com.example.myjavaapps.R;
import com.example.myjavaapps.data.User;
import com.example.myjavaapps.database.DatabaseHelper;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Home extends AppCompatActivity {

    Button edit, reset, collection, custom_list, module_dialog;
    EditText email, username, password, confirm_password;
    RadioButton male, female;
    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        collection = findViewById(R.id.collection);
        edit = findViewById(R.id.edit_btn);
        custom_list = findViewById(R.id.custom_list);
        reset = findViewById(R.id.reset_btn);
        module_dialog = findViewById(R.id.module_dialog);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        username = findViewById(R.id.username);
        confirm_password = findViewById(R.id.confirm_password);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);

        collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, DashBoard.class));
            }
        });
        String uname = sharedPreferences.getString("username", "");
        Toast.makeText(this, "Username: "+uname, Toast.LENGTH_SHORT).show();
        final User user = databaseHelper.getUserData(uname);
        email.setText(user.getEmail());
        username.setText(user.getUsername());
        password.setText(user.getPassword());
        username.setEnabled(false);
        if(user.getGender().equals(male.getText().toString())){
            male.setChecked(true);
        } else if(user.getGender().equals(female.getText().toString())){
            female.setChecked(true);
        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.setEmail(email.getText().toString());
                user.setPassword(password.getText().toString());
                String gender = male.getText().toString();
                if(female.isChecked())
                    gender = female.getText().toString();
                user.setGender(gender);
                databaseHelper.updateuserData(user);
            }
        });

        custom_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, MyCustomList.class));
            }
        });

        module_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                MyLibraryDialog myDialogFragment = new MyLibraryDialog("Clears");
//                myDialogFragment.setCancelable(false);
//                myDialogFragment.show(getSupportFragmentManager(), "myDialog");

                new SweetAlertDialog(view.getContext(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Good job!")
                        .setContentText("You clicked the button!")
                        .show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.about){
            Toast.makeText(this, "About Menu Clicked", Toast.LENGTH_SHORT).show();
            return true;
        }else if(id ==R.id.logout){
            SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();
            startActivity(new Intent(Home.this,
                    MainActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
