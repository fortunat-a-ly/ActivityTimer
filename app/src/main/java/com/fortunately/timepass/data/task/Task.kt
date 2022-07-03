package com.fortunately.timepass.data.task

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fortunately.timepass.data.ITask
import com.fortunately.timepass.utils.Constants.CATEGORY_OTHER_INDEX

@Entity(tableName = "task_table")
data class Task (
    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "time")
    var duration: Long = 0,

    ) : Parcelable, ITask {

    @PrimaryKey(autoGenerate = true)
    override var id: Long = 0

    @ColumnInfo(name = "is_constant")
    var isConstant: Boolean = true

    @ColumnInfo(name = "category")
    var category: Int = CATEGORY_OTHER_INDEX

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
            writeLong(duration)
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