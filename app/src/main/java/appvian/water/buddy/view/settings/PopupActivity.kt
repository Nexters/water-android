package appvian.water.buddy.view.settings

import android.app.Activity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import appvian.water.buddy.R
import kotlinx.android.synthetic.main.activity_profile_edit.*
import kotlinx.android.synthetic.main.popup_setting.*

class PopupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE) //타이틀바없애기
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.popup_setting)

        var message = intent.getStringExtra("Message")
        txt_top.text = message
        btn_ok.setOnClickListener {
            onBackPressed()
        }
    }

}