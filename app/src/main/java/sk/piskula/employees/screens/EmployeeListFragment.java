package sk.piskula.employees.screens;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import sk.piskula.employees.R;
import sk.piskula.employees.adapter.EmployeeAdapter;
import sk.piskula.employees.adapter.loaders.EmployeeLoader;
import sk.piskula.employees.data.AppDatabase;
import sk.piskula.employees.entity.Employee;

/**
 * @author Ondrej Oravcok
 * @version 1.8.2017
 */

public class EmployeeListFragment extends Fragment implements EmployeeAdapter.Callback, LoaderManager.LoaderCallbacks<List<Employee>> {

    private ProgressBar loadingBar;
    private TextView emptyList;
    private RecyclerView recyclerView;

    private List<Employee> data;
    private EmployeeAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_employees, container, false);

        loadingBar = view.findViewById(R.id.fill_ups_list_loading);
        emptyList = view.findViewById(R.id.fill_ups_list_empty);
        recyclerView = view.findViewById(R.id.fill_ups_list);

        if (adapter == null)
            adapter = new EmployeeAdapter(this);

        recyclerView.setAdapter(adapter);

        getLoaderManager().initLoader(EmployeeLoader.ID, null, this);

        return view;
    }

    @Override
    public Loader<List<Employee>> onCreateLoader(int id, Bundle args) {
        return new EmployeeLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<Employee>> loader, List<Employee> data) {
        this.data = data;
        adapter.dataChange(data);
        loadingBar.setVisibility(View.GONE);
        if (data.isEmpty()) {
            emptyList.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyList.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Employee>> loader) {
        if (!data.isEmpty())
            data.clear();
    }

    @Override
    public void onItemClick(View v, Employee employee, int position) {
        Toast.makeText(getActivity(), "Clicked " + employee.getFirstName(), Toast.LENGTH_SHORT).show();
    }
}
