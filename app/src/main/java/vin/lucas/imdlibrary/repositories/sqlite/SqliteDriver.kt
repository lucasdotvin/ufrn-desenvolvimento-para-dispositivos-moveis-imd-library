package vin.lucas.imdlibrary.repositories.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SqliteDriver(
    context: Context,
    databaseFile: String,
) : SQLiteOpenHelper(context, databaseFile, null, DATABASE_VERSION) {
    private companion object {
        private const val DATABASE_VERSION = 1
    }

    object Books {
        const val TABLE = "books"

        const val ID_COLUMN = "id"
        const val ISBN_COLUMN = "isbn"
        const val TITLE_COLUMN = "title"
        const val AUTHOR_COLUMN = "author"
        const val PUBLISHER_COLUMN = "publisher"
        const val PUBLICATION_YEAR_COLUMN = "publication_year"
        const val COVER_URL_COLUMN = "cover_url"
        const val CREATED_AT_COLUMN = "created_at"
        const val UPDATED_AT_COLUMN = "updated_at"

        const val SQL_CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS $TABLE (
                $ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT,
                $ISBN_COLUMN TEXT UNIQUE NOT NULL,
                $TITLE_COLUMN TEXT NOT NULL,
                $AUTHOR_COLUMN TEXT NOT NULL,
                $PUBLISHER_COLUMN TEXT NOT NULL,
                $PUBLICATION_YEAR_COLUMN INTEGER NOT NULL,
                $COVER_URL_COLUMN TEXT NOT NULL,
                $CREATED_AT_COLUMN TIMESTAMP NOT NULL,
                $UPDATED_AT_COLUMN TIMESTAMP NOT NULL
            );
        """
    }

    object Users {
        const val TABLE = "users"

        const val ID_COLUMN = "id"
        const val USERNAME_COLUMN = "username"
        const val CPF_COLUMN = "cpf"
        const val PASSWORD_COLUMN = "password"
        const val CREATED_AT_COLUMN = "created_at"
        const val UPDATED_AT_COLUMN = "updated_at"

        const val SQL_CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS $TABLE (
                $ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT,
                $USERNAME_COLUMN TEXT UNIQUE NOT NULL,
                $CPF_COLUMN TEXT NOT NULL,
                $PASSWORD_COLUMN TEXT NOT NULL,
                $CREATED_AT_COLUMN TIMESTAMP NOT NULL,
                $UPDATED_AT_COLUMN TIMESTAMP NOT NULL
            );
        """
    }

    override fun onCreate(p0: SQLiteDatabase) {
        p0.execSQL(Books.SQL_CREATE_TABLE)
        p0.execSQL(Users.SQL_CREATE_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase, p1: Int, p2: Int) {
        // Não temos nada a fazer aqui por enquanto.
    }
}
