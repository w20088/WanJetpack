package com.longjunhao.wanjetpack.data

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * .ArticleDao
 *
 * @author Admitor
 * @date 2021/07/05
 */

@Dao
interface ArticleDao {

    @Query("SELECT * FROM articles ORDER BY publishTime DESC")
    fun getArticles(): PagingSource<Int, Article>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<Article>)

    @Query("DELETE FROM articles")
    suspend fun clearArticles()

    @Query("SELECT * FROM articles WHERE id = :articleId")
    suspend fun getArticleById(articleId: Int): Article?

    @Query("SELECT COUNT(*) FROM articles")
    suspend fun getArticleCount(): Int

    @Query("SELECT currentPage FROM articles WHERE id = :articleId")
    suspend fun getCurrentPageByArticleId(articleId: Int): Int?
}