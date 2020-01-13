package com.androidroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidroom.adapter.ListAdapter;
import com.androidroom.model.UserActions;
import com.androidroom.model.UserTable;

import java.util.ArrayList;
import java.util.List;

import static android.widget.GridLayout.HORIZONTAL;

public class MainActivity extends AppCompatActivity {

    EditText name, status;
    Button save, edit;

    UserActions userActions;
    RecyclerView userList;
    ArrayList<UserTable> userTableArrayList;
    ListAdapter listAdapter;
    LinearLayoutManager linearLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userActions = new UserActions(getApplicationContext());
        name = (EditText)findViewById(R.id.name);
        status = (EditText)findViewById(R.id.status);
        save = (Button)findViewById(R.id.save);
        edit = (Button)findViewById(R.id.edit);

        userList = (RecyclerView)findViewById(R.id.userList);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().trim().length() > 0){
                    userActions.insertTask(name.getText().toString(), status.getText().toString());
                    name.setText("");
                    status.setText("");
                    Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_LONG).show();
                    userTableArrayList.clear();
                }else{
                    Toast.makeText(MainActivity.this, "Enter name to save", Toast.LENGTH_LONG).show();
                }

            }
        });

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        userList.setLayoutManager(linearLayoutManager);
        userTableArrayList = new ArrayList<>();

        DividerItemDecoration itemDecor = new DividerItemDecoration(getApplicationContext(), HORIZONTAL);
        userList.addItemDecoration(itemDecor);

        listAdapter = new ListAdapter(this, userTableArrayList, this);
        userList.setAdapter(listAdapter);

        loadUsers();
    }

    private void loadUsers() {
        userActions.getLiveUsers().observe(MainActivity.this, new Observer<List<UserTable>>() {
            @Override
            public void onChanged(List<UserTable> userTables) {

                Log.e("length", userTables.size()+"");
                for(UserTable user : userTables) {
                    UserTable userData = new UserTable();
                    userData.setId(user.getId());
                    userData.setName(user.getName());
                    userData.setStatus(user.getStatus());

                    userTableArrayList.add(userData);
                }

                listAdapter.notifyDataSetChanged();

            }
        });
    }

    public void editUser(final UserTable get) {
        save.setVisibility(View.GONE);
        edit.setVisibility(View.VISIBLE);

        name.setText(get.getName());
        status.setText(get.getStatus());

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserTable user = get;
                user.setName(name.getText().toString());
                user.setStatus(status.getText().toString());

                userActions.update(user);
                userTableArrayList.clear();

                save.setVisibility(View.VISIBLE);
                edit.setVisibility(View.GONE);
                name.setText("");
                status.setText("");
            }
        });
    }
}
