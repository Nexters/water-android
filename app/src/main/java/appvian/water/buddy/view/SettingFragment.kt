package appvian.water.buddy.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import appvian.water.buddy.R
import appvian.water.buddy.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {
    private lateinit var fragmentSettingBinding: FragmentSettingBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentSettingBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false)
        return fragmentSettingBinding.root
    }
}