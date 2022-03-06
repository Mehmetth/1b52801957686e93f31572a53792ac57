package com.honeycomb.spacedelivery.presentation.spacecraft.viewmodel

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.slider.Slider
import com.honeycomb.spacedelivery.presentation.MainActivity
import com.honeycomb.spacedelivery.presentation.spacecraft.dto.SpaceCraftModel
import com.honeycomb.spacedelivery.utils.Constant

class SelectSpacecraftViewModel :ViewModel() {

    var spacecraftNameLiveData = ""
    val pointsDistributedLiveData = MutableLiveData<String>()

    private var sliderHashMap : HashMap<Slider, Float> = HashMap<Slider, Float> ()

    var durabilityLiveData: Float = Constant.START_STEP
    var speedLiveData: Float = Constant.START_STEP
    var capacityLiveData: Float = Constant.START_STEP

    fun setDurabilityLiveData(slider: Slider): String{
        durabilityLiveData = slider.value
        return  sliderValueControlAndOperaiton(slider)
    }
    fun setSpeedLiveData(slider: Slider): String{
        speedLiveData = slider.value
        return sliderValueControlAndOperaiton(slider)
    }
    fun setCapacityLiveData(slider: Slider): String{
        capacityLiveData = slider.value
        return sliderValueControlAndOperaiton(slider)
    }
    fun initilazeHasMap(sliderDurability: Slider, sliderSpeed: Slider,sliderCapacity: Slider){
        sliderHashMap.putAll(mapOf(sliderDurability to Constant.START_STEP,
            sliderSpeed to Constant.START_STEP,
            sliderCapacity to Constant.START_STEP))
    }
    fun initilazeSliderValueTo(sliderDurability: Slider, sliderSpeed: Slider,sliderCapacity: Slider){
        sliderDurability.valueTo = (Constant.MAX_POINT - Constant.SLIDER_COUNT + 1).toFloat()
        sliderSpeed.valueTo = (Constant.MAX_POINT - Constant.SLIDER_COUNT + 1).toFloat()
        sliderCapacity.valueTo = (Constant.MAX_POINT - Constant.SLIDER_COUNT + 1).toFloat()
    }
    fun sliderValueControlAndOperaiton(slider: Slider): String{
        var maxValue = 0f
        var remainder = 0f

        for(itemSlider in sliderHashMap) {
            if(itemSlider.key != slider){
                maxValue += itemSlider.value
            }
        }
        remainder = Constant.MAX_POINT - maxValue

        if(slider.value > remainder){
            slider.value = remainder
        }

        sliderHashMap[slider] = slider.value
        maxValue += slider.value

        pointsDistributedLiveData.value = (Constant.MAX_POINT - maxValue).toString()
        return pointsDistributedLiveData.value!!
    }
    fun letsPlayGame(activity: Activity){
        val product = SpaceCraftModel(spacecraftNameLiveData,
            durabilityLiveData,
            speedLiveData,
            capacityLiveData)

        val intent = Intent(activity, MainActivity::class.java)
        intent.putExtra(Constant.SEND_PARSE_DATA, product)
        activity.startActivity(intent)
    }
}