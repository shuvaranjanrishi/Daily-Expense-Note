package com.example.asus.dailyexpensenote.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.asus.dailyexpensenote.R;
import com.example.asus.dailyexpensenote.fragment.DashBoardFragment;
import com.example.asus.dailyexpensenote.fragment.ExpensesFragment;

public class MainActivity extends AppCompatActivity {

    private int mark = 0;
    private BottomNavigationView bottomNavigationView;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.createBackupItemId:
                break;
            case R.id.importDataItemId:
                break;
            case R.id.clearAllDataItemId:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    //initialize all component
    private void init() {
        bottomNavigationView = findViewById(R.id.bottomNavigationViewId);
    }
}
