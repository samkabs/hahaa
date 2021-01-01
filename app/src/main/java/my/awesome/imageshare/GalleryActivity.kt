package my.awesome.imageshare

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import kotlinx.android.synthetic.main.activity_gallery.*
import kotlinx.android.synthetic.main.activity_gallery.Phone
import kotlinx.android.synthetic.main.activity_gallery.item_description
import kotlinx.android.synthetic.main.activity_gallery.item_location
import kotlinx.android.synthetic.main.activity_gallery.item_name
import kotlinx.android.synthetic.main.activity_gallery.item_price
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

private lateinit var photoFile: File
private lateinit var photoFil: File
private lateinit var photoF: File


private lateinit var front: File
private lateinit var back: File
private lateinit var side: File

class GalleryActivity : AppCompatActivity() {

    lateinit var pick_1: Uri
    lateinit var pick_2: Uri
    lateinit var pick_3: Uri

    lateinit var url: Uri
    lateinit var url2: Uri
    lateinit var url3: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        supportActionBar?.title = "Pick 3 images from Gallery"

        btn_mbele.setOnClickListener{
            if(Build.VERSION.SDK_INT  >= Build.VERSION_CODES.M){
                if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                ){
                    //permission denied
                    val permissions=arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, PERMISSION_CODE)
                } else{
                    //permission already granted
                    pickImageFromGallery(1000)


                }
            }else{
                //Os is below android 6
                pickImageFromGallery(1000)

            }
        }
        btn_nyuma.setOnClickListener{
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                ){
                   //permission denied
                    val permissions=arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, PERMISSION_CODE)
                } else{
                    //permission already granted
                    pickImageFromGallery(2000)

                }
            }else{
                //Os is below android 6
                pickImageFromGallery(2000)

            }
        }

        btn_kando.setOnClickListener{
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                ){
                    //permission denied
                    val permissions=arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, PERMISSION_CODE)
                } else{
                    //permission already granted

                    pickImageFromGallery(3000)
                }
            }else{
                //Os is below android 6
                pickImageFromGallery(3000)

            }
        }
        btn_chargi.setOnClickListener{
            val intent= Intent(this, MainActivity3::class.java)
            startActivity(intent)
        }

        btn_posti.setOnClickListener {
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

                    //val phoneNumber: String = Phone.text.toString()

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


                    btn_posti.isEnabled = false
                    btn_mbele.isEnabled = false
                    btn_nyuma.isEnabled = false
                    btn_kando.isEnabled = false

                    Toast.makeText(applicationContext, "Uploading...Wait!", Toast.LENGTH_LONG)
                        .show()

                    photoFile= File((pick_1.path)!!)
                    photoFil= File((pick_2.path)!!)
                    photoF= File((pick_3.path)!!)
                    val kazi = CoroutineScope(Dispatchers.Main).launch {
                        withContext(Dispatchers.Main) {
                            front = Compressor.compress(applicationContext, photoFile) {
                                resolution(500, 375)
                                quality(70)
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                                    format(Bitmap.CompressFormat.JPEG)
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
                                resolution(500, 375)
                                quality(70)
                                format(Bitmap.CompressFormat.JPEG)
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
                                resolution(500, 375)
                                quality(70)
                                format(Bitmap.CompressFormat.JPEG)
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
                                calendar.add(Calendar.DAY_OF_MONTH, 30)
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

                                    pichat.setImageResource(R.drawable.finepic)
                                    pichat_1.setImageResource(R.drawable.finepic)
                                    pichat_2.setImageResource(R.drawable.finepic)
                                    btn_posti.isEnabled = true
                                    btn_mbele.isEnabled = true
                                    btn_nyuma.isEnabled = true
                                    btn_kando.isEnabled = true
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


        }

    }


    private fun pickImageFromGallery(picking:Int){
     val intent=Intent()
        intent.type="image/*"
        intent.action=Intent.ACTION_GET_CONTENT
        /*val fileProvider =
            FileProvider.getUriForFile(this, "my.awesome.imageshare.fileprovider", photoFile)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)*/
        startActivityForResult(intent,picking)
    }
    companion object{
       // private const val IMAGE_PICK_CODE = 1000
        private const val PERMISSION_CODE = 1001

    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISSION_CODE -> {
                if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery(1000)
                }else{
                    //permission from pop up denied
                        Toast.makeText(this,"permission denied",Toast.LENGTH_SHORT).show()

               }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode== 1000 && resultCode== Activity.RESULT_OK){
             //pick_1= data?.data!!
           // pichat.setImageURI(pick_1)
            val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
            pichat.setImageBitmap(takenImage)
        }
        else if(requestCode== 2000 && resultCode== Activity.RESULT_OK){
             //pick_2= data?.data!!
            //pichat_1.setImageURI(pick_2)
            val taken = BitmapFactory.decodeFile(photoFil.absolutePath)
            pichat_1.setImageBitmap(taken)
        }
        else if(requestCode== 3000 && resultCode== Activity.RESULT_OK){
            // pick_3= data?.data!!
            //pichat_2.setImageURI(pick_3)
            val take = BitmapFactory.decodeFile(photoF.absolutePath)
            pichat_2.setImageBitmap(take)
            vf3.flipInterval = 3000
            this.vf3.startFlipping()
        }else

        { super.onActivityResult(requestCode, resultCode, data)}
    }
}
