package sk.piskula.employees.job;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
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

import static sk.piskula.employees.job.ParseSampleDataWithAsyncTask.IS_FIRST_RUN_PARAM;

/**
 * @author Ondrej Oravcok
 * @version 2.8.2017
 */
public class ParseSampleDataInThread implements Runnable {

    private static final String LOG_TAG = ParseSampleDataInThread.class.getSimpleName();

    private static final String INPUT_FILE = "input_data.json";

    private Context mContext;

    public ParseSampleDataInThread(Context context) {
        super();
        mContext = context;
    }

    @Override
    public void run() {
        // this sleep is only to slow down system to see loaders
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.interrupted();
        }

        String errMsg = null;
        List<ContentValues> employees = null;
        try {
            InputStream is = mContext.getAssets().open(INPUT_FILE);
            JSONObject json = new JSONObject(read(new BufferedReader(new InputStreamReader(is))));
            employees = transformJsonToEmployees(json);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Failed opening sample data file " + INPUT_FILE);
            return;
        } catch (JSONException e) {
            Log.e(LOG_TAG, "JSON input data file " + INPUT_FILE + " is not valid.");
            return;
        }

        ContentResolver contentResolver = mContext.getContentResolver();
        for (ContentValues current : employees) {
            // if insertion failed
            if (contentResolver.insert(EmployeeContract.EmployeeEntry.CONTENT_URI, current) == null) {
                Log.e(LOG_TAG, "Cannot create Employee " + current);
            }
        }

        Log.i(LOG_TAG, "JSON input data parsed successfully.");

        SharedPreferences.Editor editor = mContext.getSharedPreferences(IS_FIRST_RUN_PARAM, Context.MODE_PRIVATE).edit();
        editor.putBoolean(IS_FIRST_RUN_PARAM, false);

        editor.apply();
        Log.i(LOG_TAG, "Shared preference '" + IS_FIRST_RUN_PARAM + "' has been saved.");
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

    private String read(BufferedReader bufferedReader) throws IOException {
        StringBuilder sb = new StringBuilder();

        String line = null;
        while ((line = bufferedReader.readLine()) != null)
            sb.append(line);

        return sb.toString();
    }

}
