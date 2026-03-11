package com.example.contafacil.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.contafacil.data.local.dao.ExpenseDao
import com.example.contafacil.data.local.dao.ProductDao
import com.example.contafacil.data.local.dao.TransactionDao
import com.example.contafacil.data.local.entity.ExpenseEntity
import com.example.contafacil.data.local.entity.ProductEntity
import com.example.contafacil.data.local.entity.TransactionEntity

@Database(
    entities = [
        ProductEntity::class,
        TransactionEntity::class,
        ExpenseEntity::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun transactionDao(): TransactionDao
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "contafacil_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
