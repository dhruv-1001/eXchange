package com.exchange.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.exchange.R
import com.exchange.databinding.ActivitySellBinding
import com.exchange.repository.FirebaseRepository
import com.exchange.viewmodel.SellActivityViewModel
import com.exchange.viewmodel.SellActivityViewModelFactory
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import java.util.*


class SellActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySellBinding
    private lateinit var viewModel: SellActivityViewModel
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    private val PERMISSION_ID = 1010


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sell)
        supportActionBar?.hide()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val firebaseRepo = FirebaseRepository()
        val viewModelFactory = SellActivityViewModelFactory(firebaseRepo)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SellActivityViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.missingData.observe(this,{
            Snackbar.make(findViewById(android.R.id.content), viewModel.errorMessage.value.toString(), Snackbar.LENGTH_LONG).show()
        })

    }

    override fun onResume() {
        super.onResume()

        viewModel.galleryIntent.observe(this, {
            if (it == true) {
                viewModel.galleryIntent.value = false
                fetchImages()
            }
        })

        viewModel.finishActivity.observe(this, {
            if (it == true){
                viewModel.finishActivity.value = false
                this.finish()
            }
        })

        viewModel.locationIntent.observe(this, {
            if (it == true){
                viewModel.locationIntent.value = false
                fetchLocation()
            }
        })



    }

    private var galleryResultActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if (result.resultCode == Activity.RESULT_OK){
            val data: Intent? = result.data
            viewModel.updateSelectedImages(data)
        }
    }

    private fun fetchImages(){

        val galleryIntent = Intent()
        galleryIntent.type = "image/*"
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        galleryIntent.action = Intent.ACTION_GET_CONTENT
        galleryResultActivity.launch(galleryIntent)

    }




    //location fetching happens here


    private fun fetchLocation(){
        val locationIntent = Intent()
        locationIntent.action = Intent.ACTION_VIEW
        RequestPermission()

        getLastLocation()

    }

    fun CheckPermission():Boolean{

        if(
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ){
            return true
        }

        return false

    }

    fun RequestPermission(){
        //this function will allows us to tell the user to requesut the necessary permsiion if they are not garented
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }

    fun isLocationEnabled():Boolean{
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }


    fun getLastLocation(){
        if(CheckPermission()){
            if(isLocationEnabled()){
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task->
                    var location: Location? = task.result
                    if(location == null){
                        NewLocationData()
                    }else{
                        Log.d("Debug:" ,"Your Location:"+ location.longitude)
                        viewModel.locationAdress.value = "You Current Location is : Long: "+ location.longitude + " , Lat: " + location.latitude + "\n" + getCityName(location.latitude,location.longitude)
                    }
                }
            }else{
                Toast.makeText(this,"Please Turn on Your device Location", Toast.LENGTH_SHORT).show()
            }
        }else{
            RequestPermission()
        }
    }

    fun NewLocationData(){
        var locationRequest =  LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        fusedLocationProviderClient!!.requestLocationUpdates(
            locationRequest,locationCallback, Looper.myLooper()
        )
    }


    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            var lastLocation: Location = locationResult.lastLocation
            Log.d("Debug:","your last last location: "+ lastLocation.longitude.toString())
            viewModel.locationAdress.value = "You Last Location is : Long: "+ lastLocation.longitude + " , Lat: " + lastLocation.latitude + "\n" + getCityName(lastLocation.latitude,lastLocation.longitude)
        }
    }


    private fun getCityName(lat: Double,long: Double):String{
        var cityName:String = ""
        var countryName = ""
        var localAddress = ""
        val geoCoder = Geocoder(this, Locale.getDefault())
        val address = geoCoder.getFromLocation(lat,long,3)

        localAddress = address[0].adminArea
        cityName = address[0].locality
        countryName = address[0].countryName

        Log.d("Debug:", "Your City: $cityName ; your Country $countryName")
        return address[0].getAddressLine(0)+" "+cityName+" "+localAddress+" "+countryName
    }



    //location fetching ends here

}