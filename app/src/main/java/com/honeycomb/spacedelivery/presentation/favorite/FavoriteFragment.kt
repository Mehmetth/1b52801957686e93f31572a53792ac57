package com.honeycomb.spacedelivery.presentation.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.honeycomb.spacedelivery.R
import com.honeycomb.spacedelivery.databinding.FragmentFavoriteBinding
import com.honeycomb.spacedelivery.domain.db.StationDatabase
import com.honeycomb.spacedelivery.domain.db.StationItem
import com.honeycomb.spacedelivery.domain.db.StationRepository
import com.honeycomb.spacedelivery.presentation.favorite.viewmodel.DatabaseViewModel
import com.honeycomb.spacedelivery.presentation.favorite.viewmodel.DatabaseViewModelFactory

class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    lateinit var databaseViewModel: DatabaseViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavoriteBinding.bind(view)

        val groceryRepository = StationRepository(StationDatabase(requireContext()))
        val factory = DatabaseViewModelFactory(groceryRepository)
        databaseViewModel = ViewModelProvider(this,factory).get(DatabaseViewModel::class.java)

        setupRecyclerView()
        fecthData()
    }

    fun fecthData(){
        databaseViewModel.allStationItems().observe(viewLifecycleOwner, Observer{
            handleDatas(it)
        })
    }
    fun handleDatas(items : List<StationItem>){
        binding.favoriteRecyclerview.adapter?.let {
            if(it is FavoriteAdapter){
                it.updateDataList(items)
            }
        }
    }
    private fun setupRecyclerView(){
        val mAdapter = FavoriteAdapter( FavoriteAdapter.OnClickListener{ item ->
                activity?.let{
                    databaseViewModel.delete(item)
                    fecthData()
                }
            },mutableListOf())

        binding.favoriteRecyclerview.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }
    }
}