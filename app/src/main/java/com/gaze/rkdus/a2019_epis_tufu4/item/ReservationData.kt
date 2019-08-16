package com.gaze.rkdus.a2019_epis_tufu4.item

data class VaccineReservationData(var user_id: String, var owner_name: String, var owner_hp: String, var pet_name: String,
                                  var pet_age: Int, var pet_weight: String, var pet_gender: Int, var vaccine_name: String,
                                  var vaccine_date: String, var vaccine_time : String)

data class HealthCheckupReservationData(var user_id: String, var owner_name: String, var owner_hp: String, var pet_name: String,
                                        var pet_age: Int, var pet_weight: String, var pet_gender: Int,
                                        var healthcheckup_date: String, var healthcheckup_time : String)