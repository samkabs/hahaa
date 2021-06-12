package my.awesome.imageshare


import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main2.*

class ShowActivity : AppCompatActivity() {
    private lateinit var mProgressDialog: ProgressDialog
    lateinit var dataItem: Item
    lateinit var dataItems: MutableList<Item>
    lateinit var nameList: MutableList<Item>
    lateinit var adapter: ItemAdapter
    lateinit var layout: LinearLayoutManager
    lateinit var database: FirebaseDatabase
    lateinit var reference: DatabaseReference

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        supportActionBar?.title = "Latest Items by PostTime"

        // FirebaseMessaging.getInstance().isAutoInitEnabled=true
        intent = intent
        val message = intent.getStringExtra("new_message")


        if (!message.isNullOrEmpty()) {
            Toast.makeText(applicationContext, "new message $message", Toast.LENGTH_LONG)
                .show()

        }
        MobileAds.initialize(this) {}

        //mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        mProgressDialog = ProgressDialog(this)
        dataItems = ArrayList()
        nameList = ArrayList()
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("Items")
        adapter = ItemAdapter(this, nameList)



        recycler_view.layoutManager = LinearLayoutManager(this, OrientationHelper.HORIZONTAL, false)
        recycler_view.adapter = adapter





        mProgressDialog.setTitle("Please Wait!")
        mProgressDialog.setMessage("Loading items on sale to My mall")
        mProgressDialog.isIndeterminate = true
        mProgressDialog.show()

        reference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                dataItem = snapshot.getValue(Item::class.java)!!
                dataItems.add(dataItem)
                nameList.add(0, dataItem)
                nameList.take(5000)

                mProgressDialog.dismiss()
                recycler_view.adapter?.notifyDataSetChanged()

                /*for(dataItem in dataItems){
                    val calendar=Calendar.getInstance()
                    val today:Long=calendar.timeInMillis
                    if (today == dataItem.toa.trim().toLong()  || today > dataItem.toa.trim().toLong())
                    {
                        dataItems.remove(dataItem)
                        nameList.remove(dataItem)


                    }
                }*/


                 recycler_view.adapter?.notifyDataSetChanged()


            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}


        })

        search_me.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(newText: String?): Boolean {

                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText!!.isNotEmpty()) {
                        nameList.clear()
                        val search = newText.toLowerCase()
                        for (dataItem in dataItems) {
                            if (dataItem.combined.toLowerCase().contains(search)) {
                                nameList.add(dataItem)

                            }

                        }
                        recycler_view.adapter?.notifyDataSetChanged()
                    } else {
                        nameList.clear()
                        nameList.add(dataItem)
                        recycler_view.adapter?.notifyDataSetChanged()
                    }

                    return true
                }

            })

        fab.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        fab_gallery.setOnClickListener {
            val intent = Intent(this, GalleryActivity::class.java)
            startActivity(intent)
        }

    }

}
