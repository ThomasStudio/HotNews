package com.hotnews.api

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ApiResultTest {

    @Test
    fun `Success data and type check`() {
        val testData = "Test Data"
        val success = ApiResult.Success(testData)

        assertEquals(success.data, testData)

        assertThat(success).isInstanceOf(ApiResult.Success::class.java)
        assertThat(success).isInstanceOf(ApiResult::class.java)
    }


    @Test
    fun `Success equal test`() {
        val success1 = ApiResult.Success(100)
        val success2 = ApiResult.Success(100)

        assertThat(success1).isEqualTo(success2)
        assertThat(success1.hashCode()).isEqualTo(success2.hashCode())
    }

    @Test
    fun `Success not equal test`() {
        val success1 = ApiResult.Success("A")
        val success2 = ApiResult.Success("B")

        assertThat(success1).isNotEqualTo(success2)
    }
    // endregion

    // region Error 类型测试
    @Test
    fun `Error data test`() {
        val errorCode = 404
        val errorMessage = "Not Found"
        val error = ApiResult.Error(errorCode, errorMessage)

        assertThat(error.code).isEqualTo(errorCode)
        assertThat(error.message).isEqualTo(errorMessage)
        assertThat(error).isInstanceOf(ApiResult.Error::class.java)
    }

    @Test
    fun `Error equal test`() {
        val error1 = ApiResult.Error(500, "Internal Error")
        val error2 = ApiResult.Error(500, "Internal Error")

        assertThat(error1).isEqualTo(error2)
        assertThat(error1.hashCode()).isEqualTo(error2.hashCode())
    }

    @Test
    fun `Error not equal test`() {
        val baseError = ApiResult.Error(400, "Bad Request")
        val diffCode = baseError.copy(code = 401)
        val diffMessage = baseError.copy(message = "Invalid Request")
        val nullMessage = baseError.copy(message = null)

        assertThat(baseError).isNotEqualTo(diffCode)
        assertThat(baseError).isNotEqualTo(diffMessage)
        assertThat(baseError).isNotEqualTo(nullMessage)
    }
    // endregion

    // region 类型互斥测试
    @Test
    fun `Success should not equal Error`() {
        val success = ApiResult.Success(Unit)
        val error = ApiResult.Error(-1, null)

        assertThat(success).isNotEqualTo(error)
        assertThat(error).isNotEqualTo(success)
    }
    // endregion

    // region 边界条件测试
    @Test
    fun `Error with null message test`() {
        val error = ApiResult.Error(204, null)

        assertThat(error.code).isEqualTo(204)
        assertThat(error.message).isNull()
    }

    @Test
    fun `Success with null message test`() {
        val success = ApiResult.Success(emptyList<String>())

        assertThat(success.data).isEmpty()
    }

    @Test
    fun `Error copy test`() {
        val original = ApiResult.Error(500, "Server Error")
        val modified = original.copy(code = 503)

        assertThat(modified.code).isEqualTo(503)
        assertThat(modified.message).isEqualTo("Server Error")
    }

    @Test
    fun `toString test`() {
        val success = ApiResult.Success(listOf(1, 2, 3))
        val error = ApiResult.Error(301, "Moved")

        assertThat(success.toString()).contains("data=[1, 2, 3]")
        assertThat(error.toString()).contains("code=301")
        assertThat(error.toString()).contains("message=Moved")
    }

}