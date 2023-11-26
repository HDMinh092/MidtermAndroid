package vn.edu.tdtu.midterm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder>
{
    private ArrayList<Models> dataSource;
    ListActivity listActivity;

    public StudentAdapter(ArrayList<Models> dataSource, ListActivity listActivity) {
        this.dataSource = dataSource;
        this.listActivity = listActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_profile_activity, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.setClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String stnum = dataSource.get(position).getStudentNumber();
                Toast.makeText(listActivity, stnum, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(listActivity);

                String[] options = {"Update Data", "Delete Data"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            //update is clicked
                            String id = dataSource.get(position).getId();
                            String stnum= dataSource.get(position).getStudentNumber();
                            String sname = dataSource.get(position).getName();
                            String sage = dataSource.get(position).getAge();
                            String sphone = dataSource.get(position).getPhone();

                            Intent intent = new Intent(listActivity, AddStudent.class);
                            //put data into intent

                            intent.putExtra("sid", id);
                            intent.putExtra("snumber", stnum);
                            intent.putExtra("sname", sname);
                            intent.putExtra("sage", sage);
                            intent.putExtra("sphone", sphone);

                            listActivity.startActivity(intent);
                        }
                        if (which == 1){
                            listActivity.deleteData(position);
                        }
                    }
                }).create().show();
            }


        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvStudentNumber.setText(dataSource.get(position).getStudentNumber());
        holder.tvName.setText(dataSource.get(position).getName());
        holder.tvAge.setText(dataSource.get(position).getAge());
        holder.tvPhone.setText(dataSource.get(position).getPhone());
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvStudentNumber, tvName, tvAge, tvPhone;
        View viewcontainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewcontainer = itemView;
            tvStudentNumber = itemView.findViewById(R.id.tvStudentNumber);
            tvName = itemView.findViewById(R.id.tvName);
            tvAge = itemView.findViewById(R.id.tvAge);
            tvPhone = itemView.findViewById(R.id.tvPhone);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myclick.onItemClick(v, getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    myclick.onItemLongClick(v, getAdapterPosition());
                    return false;
                }
            });
        }
        private ViewHolder.ClickListener myclick;
        public interface ClickListener{
            void onItemClick(View view, int position);
            void onItemLongClick(View view, int position);
        }

        public void setClickListener(ViewHolder.ClickListener clickListener) {
            myclick = clickListener;
        }
    }
}
