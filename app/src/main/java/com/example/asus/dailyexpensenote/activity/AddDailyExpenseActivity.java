package com.example.asus.dailyexpensenote.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.asus.dailyexpensenote.database.MyDBHelper;
import com.example.asus.dailyexpensenote.R;

import java.util.Calendar;

public class AddDailyExpenseActivity extends AppCompatActivity {

    private MyDBHelper myDBHelper;
    private SQLiteDatabase sqLiteDatabase;

    private Spinner spinner;
    private String[] spinnerList;
    private ArrayAdapter<String> arrayAdapter;

    private EditText amountET,dateET,timeET;
    private Button addDocumentBtn,addExpenseBtn;
    private ImageView dateIV,timeIV;

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;

    private String expenseType,expenseAmount,expenseDate,expenseTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_daily_expense);

        init();

        getDate();

        getTime();

        //add Expense button action
        addExpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //getDataFromUser();
                getDataFromUser();

                if(!validate()){
                    return;
                }else {
                    //insert data to database
                    insertData();
                }

            }
        });
    }

    //check all data empty validation
    private boolean validate() {

        boolean valid = true;

        if(expenseType.equals("Select Expense Type")){
            Toast.makeText(this, "Please Select Expense Type !", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if(expenseAmount.isEmpty()){
            amountET.setError("Enter Amount");
            valid = false;
        }else {
            amountET.setError(null);
        }
        if(expenseDate.isEmpty()){
            dateET.setError("Select Date");
            valid = false;
        }else {
            dateET.setError(null);
        }

        return valid;
    }

    //set time to edittextView by clicking date icon
    private void getTime() {

        timeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AddDailyExpenseActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mTimeSetListener,
                        hour, minute, false
                );
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.show();
            }
        });

        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time = hourOfDay + " : " + minute;
                timeET.setText(time);
            }
        };
    }

    //set date to edittextView by clicking date icon
    private void getDate() {

        dateIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddDailyExpenseActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day
                );
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                dateET.setText(date);
            }
        };
    }

    // insert data to database
    private void insertData() {

        long resultId = myDBHelper.insertDataToDatabase(expenseType,expenseAmount,expenseDate,expenseTime);

         if(resultId > 0){
             setResult(RESULT_OK);
             Toast.makeText(AddDailyExpenseActivity.this, "Row "+resultId+" inserted Successfully", Toast.LENGTH_SHORT).show();
             finish();
         }else {
             Toast.makeText(AddDailyExpenseActivity.this, "Data are not inserted", Toast.LENGTH_SHORT).show();
        }
    }

    //get all data from user input
    private void getDataFromUser() {
        expenseType = spinner.getSelectedItem().toString();
        expenseAmount = amountET.getText().toString().trim();
        expenseDate = dateET.getText().toString().trim();
        expenseTime = timeET.getText().toString().trim();

    }

    //initialize all components
    private void init() {

        myDBHelper = new MyDBHelper(AddDailyExpenseActivity.this);
        sqLiteDatabase = myDBHelper.getWritableDatabase();

        spinner = findViewById(R.id.selectExpenseTypeSpinnerId);
        spinnerList = getResources().getStringArray(R.array.spinner_list);
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,spinnerList);
        spinner.setAdapter(arrayAdapter);

        amountET = findViewById(R.id.expenseAmountETId);
        dateET = findViewById(R.id.expenseDateETId);
        timeET = findViewById(R.id.expenseTimeETId);

        dateIV = findViewById(R.id.dateIVId);
        timeIV = findViewById(R.id.timeIVId);

        addDocumentBtn = findViewById(R.id.addDocumentBtnId);
        addExpenseBtn = findViewById(R.id.addExpenseBtnId);
    }
}
