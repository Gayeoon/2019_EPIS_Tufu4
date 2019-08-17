package com.gaze.rkdus.a2019_epis_tufu4.item

data class VaccineReservationData(var user_id: String, var owner_name: String, var owner_phone: String, var pet_name: String,
                                  var pet_age: Int, var pet_gender: Int, var pet_weight: String, var vaccine_name: String,
                                  var reservation_date: String, var reservation_time : String)

data class HealthCheckupReservationData(var user_id: String, var owner_name: String, var owner_phone: String, var pet_name: String,
                                        var pet_age: Int, var pet_gender: Int, var pet_weight: String,
                                        var reservation_date: String, var reservation_time : String)