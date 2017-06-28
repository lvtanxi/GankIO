package com.lv.gankio.net

/**
 * Date: 2017-06-21
 * Time: 17:29
 * Description:
 */
class HttpException(val code: Int, override val message: String?) : RuntimeException(message)