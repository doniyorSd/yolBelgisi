package com.example.trafficregulations.models

class User {
    var id: Int? = null
    var name: String? = null
    var about: String? = null
    var mark: String? = null
    var path_img: String? = null
    var love:Int ?= null
    constructor()

    constructor(name: String?, about: String?, mark: String?, path_img: String?,love:Int) {
        this.name = name
        this.about = about
        this.mark = mark
        this.path_img = path_img
        this.love = love
    }

    constructor(id: Int?, name: String?, about: String?, mark: String?, path_img: String?) {
        this.id = id
        this.name = name
        this.about = about
        this.mark = mark
        this.path_img = path_img
    }

}