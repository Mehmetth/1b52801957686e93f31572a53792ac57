package com.honeycomb.spacedelivery.presentation.station

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.honeycomb.spacedelivery.R
import com.honeycomb.spacedelivery.presentation.spacecraft.dto.SpaceCraftModel
import com.honeycomb.spacedelivery.databinding.FragmentStationBinding
import com.honeycomb.spacedelivery.domain.station.entity.StationEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import android.widget.SearchView
import androidx.lifecycle.*
import com.honeycomb.spacedelivery.PlanetCoordinates
import com.honeycomb.spacedelivery.domain.db.StationDatabase
import com.honeycomb.spacedelivery.domain.db.StationItem
import com.honeycomb.spacedelivery.domain.db.StationRepository
import com.honeycomb.spacedelivery.presentation.favorite.viewmodel.DatabaseViewModel
import com.honeycomb.spacedelivery.presentation.favorite.viewmodel.DatabaseViewModelFactory
import com.honeycomb.spacedelivery.presentation.station.viewmodel.StationViewModel
import com.honeycomb.spacedelivery.utils.CalculationHelper
import com.honeycomb.spacedelivery.utils.Constant
import com.honeycomb.spacedelivery.utils.UIHelper
import kotlin.math.roundToInt

@AndroidEntryPoint
class StationFragment : Fragment(R.layout.fragment_station), IStationClickHandle{

    private var _binding: FragmentStationBinding? = null
    private val binding get() = _binding!!

    private val stationServiceViewModel: StationServiceViewModel by viewModels()
    lateinit var databaseViewModel: DatabaseViewModel
    private var stationViewModel = StationViewModel()

    private lateinit var mAdapter : StationAdapter

    private var timerStartValue : Long = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentStationBinding.bind(view)

        val groceryRepository = StationRepository(StationDatabase(requireContext()))
        val factory = DatabaseViewModelFactory(groceryRepository)
        databaseViewModel = ViewModelProvider(this,factory).get(DatabaseViewModel::class.java)

        initializeDatasAndView(requireActivity().intent.getParcelableExtra<SpaceCraftModel>(Constant.SEND_PARSE_DATA)!!)

        stationViewModel = ViewModelProviders.of(requireActivity())[StationViewModel::class.java]
        observe()
        setupRecyclerView()
        binding.leftArrowImagebutton.setOnClickListener {
            stationViewModel.currentPosition = UIHelper.goPreviousStation(stationViewModel.currentPosition,binding.stationRecyclerView)
        }
        binding.rightArrowImagebutton.setOnClickListener {
            stationViewModel.currentPosition =  UIHelper.goNextStation(stationViewModel.currentPosition,binding.stationRecyclerView,mAdapter)
        }
    }

    private fun initializeDatasAndView(spaceCraftModel : SpaceCraftModel){
        Constant.currentDatas.ugs = spaceCraftModel.capacity * 10000
        Constant.currentDatas.eus = spaceCraftModel.speed * 20

        binding.ugsValueTextview.text = Constant.currentDatas.ugs.toString()
        binding.eusValueTextview.text = Constant.currentDatas.eus .toString()
        binding.dsValueTextview.text = (spaceCraftModel.durability * 10000).toString()
        binding.endValueTextview.text = (spaceCraftModel.durability * 10).toString() + "s"
        binding.spaceVehicleNameTextview.text = spaceCraftModel.spaceCraftName
        binding.stationNameTextview.text = Constant.currentDatas.name

        timerStartValue = (spaceCraftModel.durability * 10000).toLong()
    }
    private fun countDownTimer(millisInFuture:Long):CountDownTimer{
        return object: CountDownTimer(millisInFuture,1000){
            override fun onTick(millisUntilFinished: Long){

                if (stationViewModel.timerPauseState){
                    stationViewModel.timerFromMillis = millisUntilFinished
                    cancel()
                }
                else if(stationViewModel.timerStopState){
                    cancel()
                }
                else{
                    binding.endValueTextview.text = (millisUntilFinished / 1000).toString() + "s"
                }
            }

            override fun onFinish() {
                countDownTimer(timerStartValue).start()
                if(stationViewModel.damageCapacity >= 10){
                    binding.damageCapacityTextview.text = stationViewModel.reduceDamageCapacity().toString()
                }
                else{
                    GameOver(resources.getString(R.string.spaceCraftDamage))
                }
            }
        }
    }
    private fun setupRecyclerView(){
        mAdapter = StationAdapter(requireActivity(),this,mutableListOf())

        binding.stationRecyclerView.apply {
            adapter = mAdapter
        }
    }
    private fun observe(){
        fetchStations()
        observeState()
        observeStations()
    }
    private fun fetchStations(){
        stationServiceViewModel.fetchStation()
    }
    private fun observeState(){
        stationServiceViewModel.mState
            .flowWithLifecycle (viewLifecycleOwner.lifecycle,  Lifecycle.State.STARTED)
            .onEach { state ->
                handleState(state)
            }
            .launchIn (viewLifecycleOwner.lifecycleScope)
    }
    private fun observeStations(){
        stationServiceViewModel.mStations
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { stations ->
                handleProducts(stations)

                if(stationViewModel.travelControl == 0){
                    stationViewModel.travelControl = stations.size
                }

                binding.searchNameSearchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        return false
                    }
                    override fun onQueryTextChange(newText: String): Boolean {
                        if(newText.isEmpty()){
                            mAdapter.updateDataList(stations)
                        }
                        else{
                            mAdapter.filter.filter(newText)
                        }
                        return false
                    }
                })
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }
    private fun handleProducts(products: List<StationEntity>){
        binding.stationRecyclerView.adapter?.let {
            if(it is StationAdapter){
                databaseViewModel.allStationItems().observe(viewLifecycleOwner, Observer{ stationItem ->
                    stationItem.forEach { _stationItem ->
                        products.forEach { product ->
                            if(_stationItem.name == product.name){
                                product.fav = true
                            }
                        }
                    }

                    val hideTravel = products.size - stationViewModel.travelControl
                    products.forEachIndexed { index, element ->
                        if(hideTravel > index){
                            element.travel = true
                        }
                    }
                })
                it.updateDataList(products)
            }
        }
    }
    private fun handleState(state: StationState){
        when(state){
            is StationState.IsLoading -> handleLoading(state.isLoading)
            is StationState.ShowToast -> Toast.makeText(requireActivity(), state.message, Toast.LENGTH_LONG).show()
            is StationState.Init -> Unit
        }
    }
    private fun handleLoading(isLoading: Boolean){
        if(isLoading){
            binding.stationFragmentProgressBar.visibility = View.VISIBLE
        }else{
            binding.stationFragmentProgressBar.visibility = View.GONE
        }
    }
    override fun favoriteClicked(list: List<StationEntity>, station: StationEntity) {
        list.forEach {
            if(it.name == station.name){
                if(it.fav){
                    it.fav = false
                    databaseViewModel.delete(StationItem(it.name, it.coordinateX,it.coordinateY,it.capacity,it.stock,it.need,it.fav,it.travel))
                }
                else{
                    it.fav = true
                    val eusCalculate = (((CalculationHelper.calculateTwoPlanetDistance(
                        PlanetCoordinates(0f,0f),
                        PlanetCoordinates(it.coordinateX,
                            it.coordinateY))* 100.0).roundToInt()) / 100.0).toString() + "EUS"
                    databaseViewModel.insert(StationItem(it.name, it.coordinateX,it.coordinateY,it.capacity,it.stock,it.need,it.fav,it.travel))
                }
            }
            mAdapter.updateDataList(list)
        }
    }
    override fun travelClicked(list: List<StationEntity>, station: StationEntity) {
        if(Constant.currentDatas.ugs >= station.need)
        {
            val rotateUes = (((CalculationHelper.calculateTwoPlanetDistance(
                PlanetCoordinates(Constant.currentDatas.coordinateX,Constant.currentDatas.coordinateY),
                PlanetCoordinates(station.coordinateX, station.coordinateY))* 100.0).roundToInt()) / 100.0)
            if(Constant.currentDatas.eus >= rotateUes){
                Constant.currentDatas.name = station.name
                Constant.currentDatas.eus = (Constant.currentDatas.eus - rotateUes).toFloat()
                Constant.currentDatas.ugs = Constant.currentDatas.ugs - station.need.toFloat()
                Constant.currentDatas.coordinateX = station.coordinateX
                Constant.currentDatas.coordinateY = station.coordinateY

                binding.ugsValueTextview.text = ((Constant.currentDatas.ugs * 100).roundToInt() / 100.0).toString()
                binding.eusValueTextview.text = ((Constant.currentDatas.eus * 100).roundToInt() / 100.0).toString()
                binding.stationNameTextview.text = Constant.currentDatas.name

                list.forEach {
                    if(it.name == station.name){
                        it.travel = true
                    }
                    mAdapter.updateDataList(list)
                }

                val travelControl = stationViewModel.reduceTravelCapacity()
                if(travelControl == 1){
                    UIHelper.snackHelper(requireView(),resources.getString(R.string.missionCompleted))
                    stationViewModel.timerPauseState = false
                    stationViewModel.timerStopState = true
                    disableViews()
                }
            }
            else{
                GameOver(resources.getString(R.string.enoughEUS))
            }
        }
        else{
            GameOver(resources.getString(R.string.enoughUGS))
        }
    }
    fun GameOver(message : String){
        UIHelper.snackHelper(requireView(),message)
        stationViewModel.timerPauseState = false
        stationViewModel.timerStopState = true
        disableViews()
    }
    fun disableViews(){
        UIHelper.uıOperation(binding.stationRecyclerView)
        UIHelper.uıOperation(binding.leftArrowImagebutton)
        UIHelper.uıOperation(binding.rightArrowImagebutton)
    }

    override fun onStart() {
        super.onStart()

        if(!stationViewModel.timerPauseState && !stationViewModel.timerStopState)
        {
            countDownTimer(timerStartValue).start()
        }
        else if(stationViewModel.timerPauseState && !stationViewModel.timerStopState){
            binding.damageCapacityTextview.text = stationViewModel.damageCapacity.toString()
            countDownTimer(stationViewModel.timerFromMillis).start()

            stationViewModel.timerPauseState = false
            stationViewModel.timerStopState = false
        }
    }
    override fun onStop() {
        super.onStop()
        stationViewModel.timerPauseState = true
        stationViewModel.timerStopState = false
    }
    override fun onDestroy() {
        super.onDestroy()
        stationViewModel.timerPauseState = true
        stationViewModel.timerStopState = true
    }
}