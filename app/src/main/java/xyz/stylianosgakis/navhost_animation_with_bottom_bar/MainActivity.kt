package xyz.stylianosgakis.navhost_animation_with_bottom_bar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import xyz.stylianosgakis.navhost_animation_with_bottom_bar.ui.theme.NavhostanimationwithbottombarTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)
    setContent {
      val systemUiController = rememberSystemUiController()
      val useDarkIcons = !isSystemInDarkTheme()
      DisposableEffect(systemUiController, useDarkIcons) {
        systemUiController.setSystemBarsColor(
          color = Color.Transparent,
          darkIcons = useDarkIcons,
        )
        onDispose {}
      }
      NavhostanimationwithbottombarTheme {
        App()
      }
    }
  }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun App() {
  Surface(
    modifier = Modifier.fillMaxSize(),
    color = MaterialTheme.colorScheme.background,
  ) {
    val navController = rememberAnimatedNavController()
    val currentDestination: String? = navController.currentBackStackEntryAsState().value?.destination?.route
    Column {
      @Suppress("UNUSED_VARIABLE")
      val thirtyDips = with(LocalDensity.current) { 30.dp.roundToPx() }
      AnimatedNavHost(
        navController = navController,
        startDestination = "home",
        modifier = Modifier
          .weight(1f)
          .border(1.dp, Color.Red),
//        enterTransition = { slideInHorizontally { thirtyDips } }, // slide or None transitions both bring this issue
//        exitTransition = { slideOutHorizontally { -thirtyDips } },
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
      ) {
        composable("home") {
          Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier
              .fillMaxSize()
              .border(1.dp, Color.Red),
          ) {
            Text("Home", style = MaterialTheme.typography.headlineLarge)
          }
        }
        composable("detail") {
          Surface(
            color = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier
              .fillMaxSize()
              .border(1.dp, Color.Red),
          ) {
            Text("Detail", style = MaterialTheme.typography.headlineLarge)
          }
        }
      }
      val showBottomNav by produceState(false) {
        while (isActive) {
          delay(1_000)
          value = !value
        }
      }
      val height by animateDpAsState(
        targetValue = if (showBottomNav) 120.dp else 0.dp,
        animationSpec = spring(
          stiffness = Spring.StiffnessMediumLow,
          visibilityThreshold = Dp.VisibilityThreshold,
        ),
        label = "height",
      )
      Surface(
        color = MaterialTheme.colorScheme.tertiaryContainer,
        modifier = Modifier
          .fillMaxWidth()
          .height(height),
      ) {
        Box(contentAlignment = Alignment.TopCenter) {
          Text("Bottom nav")
        }
      }
    }
    Box(contentAlignment = Alignment.Center) {
      Button(
        onClick = {
          if (currentDestination == "home") {
            navController.navigate("detail")
          } else if (currentDestination == "detail") {
            navController.navigate("home")
          }
        },
      ) {
        Text("navigate")
      }
    }
  }
}
