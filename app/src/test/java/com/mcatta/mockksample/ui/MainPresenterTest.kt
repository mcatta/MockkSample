package com.mcatta.mockksample.ui

import com.mcatta.mockksample.data.DataRepository
import com.mcatta.mockksample.domain.DataModel
import com.mcatta.mockksample.utils.MyUselessUtils
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import io.reactivex.Single
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MainPresenterTest {

    @RelaxedMockK
    lateinit var view: MainContract.View

    @RelaxedMockK
    lateinit var dataRepository: DataRepository

    lateinit var mainPresenter: MainContract.Presenter

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        mainPresenter = MainPresenter(view, dataRepository)
    }

    @Test
    fun `test fetchData with an empty result`() {
        every { dataRepository.fetchData() } returns listOf()
        mainPresenter.fetchData()

        val captureData = slot<List<UiDataModel>>()

        verify(exactly = 1) { view.onResult(capture(captureData)) }
        captureData.captured.let { res ->
            assertNotNull(res)
            assert(res.isEmpty())
        }
    }

    @Test
    fun `Test fetchData with an exception`() {
        every { dataRepository.fetchData() } throws IllegalStateException("Houston we have a problem")
        mainPresenter.fetchData()

        verify(exactly = 0) { view.onResult(any()) }
        verify(exactly = 1) { view.onError(any()) }
    }

    @Test
    fun `test fetchDataViaCallback with an result`() {
        val captureCallback = slot<(data: List<DataModel>) -> Unit>()
        every { dataRepository.fetchData(capture(captureCallback)) } answers {
            val fakeList = listOf(DataModel(1, "Value 1"))
            captureCallback.captured.invoke(fakeList)
        }
        mainPresenter.fetchDataViaCallback()

        val captureData = slot<List<UiDataModel>>()

        verify(exactly = 1) { view.onResult(capture(captureData)) }
        captureData.captured.let { res ->
            assertNotNull(res)
            assert(res.isNotEmpty())
            assertEquals("Value 1", res.first().value)
        }
    }

    @Test
    fun `test fetchDataViaRX with an empty result`() {
        every { dataRepository.fetchDataWithRX() } returns Single.error(Throwable("Oh oh something is broken"))
        mainPresenter.fetchData()

        val captureData = slot<List<UiDataModel>>()

        verify(exactly = 1) { view.onResult(capture(captureData)) }
        captureData.captured.let { res ->
            assertNotNull(res)
            assert(res.isEmpty())
        }
    }

    @Test
    fun `test fetchData with a different behaviour of UUID generation`() {
        val uuid = "my-fake-uuid"
        mockkObject(MyUselessUtils)
        every { MyUselessUtils.generateUUID() } returns uuid
        every { dataRepository.fetchData() } returns listOf(DataModel(1, "Value"))
        mainPresenter.fetchData()

        val captureData = slot<List<UiDataModel>>()

        verify(exactly = 1) { view.onResult(capture(captureData)) }
        captureData.captured.let { res ->
            assert(res.isNotEmpty())
            assertEquals(uuid, res.first().uuid)
        }

        unmockkObject(MyUselessUtils)
    }

    @Test
    fun `Test log() method`() {
        val spiedPresenter = spyk(mainPresenter)
        every { dataRepository.fetchData() } returns listOf()
        spiedPresenter.fetchData()

        verify(exactly = 1) { spiedPresenter.log(any(), any()) }
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}