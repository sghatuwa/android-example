package com.example.myjavaapps.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myjavaapps.R;
import com.example.myjavaapps.adapter.MyCustomListAdapter;
import com.example.myjavaapps.data.User;
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
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = (User) adapterView.getAdapter().getItem(i);
                long user_id = (long) adapterView.getAdapter().getItemId(i);
                Toast.makeText(MyCustomList.this, user_id+ " Clicked: "+user.getUsername(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
