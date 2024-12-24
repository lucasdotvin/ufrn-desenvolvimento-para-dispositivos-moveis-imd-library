package vin.lucas.imdlibrary.repositories

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
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

        private const val SQL_INSERT = """
            INSERT INTO $TABLE ($USERNAME_COLUMN, $CPF_COLUMN, $PASSWORD_COLUMN, $CREATED_AT_COLUMN, $UPDATED_AT_COLUMN)
            VALUES (?, ?, ?, ?, ?);
        """

        private const val SQL_UPDATE = """
            UPDATE $TABLE
            SET $USERNAME_COLUMN = ?, $CPF_COLUMN = ?, $PASSWORD_COLUMN = ?, $UPDATED_AT_COLUMN = ?
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
        val count = cursor.getInt(0)

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

        val id = cursor.getInt(0)
        val cpf = cursor.getString(2)
        val password = cursor.getString(3)
        val createdAt = cursor.getLong(4)
        val updatedAt = cursor.getLong(5)

        cursor.close()
        readableDatabase.close()

        return User(
            id,
            username,
            cpf,
            password,
            Date(createdAt),
            Date(updatedAt),
        )
    }

    override fun store(username: String, cpf: String, password: String): User {
        val now = System.currentTimeMillis()

        writableDatabase.execSQL(
            SQL_INSERT,
            arrayOf(username, cpf, password, now, now),
        )

        val cursor = writableDatabase.rawQuery("SELECT last_insert_rowid()", null)
        cursor.moveToFirst()

        val id = cursor.getInt(0)

        cursor.close()
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

        writableDatabase.execSQL(SQL_UPDATE, arrayOf(user.username, user.password, now, user.id))

        writableDatabase.close()

        user.updatedAt = Date(now)
    }
}
