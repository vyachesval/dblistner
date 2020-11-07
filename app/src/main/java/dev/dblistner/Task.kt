package dev.dblistner

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    var description: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val created: Long = System.currentTimeMillis(),
    var isDone: Boolean = false
)