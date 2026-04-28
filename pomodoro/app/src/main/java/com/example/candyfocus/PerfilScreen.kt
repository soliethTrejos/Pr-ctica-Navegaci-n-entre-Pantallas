package com.example.candyfocus

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun PerfilScreen(
    totalSessions: Int,
    completedTasks: Int,
    totalTasks: Int,
    onGoToInicio: () -> Unit
) {
    val context = LocalContext.current

    var selectedImageUri by rememberSaveable {
        mutableStateOf(loadProfileImageUri(context))
    }

    var showActivityChart by rememberSaveable {
        mutableStateOf(false)
    }

    var showFocusInfo by rememberSaveable {
        mutableStateOf(false)
    }

    val focusMinutes = totalSessions * 25
    val focusHours = focusMinutes / 60
    val focusRemainingMinutes = focusMinutes % 60

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: SecurityException) {
            }

            val uriString = uri.toString()
            selectedImageUri = uriString
            saveProfileImageUri(context, uriString)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color.White, LightPink, SoftPink)
                )
            )
            .verticalScroll(rememberScrollState())
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
                text = "Perfil",
                fontSize = 35.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextDark
            )



            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .size(142.dp)
                    .clip(CircleShape)
                    .background(SoftPink)
                    .border(4.dp, CandyPink, CircleShape)
                    .clickable {
                        imagePickerLauncher.launch(arrayOf("image/*"))
                    },
                contentAlignment = Alignment.Center
            ) {
                if (selectedImageUri != null) {
                    AsyncImage(
                        model = selectedImageUri,
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Usuario",
                        tint = CandyPink,
                        modifier = Modifier.size(54.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    imagePickerLauncher.launch(arrayOf("image/*"))
                },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CandyPink
                )
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = "Cambiar foto"
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Cambiar foto")
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Solieth Trejos",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextDark
            )

            Text(
                text = "Estudiante activa en CandyFocus",
                color = TextMuted,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CandyPink
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(22.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Sesiones",
                        tint = Color.White
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Sesiones completadas",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "$totalSessions",
                        color = Color.White,
                        fontSize = 44.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Text(
                        text = "Progreso acumulado durante la práctica.",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
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
                        text = "Resumen del día",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = TextDark
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Tareas completadas: $completedTasks de $totalTasks",
                        color = TextMuted
                    )

                    Text(
                        text = "Pantallas conectadas correctamente: 3",
                        color = TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MiniPerfilCard(
                    title = "Actividades",
                    value = "$completedTasks/$totalTasks",
                    icon = Icons.Default.EmojiEvents,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        showActivityChart = !showActivityChart
                        showFocusInfo = false
                    }
                )

                MiniPerfilCard(
                    title = "Enfoque",
                    value = "${focusHours}h ${focusRemainingMinutes}m",
                    icon = Icons.Default.Bolt,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        showFocusInfo = !showFocusInfo
                        showActivityChart = false
                    }
                )
            }

            if (showActivityChart) {
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(26.dp),
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
                            text = "Gráfico de actividades",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextDark
                        )

                        Text(
                            text = "Actividades completadas durante la semana",
                            color = TextMuted,
                            fontSize = 13.sp
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        WeeklyActivityChart()
                    }
                }
            }

            if (showFocusInfo) {
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = SoftPurple
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 5.dp
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = "Tiempo de concentración",
                                tint = CandyPink
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "Tiempo de concentración",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = TextDark
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "${focusHours}h ${focusRemainingMinutes}m",
                            fontSize = 34.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = CandyPink
                        )

                        Text(
                            text = "Este tiempo se calcula usando sesiones de 25 minutos.",
                            color = TextMuted,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onGoToInicio,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CandyPink
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver al inicio")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

fun saveProfileImageUri(context: Context, uri: String) {
    val sharedPreferences = context.getSharedPreferences(
        "candyfocus_preferences",
        Context.MODE_PRIVATE
    )

    sharedPreferences.edit()
        .putString("profile_image_uri", uri)
        .apply()
}

fun loadProfileImageUri(context: Context): String? {
    val sharedPreferences = context.getSharedPreferences(
        "candyfocus_preferences",
        Context.MODE_PRIVATE
    )

    return sharedPreferences.getString("profile_image_uri", null)
}

@Composable
fun WeeklyActivityChart() {
    val weeklyData = listOf(
        "Lun" to 2,
        "Mar" to 4,
        "Mié" to 5,
        "Jue" to 3,
        "Vie" to 6,
        "Sáb" to 2,
        "Dom" to 1
    )

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.Bottom
        ) {
            weeklyData.forEach { item ->
                ActivityBar(
                    day = item.first,
                    value = item.second
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "El día con mayor actividad fue viernes.",
            color = TextMuted,
            fontSize = 13.sp
        )
    }
}

@Composable
fun ActivityBar(
    day: String,
    value: Int
) {
    val barHeight = value * 22

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            text = "$value",
            fontSize = 11.sp,
            color = TextMuted
        )

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .width(24.dp)
                .height(barHeight.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            CandyPink,
                            SoftBlue
                        )
                    )
                )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = day,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )
    }
}

@Composable
fun MiniPerfilCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(110.dp)
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = CandyPink
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = title,
                color = TextMuted,
                fontSize = 12.sp
            )

            Text(
                text = value,
                color = TextDark,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}