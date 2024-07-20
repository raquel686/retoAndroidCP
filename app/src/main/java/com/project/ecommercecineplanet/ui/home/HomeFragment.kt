package com.project.ecommercecineplanet.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.project.ecommercecineplanet.data.model.PremiereModel
import com.project.ecommercecineplanet.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.imaginativeworld.whynotimagecarousel.listener.CarouselListener
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel:HomeViewModel by activityViewModels()
    private lateinit var carousel: ImageCarousel
    private lateinit var loadingScreen:LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        loadingScreen=binding.loadingScreen

        carousel = binding.carousel
        carousel.registerLifecycle(viewLifecycleOwner.lifecycle)



        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            if (uiState.isLoading) {
                showLoading()
            } else {
                hideLoading()
            }
            uiState.errorMessage?.let { message ->
                showError(message)
            }
            updateDataList(uiState.dataList1)

        }


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect { uiEvent ->
                when (uiEvent) {
                    HomeViewModel.UIEvent.NavigateToLogin->navigationToLogin()

                    else->{}
                }
            }
        }

        viewModel.obtenerListaPremieres()

        return root
    }

    private fun showLoading() {
        loadingScreen.visibility=View.VISIBLE
    }

    private fun hideLoading() {
        loadingScreen.visibility=View.GONE

    }

    private fun updateDataList(list: List<PremiereModel>) {
        val carouselItems = convertToCarouselItems(list)
        carousel.setData(carouselItems)
        onItemSelected()
    }

    private fun onItemSelected(){
        carousel.carouselListener = object : CarouselListener {
            override fun onClick(position: Int, carouselItem: CarouselItem) {
                viewModel.onItemSelected()
            }
        }
    }

    private fun navigationToLogin(){
        val action = HomeFragmentDirections.actionNavigationHomeToNavigationNotifications()
        findNavController().navigate(action)
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun convertToCarouselItems(premiereModels: List<PremiereModel>): List<CarouselItem> {
        return premiereModels.map { premiereModel ->
            CarouselItem(
                imageUrl = premiereModel.image,
                caption = premiereModel.description
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}