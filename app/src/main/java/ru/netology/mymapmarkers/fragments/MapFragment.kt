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
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.mapview.MapView
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

        val pinsCollection = binding

        setFragmentResultListener("goToMarker") { key, bundle ->
            val id = bundle.getLong(key)

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