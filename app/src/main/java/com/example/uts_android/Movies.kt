package com.example.uts_android

import java.io.Serializable

data class Movies(
    val title:String,
    val director:String,
    val genres:List<String>,
    val description:String,
    val image:String,
):Serializable{
    constructor():this("", "",  mutableListOf(),"", "",)
}
