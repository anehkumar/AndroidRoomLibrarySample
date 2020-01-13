package com.androidroom.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserQueryDao {

    @Query("SELECT * FROM users")
    LiveData<List<UserTable>> fetchAllUsers();


    @Query("SELECT * FROM users")
    List<UserTable> getAllUsersList();

    @Insert
    Long insertTask(UserTable user);

    @Query("SELECT id, name, status FROM users WHERE id=:id")
    LiveData<UserTable> getUser(int id);

    @Query("DELETE FROM users WHERE id=:id")
    void deleteSpecific(int id);

    @Query("DELETE FROM users WHERE 1=1")
    void deleteAll();

    @Update
    void updateUsers(UserTable userTable);
}
