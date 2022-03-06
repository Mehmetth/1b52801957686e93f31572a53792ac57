package com.honeycomb.spacedelivery.presentation.station

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.honeycomb.spacedelivery.PlanetCoordinates
import com.honeycomb.spacedelivery.R
import com.honeycomb.spacedelivery.domain.station.entity.StationEntity
import com.honeycomb.spacedelivery.utils.CalculationHelper
import com.honeycomb.spacedelivery.utils.Constant.Companion.currentDatas
import kotlinx.android.synthetic.main.station_rv_item.view.*
import java.util.*
import kotlin.math.roundToInt

class StationAdapter(val activity: Activity,
                     val stationClickHandle: IStationClickHandle,
                     var stationList: List<StationEntity>):
    RecyclerView.Adapter<StationAdapter.DataViewHolder> (), Filterable {
    class DataViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.station_rv_item,parent,false)
        return DataViewHolder(view)
    }

    fun getStationsSize(): Int = stationList.size
    override fun getItemCount(): Int {
        return getStationsSize()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.view.stationName_textView.text = stationList[position].name
        if(stationList[position].name == "Dünya"){
            holder.view.stationUgsCapacity_textView.visibility = View.INVISIBLE
            holder.view.stationDistance_textview.visibility = View.INVISIBLE
            holder.view.stationFavorite_imageview.visibility = View.INVISIBLE
            holder.view.stationTravel_button.visibility = View.INVISIBLE
        }
        else{
            holder.view.stationUgsCapacity_textView.visibility = View.VISIBLE
            holder.view.stationDistance_textview.visibility = View.VISIBLE
            holder.view.stationFavorite_imageview.visibility = View.VISIBLE
            holder.view.stationTravel_button.visibility = View.VISIBLE

            holder.view.stationUgsCapacity_textView.text = String.format(Locale.US, "%d/%d", stationList[position].capacity, stationList[position].need)
            holder.view.stationDistance_textview.text =

                (((CalculationHelper.calculateTwoPlanetDistance(
                PlanetCoordinates(currentDatas.coordinateX,currentDatas.coordinateY),
                    PlanetCoordinates(stationList[position].coordinateX,
                        stationList[position].coordinateY))* 100.0).roundToInt()) / 100.0).toString() + "EUS"

            if(stationList[position].travel){
                holder.view.stationTravel_button.visibility = View.INVISIBLE
            }
            else{
                holder.view.stationTravel_button.visibility = View.VISIBLE
            }
            if(stationList[position].fav){
                holder.view.stationFavorite_imageview.setImageDrawable(
                                                    ContextCompat.getDrawable(
                                                    activity, // Context
                                                    R.drawable.favorite // Drawable
                                                ))
            }
            else{
                holder.view.stationFavorite_imageview.setImageDrawable(
                    ContextCompat.getDrawable(
                        activity, // Context
                        R.drawable.unfavorite // Drawable
                    ))
            }

            holder.view.stationTravel_button.setOnClickListener {
                stationClickHandle.travelClicked(stationList,stationList[position])
            }
            holder.view.stationFavorite_imageview.setOnClickListener {
                stationClickHandle.favoriteClicked(stationList,stationList[position])
            }
        }
    }


    fun updateDataList(newDataList: List<StationEntity>) {
        stationList = newDataList
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(charSequence: CharSequence?, filterResults: Filter.FilterResults) {
                stationList = filterResults.values as List<StationEntity>
                notifyDataSetChanged()
            }

            override fun performFiltering(charSequence: CharSequence?): Filter.FilterResults {
                val queryString = charSequence?.toString()?.lowercase(Locale.getDefault())

                val filterResults = Filter.FilterResults()
                filterResults.values = if (queryString==null || queryString.isEmpty())
                    stationList
                else
                    stationList.filter {
                        it.name.lowercase(Locale.getDefault()).contains(queryString)
                    }
                return filterResults
            }
        }
    }
}