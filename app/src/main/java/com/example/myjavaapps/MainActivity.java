package com.example.myjavaapps;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myjavaapps.activity.Home;
import com.example.myjavaapps.activity.Signup;
import com.example.myjavaapps.database.DatabaseHelper;
import com.example.myjavaapps.receiver.ConnectionReceiver;
import com.example.myjavaapps.utils.Utils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    TextView forgotpwd, signup, err_msg;
    EditText uname, password;
    Button login_btn, reset_btn;
    final String loggedIn = "loggedIn";
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    ConnectionReceiver receiver;
    IntentFilter intentFilter;

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
        receiver = new ConnectionReceiver();
        intentFilter = new IntentFilter("com.example.myjavaapps.SOME_ACTION");
        registerReceiver(receiver, intentFilter);

        Intent intent = new Intent("com.example.myjavaapps.SOME_ACTION");
        sendBroadcast(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
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

//                new ValidateLogin(view.getContext(), username, pwd).execute();
                if(databaseHelper.validateUser(username, pwd)){
                    Toast.makeText(MainActivity.this, "Congratulation", Toast.LENGTH_LONG).show();
                    editor.putBoolean(loggedIn, true);
                    editor.putString("username", username);
                    editor.apply();
                    startDashBoard();
                } else {
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
        Intent dashBoardIntent = new Intent(MainActivity.this, Home.class);
        startActivity(dashBoardIntent);
        finish();
        Utils.getValue();
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

    class ValidateLogin extends AsyncTask<Void, Void, Boolean>{

        private ProgressDialog progressDialog;

        String uname, pwd;
        Context context;
        public ValidateLogin(Context context, String uname, String pwd){
            this.context = context;
            this.uname = uname;
            this.pwd = pwd;
        }

        @Override
        protected void onPreExecute() {
            this.progressDialog = new ProgressDialog(context);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            this.progressDialog.setMessage("Logging");
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                JSONObject postDataParams = new JSONObject()
                        .put("email", "eve.holt@reqres.in")
                        .put("password", "cityslicka");
                URL url = new URL("https://reqres.in/api/login");

                HttpsURLConnection  conn = (HttpsURLConnection ) url.openConnection();
                conn.setReadTimeout(20000);
                conn.setConnectTimeout(20000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);

                // Write Request to output stream to server.
                OutputStream os = conn.getOutputStream();
                os.write(postDataParams.toString().getBytes("UTF-8"));
                os.flush();
                os.close();
                conn.connect();

                int responseCode=conn.getResponseCode();
                System.out.println("responseCode = " + responseCode);
                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader( new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line="";
                    while((line = in.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    System.out.println("sb: "+sb);
                    JSONObject js = new JSONObject(sb.toString());
                    System.out.println("js.get(toke); = " + js.get("token"));
                    in.close();
                    return true;
                }
                return false;

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Error: ", e.getMessage());
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            Toast.makeText(context, "Status: "+aBoolean, Toast.LENGTH_SHORT).show();
            this.progressDialog.dismiss();
            if(aBoolean){
                    Toast.makeText(MainActivity.this, "Congratulation", Toast.LENGTH_LONG).show();
                    editor.putBoolean(loggedIn, true);
                    editor.putString("username", this.uname);
                    editor.apply();
                    startDashBoard();
                } else {
                    err_msg.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this, "Opps!!! Sorry", Toast.LENGTH_LONG).show();
                }
        }
    }
}
