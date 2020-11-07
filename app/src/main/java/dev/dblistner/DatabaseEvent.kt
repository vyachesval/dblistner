package dev.dblistner

data class DatabaseEvent<T>(val eventType: DatabaseEventType, val value: T)