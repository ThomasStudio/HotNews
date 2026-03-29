package com.hotnews.feature.zhihu.viewmodel

import androidx.lifecycle.viewModelScope
import com.hotnews.api.ApiResult.Error
import com.hotnews.api.ApiResult.Success
import com.hotnews.base.viewmodel.BaseError
import com.hotnews.base.viewmodel.BaseViewModel
import com.hotnews.base.viewmodel.Status
import com.hotnews.repository.ZhihuRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by thomas on 3/5/2025.
 */

@HiltViewModel
class ZhihuViewModel @Inject constructor(
    val repository: ZhihuRepository,
) : BaseViewModel<ZhihuState>(), ZhihuContract {
    override fun initialState() = ZhihuState()

    init {
        getData()
    }

    private fun getData() =
        viewModelScope.launch {
            when (val result = repository.getHot()) {
                is Success -> updateState {
                    copy(status = Status.Success(ZhihuData(result.data)))
                }


                is Error -> updateState {
                    copy(
                        status = Status.Error(
                            BaseError(
                                result.code,
                                result.message ?: "Unknown error"
                            )
                        )
                    )

                }
            }
        }
}