package sk.piskula.employees.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import sk.piskula.employees.R;
import sk.piskula.employees.data.EmployeeContract.EmployeeEntry;

/**
 * @author Ondrej Oravcok
 * @version 3.8.2017
 */
public class EmployeeAdapter extends RecyclerViewCursorAdapter<EmployeeAdapter.EmployeeViewHolder> {

    private static final String LOG_TAG = EmployeeAdapter.class.getSimpleName();
    private final Context mContext;
    private Callback mCallback;

    public interface Callback {
        void onItemClick(int employeeId);
    }

    public EmployeeAdapter(Context context, Callback callback)
    {
        super(null);
        mContext = context;
        mCallback = callback;
    }

    @Override
    public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_employee, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmployeeViewHolder holder, final Cursor cursor) {

        final int idColumnIndex = cursor.getColumnIndexOrThrow(EmployeeEntry._ID);
        int lastNameColumnIndex = cursor.getColumnIndexOrThrow(EmployeeEntry.COLUMN_LAST_NAME);
        int firstNameColumnIndex = cursor.getColumnIndexOrThrow(EmployeeEntry.COLUMN_FIRST_NAME);
        int departmentColumnIndex = cursor.getColumnIndexOrThrow(EmployeeEntry.COLUMN_DEPARTMENT);
        int avatarColumnIndex = cursor.getColumnIndexOrThrow(EmployeeEntry.COLUMN_AVATAR);

        holder.txtLastName.setText(cursor.getString(lastNameColumnIndex));
        holder.txtFirstName.setText(cursor.getString(firstNameColumnIndex));
        holder.txtDepartment.setText(cursor.getString(departmentColumnIndex));
        Picasso.with(mContext).load(cursor.getString(avatarColumnIndex)).placeholder(
                R.mipmap.ic_launcher_round).resize(150,150).centerInside().into(holder.icon);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onItemClick(cursor.getInt(idColumnIndex));
            }
        });
    }


    public static class EmployeeViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtLastName;
        TextView txtFirstName;
        TextView txtDepartment;
        ImageView icon;

        EmployeeViewHolder(View view) {
            super(view);

            txtLastName = view.findViewById(R.id.txt_item_employee_lastName);
            txtFirstName = view.findViewById(R.id.txt_item_employee_firstName);
            txtDepartment = view.findViewById(R.id.txt_item_employee_department);
            icon = view.findViewById(R.id.img_item_employee_icon);
        }
    }

}
