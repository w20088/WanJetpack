package com.longjunhao.wanjetpack.data

import com.google.gson.annotations.SerializedName

/**
 * .WanJetResponse
 *
 * @author Admitor
 * @date 2021/06/08
 */

data class ApiPage<T>(
    val curPage: Int,
    val datas: List<T>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)
