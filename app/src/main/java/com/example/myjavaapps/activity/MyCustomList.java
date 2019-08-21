package com.example.myjavaapps.activity;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myjavaapps.R;
import com.example.myjavaapps.adapter.MyCustomListAdapter;
import com.example.myjavaapps.database.DatabaseHelper;

public class MyCustomList extends AppCompatActivity {

    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    ListView list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_custom_list);
        list = findViewById(R.id.list);

        MyCustomListAdapter customListAdapter = new MyCustomListAdapter(this, databaseHelper.getAllUserData());
        list.setAdapter(customListAdapter);
    }
}
