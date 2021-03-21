package com.francisco.geovane.marcello.felipe.projetofinalandroid.main.activity.edit

import android.annotation.SuppressLint

import android.content.Intent
import android.os.Bundle
import android.text.InputType

import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

import com.bumptech.glide.Glide
import com.francisco.geovane.marcello.felipe.projetofinalandroid.R
import com.francisco.geovane.marcello.felipe.projetofinalandroid.main.model.LocationObj
import com.francisco.geovane.marcello.felipe.projetofinalandroid.main.service.FirebasePlaceService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


@SuppressLint("UseSwitchCompatOrMaterialCode")
class EditPlaceActivity : AppCompatActivity() {

    private lateinit var etPlaceImage: ImageView
    private lateinit var etPlaceName: EditText
    private lateinit var etPlaceAddress: EditText
    private lateinit var etPlaceDescription: EditText
    private lateinit var etPlacePhone: EditText
    private lateinit var etPlaceLat: EditText
    private lateinit var etPlaceLng: EditText
    private lateinit var etPlaceFlavor: EditText
    private lateinit var etPlaceVisited: CheckBox
    private lateinit var auth: FirebaseAuth

    private var id: String? = null
    private var name: String? = null
    private var phoneNumber:String? = null
    private var address: String? = null
    private var lat: String? = null
    private var lng: String? = null
    private var flavor: String? = null
    private val firebasePlaceService = FirebasePlaceService()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        supportActionBar?.hide()
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        etPlaceImage = findViewById(R.id.etPlaceImage)
        etPlaceName = findViewById(R.id.etPlaceName)
        etPlaceAddress = findViewById(R.id.etPlaceAddress)
        etPlaceDescription = findViewById(R.id.etPlaceDescription)
        etPlacePhone = findViewById(R.id.etPlacePhone)
        etPlaceVisited = findViewById(R.id.etPlaceVisited)
        etPlaceLat = findViewById(R.id.etPlaceLat)
        etPlaceLng= findViewById(R.id.etPlaceLng)
        etPlaceFlavor = findViewById(R.id.etPlaceFlavor)

        etPlaceLat.inputType = InputType.TYPE_NULL
        etPlaceLng.inputType = InputType.TYPE_NULL
        etPlaceFlavor.inputType = InputType.TYPE_NULL

        // Firebase
        auth = Firebase.auth

        val action = intent.getStringExtra("action")
        if(action != null) {
            toolbar.title = resources?.getString(R.string.new_place_title)
            setCreationFields(intent)
        } else {
            toolbar.title = resources?.getString(R.string.edit_place_title)
            setDataFields()
        }

        setSupportActionBar(toolbar)

        val saveButton = findViewById<Button>(R.id.btnSave)
        saveButton.setOnClickListener {
            val replyIntent = Intent()
            if (etPlaceName.text.toString().trim().isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    R.string.forbidden_empty,
                    Toast.LENGTH_LONG
                ).show()
            } else {
                val place = LocationObj(
                    id.toString(),
                    etPlaceName.text.toString(),
                    etPlaceDescription.text.toString(),
                    etPlaceLat.text.toString(),
                    etPlaceLng.text.toString(),
                    etPlaceVisited.isChecked,
                    etPlacePhone.text.toString(),
                    etPlaceAddress.text.toString(),
                    "",
                    etPlaceFlavor.text.toString(),
                    auth.currentUser?.uid
                )
                if ( action != null) {
                    firebasePlaceService.saveNewLocation(place)
                } else {
                    val id = intent.getStringExtra("id")
                    firebasePlaceService.saveEditedLocation(id, place)
                }
                setResult(RESULT_OK, replyIntent)
                finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_close_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.clear -> {
            finish()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun setDataFields() {
        val originalImage = intent.getStringExtra("image")
        val originalName = intent.getStringExtra("name")
        val originalAddress = intent.getStringExtra("address")
        val originalDescription = intent.getStringExtra("description")
        val originalPhone = intent.getStringExtra("phone")
        val originalVisit = intent.getStringExtra("isVisited").toBoolean()
        lat = intent.getStringExtra("lat")
        lng = intent.getStringExtra("lng")
        flavor = intent.getStringExtra("flavor")

        Glide.with(applicationContext).load(originalImage).into(etPlaceImage)
        etPlaceName.setText(originalName)
        etPlaceAddress.setText(originalAddress)
        etPlaceDescription.setText(originalDescription)
        etPlacePhone.setText(originalPhone)
        etPlaceVisited.isChecked = originalVisit
        etPlaceLat.setText(lat)
        etPlaceLng.setText(lng)
        etPlaceFlavor.setText(flavor)
    }

    private fun setCreationFields(intent: Intent) {
        id = intent.getStringExtra("id")
        name = intent.getStringExtra("name")
        phoneNumber = intent.getStringExtra("phoneNumber")
        address = intent.getStringExtra("address")
        lat = intent.getStringExtra("lat")
        lng = intent.getStringExtra("lng")
        flavor = intent.getStringExtra("flavor")

        etPlaceName.setText(name)
        etPlacePhone.setText(phoneNumber)
        etPlaceAddress.setText(address)
        etPlaceLat.setText(lat)
        etPlaceLng.setText(lng)
        etPlaceFlavor.setText(flavor)
    }
}