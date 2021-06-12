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

class CarsAndBikesActivity : AppCompatActivity() {
    private lateinit var mProgressDialog: ProgressDialog
    lateinit var dataItem: Item
    lateinit var dataItems:MutableList<Item>
    lateinit var nameList:MutableList<Item>
    lateinit var adapter:ItemAdapter
    lateinit var layout:LinearLayoutManager
    lateinit var database:FirebaseDatabase
    lateinit var reference: DatabaseReference
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        supportActionBar?.title = "Buy/sell brand/2nd cars/bikes"

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

        database = FirebaseDatabase.getInstance()
        reference = database.getReference("Items")
        adapter= ItemAdapter(this,nameList)
        recycler_view.layoutManager=LinearLayoutManager(this, OrientationHelper.HORIZONTAL,false)
        recycler_view.adapter= adapter





        mProgressDialog.setTitle("Please Wait!")
        mProgressDialog.setMessage("Loading items on sale to My mall")
        mProgressDialog.isIndeterminate=true
        mProgressDialog.show()

        reference.addChildEventListener(object:ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                dataItem = snapshot.getValue(Item::class.java)!!
                val search="cars".toLowerCase()
                val bike="bike".toLowerCase()
                val taxi="Taxi".toLowerCase()
                val boda="Boda".toLowerCase()
                val honda="Honda".toLowerCase()
                val bmw="BMW".toLowerCase()
                val mercedes="mercedes".toLowerCase()
                val toyota="Toyota".toLowerCase()
                val porshe="Porshe".toLowerCase()
                val saloon="Saloon".toLowerCase()
                val subaru="Subaru".toLowerCase()
                val jeep="jeep".toLowerCase()
                val chev="Chevrolet".toLowerCase()
                val jaguar="Jaguar".toLowerCase()
                val nissan="Nissan".toLowerCase()
                val ford="ford".toLowerCase()
                val mazda="Mazda".toLowerCase()
                val volkswagen="volkswagen".toLowerCase()
                val volvo="volvo".toLowerCase()
                val mitsubishi="Mitsubishi".toLowerCase()
                val fiat="Fiat".toLowerCase()
                val suzuki="Suzuki".toLowerCase()
                val lexus="Lexux".toLowerCase()
                val tesla="Tesla".toLowerCase()

                val yamaha="yamaha".toLowerCase()
                val tvs="tvs".toLowerCase()
                val kawasaki ="kawasaki".toLowerCase()
                val boxer="Boxer".toLowerCase()
                val sedan="Sedan".toLowerCase()
                val daihatsu="Daihatsu".toLowerCase()
                val audi="audi".toLowerCase()




                if (dataItem.name.toLowerCase().contains(search) ||dataItem.name.toLowerCase().contains(bike) ||
                    dataItem.name.toLowerCase().contains(taxi) || dataItem.name.toLowerCase().contains(boda) ||
                    dataItem.name.toLowerCase().contains(honda)||  dataItem.name.toLowerCase().contains(mercedes)
                    || dataItem.name.toLowerCase().contains(bmw) || dataItem.name.toLowerCase().contains(toyota)
                    || dataItem.name.toLowerCase().contains(porshe) || dataItem.name.toLowerCase().contains(jeep)
                    || dataItem.name.toLowerCase().contains(subaru) || dataItem.name.toLowerCase().contains(jaguar)
                    || dataItem.name.toLowerCase().contains(chev) || dataItem.name.toLowerCase().contains(ford)
                    || dataItem.name.toLowerCase().contains(nissan) || dataItem.name.toLowerCase().contains(mazda)
                    || dataItem.name.toLowerCase().contains(volvo) || dataItem.name.toLowerCase().contains(volkswagen)
                    || dataItem.name.toLowerCase().contains(mitsubishi) || dataItem.name.toLowerCase().contains(fiat)
                    || dataItem.name.toLowerCase().contains(suzuki) || dataItem.name.toLowerCase().contains(lexus)
                    || dataItem.name.toLowerCase().contains(tesla) || dataItem.name.toLowerCase().contains(yamaha)
                    || dataItem.name.toLowerCase().contains(tvs)|| dataItem.name.toLowerCase().contains(kawasaki)
                    || dataItem.name.toLowerCase().contains(boxer) || dataItem.name.toLowerCase().contains(sedan)
                    || dataItem.name.toLowerCase().contains(daihatsu)|| dataItem.name.toLowerCase().contains(audi))
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

