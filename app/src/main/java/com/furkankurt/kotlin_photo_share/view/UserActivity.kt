package com.furkankurt.kotlin_photo_share.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.furkankurt.kotlin_photo_share.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class UserActivity : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth=FirebaseAuth.getInstance()

        val guncelkullanici=auth.currentUser
        if(guncelkullanici!=null)
        {
            val intent=Intent(this, FeedActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
    fun giris_yap(view: View)
    {
        auth.signInWithEmailAndPassword(emailText.text.toString(),PasswordText.text.toString()).addOnCompleteListener { task->
            if(task.isSuccessful)
            {
                val guncelkullanici=auth.currentUser?.email.toString()
                Toast.makeText(this,"Hoşgeldin:${guncelkullanici}",Toast.LENGTH_LONG).show()

                val intent=Intent(this, FeedActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener {exception->
            Toast.makeText(this,exception.localizedMessage,Toast.LENGTH_LONG).show()
        }

    }
    fun kayit_ol(view:View)
    {
        val email=emailText.text.toString()
        val password=PasswordText.text.toString()

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task->
        //asenkron çalışır.
            if(task.isSuccessful)
            {
                val intent= Intent(applicationContext, FeedActivity::class.java)
                startActivity(intent)

                finish()
            }
        }.addOnFailureListener { exception ->
        Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()

        }


    }
}