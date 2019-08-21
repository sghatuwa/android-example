package com.example.myjavaapps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myjavaapps.R;
import com.example.myjavaapps.data.User;

import java.util.List;

public class MyCustomListAdapter extends BaseAdapter {

    List<User> users;
    Context context;

    public MyCustomListAdapter(Context context, List<User> users){
        this.context = context;
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.custom_user_list_design, viewGroup, false);

        ImageView logo = view.findViewById(R.id.logo);
        TextView username = view.findViewById(R.id.username);
        TextView email = view.findViewById(R.id.email);
        username.setText(users.get(i).getUsername());
        email.setText(users.get(i).getEmail());
        if(i%2 == 0)
            logo.setVisibility(View.GONE);
        return view;
    }
}
