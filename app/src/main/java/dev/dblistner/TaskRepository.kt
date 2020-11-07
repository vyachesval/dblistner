package dev.dblistner

import android.content.Context
import androidx.room.Room
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class TaskRepository private constructor(context: Context) {

    private val mTaskDao = Room.databaseBuilder(context, TaskDatabase::class.java, "tasks-database")
        .build()
        .taskDao()

    private val mObserverSubject = PublishSubject.create<DatabaseEvent<Task>>()

    fun addTask(task: Task): Completable {
        val insertEvent = DatabaseEvent(DatabaseEventType.INSERTED, task)
        return mTaskDao.insertTask(task)
            .doOnComplete { mObserverSubject.onNext(insertEvent) }
    }

    fun deleteTask(task: Task): Completable {
        val deleteEvent = DatabaseEvent(DatabaseEventType.REMOVED, task)
        return mTaskDao.deleteTask(task)
            .doOnComplete { mObserverSubject.onNext(deleteEvent) }
    }

    fun deleteAll(): Completable {
        val deleteEvent = DatabaseEvent(DatabaseEventType.REMOVED, Task(""))
        return mTaskDao.deleteAll()
            .doOnComplete { mObserverSubject.onNext(deleteEvent) }
    }

    fun updateTask(task: Task): Completable {
        val updateEvent = DatabaseEvent(DatabaseEventType.UPDATED, task)
        return mTaskDao.updateTask(task)
            .doOnComplete { mObserverSubject.onNext(updateEvent) }
    }

    fun observeTasks(): Observable<DatabaseEvent<Task>> {
        return mTaskDao.getAll()
            .flatMapObservable { Observable.fromIterable(it) }
            .map { DatabaseEvent(DatabaseEventType.INSERTED, it) }
            .concatWith(mObserverSubject)
    }

    companion object {
        @Volatile
        private var instance: TaskRepository? = null

        fun getInstance(context: Context): TaskRepository {
            return instance ?: synchronized(this) {
                instance ?: TaskRepository(context.applicationContext).also { instance = it }
            }
        }
    }
}