package vin.lucas.imdlibrary.repositories.sqlite

import android.database.Cursor
import androidx.core.content.contentValuesOf
import vin.lucas.imdlibrary.contracts.repositories.UserRepository
import vin.lucas.imdlibrary.entities.User
import java.util.Date

class SqliteUserRepository(private val driver: SqliteDriver) : UserRepository {
    private companion object Queries {
        private const val SQL_EXISTS_BY_USERNAME = """
            SELECT COUNT(*)
            FROM ${SqliteDriver.Users.TABLE}
            WHERE ${SqliteDriver.Users.USERNAME_COLUMN} = ?;
        """

        private const val SQL_FIND_BY_USERNAME = """
            SELECT *
            FROM ${SqliteDriver.Users.TABLE}
            WHERE ${SqliteDriver.Users.USERNAME_COLUMN} = ?;
        """

        private const val SQL_FIND_BY_ID = """
            SELECT *
            FROM ${SqliteDriver.Users.TABLE}
            WHERE ${SqliteDriver.Users.ID_COLUMN} = ?;
        """
    }

    override fun existsByUsername(username: String): Boolean {
        val cursor = driver.readableDatabase.rawQuery(SQL_EXISTS_BY_USERNAME, arrayOf(username))
        val count = cursor.moveToFirst().let { cursor.getInt(0) }

        cursor.close()
        driver.readableDatabase.close()

        return count > 0
    }

    override fun findByUsername(username: String): User? {
        val cursor = driver.readableDatabase.rawQuery(SQL_FIND_BY_USERNAME, arrayOf(username))

        if (!cursor.moveToFirst()) {
            cursor.close()
            driver.readableDatabase.close()

            return null
        }

        val user = cursorToUser(cursor)

        cursor.close()
        driver.readableDatabase.close()

        return user
    }

    override fun findById(id: Long): User? {
        val cursor = driver.readableDatabase.rawQuery(SQL_FIND_BY_ID, arrayOf(id.toString()))

        if (!cursor.moveToFirst()) {
            cursor.close()
            driver.readableDatabase.close()

            return null
        }

        val user = cursorToUser(cursor)

        cursor.close()
        driver.readableDatabase.close()

        return user
    }

    override fun store(username: String, cpf: String, password: String): User {
        val now = System.currentTimeMillis()

        val id = driver.writableDatabase.insert(
            SqliteDriver.Users.TABLE,
            null,
            contentValuesOf(
                SqliteDriver.Users.USERNAME_COLUMN to username,
                SqliteDriver.Users.CPF_COLUMN to cpf,
                SqliteDriver.Users.PASSWORD_COLUMN to password,
                SqliteDriver.Users.CREATED_AT_COLUMN to now,
                SqliteDriver.Users.UPDATED_AT_COLUMN to now,
            ),
        )

        driver.writableDatabase.close()

        return User(
            id,
            username,
            cpf,
            password,
            Date(now),
            Date(now),
        )
    }

    override fun update(user: User) {
        val now = System.currentTimeMillis()

        driver.writableDatabase.update(
            SqliteDriver.Users.TABLE,
            contentValuesOf(
                SqliteDriver.Users.USERNAME_COLUMN to user.username,
                SqliteDriver.Users.CPF_COLUMN to user.cpf,
                SqliteDriver.Users.PASSWORD_COLUMN to user.password,
                SqliteDriver.Users.UPDATED_AT_COLUMN to now,
            ),
            "${SqliteDriver.Users.ID_COLUMN} = ?",
            arrayOf(user.id.toString()),
        )

        driver.writableDatabase.close()

        user.updatedAt = Date(now)
    }

    private fun cursorToUser(cursor: Cursor): User {
        return User(
            cursor.getLong(0),
            cursor.getString(1),
            cursor.getString(2),
            cursor.getString(3),
            Date(cursor.getLong(4)),
            Date(cursor.getLong(5)),
        )
    }
}
