package sk.piskula.employees.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import sk.piskula.employees.R;
import sk.piskula.employees.entity.Employee;

/**
 * @author Ondrej Oravcok
 * @version 31.7.2017
 */
public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder> {

    private List<Employee> items;
    private Callback callback;
    private Context context;

    public EmployeeAdapter(Callback callback) {
        super();
        this.items = new ArrayList<>();
        this.callback = callback;
    }

    public interface Callback {
        void onItemClick(View v, Employee employee, int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_employee, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Employee currentItem = items.get(position);
        if (currentItem != null) {
            //set views
            holder.txtLastName.setText(currentItem.getLastName());
            holder.txtFirstName.setText(currentItem.getFirstName());
            holder.txtDepartment.setText(currentItem.getDepartment().getName());
            Picasso.with(context).load(currentItem.getAvatar()).placeholder(
                    R.mipmap.ic_launcher_round).resize(150,150).centerInside().into(holder.icon);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onItemClick(v, items.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void dataChange(List<Employee> items) {
        this.items = items;
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;

        TextView txtLastName;
        TextView txtFirstName;
        TextView txtDepartment;
        ImageView icon;

        ViewHolder(View v) {
            super(v);
            mView = v;
            txtLastName = v.findViewById(R.id.txt_item_employee_lastName);
            txtFirstName = v.findViewById(R.id.txt_item_employee_firstName);
            txtDepartment = v.findViewById(R.id.txt_item_employee_department);
            icon = v.findViewById(R.id.img_item_employee_icon);
        }

    }

}
