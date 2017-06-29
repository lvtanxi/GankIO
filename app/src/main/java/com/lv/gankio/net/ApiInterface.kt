package com.lv.gankio.net

import com.lv.gankio.model.DailyData
import com.lv.gankio.model.DetailsData
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import rx.Observable

/**
 * Date: 2017-06-21
 * Time: 15:18
 * Description:
 */
interface ApiInterface {
    @GET("day/{year}/{month}/{day}")
    fun getDaily(@Path("year") year: Int,
                 @Path("month") month: Int, @Path("day") day: Int): Observable<DailyData>


    @GET("data/{category}/{pagesize}/{pagenum}")
    fun getContent(
            @Path("category") category: String, @Path("pagesize") pagesize: Int,
            @Path("pagenum") pagenum: Int): Observable<List<DetailsData>>


    /**
     * 搜索
     */
    @GET("search/query/{keyword}/category/{category}/count/20/page/{pageIndex}")
    fun search(@Path("category") category: String,
               @Path("keyword") keyword: String,
               @Path("pageIndex") pageIndex: Int): Observable<List<DetailsData>>

    /**
     * 搜索
     */
    @POST("add2gank")
    fun submitGank(@Body params:Map<String,String>): Observable<Void>


}