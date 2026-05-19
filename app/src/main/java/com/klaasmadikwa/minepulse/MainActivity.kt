package com.klaasmadikwa.minepulse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.klaasmadikwa.minepulse.ui.theme.MinePulseTheme

class MainActivity : ComponentActivity() {
    private lateinit var telemetryProcessor: TelemetryProcessor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        telemetryProcessor = TelemetryProcessor(this)

        setContent {
            val truckState = remember { mutableStateOf(HaulTruckMetrics()) }

            LaunchedEffect(Unit) {
                telemetryProcessor.getLiveTelemetryStream().collect { newData ->
                    truckState.value = newData
                }
            }

            MinePulseTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
                    Column(
                        modifier = Modifier
                            .padding(padding)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "MinePulse: ${truckState.value.truckId}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))


                        SafetyStatusCard(truckState.value.fatigueScore)

                        Spacer(modifier = Modifier.height(12.dp))


                        MaintenanceAlertCard(truckState.value)

                        Spacer(modifier = Modifier.height(16.dp))


                        Row(modifier = Modifier.fillMaxWidth()) {
                            MetricTile("Payload", "${truckState.value.payloadTonnes}", "T", Modifier.weight(1f))
                            MetricTile("Vibration", "${truckState.value.vibration}", "mm/s", Modifier.weight(1f))
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.2f))
                        ) {
                            Text(
                                "RECEIVING LIVE TELEMETRY...",
                                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun SafetyStatusCard(score: Float) {
    val isDanger = score > 80f
    val statusColor = if (isDanger) Color.Red else Color(0xFF4CAF50)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = statusColor.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("DRIVER FATIGUE: $score%", color = statusColor, fontWeight = FontWeight.Bold)
            Text(
                text = if (isDanger) "ALERT: Erratic behavior detected!" else "Driver behavior within safety limits.",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun MaintenanceAlertCard(metrics: HaulTruckMetrics) {
    val isFailing = metrics.vibration > 12f
    val alertColor = if (isFailing) Color.Red else Color.Gray

    Card(
        modifier = Modifier.fillMaxWidth(),
        border = if (isFailing) androidx.compose.foundation.BorderStroke(2.dp, Color.Red) else null
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            Column {
                Text(
                    text = if (isFailing) "SERVICE REQUIRED" else "SYSTEM HEALTH: STABLE",
                    fontWeight = FontWeight.ExtraBold,
                    color = alertColor
                )
                Text(text = metrics.statusMessage, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun MetricTile(label: String, value: String, unit: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier.padding(4.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(label, style = MaterialTheme.typography.labelSmall)
            Text("$value $unit", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}