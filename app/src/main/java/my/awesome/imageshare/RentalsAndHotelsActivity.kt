package my.awesome.imageshare


import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main2.*

class RentalsAndHotelsActivity : AppCompatActivity() {
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
        supportActionBar?.title = "Market your rentals/hotels here"

        // FirebaseMessaging.getInstance().isAutoInitEnabled=true
        intent=intent
        val message=intent.getStringExtra("new_message")


        if(!message.isNullOrEmpty()){
            Toast.makeText(applicationContext, "new message $message", Toast.LENGTH_LONG)
                .show()

        }
        MobileAds.initialize(this) {}

        //mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

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
                val search="rental".toLowerCase()
                val hotel="hotel".toLowerCase()
                val lodge="lodge".toLowerCase()
                val inn="inn".toLowerCase()
                val pub="pub".toLowerCase()
                val restaurant="restaurant".toLowerCase()
                val nyama="nyama".toLowerCase()
                val meat="meat".toLowerCase()
                val morgage="morgage".toLowerCase()
                val bedroom="bedroom".toLowerCase()
                val toilet="toilet".toLowerCase()
                val bedsitter="bedsitter".toLowerCase()
                val office="office".toLowerCase()

                if (dataItem.name.toLowerCase().contains(search) ||dataItem.name.toLowerCase().contains(hotel) ||
                    dataItem.name.toLowerCase().contains(lodge) || dataItem.name.toLowerCase().contains(inn) ||
                    dataItem.name.toLowerCase().contains(pub)||  dataItem.name.toLowerCase().contains(restaurant)
                    || dataItem.name.toLowerCase().contains(nyama) || dataItem.name.toLowerCase().contains(meat)
                    || dataItem.name.toLowerCase().contains(morgage) || dataItem.name.toLowerCase().contains(bedroom)
                    || dataItem.name.toLowerCase().contains(bedsitter)||  dataItem.name.toLowerCase().contains(toilet)
                    ||  dataItem.name.toLowerCase().contains(office))
                {
                    nameList.add(dataItem)
                    dataItems.add(dataItem)
                }


                mProgressDialog.dismiss()
                recycler_view.adapter?.notifyDataSetChanged()


               /* for(dataItem in dataItems){
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
            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
        }



    }


}

