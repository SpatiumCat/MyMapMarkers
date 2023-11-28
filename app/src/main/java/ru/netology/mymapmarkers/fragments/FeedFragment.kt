package ru.netology.mymapmarkers.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import ru.netology.mymapmarkers.R
import ru.netology.mymapmarkers.adapter.MarkerAdapter
import ru.netology.mymapmarkers.adapter.OnInteractionListener
import ru.netology.mymapmarkers.databinding.FragmentFeedBinding
import ru.netology.mymapmarkers.viewmodels.MarkerViewModel

class FeedFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(inflater, container, false)
        val markerViewModel: MarkerViewModel by viewModels({ requireParentFragment() })
        val adapter = MarkerAdapter(object : OnInteractionListener {
            override fun goToMarker(id: Long) {
                setFragmentResult("goToMarker", bundleOf("myMarker" to id))
                binding.root.findNavController().navigate(R.id.action_feedFragment_to_mapFragment)
            }
        })

        with(binding) {
            listRecyclerView.adapter = adapter
            listRecyclerView.isVisible = (markerViewModel.markerList.value?.isNotEmpty() ?: false)
            emptyList.isVisible = markerViewModel.markerList.value?.isEmpty() ?: true
            toMapButton.isVisible = markerViewModel.markerList.value?.isEmpty() ?: true

        }
        binding.toMapButton.setOnClickListener {
            binding.root.findNavController().navigate(R.id.action_feedFragment_to_mapFragment)
        }


        markerViewModel.markerList.observe(requireActivity()) {
            adapter.submitList(it)
        }


        return binding.root
    }
}