package com.example.candyfocus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import kotlinx.coroutines.delay

val CandyPink = Color(0xFFE83E9F)
val SoftPink = Color(0xFFFFEEF8)
val LightPink = Color(0xFFFFF6FB)
val SoftPurple = Color(0xFFEEDBFF)
val SoftBlue = Color(0xFF54C4E8)
val TextDark = Color(0xFF382333)
val TextMuted = Color(0xFF8D6C82)

data class FocusTask(
    val id: Int,
    val title: String,
    val done: Boolean = false
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CandyFocusTheme {
                CandyFocusApp()
            }
        }
    }
}

@Composable
fun CandyFocusTheme(content: @Composable () -> Unit) {
    val colorScheme = lightColorScheme(
        primary = CandyPink,
        background = LightPink,
        surface = Color.White,
        onPrimary = Color.White,
        onBackground = TextDark,
        onSurface = TextDark
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}

@Composable
fun CandyFocusApp() {
    val navController = rememberNavController()

    var totalSessions by rememberSaveable {
        mutableStateOf(8)
    }

    val tasks = remember {
        mutableStateListOf(
            FocusTask(1, "Estudiar POO II"),
            FocusTask(2, "Terminar práctica de Android"),
            FocusTask(3, "Subir proyecto a GitHub")
        )
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        },
        containerColor = LightPink
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = "inicio",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("inicio") {
                InicioScreen(
                    totalSessions = totalSessions,
                    onSessionComplete = {
                        totalSessions++
                    },
                    onGoToDetalle = {
                        navController.navigate("detalle")
                    }
                )
            }

            composable("detalle") {
                DetalleScreen(
                    tasks = tasks,
                    onAddTask = { title ->
                        tasks.add(
                            FocusTask(
                                id = tasks.size + 1,
                                title = title
                            )
                        )
                    },
                    onToggleTask = { task ->
                        val index = tasks.indexOfFirst { it.id == task.id }

                        if (index != -1) {
                            tasks[index] = task.copy(done = !task.done)
                        }
                    },
                    onGoToPerfil = {
                        navController.navigate("perfil")
                    }
                )
            }

            composable("perfil") {
                PerfilScreen(
                    totalSessions = totalSessions,
                    completedTasks = tasks.count { it.done },
                    totalTasks = tasks.size,
                    onGoToInicio = {
                        navController.navigate("inicio") {
                            popUpTo("inicio") {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = currentRoute == "inicio",
            onClick = {
                navController.navigate("inicio") {
                    launchSingleTop = true
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Inicio"
                )
            },
            label = {
                Text("Inicio")
            },
            colors = navColors()
        )

        NavigationBarItem(
            selected = currentRoute == "detalle",
            onClick = {
                navController.navigate("detalle") {
                    launchSingleTop = true
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Checklist,
                    contentDescription = "Detalle"
                )
            },
            label = {
                Text("Detalle")
            },
            colors = navColors()
        )

        NavigationBarItem(
            selected = currentRoute == "perfil",
            onClick = {
                navController.navigate("perfil") {
                    launchSingleTop = true
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.BarChart,
                    contentDescription = "Perfil"
                )
            },
            label = {
                Text("Perfil")
            },
            colors = navColors()
        )
    }
}

@Composable
fun navColors(): NavigationBarItemColors {
    return NavigationBarItemDefaults.colors(
        selectedIconColor = CandyPink,
        selectedTextColor = CandyPink,
        indicatorColor = SoftPink,
        unselectedIconColor = TextMuted,
        unselectedTextColor = TextMuted
    )
}

@Composable
fun CandyLogoText(
    modifier: Modifier = Modifier
) {
    Text(
        text = "CandyFocus",
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Cursive,
        color = CandyPink,
        style = TextStyle(
            shadow = Shadow(
                color = Color(0x55C66DA0),
                offset = Offset(2f, 2f),
                blurRadius = 4f
            )
        ),
        modifier = modifier
    )
}

@Composable
fun InicioScreen(
    totalSessions: Int,
    onSessionComplete: () -> Unit,
    onGoToDetalle: () -> Unit
) {
    var selectedMinutes by rememberSaveable {
        mutableStateOf(25)
    }

    var remainingSeconds by rememberSaveable {
        mutableStateOf(25 * 60)
    }

    var isRunning by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(isRunning, remainingSeconds) {
        if (isRunning && remainingSeconds > 0) {
            delay(1000)
            remainingSeconds--
        }

        if (remainingSeconds == 0 && isRunning) {
            isRunning = false
            onSessionComplete()
        }
    }

    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color.White, LightPink, SoftPink)
                )
            )
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CandyLogoText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, top = 22.dp, bottom = 8.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Focus Timer",
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextDark
            )

            Text(
                text = "Organiza tus sesiones de estudio",
                fontSize = 14.sp,
                color = TextMuted,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TimeButton("Pomodoro", selectedMinutes == 25) {
                    selectedMinutes = 25
                    remainingSeconds = 25 * 60
                    isRunning = false
                }

                TimeButton("Break", selectedMinutes == 5) {
                    selectedMinutes = 5
                    remainingSeconds = 5 * 60
                    isRunning = false
                }

                TimeButton("Long", selectedMinutes == 15) {
                    selectedMinutes = 15
                    remainingSeconds = 15 * 60
                    isRunning = false
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            Box(
                modifier = Modifier
                    .size(230.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(10.dp, SoftPink, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = "Timer",
                        tint = CandyPink,
                        modifier = Modifier.size(34.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "%02d:%02d".format(minutes, seconds),
                        fontSize = 42.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextDark
                    )

                    Text(
                        text = if (isRunning) "ENFOCÁNDOTE" else "FOCUS TIME",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        isRunning = !isRunning
                    },
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CandyPink
                    ),
                    modifier = Modifier
                        .height(54.dp)
                        .width(150.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Iniciar"
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = if (isRunning) "PAUSAR" else "START",
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(
                    onClick = {
                        remainingSeconds = selectedMinutes * 60
                        isRunning = false
                    },
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reiniciar",
                        tint = CandyPink
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 5.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Sesiones completadas",
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )

                    Text(
                        text = "$totalSessions sesiones registradas",
                        color = TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onGoToDetalle,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CandyPink
                )
            ) {
                Text("Ir a mis tareas")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun TimeButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(30.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) CandyPink else Color.White,
            contentColor = if (selected) Color.White else TextDark
        )
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}