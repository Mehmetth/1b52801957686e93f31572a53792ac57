package com.honeycomb.spacedelivery.presentation.spacecraft.dto

import android.os.Parcel
import android.os.Parcelable

data class SpaceCraftModel(
    val spaceCraftName : String,
    val durability : Float,
    val speed : Float,
    val capacity : Float,
) : Parcelable
{
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(spaceCraftName)
        parcel.writeFloat(durability)
        parcel.writeFloat(speed)
        parcel.writeFloat(capacity)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SpaceCraftModel> {
        override fun createFromParcel(parcel: Parcel): SpaceCraftModel {
            return SpaceCraftModel(parcel)
        }

        override fun newArray(size: Int): Array<SpaceCraftModel?> {
            return arrayOfNulls(size)
        }
    }
}