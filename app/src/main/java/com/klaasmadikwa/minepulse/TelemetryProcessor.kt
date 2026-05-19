package com.klaasmadikwa.minepulse

import android.content.Context
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TelemetryProcessor(context: Context) {
    private val db = MinePulseDB(context)

    /**
     * Data Engineering Pipeline:
     * Simulates a real-time stream of sensor data every 2 seconds.
     */
    fun getLiveTelemetryStream(): Flow<HaulTruckMetrics> = flow {
        while (true) {
            // 1. Ingest raw data (Simulating sensors)
            val raw = HaulTruckMetrics(
                payloadTonnes = (190..210).random().toFloat(),
                speedKph = (30..55).random().toFloat(),
                fatigueScore = (10..95).random().toFloat(),
                vibration = (2..15).random().toFloat()
            )

            // 2. Process through Forensic & Predictive Maintenance logic
            val processed = processTelemetry(raw)

            // 3. Emit the cleaned data to the UI "pipe"
            emit(processed)

            // 4. Frequency of the heartbeat (2 seconds)
            delay(2000)
        }
    }

    fun processTelemetry(raw: HaulTruckMetrics): HaulTruckMetrics {
        var processed = runForensicScan(raw)

        if (processed.vibration > 12f) {
            processed = processed.copy(
                statusMessage = "CRITICAL: Mechanical Failure Imminent",
                isAnomalyDetected = true
            )
        }

        db.insertLog(processed)
        return processed
    }

    private fun runForensicScan(raw: HaulTruckMetrics): HaulTruckMetrics {
        val speedError = raw.speedKph > 100f
        val payloadError = raw.payloadTonnes < 0f

        return if (speedError || payloadError) {
            raw.copy(
                speedKph = if (speedError) 60f else raw.speedKph,
                payloadTonnes = if (payloadError) 0f else raw.payloadTonnes,
                isAnomalyDetected = true,
                statusMessage = "Sensor Noise Corrected"
            )
        } else {
            raw.copy(isAnomalyDetected = false, statusMessage = "Operational")
        }
    }
}