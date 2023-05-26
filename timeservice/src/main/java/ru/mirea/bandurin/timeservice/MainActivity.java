package ru.mirea.bandurin.timeservice;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

import ru.mirea.bandurin.timeservice.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private final String host = "time.nist.gov"; // или time-a.nist.gov
    private final int port = 13;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetTimeTask timeTask = new GetTimeTask();
                timeTask.execute();
            }
        });
    }
    private class GetTimeTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String current = "";
            try {
                Socket socket = new Socket(host, port);
                BufferedReader reader = SocketUtils.getReader(socket);
                reader.readLine(); // игнорируем первую стр
                current = reader.readLine();
                Log.d(TAG,current);
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return current;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String timeResult = "";
            String dataResult = "";
            int index = result.indexOf(' ');
            dataResult = result.substring(index+7,index+9)+"."+result.substring(index+4,index+6)+"."+result.substring(index+1,index+3);
            timeResult = Integer.toString(Integer.parseInt(result.substring(index+10,index+12))+3)+result.substring(index+12,index+18);
            binding.textView.setText(dataResult);
            binding.textView2.setText(timeResult);
            /*super.onPostExecute(result);
            binding.textView.setText(result);*/
        }
    }
}