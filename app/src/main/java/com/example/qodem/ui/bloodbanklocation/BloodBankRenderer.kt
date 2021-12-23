package com.example.qodem.ui.bloodbanklocation

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.qodem.R
import com.example.qodem.model.BloodBank
import com.example.qodem.utils.BitmapHelper
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

/**
 * A custom cluster renderer for Place objects.
 */
class BloodBankRenderer(
    private val context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<BloodBank>
) : DefaultClusterRenderer<BloodBank>(context, map, clusterManager) {

    /**
     * The icon to use for each cluster item
     */
    private val bloodDropIcon: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(context, R.color.primaryDarkColor)
        BitmapHelper.vectorToBitmap(context, R.drawable.ic_blood_drop, color)
    }

    /**
     * Method called before the cluster item (the marker) is rendered.
     * This is where marker options should be set.
     */
    override fun onBeforeClusterItemRendered(
        item: BloodBank,
        markerOptions: MarkerOptions
    ) {
        markerOptions.title(item.name_en)
            .position(item.coordinates)
            .icon(bloodDropIcon)
    }

    /**
     * Method called right after the cluster item (the marker) is rendered.
     * This is where properties for the Marker object should be set.
     */
    override fun onClusterItemRendered(clusterItem: BloodBank, marker: Marker) {
        marker.tag = clusterItem
    }
}