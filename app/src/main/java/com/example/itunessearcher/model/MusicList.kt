package com.example.itunessearcher.model

import android.os.Parcel
import android.os.Parcelable

data class MusicList(
    val results: List<POKOMusicTrack>
)
data class POKOMusicTrack(
    val trackName: String,
    val trackID: Int,
    val artistName: String,
    val collectionName: String,
    val artworkUrl100: String,
    val trackTimeMillis: Int,
    val primaryGenreName: String,
    val releaseDate: String,
    val trackPrice: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(trackName)
        parcel.writeInt(trackID)
        parcel.writeString(artistName)
        parcel.writeString(collectionName)
        parcel.writeString(artworkUrl100)
        parcel.writeInt(trackTimeMillis)
        parcel.writeString(primaryGenreName)
        parcel.writeString(releaseDate)
        parcel.writeString(trackPrice)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<POKOMusicTrack> {
        override fun createFromParcel(parcel: Parcel): POKOMusicTrack {
            return POKOMusicTrack(parcel)
        }

        override fun newArray(size: Int): Array<POKOMusicTrack?> {
            return arrayOfNulls(size)
        }
    }
}