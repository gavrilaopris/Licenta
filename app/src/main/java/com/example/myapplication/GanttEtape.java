package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.myapplication.Adapter.GanttTableAdapterEtape;
import com.example.myapplication.Model.GanttDataEtape;
import com.example.myapplication.tablefixheaders.TableFixHeaders;
import com.example.myapplication.tablefixheaders.adapters.TableAdapter;

public class GanttEtape extends AppCompatActivity {

    private static final String TAG = Gantt.class.getSimpleName();
    private GanttDataEtape ganttDataEtape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gantt_etape);

        ganttDataEtape = GanttDataEtape.getGanttDataEtape();
        if (ganttDataEtape != null)
            setGanttEtapeTable();
        else
            Log.e(TAG, "Gantt Data Null, Please Set Gantt Data.");
    }

    private void setGanttEtapeTable() {
        TableFixHeaders tableFixHeaders = (TableFixHeaders) findViewById(R.id.table);
        GanttTableAdapterEtape baseTableAdapterE = new GanttTableAdapterEtape(GanttEtape.this, ganttDataEtape);
        tableFixHeaders.setAdapter((TableAdapter) baseTableAdapterE);
        tableFixHeaders.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}