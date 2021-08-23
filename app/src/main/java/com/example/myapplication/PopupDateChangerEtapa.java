package com.example.myapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.myapplication.Adapter.UserAdapter;
import com.example.myapplication.Model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PopupDateChangerEtapa extends AppCompatDialogFragment {

    private AlertDialog.Builder builder ;
    private AlertDialog alertDialog;

    private TextView  dateTextEnd;

    private ImageView  arrowEnd;
    private Button btnCreate;



    private UserAdapter userAdapter;
    private List<User> mUsers;



    public static String userid, etapaid, endDate;




    FirebaseDatabase db;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_popup_date_changer, null);

        builder.setView(view);

        db = FirebaseDatabase.getInstance();


        dateTextEnd = (TextView) view.findViewById(R.id.dateTextEnd);




        arrowEnd = (ImageView) view.findViewById(R.id.arrowEnd);

        btnCreate = (Button) view.findViewById(R.id.btnCreate);

        dateTextEnd.setText(endDate);

//        taskid = getArguments().getString("taskid");
//        userid = getArguments().getString("userid");




        arrowEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(dateTextEnd);
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String endDate = dateTextEnd.getText().toString();





                if (TextUtils.isEmpty(endDate)) {
                    dateTextEnd.setError("EndDate is Require");
                    return;
                }


                Update(endDate, etapaid);
            }
        });





        return builder.create();
    }

    private void showDatePickerDialog(TextView text) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.MONTH, month);
                        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                        String date = DateFormat.getDateInstance(DateFormat.DEFAULT).format(c.getTime());
                        text.setText(date);
                    }
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void Update(String endDate, String etapaid) {



        DatabaseReference reference1 = db.getReference("Etape").child(etapaid);

        Map<String,Object> Map = new HashMap<>();

        Map.put("endDate", endDate);


        reference1.updateChildren(Map);




    }








}