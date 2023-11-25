package ru.netology.mymapmarkers.fragments

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import ru.netology.mymapmarkers.R
import ru.netology.mymapmarkers.databinding.FragmentMapBinding
import ru.netology.mymapmarkers.viewmodels.MarkerViewModel

class MapFragment: Fragment() {

    private lateinit var mapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        MapKitFactory.initialize(requireActivity())
        val binding = FragmentMapBinding.inflate(inflater, container, false)
        val markerViewModel: MarkerViewModel by viewModels(::requireParentFragment)

        mapView = binding.mapView
        val pinsCollection = binding.mapView.map.mapObjects.addCollection()
        val imageProvider = ImageProvider.fromBitmap(createBitmapFromVector(R.drawable.map_marker_24))

        markerViewModel.markerList.observe(viewLifecycleOwner){
            it.forEach { marker ->
                pinsCollection.addPlacemark().apply {
                    geometry = Point(marker.latitude, marker.longitude)
                    setIcon(imageProvider)
                }
            }
        }

        setFragmentResultListener("goToMarker") { key, bundle ->
            val marker = markerViewModel.markerList.value?.find { it.id == bundle.getLong(key) } ?: return@setFragmentResultListener
            mapView.mapWindow.map.move(CameraPosition(
                Point(marker.latitude, marker.longitude),
                17.0f,
                150.0f,
                30.0f
                ))
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    private fun createBitmapFromVector(vectorImg: Int): Bitmap? {
        val drawable = ContextCompat.getDrawable(requireActivity(), vectorImg) ?: return null
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        ) ?: return null
        val canvas = Canvas(bitmap)
        drawable.setBounds(0,0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}