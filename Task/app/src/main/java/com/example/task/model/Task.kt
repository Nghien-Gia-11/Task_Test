package com.example.task.model

import java.time.LocalDate

class Task(
     private var _id : Int,
    private var _title : String,
    private var _task : String,
    private var _date : String,
    private var _status : Int
) {
    var id : Int
        get() = _id
        set(value) {
            _id = value
        }

    var title : String
        get() = _title
        set(value) {
            _title = value
        }

    var task : String
        get() = _task
        set(value) {
            _task = value
        }

    var date : String
        get() = _date
        set(value) {
            _date = value
        }

    var status : Int
        get() = _status
        set(value) {
            _status = value
        }


    constructor(title : String, task : String, date : String, status: Int) : this (0,title, task, date, status)

}