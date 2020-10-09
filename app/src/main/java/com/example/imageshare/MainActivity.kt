package com.example.imageshare

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.androidstudy.daraja.Daraja
import com.androidstudy.daraja.DarajaListener
import com.androidstudy.daraja.model.AccessToken
import com.androidstudy.daraja.model.LNMExpress
import com.androidstudy.daraja.model.LNMResult
import com.androidstudy.daraja.util.Env
import com.androidstudy.daraja.util.TransactionType
import com.google.android.gms.ads.*
import com.google.android.gms.ads.formats.MediaView
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAd.OnUnifiedNativeAdLoadedListener
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import kotlinx.android.synthetic.main.activity_main.*

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

private const val FILE_NAME="photo.jpg"
private const val FILE_NAME_1="photo_1.jpg"
private const val FILE_NAME_2="photo_2.jpg"

private const val REQUEST_CODE=10
private const val REQUEST_CODE_1=20
private const val REQUEST_CODE_2=30
private lateinit var photoFile: File
private lateinit var photoFil: File
private lateinit var photoF: File


private lateinit var front: File
private lateinit var back: File
private lateinit var side: File



  class MainActivity : AppCompatActivity(), MpesaListener {
      lateinit var url1: Uri
      lateinit var url: Uri
      lateinit var url2: Uri
      lateinit var url3: Uri
      lateinit var daraja: Daraja



      companion object {
          lateinit var mpesaListener: MpesaListener
          //lateinit var callbackResponse: StkCallback
      }


      private lateinit var mProgressDialog: ProgressDialog


      @RequiresApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
      override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)
          setContentView(R.layout.activity_main)
          supportActionBar?.title = "Take pics -> SELLit for 7 days "

          mpesaListener = this
         //val callbackResponse=StkCallback("","",1,"")


          mProgressDialog = ProgressDialog(this)
          daraja = Daraja.with(
                  "xHy4sEXVcbAiJAxzUGDkB6gJQChixzYt",
                  "AukOvNVQiCv0kAcT",
                  Env.SANDBOX, //for Test use Env.PRODUCTION when in production
                  object : DarajaListener<AccessToken> {
                      override fun onResult(accessToken: AccessToken) {

                          Toast.makeText(
                                  this@MainActivity,
                                  "MPESA TOKEN : ${accessToken.access_token}",
                                  Toast.LENGTH_SHORT
                          ).show()
                      }

                      override fun onError(error: String) {

                      }
                  })


          btn_front.setOnClickListener {

              val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
              photoFile = getphotoFile(FILE_NAME)
              val fileProvider =
                      FileProvider.getUriForFile(this, "com.example.imageshare.fileprovider", photoFile)
              takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
              if (takePictureIntent.resolveActivity(this.packageManager) != null) {
                  startActivityForResult(takePictureIntent, REQUEST_CODE)
              } else {
                  Toast.makeText(this, "cant open camera", Toast.LENGTH_LONG).show()
              }
          }
          btn_post.setOnClickListener {
             // Toast.makeText(applicationContext, "Attempting post...please wait!", Toast.LENGTH_LONG)
                     // .show()


              run loop@{
                  if (item_name.text.toString().isNullOrEmpty()) {
                      Toast.makeText(
                              applicationContext,
                              "Enter your product name",
                              Toast.LENGTH_SHORT
                      ).show()
                      return@loop
                  }
                  if (Phone.text.toString().isNullOrEmpty()) {
                      Toast.makeText(
                              applicationContext,
                              "Please Enter your Phone Number",
                              Toast.LENGTH_SHORT
                      ).show()
                      return@loop
                  }



                  if (item_description.text.toString().isNullOrEmpty()) {
                      Toast.makeText(
                              applicationContext,
                              "Please describe your item ",
                              Toast.LENGTH_SHORT
                      ).show()
                      return@loop
                  }
                  if (item_price.text.toString().isNullOrEmpty()) {
                      Toast.makeText(
                              applicationContext,
                              "Enter a price for your product ",
                              Toast.LENGTH_SHORT
                      ).show()
                      return@loop
                  }
                  if (item_location.text.toString().isNullOrEmpty()) {
                      Toast.makeText(
                              applicationContext,
                              "Please tell us where to find you ",
                              Toast.LENGTH_SHORT
                      ).show()
                      return@loop
                  } else {

                      val phoneNumber: String = Phone.text.toString()

          /* when(item_price.text.toString().toInt()){
              in 0..200 -> {lipanampesa("5")}
              in 201..500 -> {lipanampesa("10")}
              in 501..700 -> {lipanampesa("12")}
              in 701..1000 -> {lipanampesa("15")}
              in 1001..1500 -> {lipanampesa("20")}
              in 1501..2000 -> {lipanampesa("25")}
              in 2001..5000 -> {lipanampesa("50")}
              in 5001..10000 -> {lipanampesa("100")}
              in 10001..50000 -> {lipanampesa("200")}
              in 50001..100000 -> {lipanampesa("500")}
              in 100001..500000 -> {lipanampesa("700")}
              in 500001..1000000 -> {lipanampesa("1000")}
              in 1000001..100000000 -> {lipanampesa("2000")}
              in 100000000..1000000000 -> {lipanampesa("3000")}

         }*/


                      btn_post.isEnabled = false
                      btn_side.isEnabled = false
                      btn_back.isEnabled = false
                      btn_front.isEnabled = false

                      Toast.makeText(applicationContext, "Uploading...Wait!", Toast.LENGTH_LONG)
                              .show()


                      val kazi = CoroutineScope(Dispatchers.Main).launch {
                          withContext(Dispatchers.Main) {
                              front = Compressor.compress(applicationContext, photoFile) {
                                  resolution(1280, 720)
                                  quality(40)
                                  if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                                      format(Bitmap.CompressFormat.WEBP)
                                  }
                              }
                              val storageReference =
                                      FirebaseStorage.getInstance().getReference().child(
                                              "front/${
                                                  (Uri.fromFile(
                                                          front
                                                  )).lastPathSegment
                                              }"
                                      )
                              val uploadTask = storageReference!!.putFile(Uri.fromFile(front))
                              val task = uploadTask.continueWithTask { task ->
                                  if (!task.isSuccessful) {
                                      Toast.makeText(
                                              applicationContext,
                                              "upload failed",
                                              Toast.LENGTH_SHORT
                                      ).show()
                                  }
                                  storageReference!!.downloadUrl
                              }.addOnCompleteListener { task ->
                                  val downloadUri = task.result
                                  url = downloadUri!!


                              }

                          }

                      }


                      val job = CoroutineScope(Dispatchers.Main).launch {
                          withContext(Dispatchers.Main) {
                              kazi.join()
                              back = Compressor.compress(applicationContext, photoFil) {
                                  resolution(1280, 720)
                                  quality(40)
                                  format(Bitmap.CompressFormat.WEBP)
                              }
                              val storageRefer =
                                      FirebaseStorage.getInstance().getReference().child(
                                              "back/${
                                                  (Uri.fromFile(
                                                          back
                                                  )).lastPathSegment
                                              }"
                                      )
                              val uploadTas = storageRefer!!.putFile(Uri.fromFile(back))
                              val tas = uploadTas.continueWithTask { tas ->
                                  if (!tas.isSuccessful) {
                                      Toast.makeText(
                                              applicationContext,
                                              "upload failed",
                                              Toast.LENGTH_SHORT
                                      ).show()
                                  }
                                  storageRefer!!.downloadUrl
                              }.addOnCompleteListener { tas ->
                                  val downloadUr = tas.result
                                  url2 = downloadUr!!


                              }

                          }

                      }


                      val labour = CoroutineScope(Dispatchers.Main).launch {
                          withContext(Dispatchers.Main) {
                              job.join()
                              side = Compressor.compress(applicationContext, photoF) {
                                  resolution(1280, 720)
                                  quality(40)
                                  format(Bitmap.CompressFormat.WEBP)
                              }
                              val storageRef =
                                      FirebaseStorage.getInstance().getReference().child(
                                              "side/${
                                                  (Uri.fromFile(
                                                          side
                                                  )).lastPathSegment
                                              }"
                                      )
                              val uploadTa = storageRef!!.putFile(Uri.fromFile(side))
                              val ta = uploadTa.continueWithTask { ta ->
                                  if (!ta.isSuccessful) {
                                      Toast.makeText(
                                              applicationContext,
                                              "upload failed",
                                              Toast.LENGTH_SHORT
                                      ).show()
                                  }
                                  storageRef!!.downloadUrl
                              }.addOnCompleteListener { ta ->
                                  val downloadU = ta.result
                                  url3 = downloadU!!

                                  val ref = FirebaseDatabase.getInstance().getReference("Items")
                                  val itemId = ref.push().key

                                  val calendar = Calendar.getInstance()
                                  val postDat = calendar.time
                                  calendar.add(Calendar.DAY_OF_WEEK, 7)
                                  val deleteDate = calendar.time
                                  val deleteTime: Long = calendar.timeInMillis

                                  val item = Item(
                                      url.toString(),
                                      url2.toString(),
                                      url3.toString(),
                                      item_name.text.toString(),
                                      Phone.text.toString(),
                                      item_description.text.toString(),
                                      item_price.text.toString(),
                                      item_location.text.toString(),
                                      postDat.toString(),
                                      deleteDate.toString(),
                                      deleteTime.toString(),
                                      "${item_name.text.toString()} in ${item_location.text.toString()}"
                                  )


                                  ref.child(itemId!!).setValue(item).addOnCompleteListener {
                                      Toast.makeText(
                                          applicationContext,
                                          "item posted successfully",
                                          Toast.LENGTH_LONG
                                      ).show()

                                      picha.setImageResource(R.drawable.finepic)
                                      picha_1.setImageResource(R.drawable.finepic)
                                      picha_2.setImageResource(R.drawable.finepic)
                                      btn_post.isEnabled = true
                                      btn_side.isEnabled = true
                                      btn_back.isEnabled = true
                                      btn_front.isEnabled = true
                                      item_name.text.clear()
                                      item_location.text.clear()
                                      item_description.text.clear()
                                      item_price.text.clear()
                                      Phone.text.clear()


                                      val intent = Intent(applicationContext,ShowActivity::class.java)
                                      startActivity(intent)

                                  }

                                 /* if(callbackResponse.ResultCode==0){
                                  sendSuccessful("1", phoneNumber,Calendar.getInstance().time.toString(),"")
                                  } else{
                                      sendFailed("You haven't paid appreciation fee")
                                  }*/

                                  //sendSuccessful("1", phoneNumber,Calendar.getInstance().time.toString(),"")
                                  //sendFailed("You haven't paid appreciation fee")



                              }
                          }

                      }


                  }

              }

             /* val handler= Handler()
              handler.postDelayed({
                  sendFailed("You haven't paid appreciation fee")
              },62000)*/

          }




          btn_charge.setOnClickListener {
              val intent = Intent(this, MainActivity3::class.java)
              startActivity(intent)

          }

          btn_back.setOnClickListener {

              val takePicIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
              if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO) {
                  photoFil = getphotoFile(FILE_NAME_1)
              }
              val fileProvider =
                      FileProvider.getUriForFile(this, "com.example.imageshare.fileprovider", photoFil)
              takePicIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

              if (takePicIntent.resolveActivity(this.packageManager) != null) {
                  startActivityForResult(takePicIntent, REQUEST_CODE_1)
              } else {
                  Toast.makeText(this, "cant open camera", Toast.LENGTH_LONG).show()
              }
          }
          btn_side.setOnClickListener {

              val takePIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
              if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO) {
                  photoF = getphotoFile(FILE_NAME_2)
              }
              val fileProvider =
                      FileProvider.getUriForFile(this, "com.example.imageshare.fileprovider", photoF)
              takePIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

              if (takePIntent.resolveActivity(this.packageManager) != null) {
                  startActivityForResult(takePIntent, REQUEST_CODE_2)
              } else {
                  Toast.makeText(this, "cant open camera", Toast.LENGTH_LONG).show()

              }
          }


      }


      @RequiresApi(Build.VERSION_CODES.FROYO)
      private fun getphotoFile(filename: String): File {
          val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
          return File.createTempFile(filename, ".jpg", storageDirectory)

      }


      fun lipanampesa(pay: String) {
          val phoneNumber = Phone.text.trim().toString().trim()
          val lnmExpress = LNMExpress(
                "174379",
              "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",
              TransactionType.CustomerPayBillOnline,
              pay,
              phoneNumber,
              "174379",
              phoneNumber,
              "https://us-central1-imageshare-b21d9.cloudfunctions.net/function-5/myCallbackUrl",
              "SELLby",
              "Goods Payment"
          )

          daraja.requestMPESAExpress(lnmExpress,
                  object : DarajaListener<LNMResult> {
                      override fun onResult(lnmResult: LNMResult) {

                          FirebaseMessaging.getInstance()
                              .subscribeToTopic(lnmResult.CheckoutRequestID.toString())
                          Toast.makeText(
                                  this@MainActivity,
                                  "Response here ${lnmResult.ResponseDescription}",
                                  Toast.LENGTH_SHORT
                          ).show()


                      }

                      override fun onError(error: String) {

                          Toast.makeText(
                                  this@MainActivity,
                                  "Error here $error",
                                  Toast.LENGTH_SHORT
                          ).show()

                          sendFailed("Please check your internet connection")


                      }
                  }
          )


      }

      override fun sendSuccessful(amount: String, phone: String, date: String, receipt: String) {


          runOnUiThread {
              Toast.makeText(
                      this, "Payment Successful\n" +
                      "Receipt: $receipt\n" +
                      "Date: $date\n" +
                      "Phone: $phone\n" +
                      "Amount: $amount", Toast.LENGTH_LONG
              ).show()



              Toast.makeText(applicationContext, "Posting Posting... Wait!", Toast.LENGTH_LONG)
                      .show()

             /* val ref = FirebaseDatabase.getInstance().getReference("Items")
              val itemId = ref.push().key

              val calendar = Calendar.getInstance()
              val postDat = calendar.time
              calendar.add(Calendar.DAY_OF_WEEK, 7)
              val deleteDate = calendar.time
              val deleteTime: Long = calendar.timeInMillis

              val item = Item(
                      url.toString(),
                      url2.toString(),
                      url3.toString(),
                      item_name.text.toString(),
                      Phone.text.toString(),
                      item_description.text.toString(),
                      item_price.text.toString(),
                      item_location.text.toString(),
                      postDat.toString(),
                      deleteDate.toString(),
                      deleteTime.toString(),
                      "${item_name.text.toString()} in ${item_location.text.toString()}"
              )


              ref.child(itemId!!).setValue(item).addOnCompleteListener {
                  Toast.makeText(
                          applicationContext,
                          "item posted successfully",
                          Toast.LENGTH_LONG
                  ).show()

                  picha.setImageResource(R.drawable.finepic)
                  picha_1.setImageResource(R.drawable.finepic)
                  picha_2.setImageResource(R.drawable.finepic)
                  btn_post.isEnabled = true
                  btn_side.isEnabled = true
                  btn_back.isEnabled = true
                  btn_front.isEnabled = true
                  item_name.text.clear()
                  item_location.text.clear()
                  item_description.text.clear()
                  item_price.text.clear()
                 Phone.text.clear()


                  val intent = Intent(applicationContext,ShowActivity::class.java)
                  startActivity(intent)

              }*/
          }
      }

          override fun sendFailed(reason: String) {

              runOnUiThread {
                  Toast.makeText(
                          this, "Payment Failed\n" +
                          "Failed: $reason", Toast.LENGTH_LONG
                  ).show()
                  item_name.text.clear()
                  item_location.text.clear()
                  item_description.text.clear()
                  item_price.text.clear()
                  Phone.text.clear()
                  picha.setImageResource(0)
                  picha_1.setImageResource(0)
                  picha_2.setImageResource(0)
                 // picha_2.setImageResource(android.R.color.transparent)
                  val intent = Intent(applicationContext,MainActivity6::class.java)
                  startActivity(intent)
                  finish()
              }

          }


          override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
              if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                  //val takenBitmap = data?.extras?.get("data") as Bitmap
                  val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
                  picha.setImageBitmap(takenImage)


              } else if (requestCode == REQUEST_CODE_1 && resultCode == Activity.RESULT_OK) {
                  // val taken = data?.extras?.get("data") as Bitmap
                  val taken = BitmapFactory.decodeFile(photoFil.absolutePath)
                  picha_1.setImageBitmap(taken)

              } else if (requestCode == REQUEST_CODE_2 && resultCode == Activity.RESULT_OK) {
                  // val take = data?.extras?.get("data") as Bitmap
                  val take = BitmapFactory.decodeFile(photoF.absolutePath)
                  picha_2.setImageBitmap(take)
                  vf.flipInterval = 3000
                  this.vf.startFlipping()
              } else {

                  super.onActivityResult(requestCode, resultCode, data)
              }
          }


  }










