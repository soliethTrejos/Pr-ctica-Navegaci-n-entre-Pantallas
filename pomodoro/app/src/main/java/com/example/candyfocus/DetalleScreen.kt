package com.example.candyfocus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DetalleScreen(
    tasks: List<FocusTask>,
    onAddTask: (String) -> Unit,
    onToggleTask: (FocusTask) -> Unit,
    onGoToPerfil: () -> Unit
) {
    var newTask by rememberSaveable {
        mutableStateOf("")
    }

    val pendingTasks = tasks.filter { !it.done }
    val completedTasks = tasks.filter { it.done }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color.White, LightPink, SoftPink)
                )
            )
    ) {
        CandyLogoText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, top = 22.dp, bottom = 8.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            item {
                Text(
                    text = "Detalle de tareas",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextDark
                )

                Text(
                    text = "Agrega, revisa y completa tus actividades de enfoque.",
                    color = TextMuted,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = newTask,
                            onValueChange = {
                                newTask = it
                            },
                            placeholder = {
                                Text("Escribe una tarea...")
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Buscar"
                                )
                            },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = RoundedCornerShape(18.dp)
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        FloatingActionButton(
                            onClick = {
                                if (newTask.isNotBlank()) {
                                    onAddTask(newTask.trim())
                                    newTask = ""
                                }
                            },
                            containerColor = CandyPink,
                            contentColor = Color.White
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Agregar tarea"
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Tareas pendientes: ${pendingTasks.size}",
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(10.dp))
            }

            items(pendingTasks) { task ->
                TaskCard(
                    task = task,
                    onToggleTask = onToggleTask
                )
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = SoftPurple
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Completadas",
                                tint = CandyPink
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "Completadas hoy",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = TextDark
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        if (completedTasks.isEmpty()) {
                            Text(
                                text = "Aún no has completado tareas.",
                                color = TextMuted,
                                fontSize = 14.sp
                            )
                        } else {
                            completedTasks.forEach { task ->
                                Text(
                                    text = "✓ ${task.title}",
                                    color = TextMuted,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onGoToPerfil,
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CandyPink
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ver resumen del perfil")
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun TaskCard(
    task: FocusTask,
    onToggleTask: (FocusTask) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.done,
                onCheckedChange = {
                    onToggleTask(task)
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = CandyPink,
                    uncheckedColor = CandyPink
                )
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = task.title,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )

                Text(
                    text = "Tarea de concentración",
                    color = TextMuted,
                    fontSize = 12.sp
                )
            }
        }
    }
}