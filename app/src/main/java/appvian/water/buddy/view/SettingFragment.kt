package appvian.water.buddy.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import appvian.water.buddy.R
import appvian.water.buddy.databinding.FragmentSettingBinding
import appvian.water.buddy.viewmodel.SettingViewModel
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingFragment : Fragment() {
    private lateinit var fragmentSettingBinding: FragmentSettingBinding
    private lateinit var settingviewModel: SettingViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingviewModel = ViewModelProvider(this).get(SettingViewModel::class.java)
        fragmentSettingBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false)

        //테스트 코드 . 초기에 목표량 설정할테니까 바로 get하면 될 듯
        settingviewModel.setSharedPreferences("target_amount", "변경 전 ")
        //참고 https://www.thetopsites.net/article/51790868.shtml
        settingviewModel.getSharedPrefs()!!.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            txt_test.text = it
            Log.d("TAG",it)
            fragmentSettingBinding.executePendingBindings()
        })

        return fragmentSettingBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //테스트 코드
        btn_prefs_change.setOnClickListener {
            settingviewModel.setSharedPreferences("target_amount", "변경 후 ")
        }
    }


}
