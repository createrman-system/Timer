package com.frog.timer

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [TimerViewModel].
 */
@OptIn(ExperimentalCoroutinesApi::class)
class TimerViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: TimerViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = TimerViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `setTimer updates state correctly`() {
        viewModel.setTimer(5000L)
        assertEquals(5000L, viewModel.state.value.timeMillis)
        assertEquals(TimerStatus.INITIAL, viewModel.state.value.status)
    }

    @Test
    fun `startTimer transitions status to RUNNING`() = runTest {
        viewModel.setTimer(5000L)
        viewModel.startTimer()
        assertEquals(TimerStatus.RUNNING, viewModel.state.value.status)
    }

    @Test
    fun `timer countdown updates timeMillis`() = runTest {
        viewModel.setTimer(1000L)
        viewModel.startTimer()
        
        // Advance time by 500ms
        // Since we update every 16ms, we should see progress
        testDispatcher.scheduler.advanceTimeBy(500)
        
        assertNotEquals(1000L, viewModel.state.value.timeMillis)
        assert(viewModel.state.value.timeMillis < 1000L)
    }

    @Test
    fun `timer finishes correctly`() = runTest {
        viewModel.setTimer(100L)
        viewModel.startTimer()
        
        // Advance enough time to finish
        testDispatcher.scheduler.advanceTimeBy(150)
        
        assertEquals(0L, viewModel.state.value.timeMillis)
        assertEquals(TimerStatus.FINISHED, viewModel.state.value.status)
    }

    @Test
    fun `pauseTimer stops countdown`() = runTest {
        viewModel.setTimer(1000L)
        viewModel.startTimer()
        
        testDispatcher.scheduler.advanceTimeBy(100)
        val timeAfter100 = viewModel.state.value.timeMillis
        
        viewModel.pauseTimer()
        assertEquals(TimerStatus.PAUSED, viewModel.state.value.status)
        
        testDispatcher.scheduler.advanceTimeBy(100)
        // Time should not have changed while paused
        assertEquals(timeAfter100, viewModel.state.value.timeMillis)
    }

    @Test
    fun `resetTimer resets time and status`() = runTest {
        viewModel.setTimer(1000L)
        viewModel.startTimer()
        testDispatcher.scheduler.advanceTimeBy(100)
        
        viewModel.resetTimer()
        assertEquals(1000L, viewModel.state.value.timeMillis)
        assertEquals(TimerStatus.INITIAL, viewModel.state.value.status)
    }
}
