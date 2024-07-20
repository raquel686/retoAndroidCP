package com.project.ecommercecineplanet.ui.dulceria

import android.os.Bundle
import android.util.Log
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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.project.ecommercecineplanet.adapter.CandyAdapter
import com.project.ecommercecineplanet.data.model.PremiereModel
import com.project.ecommercecineplanet.databinding.FragmentDulceriaBinding
import com.project.ecommercecineplanet.domain.model.CandyItemDomainModel
import com.project.ecommercecineplanet.domain.model.toDomain
import com.project.ecommercecineplanet.ui.home.HomeFragmentDirections
import com.project.ecommercecineplanet.ui.home.HomeViewModel
import com.project.ecommercecineplanet.ui.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DulceriaFragment : Fragment() {

    private var _binding: FragmentDulceriaBinding? = null
    private val viewModel: DulceriaViewModel by activityViewModels()
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var loadingScreen: LinearLayout
    private lateinit var adapter:CandyAdapter
    private lateinit var continuarButton:MaterialButton
    private var total ="0.00"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDulceriaBinding.inflate(inflater, container, false)
        val root: View = binding.root
        recyclerView=binding.recyclerView
        recyclerView.layoutManager= GridLayoutManager(context, 2)

        loadingScreen=binding.loadingScreen
        continuarButton=binding.continuarButton


        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            if (uiState.isLoading) {
                showLoading()
            } else {
                hideLoading()
            }
            uiState.errorMessage?.let { message ->
                showError(message)
            }
            updateDataList(uiState.dataList1.map { it.toDomain() })

        }


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect { uiEvent ->
                when (uiEvent) {
                    DulceriaViewModel.UIEvent.ChangeTotal->{
                        binding.totalDulceria.text= "s/${viewModel.total.value}"
                        continuarButton.isEnabled = viewModel.total.value!="0.00"
                        total=viewModel.total.value
                    }
                    else->{}
                }
            }
        }

        viewModel.obtenerCandiesPremieres()

        continuarButton.setOnClickListener {
            navigationToPago()
        }

        return root
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
    private fun showLoading() {
        loadingScreen.visibility=View.VISIBLE
    }
    private fun navigationToPago(){
        Log.i("argumento dulceria",total)
        val action = DulceriaFragmentDirections.actionNavigationDashboardToPagoFragment(total)
        findNavController().navigate(action)
    }
    private fun hideLoading() {
        loadingScreen.visibility=View.GONE

    }

    private fun updateDataList(list: List<CandyItemDomainModel>) {
        adapter=CandyAdapter(list.toMutableList(),{
                                  addItem(it)
        },{
          removeItem(it)
        },requireContext())
        recyclerView.adapter=adapter
    }
    private fun addItem(item:CandyItemDomainModel){
        item.cantidad = (item.cantidad ?: 0) + 1
        adapter.updateItem(item)
        viewModel.actualizarTotal(adapter.getCandyList())
    }

    private fun removeItem(item:CandyItemDomainModel){
        if ((item.cantidad ?: 0) > 0) {
            item.cantidad = (item.cantidad ?: 0) - 1
            adapter.updateItem(item)
            viewModel.actualizarTotal(adapter.getCandyList())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}