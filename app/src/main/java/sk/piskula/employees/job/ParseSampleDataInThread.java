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

import sk.piskula.employees.data.EmployeeContract.EmployeeEntry;

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
        for (ContentValues currentEmployee : employees) {
            // if insertion failed
            if (contentResolver.insert(EmployeeEntry.CONTENT_URI, currentEmployee) == null) {
                Log.e(LOG_TAG, "Cannot create Employee " + currentEmployee);
            }
        }

        Log.i(LOG_TAG, "JSON input data parsed successfully.");

        mContext.getSharedPreferences(IS_FIRST_RUN_PARAM, Context.MODE_PRIVATE)
                .edit().putBoolean(IS_FIRST_RUN_PARAM, false).apply();

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

                createdEmployee.put(EmployeeEntry.COLUMN_LAST_NAME, employee.getString("lastName"));
                createdEmployee.put(EmployeeEntry.COLUMN_FIRST_NAME, employee.getString("firstName"));
                createdEmployee.put(EmployeeEntry.COLUMN_DEPARTMENT, departmentString);
                createdEmployee.put(EmployeeEntry.COLUMN_AVATAR, employee.getString("avatar"));

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
