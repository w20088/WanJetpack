package com.longjunhao.wanjetpack.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * .Article
 *
 * @author Admitor
 * @date 2021/06/11
 */
@Parcelize
@Entity(tableName = "articles")
data class Article(
    @PrimaryKey
    val id: Int,
    val originId: Int,
    val title: String,
    val link: String,
    val author: String,
    val shareUser: String,
    val chapterName: String,
    val superChapterName: String,
    val niceDate: String,
    val publishTime: Long,
    val envelopePic: String,
    val desc: String,
    var collect: Boolean = false,
    val fresh: Boolean = false,
    val currentPage: Int = 0
) : Parcelable