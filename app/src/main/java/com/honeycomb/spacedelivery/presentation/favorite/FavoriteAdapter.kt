package com.honeycomb.spacedelivery.presentation.favorite

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.honeycomb.spacedelivery.PlanetCoordinates
import com.honeycomb.spacedelivery.R
import com.honeycomb.spacedelivery.domain.db.StationItem
import com.honeycomb.spacedelivery.utils.CalculationHelper
import kotlinx.android.synthetic.main.favorite_item.view.*
import java.util.*
import kotlin.math.roundToInt

class FavoriteAdapter(private val onClickListener: OnClickListener,
                      var stationList: List<StationItem>):
    RecyclerView.Adapter<FavoriteAdapter.DataViewHolder> (){

    class DataViewHolder(var view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.favorite_item,parent,false)
        return DataViewHolder(view)
    }

    fun getStationsSize(): Int = stationList.size
    override fun getItemCount(): Int {
        return getStationsSize()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {

        if(stationList[position].fav){
            holder.view.favoriteName_textView.text = stationList[position].name
            holder.view.favoriteCapacityNeed_textView.text = String.format(Locale.US, "%d/%d", stationList[position].capacity, stationList[position].need)
            holder.view.favoriteDistance_textView.text =
                (((CalculationHelper.calculateTwoPlanetDistance(
                PlanetCoordinates(0f,0f),
                PlanetCoordinates(stationList[position].coordinateX,stationList[position].coordinateY))* 100.0)
                    .roundToInt()) / 100.0)
                    .toString() +
                        "EUS"
        }
        holder.view.favorite_imageview.setOnClickListener {
            onClickListener.onClick(stationList[position])
        }
    }

    fun updateDataList(newDataList: List<StationItem>) {
        stationList = newDataList
        notifyDataSetChanged()
    }

    class OnClickListener(val clickListener: (sportEntity: StationItem) -> Unit) {
        fun onClick(sportEntity: StationItem) = clickListener(sportEntity)
    }
}