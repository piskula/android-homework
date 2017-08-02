package sk.piskula.employees;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sk.piskula.employees.data.EmployeeContract.EmployeeEntry;
import sk.piskula.employees.entity.Employee;
import sk.piskula.employees.screens.EmployeeListFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String IS_FIRST_RUN_PARAM = "isThisFirstRunOfApplication";
    public static final String INPUT_FILE = "input_data.json";
    public static final String RESULT_OK = "OK";

    private List<String> departments = new ArrayList<>();
    private Set<String> departmentsSelected = new HashSet<>();

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
        menu.clear();
        int i = Menu.FIRST;
        int defaultRdIndex = -1;

        String[] projection = { EmployeeEntry.COLUMN_DEPARTMENT };
        Cursor cursor = getContentResolver().query(
                EmployeeEntry.CONTENT_URI, projection, null, null, null);

        while (cursor.moveToNext()) {
            String department = cursor.getString(cursor.getColumnIndex(EmployeeEntry.COLUMN_DEPARTMENT));
            departments.add(department);
            menu.add(0, i, Menu.NONE, department).setTitle(department).setCheckable(true);
            if ("RD".equals(department))
                defaultRdIndex = i - 1;
            i++;
        }

        if (defaultRdIndex >= 0)
            onOptionsItemSelected(menu.getItem(defaultRdIndex));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id <= departments.size()) {
            if (!item.isChecked()) {
                item.setChecked(true);
                departmentsSelected.add(departments.get(id - 1));
            } else {
                item.setChecked(false);
                departmentsSelected.remove(departments.get(id - 1));
            }
        }

        EmployeeListFragment fragment = (EmployeeListFragment) getSupportFragmentManager().findFragmentByTag(TAG);
        fragment.notifyChangeDepartments(departmentsSelected);

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
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
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.interrupted();
            }

            AssetManager assetManager = getAssets();
            JSONObject json = null;
            try {
                InputStream is = assetManager.open(INPUT_FILE);
                json = new JSONObject(read(new BufferedReader(new InputStreamReader(is))));
            } catch (IOException | JSONException e) {
                Log.e(TAG, "Failed reading " + INPUT_FILE, e);
                return "Failed opening sample data: " + e.getMessage();
            }

            ContentResolver contentResolver = context.getContentResolver();
            try {
                for (Employee current : transformJsonToEmployees(json)) {
                    ContentValues values = new ContentValues();

                    values.put(EmployeeEntry.COLUMN_LAST_NAME, current.getLastName());
                    values.put(EmployeeEntry.COLUMN_FIRST_NAME, current.getFirstName());
                    values.put(EmployeeEntry.COLUMN_DEPARTMENT, current.getDepartment());
                    values.put(EmployeeEntry.COLUMN_AVATAR, current.getAvatar());

                    if (contentResolver.insert(EmployeeEntry.CONTENT_URI, values) == null) {
                        Toast.makeText(context, "Cannot create " + current, Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                Log.e(TAG, "Error while parsing input data.", e);
                return "Error while parsing JSON." + e.getMessage();
            }

            return RESULT_OK;
        }

        @Override
        protected void onPostExecute(String result) {
            if (RESULT_OK.equals(result)) {
                Log.i(TAG, "JSON input data parsed successfully.");

                SharedPreferences.Editor editor = context
                        .getSharedPreferences(IS_FIRST_RUN_PARAM, Context.MODE_PRIVATE).edit();
                editor.putBoolean(IS_FIRST_RUN_PARAM, false);

                editor.apply();
                Log.i(TAG, "Shared preference '" + IS_FIRST_RUN_PARAM + "' has been saved.");
            } else {
                Log.e(TAG, "Loading of sample data ended with errors.");
                Toast.makeText(context, "Sample data may be corrupted!", Toast.LENGTH_SHORT).show();
            }
        }

        private String read(BufferedReader bufferedReader) throws IOException {
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = bufferedReader.readLine()) != null)
                sb.append(line);

            return sb.toString();
        }

        private List<Employee> transformJsonToEmployees(JSONObject json) throws JSONException {
            List<Employee> result = new ArrayList<>();

            JSONArray departments = json.getJSONArray("Departments");

            // iterate through departments
            for (int i = 0; i < departments.length(); i++) {
                JSONObject department = (JSONObject) departments.get(i);
                String rd = department.getString("Name");
                JSONArray employees = department.getJSONArray("employees");

                // iterate through employees
                for (int j = 0; j < employees.length(); j++) {
                    JSONObject employee = (JSONObject) employees.get(j);
                    Employee createdEmployee = new Employee();

                    createdEmployee.setDepartment(rd);
                    createdEmployee.setFirstName(employee.getString("firstName"));
                    createdEmployee.setLastName(employee.getString("lastName"));
                    createdEmployee.setAvatar(employee.getString("avatar"));

                    result.add(createdEmployee);
                }
            }
            return result;
        }

    }

}
