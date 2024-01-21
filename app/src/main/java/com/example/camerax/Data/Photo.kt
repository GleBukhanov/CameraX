package com.example.camerax.Data

class Photo {
    var photoPath:String?=null
    var photoName:String?=null
    constructor(photoPath:String?,photoName:String?){
        this.photoPath=photoPath
        this.photoName=photoName
    }
    constructor()
    {}
}