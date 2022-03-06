package com.honeycomb.spacedelivery.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.honeycomb.spacedelivery.presentation.station.StationAdapter

class UIHelper {
    companion object{
        fun goNextStation(currentPosition : Int, recyclerView: RecyclerView, mAdapter : StationAdapter) :Int {
            var position = 0
            val newPos = currentPosition + 1
            if (newPos < mAdapter.getStationsSize()) {
                position =  scrollTpStation(newPos,recyclerView)
            }
            return position
        }
        fun goPreviousStation(currentPosition : Int, recyclerView: RecyclerView) :Int {
            var position = 0
            val newPos = currentPosition - 1
            if (newPos >= 0) {
                position = scrollTpStation(newPos,recyclerView)
            }
            return position
        }

        private fun scrollTpStation(pos: Int, recyclerView: RecyclerView):Int {
            recyclerView.smoothScrollToPosition(pos)

            return pos
        }

        fun snackHelper(view: View, message: String){
            val snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            snack.show()
        }
        fun uıOperation(view:View){
            view.alpha = 0.25f
            view.isEnabled = false
            view.isClickable = false
        }
    }
}