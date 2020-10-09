package com.example.imageshare


import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imageshare.ItemAdapter
import com.google.firebase.database.*

import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class ShowActivity : AppCompatActivity() {
    private lateinit var mProgressDialog: ProgressDialog
     lateinit var dataItem: Item
     lateinit var dataItems:MutableList<Item>
     lateinit var nameList:MutableList<Item>
     lateinit var adapter:ItemAdapter
     lateinit var layout:LinearLayoutManager
     lateinit var database:FirebaseDatabase
     lateinit var reference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        supportActionBar?.title = "Buy anything -> Sell everything"

        mProgressDialog = ProgressDialog(this)
        dataItems =ArrayList()
        nameList = ArrayList()
        layout= LinearLayoutManager(this)
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("Items")
          adapter= ItemAdapter(this,nameList)



        recycler_view.layoutManager=layout
        recycler_view.adapter= adapter





        mProgressDialog.setTitle("Please Wait!")
        mProgressDialog.setMessage("Loading items on sale to My mall")
        mProgressDialog.isIndeterminate=true
        mProgressDialog.show()

        reference.addChildEventListener(object:ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                dataItem = snapshot.getValue(Item::class.java)!!
                dataItems.add(dataItem)
                nameList.add(0, dataItem)
                nameList.take(5000)

                mProgressDialog.dismiss()
       /*CoroutineScope(Dispatchers.Main).launch {
         withContext(Dispatchers.Main) {
         for(dataItem in dataItems){
             val calendar=Calendar.getInstance()
             val today:Long=calendar.timeInMillis
             if (today == dataItem.toa.trim().toLong()  || today > dataItem.toa.trim().toLong())
             {
                 dataItems.remove(dataItem)
                 nameList.remove(dataItem)

                 recycler_view.adapter?.notifyDataSetChanged()

             }
         }

     }
 }*/
                for(dataItem in dataItems){
                    val calendar=Calendar.getInstance()
                    val today:Long=calendar.timeInMillis
                    if (today == dataItem.toa.trim().toLong()  || today > dataItem.toa.trim().toLong())
                    {
                        dataItems.remove(dataItem)
                        nameList.remove(dataItem)


                    }
                }

                Toast.makeText(applicationContext, "Sell your items here for 7 days\nClick on item Description \n\t\t\t\t\t\t\t\t\t\t for \npost date and disappear date", Toast.LENGTH_LONG)
                    .show()


                recycler_view.adapter?.notifyDataSetChanged()


            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}


        })

        search_me.setOnQueryTextListener(
        object:SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(newText: String?): Boolean {

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText!!.isNotEmpty()){
                    nameList.clear()
                    val search=newText.toLowerCase()
                    for(dataItem in dataItems){
                        if (dataItem.combined.toLowerCase().contains(search) )
                        {
                            nameList.add(dataItem)

                        }

                    }
                    recycler_view.adapter?.notifyDataSetChanged()
                }else{
                    nameList.clear()
                    nameList.add(dataItem)
                    recycler_view.adapter?.notifyDataSetChanged()
                }

                return true
            }

        })

        fab.setOnClickListener{
            val intent= Intent(this,MainAdsActivity::class.java)
            startActivity(intent)
        }



    }

 /* override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main,menu)
        val searchItem = menu!!.findItem(R.id.menu_search)
        if(searchItem!=null){
            val searchView=searchItem.actionView as SearchView

            val editText=searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
            editText.hint="Search item on SELLBY mall"

            searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {

                    return true


                }

                override fun onQueryTextChange(textNew: String?): Boolean {
                    if(textNew!!.isNotEmpty()){
                        nameList.clear()
                        val search=textNew.toLowerCase(Locale.getDefault())
                        for(dataItem in dataItems){
                            if (dataItem.name.toLowerCase(Locale.getDefault()).contains(search) ||dataItem.location.toLowerCase(Locale.getDefault()).contains(search)
                                || dataItem.cost.toLowerCase(Locale.getDefault()).contains(search)
                            ){
                                nameList.add(dataItem)

                            }

                        }
                        recycler_view.adapter!!.notifyDataSetChanged()
                    }else{
                        nameList.clear()
                        nameList.add(dataItem)
                        recycler_view.adapter!!.notifyDataSetChanged()
                    }

                    return true
                }
            })

        }


        return super.onCreateOptionsMenu(menu)

    }*/
}

