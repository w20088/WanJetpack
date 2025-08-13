package com.longjunhao.wanjetpack.data.home

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.LoadType.*
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.longjunhao.wanjetpack.api.WanJetpackApi
import com.longjunhao.wanjetpack.data.Article
import com.longjunhao.wanjetpack.data.AppDatabase
import retrofit2.HttpException
import java.io.IOException

/**
 * .ArticleRemoteMediator
 *
 * @author Admitor
 * @date 2021/07/05
 *
 * todo 两个问题待优化（以下问题官方demo：PagingWithNetworkSample也会存在问题），期待正式版中能够优化：
 *   1. articleDao.insertAll(data)，插入的数据无序，导致在getLocalArticle()中还要排序
 *   2. 每次执行RemoteMediator.load()中的APPEND后，RecyclerView会整页刷新，且滚动到最顶端。
 *   3. 新安装首次进入界面时，当REFRESH后，在执行APPEND时会加载第二页，导致加载前两页数据。但是下拉刷新执行REFRESH后，再执行APPEND时，就不会加载第二页。
 *
 */

private const val TAG = "RemoteMediator"
private const val HOME_ARTICLE_REMOTE_TYPE = "homeArticle"
private const val HOME_STARTING_PAGE_INDEX = 0

@OptIn(ExperimentalPagingApi::class)
class ArticleRemoteMediator(
    private val api: WanJetpackApi,
    private val database: AppDatabase
) : RemoteMediator<Int, Article>() {
    private val articleDao = database.articleDao()

    override suspend fun initialize(): InitializeAction {
        // Require that remote REFRESH is launched on initial load and succeeds before launching
        // remote PREPEND / APPEND.
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Article>
    ): MediatorResult {
        try {
            // 计算要加载的页面
            // API 请求参数 page 从 0 开始，但返回的 curPage 从 1 开始
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val currentPage = state.anchorPosition?.let { position ->
                        state.closestItemToPosition(position)?.let { article ->
                            articleDao.getCurrentPageByArticleId(article.id)
                        }
                    }
                    (currentPage ?: 1) - 1  // 转换为 API 请求的 0 基页码
                }
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val currentPage = state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
                        ?.let { article ->
                            articleDao.getCurrentPageByArticleId(article.id)
                        }
                    if (currentPage == null) {
                        // 如果无法获取当前页码，从第1页开始加载
                        // 这通常发生在数据库为空或数据不完整的情况下
                        Log.w(TAG, "Cannot get current page, starting from page 1")
                        0  // API请求第0页，返回curPage=1
                    } else {
                        currentPage  // 加载下一页，API 请求参数 = currentPage
                    }
                }
            }

            // 从网络获取数据
            val response = api.getHomeArticle(page)

            val articles = response.data.datas
            val apiCurrentPage = response.data.curPage  // API 返回的页码（从 1 开始）
            val endOfPaginationReached = response.data.over

            // 在数据库事务中处理数据
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    // 刷新时清除所有数据
                    articleDao.clearArticles()
                }

                // 插入新数据，直接使用 API 返回的 curPage（已经是从 1 开始）
                val articlesWithPage = articles.map { article ->
                    article.copy(currentPage = apiCurrentPage)
                }
                articleDao.insertArticles(articlesWithPage)
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (e: IOException) {
            Log.e(TAG, "Error loading data，IOException: ${e.message}")
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            Log.e(TAG, "Error loading data， HttpException: ${e.message}")
            return MediatorResult.Error(e)
        }
    }
}