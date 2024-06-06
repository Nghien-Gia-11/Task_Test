package com.example.task.onClickRecycler

interface OnClick {
    fun onClickEdit(pos : Int)
    fun onClickFinish(pos: Int)
    fun onClickDelete(pos : Int)
}