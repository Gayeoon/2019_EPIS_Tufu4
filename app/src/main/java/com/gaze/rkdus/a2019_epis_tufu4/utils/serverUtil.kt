package com.gaze.rkdus.a2019_epis_tufu4.utils

import com.gaze.rkdus.a2019_epis_tufu4.item.AddReviewData
import com.gaze.rkdus.a2019_epis_tufu4.item.HealthCheckupReservationData
import com.gaze.rkdus.a2019_epis_tufu4.item.VaccineReservationData
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface ReservationBackgroundService {
    @POST("/checkNickname")
    fun resultRepos(@Body params: HashMap<String, String>) : Single<ResultData>
}

interface ReviewService {
    @POST("review/addReview")
    fun resultAddReviewRepos(@Body params: AddReviewData) : Single<ResultData>
}

interface ReservationService {
    @POST("vaccine/addVaccineReservation")
    fun resultVaccineRepos(@Body params: VaccineReservationData) : Single<ResultData>

    @POST("healthcheckup/addHealthCheckupReservation")
    fun resultHealthcheckupRepos(@Body params: HealthCheckupReservationData) : Single<ResultData>
}
data class ResultData(var result: Int)