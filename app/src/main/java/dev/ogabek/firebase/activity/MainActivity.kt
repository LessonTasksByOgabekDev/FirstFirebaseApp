package dev.ogabek.firebase.activity

import android.os.Bundle
import android.widget.TextView
import dev.ogabek.firebase.R
import dev.ogabek.firebase.manager.AuthManager

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

    }

    private fun initViews() {
        findViewById<TextView>(R.id.signOut).setOnClickListener {
            AuthManager.signOut()
            callSignInActivity()
        }
    }
}