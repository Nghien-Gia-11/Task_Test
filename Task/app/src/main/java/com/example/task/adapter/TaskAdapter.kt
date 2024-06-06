package com.example.task.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.task.DbHelper.TaskHelper
import com.example.task.databinding.LayoutItemTaskBinding
import com.example.task.model.Task
import com.example.task.onClickRecycler.OnClick

class TaskAdapter(private var listTask : List<Task>, private var onClick: OnClick) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {
    private lateinit var binding : LayoutItemTaskBinding

    inner class ViewHolder(binding : LayoutItemTaskBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
        binding = LayoutItemTaskBinding.inflate(view, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listTask.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            binding.txtTitle.text = listTask[position].title
            binding.txtTime.text = listTask[position].date
            if (listTask[position].status == 1){
                binding.txtStatus.text = "Hoàn thành"
            }
            else{
                binding.txtStatus.text = "Chưa hoàn thành"
            }
            binding.btnEdit.setOnClickListener {
                onClick.onClickEdit(listTask[position].id)
            }
            binding.btnDelete.setOnClickListener{
                onClick.onClickDelete(listTask[position].id)
            }
            binding.btnFinish.setOnClickListener {
                onClick.onClickFinish(listTask[position].id)
            }
            if (listTask[position].status == 1){
                binding.btnFinish.visibility = View.GONE
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setTask(listTask: List<Task>){
        this.listTask = listTask
        notifyDataSetChanged()
    }

}