package sk.piskula.employees;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sk.piskula.employees.data.EmployeeContract.EmployeeEntry;
import sk.piskula.employees.job.ParseSampleDataFromJsonJob;
import sk.piskula.employees.screens.EmployeeListFragment;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private List<String> departments = new ArrayList<>();
    private Set<String> departmentsSelected = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_activity, new EmployeeListFragment(), LOG_TAG).commit();
        }

        // parse json input data
        // if this is the first run of application
        // if not, info about it is saved in shared preference
        SharedPreferences prefs = getSharedPreferences(ParseSampleDataFromJsonJob.IS_FIRST_RUN_PARAM, MODE_PRIVATE);
        if (prefs.getBoolean(ParseSampleDataFromJsonJob.IS_FIRST_RUN_PARAM, true)) {
            new ParseSampleDataFromJsonJob(this).execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
