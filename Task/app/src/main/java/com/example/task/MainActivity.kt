package com.example.task

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.task.DbHelper.TaskHelper
import com.example.task.adapter.TaskAdapter
import com.example.task.databinding.ActivityMainBinding
import com.example.task.databinding.LayoutDialogAddBinding
import com.example.task.model.Task
import com.example.task.onClickRecycler.OnClick
import java.time.LocalDate
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dialog: AlertDialog
    private val current = Calendar.getInstance()
    private var db = TaskHelper(this)
    private var days : Int = 0
    private var months : Int = 0
    private var years : Int = 0
    private lateinit var adapterTask: TaskAdapter
    private lateinit var listTask : List<Task>


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setAdapter()

        binding.btnAddTask.setOnClickListener {
            showDiaLogAdd()
        }
    }

    private fun setAdapter() {
        listTask = db.getAllData()
        binding.recycler.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.recycler.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )

        adapterTask = TaskAdapter(listTask, object : OnClick{
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onClickEdit(pos: Int) {
                showDiaLogUpdate(pos)
            }

            override fun onClickFinish(pos: Int) {
                showDiaLogFinish(pos, true)
            }

            override fun onClickDelete(pos: Int) {
                showDiaLogFinish(pos, false)
            }
        })
        binding.recycler.adapter = adapterTask
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun showDiaLogFinish(positions: Int, check : Boolean){
        val diaLog = AlertDialog.Builder(this)
        diaLog.apply {
            setTitle("Confirm")
            if (!check){
                setMessage("Bạn chắc chắn muốn xóa ?")
            }
            else{
                setMessage("Bạn muốn hoàn thành nhiệm vụ ?")
            }
            setNegativeButton("No") { diaLogInterface: DialogInterface, _: Int ->
                diaLogInterface.dismiss()
            }
            setPositiveButton("Yes") { _ : DialogInterface, _:Int ->
                if (!check){
                    if (db.deleteData(positions) != 0){
                        Toast.makeText(this@MainActivity,"Success", Toast.LENGTH_SHORT).show()
                        listTask = db.getAllData()
                        adapterTask.setTask(listTask)
                        binding.recycler.adapter = adapterTask
                    }
                    else{
                        Toast.makeText(this@MainActivity,"Failed", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    if (db.finishTask(positions) != 0){
                        Toast.makeText(this@MainActivity,"Success", Toast.LENGTH_SHORT).show()
                        listTask = db.getAllData()
                        adapterTask.setTask(listTask)
                        binding.recycler.adapter = adapterTask
                    }
                    else{
                        Toast.makeText(this@MainActivity,"Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        diaLog.show()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDiaLogAdd() {
        val build = AlertDialog.Builder(this)
        val diaLogBinding = LayoutDialogAddBinding.inflate(LayoutInflater.from(this))
        build.setView(diaLogBinding.root)
        diaLogBinding.btnClose.setOnClickListener {
            dialog.dismiss()
        }
        getTimeOnDatePicker(diaLogBinding)
        diaLogBinding.btnAdd.visibility = View.VISIBLE
        diaLogBinding.btnEdit.visibility = View.GONE
        dialog = build.create()
        dialog.show()
        addTask(diaLogBinding)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showDiaLogUpdate(pos : Int){
        val build = AlertDialog.Builder(this)
        val diaLogBinding = LayoutDialogAddBinding.inflate(LayoutInflater.from(this))
        build.setView(diaLogBinding.root)
        diaLogBinding.btnClose.setOnClickListener {
            dialog.dismiss()
        }

        val task = db.getDataById(pos)
        diaLogBinding.edtTask.setText(task.task)
        diaLogBinding.edtTitle.setText(task.title)
        diaLogBinding.edtTime.setText(task.date)
        diaLogBinding.btnEdit.setOnClickListener {
            if (db.updateData(pos, diaLogBinding.edtTitle.text.toString(), diaLogBinding.edtTask.text.toString(), diaLogBinding.edtTime.text.toString()) != 0){
                Toast.makeText(this@MainActivity,"Success", Toast.LENGTH_SHORT).show()
                listTask = db.getAllData()
                adapterTask.setTask(listTask)
                binding.recycler.adapter = adapterTask
            }
            else{
                Toast.makeText(this@MainActivity,"Failed", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        getTimeOnDatePicker(diaLogBinding)
        diaLogBinding.btnAdd.visibility = View.GONE
        diaLogBinding.btnEdit.visibility = View.VISIBLE
        dialog = build.create()
        dialog.show()
        addTask(diaLogBinding)
    }

    private fun getTimeOnDatePicker(diaLogBinding : LayoutDialogAddBinding){
        val startYear = current.get(Calendar.YEAR)
        val startMonth = current.get(Calendar.MONTH)
        val startDay = current.get(Calendar.DATE)
        diaLogBinding.btnTime.setOnClickListener {
            DatePickerDialog(this,DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                diaLogBinding.edtTime.setText("$dayOfMonth/$month/$year")
                days = dayOfMonth
                months = month
                years = year
            },startYear,startMonth,startDay).show()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addTask(diaLogBinding : LayoutDialogAddBinding){
        diaLogBinding.btnAdd.setOnClickListener {
            if (db.insertData(diaLogBinding.edtTitle.text.toString(),diaLogBinding.edtTask.text.toString(),"$days/$months/$years", false)
                    .toInt() != 0){
                Toast.makeText(this,"Success", Toast.LENGTH_SHORT).show()
                listTask = db.getAllData()
                adapterTask.setTask(listTask)
                binding.recycler.adapter = adapterTask
            }
            else{
                Toast.makeText(this,"Failed", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }
    }
}
