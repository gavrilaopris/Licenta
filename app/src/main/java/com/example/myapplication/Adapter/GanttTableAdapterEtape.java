package com.example.myapplication.Adapter;

import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Model.Etapa;
import com.example.myapplication.Model.GanttData;
import com.example.myapplication.Model.GanttDataEtape;
import com.example.myapplication.Model.Milestone;
import com.example.myapplication.Model.Project;
import com.example.myapplication.Model.Task;
import com.example.myapplication.R;
import com.example.myapplication.tablefixheaders.adapters.BaseTableAdapter;
import com.example.myapplication.utils.AppConstant;
import com.example.myapplication.view.BarView;
import com.example.myapplication.view.BarViewEtape;
import com.example.myapplication.view.CalendarView;
import com.example.myapplication.view.ScaleView;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;

import java.util.HashMap;
import java.util.List;

public class GanttTableAdapterEtape extends BaseTableAdapter {
    private static final String TAG = GanttTableAdapterEtape.class.getSimpleName();

    private Project project;
    private float scale;
    private AppCompatActivity compatActivity;

    private DateTime minDate;
    private DateTime maxDate;
    private int nx = 0;
    private int dx = 6;

    private List<Etapa> etape = null;
    private List<Milestone> milestones = null;
    private int ny = 0;
    private int dy = 30;

    public GanttTableAdapterEtape(AppCompatActivity compatActivity, GanttDataEtape ganttDataEtape) {
        this.compatActivity = compatActivity;
        scale = compatActivity.getResources().getDisplayMetrics().density;
        dx = (int) (dx * scale);
        dy = (int) (dy * scale);

        this.project = ganttDataEtape.getproject();
        this.etape = ganttDataEtape.getEtapa();
        this.milestones = ganttDataEtape.getMilestones();

        int taskCnt = etape.size();
        ny = taskCnt * 2;

        // Determining the date to display
        DateTime etapaStartDate = DateTime.parse(project.getStartDate(), DateTimeFormat.forPattern(AppConstant.DATE_FORMAT_yyyy_MM_dd));
        int s = etapaStartDate.getDayOfWeek() - 1;
        if (s == 0) {
            minDate = etapaStartDate;
        } else {
            minDate = etapaStartDate.minusDays(s);
            if (minDate.getDayOfMonth() > 23) {
                minDate = minDate.minusWeeks(1);
            }
        }

        DateTime etapaEndDate = DateTime.parse(project.getEndDate(), DateTimeFormat.forPattern(AppConstant.DATE_FORMAT_yyyy_MM_dd));
        int e = 7 - (etapaEndDate.getDayOfWeek() + 1);
        if (e == 0) {
            maxDate = etapaEndDate;
        } else {
            maxDate = etapaEndDate.plusDays(e);
            if (maxDate.getDayOfMonth() < 5) {
                maxDate = maxDate.plusWeeks(1);
            }
        }

//        Find the number of dates (nx)
        Duration d = new Duration(minDate, maxDate);
        nx = (int) d.getStandardDays() + 1;
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public View getView(int row, int column, View convertView, ViewGroup parent) {
        final View view;
        switch (getItemViewType(row, column)) {
            case 0:
                view = getProjectView(convertView);
                break;
            case 1:
                view = getCalendarView(convertView);
                break;
            case 2:
                view = getListView(convertView);
                break;
            case 3:
                view = getGanttView(convertView);
                break;
            default:
                throw new RuntimeException("wtf?");
        }
        return view;
    }

    private View getProjectView(View convertView) {
        if (convertView == null) {
            convertView = LayoutInflater.from(compatActivity).inflate(R.layout.item_table_pj, null);
        }
        ((TextView) convertView.findViewById(R.id.project_name)).setText(R.string.text_gantt_task_title);
        ((TextView) convertView.findViewById(R.id.project_name)).setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        return convertView;
    }

    private View getCalendarView(View convertView) {
        if (convertView == null) {
            convertView = LayoutInflater.from(compatActivity).inflate(R.layout.item_table_cal, null);
        }
        CalendarView cv =  convertView.findViewById(R.id.calendar);
        cv.setRange(minDate, maxDate);
        cv.setXAxis(nx, dx);
        cv.setMilestones(milestones);


        return convertView;
    }

    private View getListView(View convertView) {
        ViewHolder holder;
        if (convertView == null || convertView.getTag() == null) {
            convertView = LayoutInflater.from(compatActivity).inflate(R.layout.item_table_list, null);

            LinearLayout list = (LinearLayout) convertView.findViewById(R.id.group_list);

            holder = new ViewHolder();
            holder.addView(list);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Display list part
        LinearLayout list = (LinearLayout) holder.getView(R.id.group_list);
        list.removeAllViews();

        for (int i = 0; i < etape.size(); i++) {
            Etapa etapa = etape.get(i);

            TextView tvTask = (TextView) LayoutInflater.from(compatActivity).inflate(R.layout.division_group, null);
            tvTask.setText(etapa.getTitlu());
            tvTask.setHeight(dy);
//            float sp = 60f / compatActivity.getResources().getDisplayMetrics().scaledDensity;
//            Log.e(TAG,"sp: "+sp);
//            tvTask.setTextSize(sp);
            list.addView(tvTask);

            TextView tvTaskBlank = (TextView) LayoutInflater.from(compatActivity).inflate(R.layout.division_group, null);
            tvTaskBlank.setText(" ");
            tvTaskBlank.setHeight(dy);
            list.addView(tvTaskBlank);
        }
        return convertView;
    }

    private View getGanttView(View convertView) {
        ViewHolder holder;
        if (convertView == null || convertView.getTag() == null) {
            convertView = LayoutInflater.from(compatActivity).inflate(R.layout.item_table_gantt, null);

            ScaleView scale = (ScaleView) convertView.findViewById(R.id.scale);
            LinearLayout gantt = (LinearLayout) convertView.findViewById(R.id.group_bars);

            holder = new ViewHolder();
            holder.addView(scale);
            holder.addView(gantt);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Display scale as background
        ScaleView scale = (ScaleView) holder.getView(R.id.scale);
        scale.setRange( minDate, maxDate);
        scale.setXAxis(nx, dx);
        scale.setMilestones(milestones);

        // Display Gantt Graph
        LinearLayout bars = (LinearLayout) holder.getView(R.id.group_bars);
        bars.removeAllViews();

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dy);

        for (int i = 0; i < etape.size(); i++) {
            final Etapa etapa = etape.get(i);

            BarViewEtape task_bar = new BarViewEtape(compatActivity);
            task_bar.setMinimumHeight(dy);
            task_bar.setRange(minDate, maxDate);
            task_bar.setDimen(dx, dy);
            task_bar.setTaskAndStatus(true, etapa);

            bars.addView(task_bar, lp);

            BarViewEtape status_task_bar = new BarViewEtape(compatActivity);
            status_task_bar.setMinimumHeight(dy);
            status_task_bar.setRange(minDate, maxDate);
            status_task_bar.setDimen(dx, dy);
            status_task_bar.setTaskAndStatus(false, etapa);

            bars.addView(status_task_bar, lp);
        }
        // Start animation
        Animation anim = AnimationUtils.loadAnimation(convertView.getContext(), R.anim.item_motion);
        bars.setAnimation(anim);

        return convertView;
    }

    @Override
    public int getWidth(int column) {
        int width;
        switch (column) {
            case -1:
                width = Math.round(160 * scale);
                break;
            case 0:
                width = nx * dx;
                break;
            default:
                throw new RuntimeException("wtf?");
        }
        return width;
    }

    @Override
    public int getHeight(int row) {
        int height;
        switch (row) {
            case -1:
                height = Math.round(32 * scale);
                break;
            case 0:
                int listHeight = Math.round((ny * (dy + 1)));

                WindowManager wm = compatActivity.getWindowManager();
                Display display = wm.getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);

                // Display size minus ProjectView
                int dispHeight = size.y - Math.round(32 * scale);

                if (dispHeight > listHeight) {
                    height = dispHeight;
                } else {
                    height = listHeight;
                }
                break;
            default:
                throw new RuntimeException("wtf?");
        }
        return height;
    }

    @Override
    public int getItemViewType(int row, int column) {
        final int itemViewType;
        if (row == -1 && column == -1) {
            itemViewType = 0;
        } else if (row == -1 && column == 0) {
            itemViewType = 1;
        } else if (row == 0 && column == -1) {
            itemViewType = 2;
        } else if (row == 0 && column == 0) {
            itemViewType = 3;
        } else {
            throw new RuntimeException("wtf?");
        }
        return itemViewType;
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    private class ViewHolder {
        private HashMap<Integer, View> storedViews = new HashMap<Integer, View>();

        public ViewHolder addView(View view) {
            int id = view.getId();
            storedViews.put(id, view);
            return this;
        }

        public View getView(int id) {
            return storedViews.get(id);
        }
    }
}
