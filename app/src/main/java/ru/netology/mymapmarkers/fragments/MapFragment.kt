package ru.netology.mymapmarkers.fragments

import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObject
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import ru.netology.mymapmarkers.R
import ru.netology.mymapmarkers.databinding.FragmentMapBinding
import ru.netology.mymapmarkers.dto.Marker
import ru.netology.mymapmarkers.viewmodels.MarkerViewModel

class MapFragment : Fragment() {

    private lateinit var mapView: MapView
    private val markerViewModel: MarkerViewModel by viewModels(::requireParentFragment)
    private lateinit var placemarkTapListener: MapObjectTapListener
    private val inputListener = object : InputListener {
        override fun onMapTap(p0: Map, p1: Point) {}

        override fun onMapLongTap(p0: Map, point: Point) {
            showNewMarkerDialog(point)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        MapKitFactory.initialize(requireActivity())
        val binding = FragmentMapBinding.inflate(inflater, container, false)

        mapView = binding.mapView
        mapView.mapWindow.map.addInputListener(inputListener)
        val pinsCollection = binding.mapView.mapWindow.map.mapObjects.addCollection()
        val imageProvider =
            ImageProvider.fromBitmap(createBitmapFromVector(R.drawable.map_marker_24))

        markerViewModel.markerList.observe(viewLifecycleOwner) {
            it.forEach { marker ->
                pinsCollection.addPlacemark().apply {
                    geometry = Point(marker.latitude, marker.longitude)
                    setIcon(imageProvider)
                    setText(marker.description)
                    placemarkTapListener = MapObjectTapListener { placemarkerMapObject, _ ->
                        showMarkerDialog(placemarkerMapObject, marker, pinsCollection)
                        true
                    }
                    addTapListener(placemarkTapListener)
                }
            }
        }

        setFragmentResultListener("goToMarker") { key, bundle ->
            val marker =
                markerViewModel.markerList.value?.find { it.id == bundle.getLong("myMarker") }
                    ?: return@setFragmentResultListener
            mapView.mapWindow.map.move(
                CameraPosition(
                    Point(marker.latitude, marker.longitude),
                    17.0f,
                    150.0f,
                    30.0f
                )
            )
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
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    private fun showMarkerDialog(
        mapPlacemarker: MapObject,
        marker: Marker,
        markersCollection: MapObjectCollection
    ) {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.layout_bottomsheet_dialog_marker)

        val description = dialog.findViewById<LinearLayout>(R.id.descriptionMarkerMenu)
        val edit = dialog.findViewById<LinearLayout>(R.id.editMarkerMenu)
        val remove = dialog.findViewById<LinearLayout>(R.id.removeMarkerMenu)

        description.setOnClickListener {
            dialog.dismiss()
            Snackbar.make(mapView, marker.description, Snackbar.LENGTH_INDEFINITE)
                .setAction("Ok") { Unit }.show()
        }

        remove.setOnClickListener {
            dialog.dismiss()
            markersCollection.remove(mapPlacemarker)
            markerViewModel.removeById(marker.id)
        }

        edit.setOnClickListener {
            dialog.dismiss()
            showEditDialog(mapPlacemarker, marker, markersCollection)
        }

        dialog.show()
        dialog.window?.let { window ->
            window.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window.attributes.windowAnimations = R.style.DialogAnimation
            window.setGravity(Gravity.BOTTOM)

        }
    }

    private fun showEditDialog(
        mapPlacemarker: MapObject,
        marker: Marker,
        markersCollection: MapObjectCollection
    ) {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.layout_bottomsheet_dialog_edit_marker)

        markerViewModel.edit(marker)

        val name = dialog.findViewById<EditText>(R.id.editName).also { it.setText(marker.name) }
        val description = dialog.findViewById<EditText>(R.id.editDescription)
            .also { it.setText(marker.description) }
        val save = dialog.findViewById<Button>(R.id.editSaveButton)

        save.setOnClickListener {
            if (name.text.isBlank()) {
                Toast.makeText(requireActivity(), "Enter the name", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            dialog.dismiss()
            markersCollection.remove(mapPlacemarker)
            markerViewModel.updateMarker(
                marker.id,
                name.text.toString(),
                description.text.toString()
            )

        }

        dialog.show()
        dialog.window?.let { window ->
            window.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window.attributes.windowAnimations = R.style.DialogAnimation
            window.setGravity(Gravity.BOTTOM)
        }
    }

    private fun showNewMarkerDialog(
        point: Point,
    ) {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.layout_bottomsheet_dialog_new_marker)


        val name = dialog.findViewById<EditText>(R.id.newMarkerName)
        val description = dialog.findViewById<EditText>(R.id.newMarkerDescription)
        val save = dialog.findViewById<Button>(R.id.newMarkerSaveButton)

        save.setOnClickListener {
            if (name.text.isBlank()) {
                Toast.makeText(requireActivity(), "Enter the name", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            dialog.dismiss()
            markerViewModel.save(
                Marker(
                    id = 0,
                    name = name.text.toString(),
                    description = description.text.toString(),
                    latitude = point.latitude,
                    longitude = point.longitude,
                    )
            )
        }

        dialog.show()
        dialog.window?.let { window ->
            window.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window.attributes.windowAnimations = R.style.DialogAnimation
            window.setGravity(Gravity.BOTTOM)
        }
    }
}