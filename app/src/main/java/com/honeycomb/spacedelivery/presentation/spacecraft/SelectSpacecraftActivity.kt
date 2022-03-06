package com.honeycomb.spacedelivery.presentation.spacecraft

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.SavedStateViewModelFactory
import com.honeycomb.spacedelivery.R
import com.honeycomb.spacedelivery.databinding.ActivitySelectspacecraftBinding
import com.honeycomb.spacedelivery.presentation.spacecraft.viewmodel.SelectSpacecraftViewModel
import com.honeycomb.spacedelivery.utils.UIHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectSpacecraftActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectspacecraftBinding
    private val viewModel: SelectSpacecraftViewModel by viewModels{
        SavedStateViewModelFactory(application, this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectspacecraftBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.initilazeHasMap(binding.durabilitySlider,binding.speedSlider,binding.capacitySlider)
        viewModel.initilazeSliderValueTo(binding.durabilitySlider,binding.speedSlider,binding.capacitySlider)

        binding.spacecraftNameEdittext.doAfterTextChanged {
            viewModel.spacecraftNameLiveData = it.toString()
        }
        detectChangeValueSlider()
        travelButtonClik()
    }
    fun detectChangeValueSlider(){
        binding.durabilitySlider.addOnChangeListener { slider, value, fromUser ->
            binding.pointsDistributedValueTextview.text = viewModel.setDurabilityLiveData(slider)
        }
        binding.speedSlider.addOnChangeListener { slider, value, fromUser ->
            binding.pointsDistributedValueTextview.text = viewModel.setSpeedLiveData(slider)
        }
        binding.capacitySlider.addOnChangeListener { slider, value, fromUser ->
            binding.pointsDistributedValueTextview.text = viewModel.setCapacityLiveData(slider)
        }
    }
    fun travelButtonClik(){
        binding.continueButton.setOnClickListener {
            if(viewModel.durabilityLiveData == 0f ||
                viewModel.speedLiveData == 0f ||
                viewModel.capacityLiveData == 0f ){
                UIHelper.snackHelper(it,resources.getString(R.string.mustValueMinOne))
            }
            else if(viewModel.spacecraftNameLiveData.isEmpty()){
                UIHelper.snackHelper(it,resources.getString(R.string.mustSpaceNameEnterValue))
            }
            else if(viewModel.pointsDistributedLiveData.value != "0.0"){
                UIHelper.snackHelper(it,resources.getString(R.string.mustUseUnitsValue))
            }
            else{
                viewModel.letsPlayGame(this)
            }
        }
    }
}