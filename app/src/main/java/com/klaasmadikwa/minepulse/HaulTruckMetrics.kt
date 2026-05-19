package com.klaasmadikwa.minepulse

/**
 * Represents a snapshot of a Haul Truck's status.
 * Covers Payload, Safety (Fatigue), and Maintenance.
 */
data class HaulTruckMetrics(
    val truckId: String = "TRK-204",
    val payloadTonnes: Float = 0f,        // Payload Monitoring
    val speedKph: Float = 0f,            // Fleet Management
    val fatigueScore: Float = 0f,        // Driver Safety
    val engineTemp: Float = 0f,          // Predictive Maintenance
    val isAnomalyDetected: Boolean = false,
    val statusMessage: String = "System Ready",
    val vibration: Float = 0f,            // Maintenance Monitoring
)

