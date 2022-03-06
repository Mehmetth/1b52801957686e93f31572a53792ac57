package com.honeycomb.spacedelivery.utils

import com.honeycomb.spacedelivery.data.common.dto.CurrentDatas

class Constant {
    companion object{
        const val MAX_POINT = 15
        const val SLIDER_COUNT = 3
        const val START_STEP = 0f
        const val SEND_PARSE_DATA = "SpaceCraftModel"

        val currentDatas = CurrentDatas("Dünya",0f,0f,0f,0f,100)
    }
}