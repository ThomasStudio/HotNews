package com.hotnews.feature.weibo.viewmodel

import com.hotnews.api.data.HotItem
import com.hotnews.api.data.WeiboHot
import com.hotnews.base.viewmodel.BaseContract
import com.hotnews.base.viewmodel.BaseData
import com.hotnews.base.viewmodel.BaseError
import com.hotnews.base.viewmodel.BaseState
import com.hotnews.base.viewmodel.Status
import com.hotnews.feature.zhihu.viewmodel.ZhihuData

/**
 * Created by thomas on 3/29/2026.
 */

interface WeiboContract : BaseContract<WeiboState> {
    fun openUrl(item: HotItem)
}

class WeiboState(
    override val status: Status<WeiboData, BaseError> = Status.Loading
) : BaseState<WeiboData, BaseError>()

data class WeiboData(val data: WeiboHot) : BaseData
