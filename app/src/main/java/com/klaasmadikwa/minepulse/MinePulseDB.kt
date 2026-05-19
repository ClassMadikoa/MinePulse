package com.klaasmadikwa.minepulse

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MinePulseDB(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "MinePulse.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "HaulTruckLogs"


        private const val COLUMN_ID = "id"
        private const val COLUMN_TRUCK_ID = "truckId"
        private const val COLUMN_PAYLOAD = "payload"
        private const val COLUMN_SPEED = "speed"
        private const val COLUMN_FATIGUE = "fatigue"
        private const val COLUMN_IS_ANOMALY = "isAnomaly"
        private const val COLUMN_TIMESTAMP = "timestamp"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = ("CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_TRUCK_ID TEXT, " +
                "$COLUMN_PAYLOAD REAL, " +
                "$COLUMN_SPEED REAL, " +
                "$COLUMN_FATIGUE REAL, " +
                "$COLUMN_IS_ANOMALY INTEGER, " +
                "$COLUMN_TIMESTAMP DEFAULT CURRENT_TIMESTAMP)")
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertLog(metrics: HaulTruckMetrics) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TRUCK_ID, metrics.truckId)
            put(COLUMN_PAYLOAD, metrics.payloadTonnes)
            put(COLUMN_SPEED, metrics.speedKph)
            put(COLUMN_FATIGUE, metrics.fatigueScore)
            put(COLUMN_IS_ANOMALY, if (metrics.isAnomalyDetected) 1 else 0)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }
}