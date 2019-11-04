package com.example.asus.dailyexpensenote.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.asus.dailyexpensenote.R;
import com.example.asus.dailyexpensenote.backup.LocalBackup;
import com.example.asus.dailyexpensenote.database.MyDBHelper;
import com.example.asus.dailyexpensenote.fragment.DashBoardFragment;
import com.example.asus.dailyexpensenote.fragment.ExpensesFragment;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private int mark = 0;
    private BottomNavigationView bottomNavigationView;
    private MyDBHelper myDBHelper;
    private LocalBackup localBackup;

    public static final int REQUEST_CODE_PERMISSIONS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Dash Board");

        replaceFragment(new DashBoardFragment());//set default fragment in dash board

        init();

        //navigation item selected action
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.dashBoardNavItemId:

                        if(mark == 1){
                            replaceFragment(new DashBoardFragment());
                            mark--;
                        }
                        setTitle("Dash Board");
                        return true;

                    case R.id.expensesNavItemId:

                        if(mark == 0){
                            replaceFragment(new ExpensesFragment());
                            mark++;
                        }
                        setTitle("Expenses");
                        return true;
                }
                return false;
            }
        });

    }

    //change fragment when nav item selected
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutId,fragment);
        fragmentTransaction.commit();
    }

    //main menu created
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.createBackupItemId:

                String outFileName = Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.app_name) + File.separator;
                localBackup.performBackup(myDBHelper,outFileName);
                break;

            case R.id.importDataItemId:

                localBackup.performRestore(myDBHelper);
                break;

            case R.id.clearAllDataItemId:

                showAlertDialog();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Are you sure to clear all data !");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SQLiteDatabase sqLiteDatabase = myDBHelper.getWritableDatabase();
                myDBHelper.onUpgrade(sqLiteDatabase,MyDBHelper.getDatabaseVersion(),MyDBHelper.getDatabaseVersion());
                Toast.makeText(MainActivity.this, "All data cleared\nplease restart your app", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        Dialog dialog = builder.create();
        dialog.show();

    }

    //initialize all component
    private void init() {
        bottomNavigationView = findViewById(R.id.bottomNavigationViewId);
        myDBHelper = new MyDBHelper(this);
        localBackup = new LocalBackup(this);
    }
}
