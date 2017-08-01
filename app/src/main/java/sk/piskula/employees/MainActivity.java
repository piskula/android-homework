package sk.piskula.employees;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import sk.piskula.employees.adapter.EmployeeAdapter;
import sk.piskula.employees.data.AppDatabase;
import sk.piskula.employees.entity.Employee;
import sk.piskula.employees.screens.EmployeeListFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String IS_FIRST_RUN_PARAM = "isThisFirstRunOfApplication";

    private ProgressBar loadingBar;
    private TextView emptyList;
    private RecyclerView recyclerView;

    private List<Employee> data;
    private EmployeeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_activity, new EmployeeListFragment(), TAG).commit();
        }

        // parse json input data if this is the first run of application
        SharedPreferences prefs = getSharedPreferences(IS_FIRST_RUN_PARAM, MODE_PRIVATE);
        if (prefs.getBoolean(IS_FIRST_RUN_PARAM, true)) {
            new ParseSampleDataJob(this).execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        AppDatabase.destroyInstance();
        super.onDestroy();
    }

    private class ParseSampleDataJob extends AsyncTask<String, Void, String> {

        private Context context;

        ParseSampleDataJob(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "Starting job: parsing JSON input data.");
        }

        @Override
        protected String doInBackground(String... strings) {
            AppDatabase database = AppDatabase.getDatabase(context);
            for (int i = 0; i < 10; i++) {
                Employee e = new Employee();
                e.setFirstName("Mojko" + i);
                e.setLastName("Capasik" + i);
                e.setAvatar("http://icons.iconarchive.com/icons/martin-berube/character/64/Chef-icon.png");
                e.setDepartment("HR");
                database.employeeModel().addEmployee(e);
            }
            return "Sample data loaded";
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG, "JSON input data parsed successfully.");

            SharedPreferences.Editor editor = context
                    .getSharedPreferences(IS_FIRST_RUN_PARAM, Context.MODE_PRIVATE).edit();
            editor.putBoolean(IS_FIRST_RUN_PARAM, false);

            editor.apply();
            Log.i(TAG, "Shared preferences '" + IS_FIRST_RUN_PARAM + "' have been saved.");
        }

    }

}
