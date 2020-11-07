package dev.dblistner

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface TaskDao {
    @Insert
    fun insertTask(task: Task): Completable

    @Update
    fun updateTask(task: Task): Completable

    @Query("SELECT * FROM Task")
    fun getAll(): Single<List<Task>>

    @Delete
    fun deleteTask(task: Task): Completable

    @Query("DELETE FROM Task")
    fun deleteAll(): Completable
}