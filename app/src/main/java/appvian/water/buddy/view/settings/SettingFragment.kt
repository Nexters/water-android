package appvian.water.buddy.view.settings

import android.content.Intent
import android.os.Bundle
import android.util.Log
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


        //참고 https://www.thetopsites.net/article/51790868.shtml
        settingviewModel.nameLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            fragmentSettingBinding.txtSettingName.text = it
            Log.d("TAG",it)
            //fragmentSettingBinding.executePendingBindings()
        })

        fragmentSettingBinding.viewModel = settingviewModel
        return fragmentSettingBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUIClick()

    }
    private fun setUIClick(){
        img_setting_edit_profile.setOnClickListener{
            var intent = Intent(context, ProfileEditActivity::class.java)
            startActivity(intent)
        }
        layout_setting_target_amount.setOnClickListener{
            var intent = Intent(context, TargetAmountSettingActivity::class.java)
            startActivity(intent)
        }
        layout_setting_favorite.setOnClickListener{
            var intent = Intent(context, FavoriteDrinkSettingActivity::class.java)
            startActivity(intent)
        }
        layout_setting_alarm.setOnClickListener{
            var intent = Intent(context, AlarmSettingActivity::class.java)
            startActivity(intent)
        }
    }


}
