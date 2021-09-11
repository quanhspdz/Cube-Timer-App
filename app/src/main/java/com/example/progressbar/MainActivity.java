package com.example.progressbar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.view.View;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button btnStart, btnReset;
    TextView txtTienTrinh, txtAvg5, txtAvg12, txtBestTime;
    View appView, appViewLand;
    boolean startIsClicked = false, resetIsClicked = false;
    long time = 0, startTime, oldTime = 0;
    ListView listViewTimeHistory;
    ArrayList<Long> arrayTime;
    ArrayList<String> arrayStrTime;
    TimeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //auto rotate app in landscape mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        //force app to display fullscreen and screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //assignment
        btnStart = findViewById(R.id.btnStart);
        btnReset = findViewById(R.id.btnReset);
        txtTienTrinh = findViewById(R.id.txtTienTrinh);
        appViewLand = findViewById(R.id.layoutConstrantMainLand);
        txtTienTrinh.setText(convertToHours(time));
        listViewTimeHistory = findViewById(R.id.listViewHistory);
        txtAvg5 = findViewById(R.id.txtAvg5);
        txtAvg12 = findViewById(R.id.txtAvg12);
        txtBestTime = findViewById(R.id.txtBestTime);

        arrayTime = new ArrayList<>();
        arrayStrTime = new ArrayList<>();

        startTime = System.nanoTime();

        //set start button action
        Handler handler = new Handler();
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startIsClicked) {
                    startIsClicked = false;
                    oldTime = time;
                    btnStart.setText("Start");
                }
                else {
                    startTime = System.nanoTime();
                    startIsClicked = true;
                    btnStart.setText("Stop");
                }
                runLoop(50);
            }
        });

        //to stop timer when user touch screen
        appViewLand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startIsClicked) {
                    startIsClicked = false;
                    oldTime = time;
                    btnStart.setText("Start");
                }
            }
        });

        //set reset button action
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {       //prevent user from reset the timer when it is running
                if (!startIsClicked) {
                    resetIsClicked = true;
                    if (time != 0) addArrayTime(time);
                    reset();
                }
                else {
                    startIsClicked = false;
                    oldTime = time;
                    btnStart.setText("Start");
                }
            }
        });

        adapter = new TimeAdapter(MainActivity.this, R.layout.each_time_item, arrayTime);
        listViewTimeHistory.setAdapter(adapter);
    }

    //function to save the time history (new time will show first)
    private void addArrayTime(long time) {
        arrayTime.add(0, time);
        adapter.notifyDataSetInvalidated();
    }

    //a function to calculate the time
    private void runLoop(int time_ms) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (startIsClicked) {
                    time = oldTime + System.nanoTime() - startTime;
                    txtTienTrinh.setText(convertToHours(time));
                    if (startIsClicked && !resetIsClicked) runLoop(time_ms);
                }
            }
        }, time_ms);
    }

    //function to reset timer
    private void reset() {
        startTime = System.nanoTime();
        btnStart.setText("Start");
        startIsClicked = false;
        time = 0;
        oldTime = 0;
        txtTienTrinh.setText(convertToHours(time));
        resetIsClicked = false;



        long best = getBest(arrayTime);
        txtBestTime.setText("Best: " + convertToHours(best));
        if (arrayTime.size() >= 5) {
            String strAvg5 = convertToHours((long) averageOfLastN(arrayTime, 5));
            txtAvg5.setText("-Avg5: " + strAvg5);
            if (arrayTime.size() >= 12) {
                String strAvg12 = convertToHours((long) averageOfLastN(arrayTime, 12));
                txtAvg12.setText("Avg12: " + strAvg12);
            }
        }
    }

    //function covert time to easier visible time
    public static String convertToHours(long time) {
        String strTime = "";

        time /= 1000000;
        SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss.SS");
        strTime = (String) timeFormat.format(time);

        return strTime;
    }

    //function calculate the average of n
    private double averageOfLastN(ArrayList<Long> arrayTime, int n) {
        int i;
        double avg = 0;
        for (i = 0; i < n; i++)  {
            avg+= arrayTime.get(i)/n;
        }
        return avg;
    }
    private long getBest(ArrayList<Long> arrayTime) {
        long best = arrayTime.get(0);
        for (int i = 1; i < arrayTime.size(); i++) {
            if (best > arrayTime.get(i))
                best = arrayTime.get(i);
        }
        return best;
    }
}

