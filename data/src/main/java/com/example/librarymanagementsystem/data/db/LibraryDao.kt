package com.example.librarymanagementsystem.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LibraryDao {
    @Query("SELECT * FROM library_items ORDER BY title ASC LIMIT :limit")
    suspend fun loadInitialByName(limit: Int): List<LibraryItemEntity>

    @Query("SELECT * FROM library_items ORDER BY addedAt DESC LIMIT :limit")
    suspend fun loadInitialByDate(limit: Int): List<LibraryItemEntity>

    @Query("""
      SELECT * FROM library_items
      WHERE title > (SELECT title FROM library_items WHERE uid = :lastUid)
      ORDER BY title ASC
      LIMIT :pageSize
    """)
    suspend fun loadAfterByName(lastUid: Long, pageSize: Int): List<LibraryItemEntity>

    @Query("""
      SELECT * FROM library_items
      WHERE addedAt < (SELECT addedAt FROM library_items WHERE uid = :lastUid)
      ORDER BY addedAt DESC
      LIMIT :pageSize
    """)
    suspend fun loadAfterByDate(lastUid: Long, pageSize: Int): List<LibraryItemEntity>

    @Query("""
      SELECT * FROM library_items
      WHERE title < (SELECT title FROM library_items WHERE uid = :firstUid)
      ORDER BY title DESC
      LIMIT :pageSize
    """)
    suspend fun loadBeforeByName(firstUid: Long, pageSize: Int): List<LibraryItemEntity>

    @Query("""
      SELECT * FROM library_items
      WHERE addedAt > (SELECT addedAt FROM library_items WHERE uid = :firstUid)
      ORDER BY addedAt ASC
      LIMIT :pageSize
    """)
    suspend fun loadBeforeByDate(firstUid: Long, pageSize: Int): List<LibraryItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: LibraryItemEntity)

    @Delete
    suspend fun delete(item: LibraryItemEntity)
}
