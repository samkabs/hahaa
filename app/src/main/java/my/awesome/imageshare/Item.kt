package my.awesome.imageshare

data class Item(

    val front: String,
    val back:String,
    val side:String,
    val name:String,
    val phoneNo:String,
    val itemD:String,
    val cost:String,
    val location:String,
    val post_date:String,
    val delete_date:String,
    val toa:String,
    val combined:String
)
{
    constructor():this("","","","","","","","","","","","")
}
