package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.NumberFormatter
import com.example.model.SimulationSpeed
import com.example.viewmodel.SimViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("TikSim", appName)
  }

  @Test
  fun `number formatter formats compact properly`() {
    assertEquals("950", NumberFormatter.formatCompact(950L))
    assertEquals("1,420", NumberFormatter.formatCompact(1420L))
    assertEquals("12.5K", NumberFormatter.formatCompact(12500L))
    assertEquals("1.2M", NumberFormatter.formatCompact(1200000L))
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun `sim view model instant likes injection increases post likes`() = runTest {
    val viewModel = SimViewModel()
    val initialPost = viewModel.posts.value.first()
    val initialLikes = initialPost.likesCount

    viewModel.injectCustomLikes(initialPost.id, 5000L, SimulationSpeed.INSTANT)
    
    // Check that likes count increased
    val updatedPost = viewModel.posts.value.first()
    assertEquals(initialLikes + 5000L, updatedPost.likesCount)
    assertTrue(updatedPost.growthHistory.isNotEmpty())
  }
}
