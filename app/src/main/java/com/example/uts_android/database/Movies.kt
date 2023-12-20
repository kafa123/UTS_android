package com.example.uts_android.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.io.Serializable
@Entity(tableName = "MovieDB")
data class Movies(
    @PrimaryKey(false)
    @ColumnInfo(name = "title")
    val title:String,
    @ColumnInfo(name = "director")
    val director:String,
    @ColumnInfo(name = "genres")
    val genres:List<String>,
    @ColumnInfo(name = "description")
    val description:String,
    @ColumnInfo(name = "image")
    val image:String,
    @ColumnInfo(name = "popularity")
    val popularity:Int
):Serializable{
    constructor():this("", "",  mutableListOf(),"", "",0)
}
