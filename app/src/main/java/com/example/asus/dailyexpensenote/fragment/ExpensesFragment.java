package com.example.asus.dailyexpensenote.fragment;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.asus.dailyexpensenote.model_class.Expense;
import com.example.asus.dailyexpensenote.adapter.ExpenseAdapter;
import com.example.asus.dailyexpensenote.database.MyDBHelper;
import com.example.asus.dailyexpensenote.R;
import com.example.asus.dailyexpensenote.activity.AddDailyExpenseActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ExpensesFragment extends Fragment {

    public static MyDBHelper myDBHelper;
    private ExpenseAdapter expenseAdapter;

    private Spinner spinner;
    private String[] spinnerList;
    private ArrayAdapter<String> arrayAdapter;

    private TextView fromDateTV,toDateTV;
    private ImageView fromDateIV,toDateIV;

    private DatePickerDialog.OnDateSetListener mFromDateSetListener;
    private DatePickerDialog.OnDateSetListener mToDateSetListener;

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private List<Expense> expenseList;

    public ExpensesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_expenses, container, false);

        init(view);

        getFromDate();

        getToDate();

        getData();

        populateDataToRecyclerView();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),AddDailyExpenseActivity.class);
                startActivityForResult(intent,100);
            }
        });

        //hide fab icon on scroll up
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0)
                    fab.hide();
                else if (dy < 0)
                    fab.show();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        return view;
    }

    //refresh data after adding


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 100){
            populateDataToRecyclerView();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //set all data to recycler view
    public void populateDataToRecyclerView() {
        expenseAdapter = new ExpenseAdapter(expenseList,getActivity());
        recyclerView.setAdapter(expenseAdapter);
        expenseAdapter.notifyDataSetChanged();
    }

    //getDataFromDatabase
    private void getData() {
        expenseList = myDBHelper.getDataFromDatabase();
    }

    //set date to fromDate TextView by clicking from date icon
    private void getFromDate() {

        fromDateIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mFromDateSetListener,
                        year, month, day
                );
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        mFromDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                fromDateTV.setText(date);
            }
        };
    }

    //set date to toDate TextView by clicking to date icon
    private void getToDate() {

        toDateIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mToDateSetListener,
                        year, month, day
                );
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        mToDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                toDateTV.setText(date);
            }
        };
    }

    //initialize all components
    private void init(View view) {

        myDBHelper = new MyDBHelper(getActivity());

        spinner = view.findViewById(R.id.selectExpenseTypeSpinnerId);
        spinnerList = getResources().getStringArray(R.array.spinner_list);
        arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,spinnerList);
        spinner.setAdapter(arrayAdapter);

        fromDateTV = view.findViewById(R.id.fromDateTVId);
        toDateTV = view.findViewById(R.id.toDateTVId);
        fromDateIV = view.findViewById(R.id.fromDateIVId);
        toDateIV = view.findViewById(R.id.toDateIVId);

        recyclerView = view.findViewById(R.id.recyclerViewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fab = view.findViewById(R.id.fabId);
        expenseList = new ArrayList<Expense>();
    }

}
