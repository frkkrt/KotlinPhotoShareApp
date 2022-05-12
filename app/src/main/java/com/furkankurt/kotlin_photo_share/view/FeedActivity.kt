package com.furkankurt.kotlin_photo_share.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.furkankurt.kotlin_photo_share.model.Post
import com.furkankurt.kotlin_photo_share.R
import com.furkankurt.kotlin_photo_share.adapter.HaberRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_feed.*
import java.util.ArrayList

class FeedActivity : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth
    private lateinit var database:FirebaseFirestore
    private lateinit var recyclerAdapter: HaberRecyclerAdapter
    var postListesi=ArrayList<Post>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        auth=FirebaseAuth.getInstance()
        database= FirebaseFirestore.getInstance()
        verileriAl()

        var layoutManager=LinearLayoutManager(this)
        recyclerview.layoutManager=layoutManager
        recyclerAdapter= HaberRecyclerAdapter(postListesi)
        recyclerview.adapter=recyclerAdapter

    }
    fun verileriAl(){
        database.collection("Post").orderBy("tarih",
            Query.Direction.DESCENDING).addSnapshotListener { snapshot, exception ->
            if(exception!=null)
            {
                Toast.makeText(this,exception.localizedMessage,Toast.LENGTH_LONG).show()
            }
            else
            {
                if(snapshot!=null)
                {
                    if(!snapshot.isEmpty)
                    {

                        val documents=snapshot.documents

                        postListesi.clear()

                        for(document in documents)
                        {
                            //casting işlemi=anyden stringe çevirme
                            val kullaniciEmail= document.get("kullaniciemail") as String
                            val kullaniciYorumu=document.get("kullaniciyorum") as String
                            val gorselUrl=document.get("gorselurl") as String

                            val indirilenPost= Post(kullaniciEmail,kullaniciYorumu,gorselUrl)
                            postListesi.add(indirilenPost)

                        }

                        recyclerAdapter.notifyDataSetChanged()


                    }
                }
            }

        }

    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater=menuInflater
        menuInflater.inflate(R.menu.select_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId== R.id.fotograf_share)
        {
            //fotograf paylasma aktivitesine gidilecek
            val intent= Intent(this, Photo_Share_Activity::class.java)
            startActivity(intent)
        }
        else if(item.itemId== R.id.cikis_yap)
        {
            auth.signOut()
            val intent= Intent(this, UserActivity::class.java)
            startActivity(intent)
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}