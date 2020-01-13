package com.androidroom.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.androidroom.MainActivity;
import com.androidroom.R;
import com.androidroom.model.UserActions;
import com.androidroom.model.UserTable;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.CustomViewHolder> {

    private List<UserTable> userTableList;
    private Context mContext;
    private Activity act;
    UserActions userActions;

    public ListAdapter(Context context, ArrayList<UserTable> userTables, Activity activity) {
        this.userTableList = userTables;
        this.mContext = context;
        this.act = activity;
        userActions = new UserActions(context);
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.user_list, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        final UserTable get = userTableList.get(position);

        holder.name.setText(get.getName());
        holder.status.setText(get.getStatus());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = get.getId();
                userActions.delete(id);
                userTableList.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = get.getId();
                userActions.getUser(id).observe((LifecycleOwner) act, new Observer<UserTable>() {
                    @Override
                    public void onChanged(UserTable userTable) {
                        Toast.makeText(mContext, userTable.getId()+" "+userTable.getName()+" "+userTable.getStatus(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)mContext).editUser(get);
            }
        });

    }


    @Override
    public int getItemCount() {
        return (null != userTableList ? userTableList.size() : 0);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView name, status;
        ImageButton delete, edit, view;

        public CustomViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            status = (TextView) itemView.findViewById(R.id.status);
            delete = (ImageButton) itemView.findViewById(R.id.delete);
            edit = (ImageButton) itemView.findViewById(R.id.edit);
            view = (ImageButton) itemView.findViewById(R.id.view);
        }
    }
}
