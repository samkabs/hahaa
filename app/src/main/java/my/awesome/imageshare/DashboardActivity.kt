package my.awesome.imageshare

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        supportActionBar?.title = "Buy anything -> Sell everything"


        home.setOnClickListener{
            val intent= Intent(this,ShowActivity::class.java)
            startActivity(intent)
        }

        phones.setOnClickListener{
            val intent= Intent(this,PhoneActivity::class.java)
            startActivity(intent)
        }

        Furniture.setOnClickListener{
            val intent= Intent(this,FurnitureActivity::class.java)
            startActivity(intent)
        }
        Stationery.setOnClickListener{
            val intent= Intent(this,BooksActivity::class.java)
            startActivity(intent)
        }

        Electronics.setOnClickListener{
            val intent= Intent(this,ElectronicsActivity::class.java)
            startActivity(intent)
        }

        Fashion.setOnClickListener{
            val intent= Intent(this,FashionActivity::class.java)
            startActivity(intent)
        }

        cars.setOnClickListener{
            val intent= Intent(this,CarsAndBikesActivity::class.java)
            startActivity(intent)
        }

        Rentals.setOnClickListener{
            val intent= Intent(this,RentalsAndHotelsActivity::class.java)
            startActivity(intent)
        }
        Cakes.setOnClickListener{
            val intent= Intent(this,CakesAndEventsActivity::class.java)
            startActivity(intent)
        }

        Animals.setOnClickListener{
            val intent= Intent(this,AnimalsActivity::class.java)
            startActivity(intent)
        }

    }
}
