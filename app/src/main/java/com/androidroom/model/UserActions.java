package com.androidroom.model;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class UserActions {

    private String DB_NAME = "users";

    private AppDatabase userDB;

    public UserActions(Context context) {
        userDB = Room.databaseBuilder(context, AppDatabase.class, DB_NAME).build();
    }

    public LiveData<List<UserTable>> getLiveUsers() {
        return userDB.queryDao().fetchAllUsers();
    }

    public List<UserTable> getAllUsers() throws ExecutionException, InterruptedException {
        return new getAllAsyncTask(userDB).execute().get();
    }

    private static class getAllAsyncTask extends android.os.AsyncTask<Void, Void, List<UserTable>> {

        private AppDatabase userDB;
        List<UserTable> a;

        getAllAsyncTask(AppDatabase dao) {
            userDB = dao;
        }

        @Override
        protected List<UserTable> doInBackground(Void... voids) {
            return userDB.queryDao().getAllUsersList();
        }
    }


    public void insertTask(String name, String status) {

        UserTable user = new UserTable();
        user.setName(name);
        user.setStatus(status);

        insert(user);
    }

    public void insert(final UserTable user) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                userDB.queryDao().insertTask(user);
                return null;
            }
        }.execute();
    }

    public void delete(final int id) {
        final LiveData<UserTable> user = getUser(id);
        if (user != null) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    userDB.queryDao().deleteSpecific(id);
                    return null;
                }
            }.execute();
        }
    }

    public void update(final UserTable userData) {
        final LiveData<UserTable> user = getUser(userData.getId());
        if (user != null) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    userDB.queryDao().updateUsers(userData);
                    return null;
                }
            }.execute();
        }
    }

    public void deleteAll() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                userDB.queryDao().deleteAll();
                return null;
            }
        }.execute();
    }

    public LiveData<UserTable> getUser(int id) {
        return userDB.queryDao().getUser(id);
    }


}
