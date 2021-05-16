package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Adapter.GanttTableAdapterEtape;
import com.example.myapplication.Adapter.GanttTableAdapterTasks;
import com.example.myapplication.Model.GanttData;
import com.example.myapplication.Model.GanttDataEtape;
import com.example.myapplication.tablefixheaders.TableFixHeaders;
import com.example.myapplication.tablefixheaders.adapters.TableAdapter;


public class Gantt extends AppCompatActivity {

    private static final String TAG = Gantt.class.getSimpleName();
    private GanttData ganttData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gantt);

        ganttData = GanttData.getGanttData();
        if (ganttData != null)
            setGanttTable();
        else
            Log.e(TAG, "Gantt Data Null, Please Set Gantt Data.");

    }



    private void setGanttTable() {
        TableFixHeaders tableFixHeaders = (TableFixHeaders) findViewById(R.id.table);
        GanttTableAdapterTasks baseTableAdapter = new GanttTableAdapterTasks(Gantt.this, ganttData);
        tableFixHeaders.setAdapter((TableAdapter) baseTableAdapter);
        tableFixHeaders.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
    }