package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Gantt Chart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Gantt.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

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