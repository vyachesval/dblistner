package dev.dblistner

import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import io.reactivex.Observer
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    var disposable: CompositeDisposable = CompositeDisposable()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       // val time = LocalDateTime.parse("Mon, 02 Nov 2020 14:27:13 +0300"
         //       , DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z"))
        // Log.d("iszx", time.toString())

        //var addr = "https://www.yaplakal.com/news.xml"
        var addr = "https://www.androidauthority.com/feed/news.xml"

        var uuu = Uri.parse(addr)

        var baseUrl = uuu.fragment ?: ""
        Log.d("iszx", "ret=" + baseUrl)

        val observer: Observer<DatabaseEvent<Task>> = object : Observer<DatabaseEvent<Task>> {
            override fun onNext(t: DatabaseEvent<Task>) {
                Log.d("iszx", "onNext" + t.value.description)
            }

            override fun onSubscribe(d: Disposable) {
                Log.d("iszx", "onSubscribe" + d.toString())
            }

            override fun onError(e: Throwable) {
                Log.d("iszx", "onError" + e)
            }

            override fun onComplete() {
                Log.d("iszx", "onComplete")
            }
        }
/*
        disposable.add(TaskRepository.getInstance(this).deleteAll()
            .delay(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .subscribe{ observer }
        )
*/
        disposable.add(TaskRepository.getInstance(this).observeTasks()
            .map { Log.d("iszx",  "observeTasks " + it.eventType.toString() + " " + it.value.description) }
            .delay(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .subscribe{ observer }
        )
/*
        disposable.add(TaskRepository.getInstance(this).addTask(Task("aaaaaaa"))
            .delay(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .subscribe())
        disposable.add(TaskRepository.getInstance(this).addTask(Task("bbbbbbb"))
            .delay(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .subscribe())
        disposable.add(TaskRepository.getInstance(this).addTask(Task("ccccccc"))
            .delay(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .subscribe())
*/
        disposable.add(TaskRepository.getInstance(this).addTask(Task("ddddddd"))
            .delay(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .subscribe())
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
}

