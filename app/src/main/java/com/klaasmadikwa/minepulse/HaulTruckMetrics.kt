package com.klaasmadikwa.minepulse

/**
 * Represents a snapshot of a Haul Truck's status.
 * Covers Payload, Safety (Fatigue), and Maintenance.
 */
data class HaulTruckMetrics(
    val truckId: String = "TRK-204",
    val payloadTonnes: Float = 0f,        
    val speedKph: Float = 0f,            
    val fatigueScore: Float = 0f,        
    val engineTemp: Float = 0f,          
    val isAnomalyDetected: Boolean = false,
    val statusMessage: String = "System Ready",
    val vibration: Float = 0f,            
)

