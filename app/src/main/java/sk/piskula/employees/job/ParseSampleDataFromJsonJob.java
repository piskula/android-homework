package sk.piskula.employees.job;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import sk.piskula.employees.data.EmployeeContract;

/**
 * @author Ondrej Oravcok
 * @version 2.8.2017
 */

public class ParseSampleDataFromJsonJob extends AsyncTask<String, Void, String> {

    private static final String LOG_TAG = ParseSampleDataFromJsonJob.class.getSimpleName();

    public static final String IS_FIRST_RUN_PARAM = "isThisFirstRunOfApplication";
    private static final String INPUT_FILE = "input_data.json";
    private static final String RESULT_OK = "OK";

    private Context context;

    public ParseSampleDataFromJsonJob(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        Log.i(LOG_TAG, "Starting job: parsing JSON input data.");
    }

    @Override
    protected String doInBackground(String... strings) {
        // this sleep is only to slow down system to see loaders
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.interrupted();
        }

        String errMsg = null;
        List<ContentValues> employees = null;
        try {
            InputStream is = context.getAssets().open(INPUT_FILE);
            JSONObject json = new JSONObject(read(new BufferedReader(new InputStreamReader(is))));
            employees = transformJsonToEmployees(json);
        } catch (IOException e) {
            errMsg = "Failed opening sample data file " + INPUT_FILE;
        } catch (JSONException e) {
            errMsg = "JSON input data file " + INPUT_FILE + " is not valid.";
        }

        // cancel Job in case of exception
        if (errMsg != null)
            return errMsg;

        ContentResolver contentResolver = context.getContentResolver();
        for (ContentValues current : employees) {
            // if insertion failed
            if (contentResolver.insert(EmployeeContract.EmployeeEntry.CONTENT_URI, current) == null) {
                Log.e(LOG_TAG, "Cannot create Employee " + current);
            }
        }

        return RESULT_OK;
    }

    @Override
    protected void onPostExecute(String result) {
        if (RESULT_OK.equals(result)) {
            Log.i(LOG_TAG, "JSON input data parsed successfully.");

            SharedPreferences.Editor editor = context
                    .getSharedPreferences(IS_FIRST_RUN_PARAM, Context.MODE_PRIVATE).edit();
            editor.putBoolean(IS_FIRST_RUN_PARAM, false);

            editor.apply();
            Log.i(LOG_TAG, "Shared preference '" + IS_FIRST_RUN_PARAM + "' has been saved.");
        } else {
            Log.e(LOG_TAG, "Loading of sample data ended with error. Deatiled: " + result);
        }
    }

    private String read(BufferedReader bufferedReader) throws IOException {
        StringBuilder sb = new StringBuilder();

        String line = null;
        while ((line = bufferedReader.readLine()) != null)
            sb.append(line);

        return sb.toString();
    }

    private List<ContentValues> transformJsonToEmployees(JSONObject json) throws JSONException {
        List<ContentValues> result = new ArrayList<>();

        JSONArray departments = json.getJSONArray("Departments");

        // iterate through departments
        for (int i = 0; i < departments.length(); i++) {
            JSONObject department = (JSONObject) departments.get(i);
            String departmentString = department.getString("Name");
            JSONArray employees = department.getJSONArray("employees");

            // iterate through employees
            for (int j = 0; j < employees.length(); j++) {
                JSONObject employee = (JSONObject) employees.get(j);
                ContentValues createdEmployee = new ContentValues();

                createdEmployee.put(EmployeeContract.EmployeeEntry.COLUMN_LAST_NAME, employee.getString("lastName"));
                createdEmployee.put(EmployeeContract.EmployeeEntry.COLUMN_FIRST_NAME, employee.getString("firstName"));
                createdEmployee.put(EmployeeContract.EmployeeEntry.COLUMN_DEPARTMENT, departmentString);
                createdEmployee.put(EmployeeContract.EmployeeEntry.COLUMN_AVATAR, employee.getString("avatar"));

                result.add(createdEmployee);
            }
        }
        return result;
    }

}