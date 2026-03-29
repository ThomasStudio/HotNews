package com.hotnews.feature.zhihu.viewmodel

import com.hotnews.api.data.ZhihuHot
import com.hotnews.base.viewmodel.BaseContract
import com.hotnews.base.viewmodel.BaseData
import com.hotnews.base.viewmodel.BaseError
import com.hotnews.base.viewmodel.BaseState
import com.hotnews.base.viewmodel.Status

/**
 * Created by thomas on 3/29/2026.
 */

interface ZhihuContract : BaseContract<ZhihuState>

data class ZhihuState(
    override val status: Status<ZhihuData, BaseError> = Status.Loading
) : BaseState<ZhihuData, BaseError>(status)

data class ZhihuData(val data: ZhihuHot) : BaseData
