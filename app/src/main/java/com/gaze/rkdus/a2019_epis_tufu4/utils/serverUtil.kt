package com.gaze.rkdus.a2019_epis_tufu4.utils

import com.gaze.rkdus.a2019_epis_tufu4.item.*
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface ReservationBackgroundService {
    @POST("/checkNickname")
    fun resultRepos(@Body params: HashMap<String, String>) : Single<ResultData>

    @POST("user/getRegistState")
    fun resultStateRepos(@Body params: HashMap<String, Int>) : Single<ResultStateData>
}
data class ResultStateData(var regist_state: Int)

interface SerialService {
    @POST("/user/getSerialNumber")
    fun resultSerialRepos(@Body params: HashMap<String, String>) : Single<SerialData>

}
interface ReviewService {
    @POST("hospital/review/putReview")
    fun resultAddReviewRepos(@Body params: AddReviewData) : Single<ResultData>

    @POST("hospital/review/getReviewList")
    fun resultReviewListRepos(@Body params: HashMap<String, Int>) : Single<ArrayList<ReviewListItem>>
}

interface ReservationService {
    @POST("hospital/vaccine/putVaccineReservation")
    fun resultVaccineRepos(@Body params: VaccineReservationData) : Single<ResultData>

    @POST("hospital/healthScreen/putHealthCheckupReservation")
    fun resultHealthcheckupRepos(@Body params: HealthCheckupReservationData) : Single<ResultData>
}
data class ResultData(var result: Int)