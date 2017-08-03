package sk.piskula.employees.screens;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import sk.piskula.employees.R;
import sk.piskula.employees.adapter.EmployeeCursorAdapter;
import sk.piskula.employees.data.EmployeeContract.EmployeeEntry;

/**
 * @author Ondrej Oravcok
 * @version 1.8.2017
 */

public class EmployeeListFragment extends Fragment implements EmployeeCursorAdapter.Callback,
        LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final int EMPLOYEE_LOADER = 712;

    private String filterSelection = null;
    private String[] filterSelectionArgs = null;

    private EmployeeCursorAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        adapter = new EmployeeCursorAdapter(getContext(), null, this);

        getLoaderManager().initLoader(EMPLOYEE_LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_employees, container, false);

        // employees list
        ListView employees = view.findViewById(R.id.list_employees);
        employees.setAdapter(adapter);
        employees.setEmptyView(view.findViewById(R.id.empty_list_employees));

        // add dummy employee floating button
        FloatingActionButton addDummyEmployeeBtn = getActivity().findViewById(R.id.add_dummy_employee_float_btn);
        addDummyEmployeeBtn.setOnClickListener(this);

        // filter On/Off switch
        ToggleButton filterOnOffToggleBtn = view.findViewById(R.id.toggle_filter);
        filterOnOffToggleBtn.setOnCheckedChangeListener(this);

        return view;
    }

    @Override
    public void onItemClick(View v, int employeeId) {
        Toast.makeText(getActivity(),
                getString(R.string.clicked_on) + " " + employeeId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                EmployeeEntry._ID,
                EmployeeEntry.COLUMN_LAST_NAME,
                EmployeeEntry.COLUMN_FIRST_NAME,
                EmployeeEntry.COLUMN_DEPARTMENT,
                EmployeeEntry.COLUMN_AVATAR
        };

        return new CursorLoader(getContext(),
                EmployeeEntry.CONTENT_URI,
                projection,
                filterSelection,
                filterSelectionArgs,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isFilterOn) {
        switch (compoundButton.getId()) {
            case R.id.toggle_filter:
                switchFilterOnOff(isFilterOn);
                break;
        }
    }

    private void switchFilterOnOff(boolean isOn) {
        if (isOn) {
            filterSelection = EmployeeEntry.COLUMN_DEPARTMENT + "=?";
            filterSelectionArgs = new String[] { "RD" };
        } else {
            filterSelection = null;
            filterSelectionArgs = null;
        }
        getLoaderManager().restartLoader(EMPLOYEE_LOADER, null, this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_dummy_employee_float_btn:
                addDummyEmployee();
                break;
        }
    }

    private void addDummyEmployee() {
        Context context = getActivity();
        ContentResolver contentResolver = context.getContentResolver();

        ContentValues createdEmployee = new ContentValues();

        createdEmployee.put(EmployeeEntry.COLUMN_LAST_NAME, "Dummy");
        createdEmployee.put(EmployeeEntry.COLUMN_FIRST_NAME, "John");
        createdEmployee.put(EmployeeEntry.COLUMN_DEPARTMENT, "RD");

        // check if employee has been saved successfully
        if(contentResolver.insert(EmployeeEntry.CONTENT_URI, createdEmployee) == null) {
            Toast.makeText(context,
                    getString(R.string.cannot_create_dummy_employee), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,
                    getString(R.string.created_dummy_employee), Toast.LENGTH_SHORT).show();
        }
    }

}
