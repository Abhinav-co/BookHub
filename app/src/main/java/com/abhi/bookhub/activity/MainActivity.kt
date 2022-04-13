package com.abhi.bookhub.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.abhi.bookhub.fragment.AboutAppFragment
import com.abhi.bookhub.fragment.Favourites
import com.abhi.bookhub.fragment.ProfileFragment
import com.abhi.bookhub.R
import com.google.android.material.navigation.NavigationView
import com.abhi.bookhub.fragment.Dashboard

class MainActivity : AppCompatActivity() {
    lateinit var coordinatorLayout:CoordinatorLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var frame:FrameLayout
    lateinit var navigationView:NavigationView
    lateinit var drawerLayout:DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar=findViewById(R.id.toolbar)
        coordinatorLayout=findViewById(R.id.coordinatorL)
        frame=findViewById(R.id.frame)
        navigationView=findViewById(R.id.navigationView)
        drawerLayout=findViewById(R.id.drawerLayout)
        var previousitem: MenuItem? = null
        setUpToolbar()
        dashboard()
        val hamburgerIcon=ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )

        drawerLayout.addDrawerListener(hamburgerIcon)
        hamburgerIcon.syncState()
        navigationView.setNavigationItemSelectedListener {
            if(previousitem!=null){
                previousitem?.isChecked=false
            }
            it.isCheckable=true
            it.isChecked=true
            previousitem=it
            when(it.itemId){
                R.id.dashboard -> {
                   dashboard()
                    drawerLayout.closeDrawers()
                }
                R.id.favourites ->{
                    Toast.makeText(this,"Favourites",Toast.LENGTH_SHORT).show()
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            Favourites()
                        )

                        .commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title="Favourites"
                }
                R.id.aboutApp ->{
                    Toast.makeText(this,"about_App",Toast.LENGTH_SHORT).show()
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            AboutAppFragment()
                        )

                        .commit()

                    supportActionBar?.title="About App"

                    drawerLayout.closeDrawers()
                }
                R.id.profile ->{
                    Toast.makeText(this,"Profile",Toast.LENGTH_SHORT).show()
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            ProfileFragment()
                        )

                        .commit()
                    supportActionBar?.title="profile"
                    drawerLayout.closeDrawers()
                }
            }
            return@setNavigationItemSelectedListener true
        }

    }



    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title="Book Hub"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        if (id==android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }
    fun dashboard(){
        Toast.makeText(this,"Dashboard",Toast.LENGTH_SHORT).show()
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, Dashboard())

            .commit()
        supportActionBar?.title="Dashboard"
        navigationView.setCheckedItem(R.id.dashboard)

    }

    override fun onBackPressed() {
        val frag=supportFragmentManager.findFragmentById(R.id.frame)
        when(frag){
            !is Dashboard -> dashboard()
            else -> super.onBackPressed()
        }
    }
}