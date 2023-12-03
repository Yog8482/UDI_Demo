package com.udi_demo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.core.content.ContextCompat
import com.udi_demo.youtube.R

class ChooserActivity : AppCompatActivity(), OnClickListener {
    lateinit var sharedPrefs:SharedPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chooser)
        sharedPrefs = SharedPrefs(this)
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.flipkart_btn ->{
                //set flipkart view
                //go to login
                sharedPrefs.saveAppType(GlobalConstants.APP_FLIPKART)
                gotoLogin()

            }
            R.id.youtube_btn ->{
                //set youtube view
                //go to login
                sharedPrefs.saveAppType(GlobalConstants.APP_YOUTUBE)
                gotoLogin()
            }
            R.id.insta_btn ->{
                //set instagram view
                //go to login
                sharedPrefs.saveAppType(GlobalConstants.APP_INSTA)
                gotoLogin()
            }
        }
    }
    private fun gotoLogin(){
        val intent = Intent(this, MainActivity::class.java)
        intent.apply {
        //    intent.putExtra(KEY_USERNAME, item.userName)
          //  intent.putExtra(KEY_USERID, item.id)
        }
        ContextCompat.startActivity(this, intent, null)

    }
}