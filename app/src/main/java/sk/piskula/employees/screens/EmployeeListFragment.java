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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import sk.piskula.employees.R;
import sk.piskula.employees.adapter.EmployeeRecyclerAdapter;
import sk.piskula.employees.data.EmployeeContract.EmployeeEntry;
import sk.piskula.employees.adapter.dto.EmployeeDto;

/**
 * @author Ondrej Oravcok
 * @version 1.8.2017
 */
public class EmployeeListFragment extends Fragment implements EmployeeRecyclerAdapter.Callback,
        LoaderManager.LoaderCallbacks<Cursor>,
        View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final int EMPLOYEE_LOADER = 712;

    private String filterSelection = null;
    private String[] filterSelectionArgs = null;

    private EmployeeRecyclerAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        adapter = new EmployeeRecyclerAdapter(getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_employees, container, false);

        // list of employees
        RecyclerView employees = view.findViewById(R.id.list_employees);
        employees.setHasFixedSize(true);
        employees.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        employees.setAdapter(adapter);

        // add dummy employee floating button
        FloatingActionButton addDummyEmployeeBtn = view.findViewById(R.id.add_dummy_employee_float_btn);
        addDummyEmployeeBtn.setOnClickListener(this);

        // filter On/Off switch
        ToggleButton filterOnOffToggleBtn = view.findViewById(R.id.toggle_filter);
        filterOnOffToggleBtn.setOnCheckedChangeListener(this);
        initToggleFilterButtonAsActive(filterOnOffToggleBtn);   // default filter is On

        getLoaderManager().initLoader(EMPLOYEE_LOADER, null, this);

        return view;
    }

    private void initToggleFilterButtonAsActive(ToggleButton button) {
        button.setChecked(true);
        switchFilterOn(true);
    }

    @Override
    public void onItemClick(int employeeId) {
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
        // I am not sure by this implementation
        // previous solution with CursorAdapter was nice, but it supports only
        // ListView, which is deprecated. So here I transform Cursor to List<entityDto>,
        // but it may be bottleNeck while it is done on Main Thread
        List<EmployeeDto> employeeList = new ArrayList<>();

        while (data.moveToNext()) {
            EmployeeDto employee = new EmployeeDto();

            employee.setId(data.getInt(data.getColumnIndexOrThrow(EmployeeEntry._ID)));
            employee.setLastName(data.getString(data.getColumnIndexOrThrow(EmployeeEntry.COLUMN_LAST_NAME)));
            employee.setFirstName(data.getString(data.getColumnIndexOrThrow(EmployeeEntry.COLUMN_FIRST_NAME)));
            employee.setAvatar(data.getString(data.getColumnIndexOrThrow(EmployeeEntry.COLUMN_AVATAR)));
            employee.setDepartment(data.getString(data.getColumnIndexOrThrow(EmployeeEntry.COLUMN_DEPARTMENT)));

            employeeList.add(employee);
        }

        adapter.swapData(employeeList);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapData(new ArrayList<EmployeeDto>());
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isFilterOn) {
        switch (compoundButton.getId()) {
            case R.id.toggle_filter:
                switchFilterOn(isFilterOn);
                break;
        }
    }

    private void switchFilterOn(boolean isOn) {
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
