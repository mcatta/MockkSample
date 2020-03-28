package com.mcatta.mockksample.ui

interface MainContract {

    interface Presenter {
        fun fetchData()
        fun fetchDataViaRX()
        fun fetchDataViaCallback()
        fun log(message: String, tag: String = "MainPresenter")
    }

    interface View {
        fun onResult(result: List<UiDataModel>)
        fun onError(error: Throwable)
    }
}