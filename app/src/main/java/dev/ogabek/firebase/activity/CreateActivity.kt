package dev.ogabek.firebase.activity

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter
import dev.ogabek.firebase.R
import dev.ogabek.firebase.manager.DatabaseHandler
import dev.ogabek.firebase.manager.DatabaseManager
import dev.ogabek.firebase.manager.StorageHandler
import dev.ogabek.firebase.manager.StorageManager
import dev.ogabek.firebase.model.Post
import java.lang.Exception

class CreateActivity : BaseActivity() {

    lateinit var iv_photo: ImageView
    var pickedPhoto: Uri? = null
    var allPhotos: ArrayList<Uri> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        initViews()

    }

    private fun initViews() {
        val iv_close = findViewById<ImageView>(R.id.iv_close)
        val et_title = findViewById<EditText>(R.id.et_title)
        val et_body = findViewById<EditText>(R.id.et_body)
        val b_create = findViewById<Button>(R.id.b_create)

        iv_photo = findViewById(R.id.iv_photo)
        val iv_camera: ImageView = findViewById(R.id.iv_camera)

        iv_camera.setOnClickListener {
            pickUserPhoto()
        }

        iv_close.setOnClickListener {
            finish()
        }
        b_create.setOnClickListener {
            val title = et_title.text.toString().trim()
            val body = et_body.text.toString().trim()
            val post = Post(title, body)
            storePost(post)
        }
    }


    private fun storePost(post: Post) {
        Log.d("Post", "storePost: $pickedPhoto")
        if (pickedPhoto != null) {
            storeDatabase(post)
        } else {
            storeStorage(post)
        }
    }

    private fun storeDatabase(post: Post) {
        showLoading(this)
        StorageManager.uploadPhotos(pickedPhoto!!, object : StorageHandler {
            override fun onSuccess(imgUrl: String) {
                post.img = imgUrl
                Log.d("Post", "onSuccess: $imgUrl")
                storeStorage(post)
            }

            override fun onError(exception: Exception?) {
                Log.d("Post", "onError: ${exception?.message}")
                storeDatabase(post)
            }

        })
    }

    private fun storeStorage(post: Post) {
        DatabaseManager.storePost(post, object : DatabaseHandler {
            override fun onSuccess(post: Post?, posts: ArrayList<Post>) {
                Log.d("Post", "Uploaded")
                dismissLoading()
                finishIntent()
            }

            override fun onError() {
                dismissLoading()
                Log.d("Post", "onError: Failed")
            }

        })
    }


    fun finishIntent() {
        val returnIntent = intent
        setResult(RESULT_OK, returnIntent)
        finish()
    }

    fun pickUserPhoto() {
        FishBun.with(this)
            .setImageAdapter(GlideAdapter())
            .setMaxCount(1)
            .setMinCount(1)
            .setSelectedImages(allPhotos)
            .startAlbumWithActivityResultCallback(photoLauncher)
    }

    val photoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            allPhotos = it.data?.getParcelableArrayListExtra(FishBun.INTENT_PATH) ?: arrayListOf()
            pickedPhoto = allPhotos[0]
            iv_photo.setImageURI(pickedPhoto)
        }
    }

}