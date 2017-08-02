package sk.piskula.employees.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import sk.piskula.employees.R;
import sk.piskula.employees.data.EmployeeContract.EmployeeEntry;

/**
 * @author Ondrej Oravcok
 * @version 2.8.2017
 */
public class EmployeeCursorAdapter extends CursorAdapter {

    private Callback callback;

    /**
     * constructor
     *
     * @param context context
     * @param cursor Cursor with data from DB
     * @param callback
     */
    public EmployeeCursorAdapter(Context context, Cursor cursor, Callback callback) {
        super(context, cursor, 0);
        this.callback = callback;
    }

    // to handle itemClick on employee
    public interface Callback {
        void onItemClick(View v, int employeeId);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_employee, parent, false);
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        TextView txtLastName = view.findViewById(R.id.txt_item_employee_lastName);
        TextView txtFirstName = view.findViewById(R.id.txt_item_employee_firstName);
        TextView txtDepartment = view.findViewById(R.id.txt_item_employee_department);
        ImageView icon = view.findViewById(R.id.img_item_employee_icon);

        final int employeeId = cursor.getInt(cursor.getColumnIndexOrThrow(EmployeeEntry._ID));
        txtLastName.setText(cursor.getString(cursor.getColumnIndexOrThrow(EmployeeEntry.COLUMN_LAST_NAME)));
        txtFirstName.setText(cursor.getString(cursor.getColumnIndexOrThrow(EmployeeEntry.COLUMN_FIRST_NAME)));
        // txtDepartment.setText(cursor.getString(cursor.getColumnIndexOrThrow(EmployeeEntry.COLUMN_DEPARTMENT)));  according to spec
        Picasso.with(context).load(cursor.getString(cursor.getColumnIndexOrThrow(EmployeeEntry.COLUMN_AVATAR)))
                .placeholder(R.mipmap.ic_launcher_round).resize(150,150).centerInside().into(icon);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onItemClick(view, employeeId);
            }
        });
    }
}
