package vin.lucas.imdlibrary.repositories

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.content.contentValuesOf
import vin.lucas.imdlibrary.contracts.repositories.UserRepository
import vin.lucas.imdlibrary.entities.User
import java.util.Date

class SqliteUserRepository(
    context: Context,
    databaseFile: String,
) : UserRepository, SQLiteOpenHelper(context, databaseFile, null, DATABASE_VERSION) {
    private companion object {
        private const val TABLE = "users"

        private const val ID_COLUMN = "id"
        private const val USERNAME_COLUMN = "username"
        private const val CPF_COLUMN = "cpf"
        private const val PASSWORD_COLUMN = "password"
        private const val CREATED_AT_COLUMN = "created_at"
        private const val UPDATED_AT_COLUMN = "updated_at"

        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS $TABLE (
                $ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT,
                $USERNAME_COLUMN TEXT UNIQUE NOT NULL,
                $CPF_COLUMN TEXT NOT NULL,
                $PASSWORD_COLUMN TEXT NOT NULL,
                $CREATED_AT_COLUMN TIMESTAMP NOT NULL,
                $UPDATED_AT_COLUMN TIMESTAMP NOT NULL
            );
        """

        private const val SQL_EXISTS_BY_USERNAME = """
            SELECT COUNT(*)
            FROM $TABLE
            WHERE $USERNAME_COLUMN = ?;
        """

        private const val SQL_FIND_BY_USERNAME = """
            SELECT *
            FROM $TABLE
            WHERE $USERNAME_COLUMN = ?;
        """

        private const val SQL_FIND_BY_ID = """
            SELECT *
            FROM $TABLE
            WHERE $ID_COLUMN = ?;
        """
    }

    override fun onCreate(p0: SQLiteDatabase) {
        p0.execSQL(SQL_CREATE_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase, p1: Int, p2: Int) {
        // Não temos nada a fazer aqui por enquanto.
    }

    override fun existsByUsername(username: String): Boolean {
        val cursor = readableDatabase.rawQuery(SQL_EXISTS_BY_USERNAME, arrayOf(username))
        val count = cursor.moveToFirst().let { cursor.getInt(0) }

        cursor.close()
        readableDatabase.close()

        return count > 0
    }

    override fun findByUsername(username: String): User? {
        val cursor = readableDatabase.rawQuery(SQL_FIND_BY_USERNAME, arrayOf(username))

        if (!cursor.moveToFirst()) {
            cursor.close()
            readableDatabase.close()

            return null
        }

        val user = cursorToUser(cursor)

        cursor.close()
        readableDatabase.close()

        return user
    }

    override fun findById(id: Long): User? {
        val cursor = readableDatabase.rawQuery(SQL_FIND_BY_ID, arrayOf(id.toString()))

        if (!cursor.moveToFirst()) {
            cursor.close()
            readableDatabase.close()

            return null
        }

        val user = cursorToUser(cursor)

        cursor.close()
        readableDatabase.close()

        return user
    }

    override fun store(username: String, cpf: String, password: String): User {
        val now = System.currentTimeMillis()

        val id = writableDatabase.insert(
            TABLE,
            null,
            contentValuesOf(
                USERNAME_COLUMN to username,
                CPF_COLUMN to cpf,
                PASSWORD_COLUMN to password,
                CREATED_AT_COLUMN to now,
                UPDATED_AT_COLUMN to now,
            ),
        )

        writableDatabase.close()

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

        writableDatabase.update(
            TABLE,
            contentValuesOf(
                USERNAME_COLUMN to user.username,
                CPF_COLUMN to user.cpf,
                PASSWORD_COLUMN to user.password,
                UPDATED_AT_COLUMN to now,
            ),
            "$ID_COLUMN = ?",
            arrayOf(user.id.toString()),
        )

        writableDatabase.close()

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
