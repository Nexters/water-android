package appvian.water.buddy.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import appvian.water.buddy.R
import appvian.water.buddy.view.Intro.IntroActivity
import appvian.water.buddy.viewmodel.IntroViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget

class SplashActivity : AppCompatActivity() {
    private lateinit var introViewModel: IntroViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        introViewModel =
            ViewModelProvider(this).get<IntroViewModel>(IntroViewModel::class.java)

        val splash : ImageView = findViewById(R.id.main_splash)
        val gifImage = GlideDrawableImageViewTarget(splash)
        Glide.with(this).load(R.drawable.main_splash).into(gifImage)


        startLoading()
    }

    private fun startLoading() {
        val handler = Handler()
        handler.postDelayed(Runnable {
            if(introViewModel.confirm_inform()){
                startActivity(Intent(baseContext, IntroActivity::class.java))
                startActivity(intent)
            }
            else{
                startActivity(Intent(baseContext, MainActivity::class.java))
                startActivity(intent)

            }

            finish()
        }, 2000)
    }

}
