package sk.piskula.employees.screens;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

import sk.piskula.employees.R;
import sk.piskula.employees.adapter.EmployeeCursorAdapter;
import sk.piskula.employees.data.EmployeeContract.EmployeeEntry;

/**
 * @author Ondrej Oravcok
 * @version 1.8.2017
 */

public class EmployeeListFragment extends Fragment implements EmployeeCursorAdapter.Callback, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EMPLOYEE_LOADER = 712;

    private EmployeeCursorAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (adapter == null) {
            adapter = new EmployeeCursorAdapter(getContext(), null, this);
        }

        getLoaderManager().initLoader(EMPLOYEE_LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_employees, container, false);

        ListView employees = view.findViewById(R.id.list_employees);
        employees.setAdapter(adapter);
        employees.setEmptyView(view.findViewById(R.id.empty_list_employees));

        return view;
    }

    @Override
    public void onItemClick(View v, int employeeId) {
        Toast.makeText(getActivity(), "Clicked on " + employeeId, Toast.LENGTH_SHORT).show();
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
                null,
                null,
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
}
