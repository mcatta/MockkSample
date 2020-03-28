package com.mcatta.mockksample.ui

import com.mcatta.mockksample.data.DataRepository
import com.mcatta.mockksample.utils.MyUselessUtils
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable

class MainPresenter(
    private val view: MainContract.View,
    private val dataRepository: DataRepository
) : MainContract.Presenter {

    override fun fetchData() {
        log("fetchData")
        try {
            val result = dataRepository.fetchData()
            view.onResult(result.map {
                UiDataModel(
                    MyUselessUtils.generateUUID(),
                    it.id,
                    it.value
                )
            })
        } catch (err: Throwable) {
            view.onError(err)
        }
    }

    override fun fetchDataViaRX() {
        log("fetchDataViaRX")
        dataRepository.fetchDataWithRX()
            .flatMap { result ->
                val output =
                    result.map { UiDataModel(MyUselessUtils.generateUUID(), it.id, it.value) }
                Single.just(output)
            }
            .subscribe(object : SingleObserver<List<UiDataModel>> {
                override fun onSuccess(t: List<UiDataModel>) {
                    view.onResult(t)
                }

                override fun onSubscribe(d: Disposable) {}

                override fun onError(e: Throwable) {
                    view.onError(e)
                }

            })
    }

    override fun fetchDataViaCallback() {
        log("fetchDataViaCallback")
        try {
            dataRepository.fetchData { data ->
                view.onResult(data.map {
                    UiDataModel(
                        MyUselessUtils.generateUUID(),
                        it.id,
                        it.value
                    )
                })
            }
        } catch (err: Exception) {
            view.onError(err)
        }
    }

    override fun log(message: String, tag: String) {
        print("TAG: $tag Message: $message")
    }
}