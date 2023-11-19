package com.ddx.kt.datamodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ddx.kt.viewmodel.LoginState

@Entity
data class User(
    @PrimaryKey val name: String?,
    @ColumnInfo(name = "password") val password: String?,
    @ColumnInfo(name = "icon") val icon: String?,
    @ColumnInfo(name = "intro") val intro: String?,
    @ColumnInfo(name = "loginState") val loginState: LoginState?,
    @ColumnInfo(name = "lastLoginTime") val lastOnlineTime: String?,

    )

@Dao
interface UserDao {
    @Query("SELECT * FROM user ORDER BY lastLoginTime DESC LIMIT 1")
    fun getUser(): User

    @Insert
    fun insert(vararg users: User)

    @Delete
    fun delete(user: User)
}

@Database(entities = [User::class], version = 1)
abstract class UserDataBase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: UserDataBase? = null

        fun init(context: Context): UserDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDataBase::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }

        fun getInstance(): UserDataBase? = INSTANCE
    }
}
