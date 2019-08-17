package com.example.myjavaapps.activity;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.NotificationCompat;

import com.example.myjavaapps.MainActivity;
import com.example.myjavaapps.R;
import com.example.myjavaapps.adapter.SpinnerAdapter;
import com.example.myjavaapps.fragment.MyDialogFragment;
import com.example.myjavaapps.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashBoard extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    Button showDate, showTime, change_image, context_menu, popupmenu, check_switch;
    TextView setDate, setTime;
    ImageView image;
    WebView webview;
    Spinner spinner, dynamicSpinner;
    ToggleButton toggleButton;
    Switch aSwitch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        initUI();
        Utils.getValue();
        showDateTime();
        changeImage();

//        List<String> spinnerList = new ArrayList<>();
//        spinnerList.add("Nepal");
//        spinnerList.add("India");
//        spinnerList.add("China");
//        spinnerList.add("USA");
//        OR
//        spinnerList = Arrays.asList(getResources().getStringArray(R.array.country_spinner));
//        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, spinnerList);
//        dynamicSpinner.setAdapter(spinnerAdapter);
        String[] countryNames = {"Nepal", "India", "China", "Italy"};
        int[] flags = {R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(this, flags, countryNames);
        dynamicSpinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(DashBoard.this, ":: "+adapterView.getItemAtPosition(i), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        initWebView();

        popupmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(DashBoard.this, view);
                popupMenu.inflate(R.menu.dashboard_popupmenu);
                popupMenu.setOnMenuItemClickListener(DashBoard.this);
                popupMenu.show();
            }
        });
    }

    private void initWebView() {
        //        webview.loadUrl("https://www.google.com/");
//
//        String data = "<html><body><h1>Hello, Good Morning</h1></body></html>";
//        webview.loadData(data, "text/html", "UTF-8");

        webview.loadUrl("file:///android_asset/dashboard.html");
        webview.invokeZoomPicker();
    }

    /*
    This method changed image on button click
     */
    private void changeImage(){
        change_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image.setImageResource(R.drawable.test);
            }
        });
    }

    private void showDateTime(){
        showDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(DashBoard.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                setDate.setText(i +"-"+i1+"-"+i2);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        showTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int min = calendar.get(Calendar.MINUTE);


                TimePickerDialog timePickerDialog = new TimePickerDialog(DashBoard.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                setTime.setText(i+":"+i1);
                            }
                        }, hour, min, true);
                timePickerDialog.show();
            }
        });
    }

    private void initUI(){
        showDate = findViewById(R.id.showDate);
        showTime = findViewById(R.id.showTime);
        setDate = findViewById(R.id.date_view);
        setTime = findViewById(R.id.time_view);
        change_image = findViewById(R.id.change_image);
        image = findViewById(R.id.image);
        context_menu = findViewById(R.id.context_menu);
        webview = findViewById(R.id.webview);
        spinner = findViewById(R.id.spinner);
        dynamicSpinner = findViewById(R.id.dynamicSpinner);
        popupmenu = findViewById(R.id.popupmenu);
        check_switch = findViewById(R.id.check_switch);
        toggleButton = findViewById(R.id.toggle_btn);
        aSwitch = findViewById(R.id.fine);
        registerForContextMenu(context_menu);
        registerForContextMenu(change_image);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        if(v == context_menu) {
            inflater.inflate(R.menu.dashboard_context, menu);
            menu.setHeaderTitle("Select Action");
        } else if(v == change_image){
            inflater.inflate(R.menu.dashboard_menu, menu);
            menu.setHeaderTitle("Action");
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_edit){
            Toast.makeText(this, "Edit Clicked", Toast.LENGTH_SHORT).show();
        } else if(item.getItemId() == R.id.action_delete){
            Toast.makeText(this, "Delete Clicked", Toast.LENGTH_SHORT).show();
        }
        return true;
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
            startActivity(new Intent(DashBoard.this,
                    MainActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to Exit?")
                .setCancelable(false)
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        })
        .setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle("Confirmation");
        alertDialog.setIcon(R.drawable.test);
        alertDialog.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if(item.getItemId()  == R.id.upload){
            return true;
        } else if(item.getItemId() == R.id.viewDialog){
        Toast.makeText(this, "Clicked: "+item.getTitle(), Toast.LENGTH_SHORT).show();
            MyDialogFragment myDialogFragment = new MyDialogFragment();
            myDialogFragment.setCancelable(false);
            myDialogFragment.show(getSupportFragmentManager(), "myDialog");
        } else if(item.getItemId() == R.id.view_notification){
            NotificationManager notifManager = null;

            final int NOTIFY_ID = 0; // ID of notification
            String id = "asfdkhasdjhfjsdfh";
            String title = "Title";
            Intent intent;
            PendingIntent pendingIntent;
            NotificationCompat.Builder builder;
            if (notifManager == null) {
                notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = notifManager.getNotificationChannel(id);
                if (mChannel == null) {
                    mChannel = new NotificationChannel(id, title, importance);
                    mChannel.enableVibration(true);
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    notifManager.createNotificationChannel(mChannel);
                }
                builder = new NotificationCompat.Builder(this, id);
                intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
                builder.setContentTitle("Title")                            // required
                        .setSmallIcon(android.R.drawable.ic_popup_reminder)   // required
                        .setContentText("Details Message Here") // required
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            }
            else {
                builder = new NotificationCompat.Builder(this, id);
                intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
                builder.setContentTitle("Title")                            // required
                        .setSmallIcon(android.R.drawable.ic_popup_reminder)   // required
                        .setContentText("askjhfkashdf") // required
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                        .setPriority(Notification.PRIORITY_HIGH);
            }
            Notification notification = builder.build();
            notifManager.notify(NOTIFY_ID, notification);
        }
        return false;
    }

    public void checkSwitchToggle(View view){
        boolean toggle = toggleButton.isChecked();
        boolean switchh = aSwitch.isChecked();
        Toast.makeText(this, "Toggle: "+toggle +" >> "+switchh  , Toast.LENGTH_SHORT).show();
    }
}
