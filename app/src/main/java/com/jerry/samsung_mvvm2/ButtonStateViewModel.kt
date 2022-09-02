package com.jerry.samsung_mvvm

import androidx.lifecycle.*
import com.jerry.samsung_mvvm2.CheckStateRepository
import kotlinx.coroutines.launch

class ButtonStateViewModel(    private val checkStateRepository: CheckStateRepository) : ViewModel() {
    private lateinit var liveData: MutableLiveData<Boolean>

    val initialSetupEvent = liveData {
        emit(checkStateRepository.fetchInitialPreferences())
    }

    private val userPreferencesFlow = checkStateRepository.clickedFlow


    private val clickedFlow = checkStateRepository.clickedFlow

//    public fun observeData(owner: LifecycleOwner, observer: Observer<Boolean>) =
//        liveData.observe(owner, observer)

    fun setClicked(clicked: Boolean) {
        viewModelScope.launch {
            checkStateRepository.setWidgetClicked(clicked)
        }
    }

    class ButtonStateViewModelFactory(
        private val checkStateRepository: CheckStateRepository
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ButtonStateViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ButtonStateViewModel(checkStateRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}