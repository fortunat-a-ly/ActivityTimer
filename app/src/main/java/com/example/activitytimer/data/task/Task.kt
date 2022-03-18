package com.example.activitytimer.data.task

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.activitytimer.data.ITask

@Entity(tableName = "task_table")
data class Task (
    @ColumnInfo(name = "name")
    override var name: String = "",

    @ColumnInfo(name = "time")
    var time: Long = 0,

) : Parcelable, ITask {

    @PrimaryKey(autoGenerate = true)
    override var id: Long = 0

    @ColumnInfo(name = "is_constant")
    var isConstant: Boolean = true

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readLong()
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel?, p1: Int) {
        parcel!!.apply {
            writeString(name)
            writeLong(time)
        }
    }

    companion object CREATOR : Parcelable.Creator<Task> {
        override fun createFromParcel(parcel: Parcel): Task {
            return Task(parcel)
        }

        override fun newArray(size: Int): Array<Task?> {
            return arrayOfNulls(size)
        }
    }
}