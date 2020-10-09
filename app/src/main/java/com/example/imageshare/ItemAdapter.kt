package com.example.imageshare

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.ViewFlipper
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.imageshare.Item
import com.example.imageshare.R
import com.squareup.picasso.Picasso
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
       
        Picasso.with(context).load(currentItem.front).into(holder.imageView)
        holder.textView.text=currentItem.name
        holder.textView1.text="Ksh\t${currentItem.cost}"
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

                holder.textView4.text=currentItem.phoneNo
                holder.flipper.flipInterval = 3000
                holder.flipper.startFlipping()

                Picasso.with(context).load(currentItem.back).placeholder(R.drawable.finepic).into(holder.imageView1)

                Picasso.with(context).load(currentItem.side).placeholder(R.drawable.finepic).into(holder.imageView2)

                Picasso.with(context).load(currentItem.front).placeholder(R.drawable.finepic).into(holder.imageView)


            handler.postDelayed({
                holder.flipper.stopFlipping()
                val intent= Intent(context,MainActivity6::class.java)
                context.startActivity(intent)
                //(context as Activity).finish()

            },9000)



            }





        }






    override fun getItemCount(): Int {
        return items.size

    }

    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView =itemView.picha
        val imageView1: ImageView =itemView.picha_1
        val imageView2: ImageView =itemView.picha_2
        val textView: TextView =itemView.item_name
        val textView1: TextView =itemView.item_price
        val textView2: TextView = itemView.item_description
        val textView3: TextView =itemView.item_location
        val textView4: TextView =itemView.Phone_No
        val flipper:ViewFlipper=itemView.vf2


    }
}


