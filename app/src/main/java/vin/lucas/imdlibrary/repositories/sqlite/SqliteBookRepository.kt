package vin.lucas.imdlibrary.repositories.sqlite

import android.database.Cursor
import androidx.core.content.contentValuesOf
import vin.lucas.imdlibrary.contracts.repositories.BookRepository
import vin.lucas.imdlibrary.entities.Book
import vin.lucas.imdlibrary.values.BookChangePayload
import java.util.Date

class SqliteBookRepository(private val driver: SqliteDriver) : BookRepository {
    private companion object Queries {
        private const val EXISTS_BY_ISBN = """
            SELECT ${SqliteDriver.Books.ID_COLUMN}
            FROM ${SqliteDriver.Books.TABLE}
            WHERE ${SqliteDriver.Books.ISBN_COLUMN} = ?;
        """

        private const val SQL_FIND_BY_ISBN = """
            SELECT *
            FROM ${SqliteDriver.Books.TABLE}
            WHERE ${SqliteDriver.Books.ISBN_COLUMN} = ?;
        """

        private const val SQL_FIND_BY_ID = """
            SELECT *
            FROM ${SqliteDriver.Books.TABLE}
            WHERE ${SqliteDriver.Books.ID_COLUMN} = ?;
        """

        private const val SQL_GET_ALL = """
            SELECT *
            FROM ${SqliteDriver.Books.TABLE};
        """
    }

    override fun retrieveIdByIsbn(isbn: String): Long? {
        val cursor = driver.readableDatabase.rawQuery(
            EXISTS_BY_ISBN,
            arrayOf(isbn),
        )

        val id = if (cursor.moveToFirst()) {
            cursor.getLong(0)
        } else {
            null
        }

        cursor.close()
        driver.readableDatabase.close()

        return id
    }

    override fun findByIsbn(isbn: String): Book? {
        val cursor = driver.readableDatabase.rawQuery(
            SQL_FIND_BY_ISBN,
            arrayOf(isbn),
        )

        if (!cursor.moveToFirst()) {
            cursor.close()
            driver.readableDatabase.close()

            return null
        }

        val book = cursorToBook(cursor)

        cursor.close()
        driver.readableDatabase.close()

        return book
    }

    override fun findById(id: Long): Book? {
        val cursor = driver.readableDatabase.rawQuery(
            SQL_FIND_BY_ID,
            arrayOf(id.toString()),
        )

        if (!cursor.moveToFirst()) {
            cursor.close()
            driver.readableDatabase.close()

            return null
        }

        val book = cursorToBook(cursor)

        cursor.close()
        driver.readableDatabase.close()

        return book
    }

    override fun store(payload: BookChangePayload): Book {
        val now = System.currentTimeMillis()

        val id = driver.writableDatabase.insert(
            SqliteDriver.Books.TABLE,
            null,
            contentValuesOf(
                SqliteDriver.Books.ISBN_COLUMN to payload.isbn,
                SqliteDriver.Books.TITLE_COLUMN to payload.title,
                SqliteDriver.Books.AUTHOR_COLUMN to payload.author,
                SqliteDriver.Books.PUBLISHER_COLUMN to payload.publisher,
                SqliteDriver.Books.PUBLICATION_YEAR_COLUMN to payload.publicationYear,
                SqliteDriver.Books.SUMMARY_COLUMN to payload.summary,
                SqliteDriver.Books.COVER_URL_COLUMN to payload.coverUrl,
                SqliteDriver.Books.CREATED_AT_COLUMN to now,
                SqliteDriver.Books.UPDATED_AT_COLUMN to now,
            ),
        )

        driver.writableDatabase.close()

        return Book(
            id,
            payload.isbn,
            payload.title,
            payload.author,
            payload.publisher,
            payload.publicationYear,
            payload.summary,
            payload.coverUrl,
            Date(now),
            Date(now),
        )
    }

    override fun update(book: Book): Boolean {
        val now = System.currentTimeMillis()

        val updatedCount = driver.writableDatabase.update(
            SqliteDriver.Books.TABLE,
            contentValuesOf(
                SqliteDriver.Books.ISBN_COLUMN to book.isbn,
                SqliteDriver.Books.TITLE_COLUMN to book.title,
                SqliteDriver.Books.AUTHOR_COLUMN to book.author,
                SqliteDriver.Books.PUBLISHER_COLUMN to book.publisher,
                SqliteDriver.Books.PUBLICATION_YEAR_COLUMN to book.publicationYear,
                SqliteDriver.Books.SUMMARY_COLUMN to book.summary,
                SqliteDriver.Books.COVER_URL_COLUMN to book.coverUrl,
                SqliteDriver.Books.CREATED_AT_COLUMN to book.createdAt.time,
                SqliteDriver.Books.UPDATED_AT_COLUMN to now,
            ),
            "${SqliteDriver.Books.ID_COLUMN} = ?",
            arrayOf(book.id.toString()),
        )

        driver.writableDatabase.close()

        if (updatedCount == 0) {
            return false
        }

        book.updatedAt = Date(now)

        return true
    }

    override fun delete(book: Book): Boolean {
        val deletedCount = driver.writableDatabase.delete(
            SqliteDriver.Books.TABLE,
            "${SqliteDriver.Books.ID_COLUMN} = ?",
            arrayOf(book.id.toString()),
        )

        driver.writableDatabase.close()

        return deletedCount > 0
    }

    override fun getAll(): List<Book> {
        val cursor = driver.readableDatabase.rawQuery(
            SQL_GET_ALL,
            null,
        )

        val books = mutableListOf<Book>()

        while (cursor.moveToNext()) {
            books.add(cursorToBook(cursor))
        }

        cursor.close()
        driver.readableDatabase.close()

        return books
    }

    private fun cursorToBook(cursor: Cursor): Book {
        return Book(
            id = cursor.getLong(0),
            isbn = cursor.getString(1),
            title = cursor.getString(2),
            author = cursor.getString(3),
            publisher = cursor.getString(4),
            publicationYear = cursor.getInt(5),
            summary = cursor.getString(6),
            coverUrl = cursor.getString(7),
            createdAt = Date(cursor.getLong(8)),
            updatedAt = Date(cursor.getLong(9)),
        )
    }
}
