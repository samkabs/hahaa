package my.awesome.imageshare

import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.ViewFlipper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item.view.*


class ItemAdapter(private var context:android.content.Context,private val items:List<Item>): RecyclerView.Adapter<ItemAdapter.ItemViewHolder>()
    {







        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(
            R.layout.item,
            parent,false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem= items[position]

        val requestOptions= RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
        Glide.with(context).asBitmap().load(currentItem.front).apply(requestOptions).centerCrop().into(holder.imageView)
        holder.textView.text=currentItem.name
        holder.textView1.text="KSh\t${currentItem.cost}"
        holder.textView2.text=currentItem.itemD
        holder.textView3.text= currentItem.location

        // holder.textView4.text=currentItem.phoneNo
      holder.textView2.setOnClickListener {
          Toast.makeText(context, "Item Posted on:${currentItem.post_date}\nItem Disappears on:${currentItem.delete_date}",Toast.LENGTH_LONG).show()
      }

        holder.flipper.setOnClickListener {
            holder.flipper.stopFlipping()
        }
        holder.textView4.setOnClickListener {// start here
            val handler= Handler()

                holder.imageView3.visibility=View.VISIBLE
                holder.imageView4.visibility=View.VISIBLE
                holder.textView4.text=currentItem.phoneNo
                holder.flipper.flipInterval = 3000
                holder.flipper.startFlipping()
            Glide.with(context).asBitmap().load(currentItem.back).placeholder(R.drawable.finepic).apply(requestOptions).centerCrop().into(holder.imageView1)

            Glide.with(context).asBitmap().load(currentItem.side).placeholder(R.drawable.finepic).apply(requestOptions).centerCrop().into(holder.imageView2)

            //Glide.with(context).asBitmap().load(currentItem.front).placeholder(R.drawable.finepic).apply(requestOptions).centerCrop().into(holder.imageView)
            holder.textView2.visibility=View.INVISIBLE


            handler.postDelayed({
                holder.flipper.stopFlipping()
                //val intent= Intent(context,MainActivity6::class.java)
                //context.startActivity(intent)
                //(context as Activity).finish()

            },9000)



            }


        holder.imageView3.setOnClickListener{


           val number=currentItem.phoneNo.toString().trim()
            val intent=Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + Uri.encode(number)))
            context.startActivity(intent)

         }

        holder.imageView4.setOnClickListener{
       //openWhatsappContact(currentItem.phoneNo)

            val number=currentItem.phoneNo.toString().trim()
            val intent=Intent(Intent.ACTION_VIEW,Uri.parse("sms:" + Uri.encode(number)))
            context.startActivity(intent)

        }


        }






    override fun getItemCount(): Int {
        return items.size

    }

   inner class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView =itemView.picha
        val imageView1: ImageView =itemView.picha_1
        val imageView2: ImageView =itemView.picha_2
        val textView: TextView =itemView.item_name
        val textView1: TextView =itemView.item_price
        val textView2: TextView = itemView.item_description
        val textView3: TextView =itemView.item_location
        val textView4: TextView =itemView.Phone_No
        val flipper:ViewFlipper=itemView.vf2
        val imageView3:ImageView=itemView.call_icon
        val imageView4:ImageView=itemView.text_icon

    }

fun  openWhatsappContact(number:String){
    val uri = Uri.parse("sms to:" + number)
    val i = Intent(Intent.ACTION_SENDTO,uri)
    i.setPackage("com.whatsapp")
    context.startActivity(Intent.createChooser(i,"Chat Seller"))
}
}


