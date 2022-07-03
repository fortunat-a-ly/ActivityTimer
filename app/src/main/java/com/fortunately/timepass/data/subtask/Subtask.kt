package com.fortunately.timepass.data.subtask

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fortunately.timepass.data.ITask
import com.fortunately.timepass.data.task.Task

@Entity(tableName = "subtask_table")
data class Subtask (
    @ColumnInfo(name = "task_id")
    var taskId: Long = 0,

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "time")
    var duration: Long = 0L,

    @ColumnInfo(name = "sets_count")
    var count: Int = 1,

    @ColumnInfo(name = "play_automatically")
    var playAutomatically: Boolean = false
) : Parcelable, ITask {

    @PrimaryKey(autoGenerate = true)
    override var id: Long = 0

    @ColumnInfo(name = "break_interval")
    var breakInterval: Long = 0

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readLong(),
        parcel.readInt()
    ) {
    }

    constructor(task: Task) : this(
        name = task.name,
        duration = task.duration
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel?, p1: Int) {
        parcel!!.apply {
            writeLong(taskId)
            writeString(name)
            writeLong(duration)
            writeInt(count)
        }
    }

    companion object CREATOR : Parcelable.Creator<Subtask> {
        override fun createFromParcel(parcel: Parcel): Subtask {
            return Subtask(parcel)
        }

        override fun newArray(size: Int): Array<Subtask?> {
            return arrayOfNulls(size)
        }
    }
}
