package com.udi_demo

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import org.slf4j.LoggerFactory


class MainActivity : AppCompatActivity() {
    var logginUser = ""
    val logger = LoggerFactory.getLogger(MainActivity::class.java)
    lateinit var sharedPrefs: SharedPrefs
    var APPTYPE = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
// Enable the home button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        sharedPrefs = SharedPrefs(this)
        APPTYPE = sharedPrefs.getAppType()
        logger.debug("\n\t......x${APPTYPE.uppercase()}.......")

        // SLF4J
        setContentView(R.layout.activity_main)
        val loginButton = findViewById<Button>(R.id.button)
        val emailLogsBtn = findViewById<Button>(R.id.email_logs_btn)
        val loginUser = findViewById<EditText>(R.id.login_name_et)
        val logincc = findViewById<LinearLayout>(R.id.login_cc)
        val messageText = findViewById<EditText>(R.id.editTextText2)


        when (APPTYPE) {
            GlobalConstants.APP_FLIPKART -> {
                messageText.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.flipkart_bg, null)
            }

            GlobalConstants.APP_INSTA -> {
                messageText.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.insta_bg, null)
            }

            GlobalConstants.APP_YOUTUBE -> {
                messageText.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.youtube_bg, null)
            }

            else -> {}
        }

        emailLogsBtn.setOnClickListener {
            LogFileEmailHelper.sendMailWithLogFiles(this, "Check this .log file", "Log Files-UDI")
        }
        loginButton.setOnClickListener {
            logginUser = loginUser.text.toString() + "@udi"
            if (logginUser.isEmpty()) {
                loginUser.error = "Empty"
            } else {
                logger.debug("\t..........UserName:$logginUser...........")
//                Log.i(TAG,"\n\t..........UserName:$logginUser...........")
                this.currentFocus?.let { it1 -> hideKeyboard(it1) }
                loginUser.error = null
                FirebaseAnalytics.setUser(logginUser)
                logincc.visibility = View.GONE
                messageText.visibility = View.VISIBLE
            }
        }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        super.onKeyDown(keyCode, event)
        val unicodeChar = event?.unicodeChar?.toChar()
        if (logginUser.isNotEmpty()) {
            val bundle = Bundle()
            bundle.putString("KeyCode_$keyCode", unicodeChar.toString())
//           Log.i(TAG,"Key pressed->\nKeyCode=$keyCode \n Key Character=${unicodeChar.toString()}")
            logger.debug("Key code:$keyCode, char:$unicodeChar")
//            Log.i("MainActivity", "Key code:$keyCode, char:$unicodeChar")
            FirebaseAnalytics.logEvent(FirebaseEvents.KEYBOARD_KEY_PRESS, bundle)
        }
        return true
    }
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}