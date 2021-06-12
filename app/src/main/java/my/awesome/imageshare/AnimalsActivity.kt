package my.awesome.imageshare


import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main2.*

class AnimalsActivity : AppCompatActivity() {
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
        supportActionBar?.title = "Buy/Sell your Home animals"


        MobileAds.initialize(this) {}
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
                val search="Animal".toLowerCase()
                val cow="Cow".toLowerCase()
                val goat="Goat".toLowerCase()
                val sheep="Sheep".toLowerCase()
                val chicken="Chicken".toLowerCase()
                val donkey="Donkey".toLowerCase()
                val farasi="Farasi".toLowerCase()
                val ngombe="Ng'ombe".toLowerCase()
                val mbuzi="Mbuzi".toLowerCase()
                val kuku="paper".toLowerCase()
                val punda="Punda".toLowerCase()
                val rabbit="Rabbit".toLowerCase()

                if (dataItem.name.toLowerCase().contains(search) ||dataItem.name.toLowerCase().contains(cow) ||
                    dataItem.name.toLowerCase().contains(goat) || dataItem.name.toLowerCase().contains(sheep) ||
                    dataItem.name.toLowerCase().contains(chicken)||  dataItem.name.toLowerCase().contains(donkey)
                    || dataItem.name.toLowerCase().contains(farasi) || dataItem.name.toLowerCase().contains(ngombe)
                    || dataItem.name.toLowerCase().contains(mbuzi) || dataItem.name.toLowerCase().contains(kuku)
                    || dataItem.name.toLowerCase().contains(punda)||  dataItem.name.toLowerCase().contains(rabbit))
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

        /*fab_gallery.setOnClickListener{
            val intent= Intent(this, GalleryActivity::class.java)
            startActivity(intent)
        }*/

    }


}

