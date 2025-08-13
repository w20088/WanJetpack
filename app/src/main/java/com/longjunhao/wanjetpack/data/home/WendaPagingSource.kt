package com.longjunhao.wanjetpack.data.home

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.longjunhao.wanjetpack.api.WanJetpackApi
import com.longjunhao.wanjetpack.data.Article

/**
 * .WendaPagingSource
 *
 * @author Admitor
 * @date 2021/05/25
 */

private const val WENDA_STARTING_PAGE_INDEX = 1

class WendaPagingSource(
    private val api: WanJetpackApi
) : PagingSource<Int, Article>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: WENDA_STARTING_PAGE_INDEX
        return try {
            val response = api.getWenda(page)
            val datas = response.data.datas
            LoadResult.Page(
                data = datas,
                prevKey = if (page == WENDA_STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (page == response.data.pageCount) null else page + 1,
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return null
    }
}