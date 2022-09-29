package com.example.assignment8;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onButtonClick(View view) {
        String s = "https://api.covidtracking.com/v1/states/daily.json";
        EditText et = findViewById(R.id.editTextTextPersonName);
        s += et.getText().toString();
        new NetTask().execute(s);
    }
    class NetTask extends AsyncTask<String, Void, String> {

        @Override
        public String doInBackground(String... strings) {
            String address = strings[0];
            try {
                URL url = new URL(address);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                if (inputStream == null) return null;
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                reader.close();
                return buffer.toString();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        public void onPostExecute(String result) {
            try {
                JSONObject jo = new JSONObject(result);
                JSONArray ja = jo.getJSONArray("data");
                JSONObject jo1 = ja.getJSONObject(0);
                double positive = jo.getDouble("positive");
                TextView tvPos = findViewById(R.id.tvPos);
                tvPos.setText("" +positive);
                JSONObject joDeath = jo.getJSONObject("death");
                double death = joDeath.getDouble("death");
                TextView tvDeath = findViewById(R.id.tvDeath);
                tvDeath.setText(""+death);
                JSONObject joHosp = jo.getJSONObject("hospitalized");
                double hospitalised = joHosp.getDouble("hospitalizedCurrently");
                TextView tvHosp = findViewById(R.id.tvHosp);
                tvHosp.setText("" + hospitalised);
                double dateChecked = jo.getDouble("dateChecked");
                TextView tvDC = findViewById(R.id.tvDate);
                tvDC.setText(""+ dateChecked);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}