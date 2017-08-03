package sk.piskula.employees.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import sk.piskula.employees.R;
import sk.piskula.employees.adapter.dto.EmployeeDto;

/**
 * @author Ondrej Oravcok
 * @version 2.8.2017
 */
public class EmployeeRecyclerAdapter extends RecyclerView.Adapter<EmployeeRecyclerAdapter.EmployeeViewHolder> {

    private List<EmployeeDto> mItems;
    private Context mContext;
    private Callback mCallback;

    public EmployeeRecyclerAdapter(Context context, Callback callback) {
        mItems = new ArrayList<>();
        mContext = context;
        mCallback = callback;
    }

    public interface Callback {
        void onItemClick(int employeeId);
    }

    @Override
    public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_employee, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmployeeViewHolder holder, int position) {
        final EmployeeDto currentEmployee = mItems.get(position);

        holder.txtLastName.setText(currentEmployee.getLastName());
        holder.txtFirstName.setText(currentEmployee.getFirstName());
        holder.txtDepartment.setText(currentEmployee.getDepartment());
        Picasso.with(mContext).load(currentEmployee.getAvatar()).placeholder(
                R.mipmap.ic_launcher_round).resize(150,150).centerInside().into(holder.icon);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onItemClick(currentEmployee.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void swapData(List<EmployeeDto> employees) {
        mItems = employees;
        notifyDataSetChanged();
    }

    class EmployeeViewHolder extends RecyclerView.ViewHolder {
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
