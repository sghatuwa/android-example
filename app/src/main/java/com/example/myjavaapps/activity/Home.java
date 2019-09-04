package com.example.myjavaapps.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myjavaapps.MainActivity;
import com.example.myjavaapps.R;
import com.example.myjavaapps.data.User;
import com.example.myjavaapps.database.DatabaseHelper;
import com.example.myjavaapps.utils.DownloadFileFromURL;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Home extends AppCompatActivity {

    Button edit, reset, collection, custom_list, module_dialog, download_file, play_songs, storetosd;
    EditText email, username, password, confirm_password;
    RadioButton male, female;
    VideoView vid;

    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    SharedPreferences sharedPreferences;
    // Progress Dialog
    private ProgressDialog pDialog;

    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;

    // File url to download
    private static String file_url = "https://api.androidhive.info/progressdialog/hive.jpg";
    MediaPlayer mp;
    private static final int PERMISSION_REQUEST_CODE = 200;

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
        download_file = findViewById(R.id.download_file);
        play_songs = findViewById(R.id.play_songs);
        storetosd = findViewById(R.id.storetosd);
        password = findViewById(R.id.password);
        username = findViewById(R.id.username);
        confirm_password = findViewById(R.id.confirm_password);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        vid = findViewById(R.id.videoView);
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

        download_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DownloadFile(Home.this).execute(file_url);
            }
        });
        play_songs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mp.isPlaying()) {
                    mp.start();
                    play_songs.setText("Stop Songs");
                } else{
                    mp.stop();
                    mp=MediaPlayer.create(getApplicationContext(), R.raw.nepathya);
                    play_songs.setText("Play Songs");
                }
            }
        });
        mp=MediaPlayer.create(this, R.raw.nepathya);

        if(!checkPermission()){
            requestPermission();
        }

        storetosd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkPermission())
                    generateNoteOnSD(getApplicationContext(), "file.txt", "This is test");
                else
                    requestPermission();
            }
        });
    }

    public void generateNoteOnSD(Context context, String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            System.out.println("> "+root.exists());
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            System.out.println("==== "+gpxfile.exists());
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (storageAccepted)
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    else {
                        Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need to allow access the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                        }
                        }

                    }
                    if(checkPermission())
                        generateNoteOnSD(getApplicationContext(), "file.txt", "This is testttt");
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(Home.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
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

    class DownloadFile extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        private boolean isDownloaded;
        private Context context;
        public DownloadFile(Context context){
            this.context = context;
        }
        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            this.progressDialog = new ProgressDialog(context);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();


                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                //Extract file name from URL
                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length());

                //Append timestamp to file name
                fileName = fileName;

                //External directory path to save file
                folder = Environment.getExternalStorageDirectory() + File.separator + "tttt";
                System.out.println("folder = " + folder);
                //Create androiddeft folder if it does not exist
                File directory = new File(Environment.getExternalStorageDirectory(), "tttt");
                System.out.println("directory.exists() = " + directory.exists());
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                System.out.println("folder = " + folder);
                System.out.println("fileName = " + fileName);
                // Output stream to write file
                FileOutputStream output = new FileOutputStream(new File(directory, fileName));

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    Log.d("dssfsdf", "Progress: " + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
                return "Downloaded at: " + folder + fileName;

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Error: ", e.getMessage());
            }

            return "Something went wrong";
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String message) {
            // dismiss the dialog after the file was downloaded
            this.progressDialog.dismiss();

            // Display File path after downloading
            Toast.makeText(context,
                    message, Toast.LENGTH_LONG).show();
        }
    }

    public void playVideo(View View){
        MediaController m = new MediaController(this);
        vid.setMediaController(m);
        String path = "android.resource://com.example.myjavaapps/"+R.raw.video;
        Uri u = Uri.parse(path);
        vid.setVideoURI(u);
        vid.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setVolume(50, 0);
            }
        });
        vid.start();
    }
}
