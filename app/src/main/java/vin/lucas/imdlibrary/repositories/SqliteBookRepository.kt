package vin.lucas.imdlibrary.repositories

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.content.contentValuesOf
import vin.lucas.imdlibrary.contracts.repositories.BookRepository
import vin.lucas.imdlibrary.entities.Book
import vin.lucas.imdlibrary.values.BookStoringPayload
import java.util.Date

class SqliteBookRepository(
    context: Context,
    databaseFile: String,
) : BookRepository, SQLiteOpenHelper(context, databaseFile, null, DATABASE_VERSION) {
    private companion object {
        private const val TABLE = "books"

        private const val ID_COLUMN = "id"
        private const val ISBN_COLUMN = "isbn"
        private const val TITLE_COLUMN = "title"
        private const val AUTHOR_COLUMN = "author"
        private const val PUBLISHER_COLUMN = "publisher"
        private const val PUBLICATION_YEAR_COLUMN = "publication_year"
        private const val COVER_URL_COLUMN = "cover_url"
        private const val CREATED_AT_COLUMN = "created_at"
        private const val UPDATED_AT_COLUMN = "updated_at"

        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE = """
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

        private const val SQL_EXISTS_BY_ISBN = """
            SELECT COUNT(*)
            FROM $TABLE
            WHERE $ISBN_COLUMN = ?;
        """

        private const val SQL_FIND_BY_ISBN = """
            SELECT *
            FROM $TABLE
            WHERE $ISBN_COLUMN = ?;
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

    override fun existsByIsbn(isbn: String): Boolean {
        val cursor = readableDatabase.rawQuery(SQL_EXISTS_BY_ISBN, arrayOf(isbn))
        val count = cursor.moveToFirst().let { cursor.getInt(0) }

        cursor.close()
        readableDatabase.close()

        return count > 0
    }

    override fun findByIsbn(isbn: String): Book? {
        val cursor = readableDatabase.rawQuery(SQL_FIND_BY_ISBN, arrayOf(isbn))

        if (!cursor.moveToFirst()) {
            cursor.close()
            readableDatabase.close()

            return null
        }

        val book = cursorToBook(cursor)

        cursor.close()
        readableDatabase.close()

        return book
    }

    override fun findById(id: Long): Book? {
        val cursor = readableDatabase.rawQuery(SQL_FIND_BY_ID, arrayOf(id.toString()))

        if (!cursor.moveToFirst()) {
            cursor.close()
            readableDatabase.close()

            return null
        }

        val book = cursorToBook(cursor)

        cursor.close()
        readableDatabase.close()

        return book
    }

    override fun store(payload: BookStoringPayload): Book {
        val now = System.currentTimeMillis()

        val id = writableDatabase.insert(
            TABLE,
            null,
            contentValuesOf(
                ISBN_COLUMN to payload.isbn,
                TITLE_COLUMN to payload.title,
                AUTHOR_COLUMN to payload.author,
                PUBLISHER_COLUMN to payload.publisher,
                PUBLICATION_YEAR_COLUMN to payload.publicationYear,
                COVER_URL_COLUMN to payload.coverUrl,
                CREATED_AT_COLUMN to now,
                UPDATED_AT_COLUMN to now,
            ),
        )

        writableDatabase.close()

        return Book(
            id,
            payload.isbn,
            payload.title,
            payload.author,
            payload.publisher,
            payload.publicationYear,
            payload.coverUrl,
            Date(now),
            Date(now),
        )
    }

    override fun update(book: Book): Boolean {
        val now = System.currentTimeMillis()

        val updatedCount = writableDatabase.update(
            TABLE,
            contentValuesOf(
                ISBN_COLUMN to book.isbn,
                TITLE_COLUMN to book.title,
                AUTHOR_COLUMN to book.author,
                PUBLISHER_COLUMN to book.publisher,
                PUBLICATION_YEAR_COLUMN to book.publicationYear,
                COVER_URL_COLUMN to book.coverUrl,
                CREATED_AT_COLUMN to book.createdAt.time,
                UPDATED_AT_COLUMN to now,
            ),
            "$ID_COLUMN = ?",
            arrayOf(book.id.toString()),
        )

        writableDatabase.close()

        if (updatedCount == 0) {
            return false
        }

        book.updatedAt = Date(now)

        return true
    }

    override fun delete(book: Book): Boolean {
        val deletedCount = writableDatabase.delete(
            TABLE,
            "$ID_COLUMN = ?",
            arrayOf(book.id.toString()),
        )

        writableDatabase.close()

        return deletedCount > 0
    }

    private fun cursorToBook(cursor: Cursor): Book {
        return Book(
            cursor.getLong(0),
            cursor.getString(1),
            cursor.getString(2),
            cursor.getString(3),
            cursor.getString(4),
            cursor.getInt(5),
            cursor.getString(6),
            Date(cursor.getLong(7)),
            Date(cursor.getLong(8)),
        )
    }
}
