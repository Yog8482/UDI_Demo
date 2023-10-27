package com.udi_demo

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import org.slf4j.LoggerFactory


class MainActivity : AppCompatActivity() {
    var logginUser = ""
    val logger = LoggerFactory.getLogger(MainActivity::class.java);
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // SLF4J
        setContentView(R.layout.activity_main)
        val loginButton = findViewById<Button>(R.id.button)
        val emailLogsBtn = findViewById<Button>(R.id.email_logs_btn)
        val loginUser = findViewById<EditText>(R.id.login_name_et)
        val logincc = findViewById<LinearLayout>(R.id.login_cc)
        val messageText = findViewById<EditText>(R.id.editTextText2)
        emailLogsBtn.setOnClickListener {
            LogFileEmailHelper.sendMailWithLogFiles(this, "Check this .log file", "Log Files-UDI")
        }
        loginButton.setOnClickListener {
            logginUser = loginUser.text.toString()
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

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
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