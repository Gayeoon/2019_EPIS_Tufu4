package com.gaze.rkdus.a2019_epis_tufu4.user

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.gaze.rkdus.a2019_epis_tufu4.BaseActivity
import com.gaze.rkdus.a2019_epis_tufu4.R
import com.gaze.rkdus.a2019_epis_tufu4.adapter.MessageSpinnerAdapter
import com.gaze.rkdus.a2019_epis_tufu4.item.AddReviewData
import com.gaze.rkdus.a2019_epis_tufu4.item.VaccineReservationData
import com.gaze.rkdus.a2019_epis_tufu4.utils.ReservationService
import com.gaze.rkdus.a2019_epis_tufu4.utils.ReviewService
import com.gaze.rkdus.a2019_epis_tufu4.utils.userUtil.Companion.checkEditText
import com.gaze.rkdus.a2019_epis_tufu4.utils.userUtil.Companion.setSpinnerMaxHeight
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_vaccine_message.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class VaccineMessageActivity : BaseActivity() {
    private val SPINNER_HEIGHT: Int = 350
    var hospitalKey: Int? = null
    var hospitalName: String? = null
    var ownerName: String? = null
    var ownerHP: String? = null
    var petName: String? = null
    var petAge: Int? = 0
    var petWeight: String? = null
    var petGender: Int = 0  // 0: default,  1: female,  2: male
    var vaccineName: String? = null
    var vaccineYear: String? = null
    var vaccineMonth: String? = null
    var vaccineDay: String? = null
    var vaccineHour: String? = null
    var vaccineMinute: String? = null
    var vaccineDate: String? = null
    var vaccineTime: String? = null

    var yearArray = ArrayList<String>()
    var monthArray = ArrayList<String>()
    var dayArray = ArrayList<String>()
    var hourArray = ArrayList<String>()
    var minuteArray = ArrayList<String>()

    var aaYear: MessageSpinnerAdapter? = null
    var aaMonth: MessageSpinnerAdapter? = null
    var aaDay: MessageSpinnerAdapter? = null
    var aaHour: MessageSpinnerAdapter? = null
    var aaMinute: MessageSpinnerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vaccine_message)

        if (intent == null)
            finishPopup()

        intent.let {
            if (intent.hasExtra("key")) {
                if (intent.hasExtra("hospitalName")) {
                    hospitalKey = intent.getIntExtra("key", 0)
                    hospitalName = intent.getStringExtra("hospitalName")
                    // Spinner 값 설정
                    setDateArrayForSpinner()
                    seTimeForSpinner()
                }
                else
                    finishPopup()
            }
            else
                finishPopup()
        }

        // ArrayAdapter 정의 및 초기변수 설정
        aaYear = MessageSpinnerAdapter(this, R.layout.message_spinner, yearArray)
        aaMonth = MessageSpinnerAdapter(this, R.layout.message_spinner, monthArray)
        aaDay = MessageSpinnerAdapter(this, R.layout.message_spinner, dayArray)
        aaHour = MessageSpinnerAdapter(this, R.layout.message_spinner, hourArray)
        aaMinute = MessageSpinnerAdapter(this, R.layout.message_spinner, minuteArray)

        aaYear!!.setDropDownViewResource(R.layout.message_custom_simple_dropdown_item)
        aaMonth!!.setDropDownViewResource(R.layout.message_custom_simple_dropdown_item)
        aaDay!!.setDropDownViewResource(R.layout.message_custom_simple_dropdown_item)
        aaHour!!.setDropDownViewResource(R.layout.message_custom_simple_dropdown_item)
        aaMinute!!.setDropDownViewResource(R.layout.message_custom_simple_dropdown_item)

        // Spinner 어댑터 설정
        vaccineYearSpinner.adapter = aaYear
        vaccineYearSpinner.setSelection(0)
        vaccineMonthSpinner.adapter = aaMonth
        vaccineMonthSpinner.setSelection(0)
        vaccineDaySpinner.adapter = aaDay
        vaccineDaySpinner.setSelection(0)
        vaccineHourSpinner.adapter = aaHour
        vaccineMinuteSpinner.adapter = aaMinute

        // spinner 최대크기 정하기
        setSpinnerMaxHeight(vaccineYearSpinner, SPINNER_HEIGHT)
        setSpinnerMaxHeight(vaccineMonthSpinner, SPINNER_HEIGHT)
        setSpinnerMaxHeight(vaccineDaySpinner, SPINNER_HEIGHT)
        setSpinnerMaxHeight(vaccineHourSpinner, SPINNER_HEIGHT)
        setSpinnerMaxHeight(vaccineMinuteSpinner, SPINNER_HEIGHT)

        // Spinner 선택 리스너 정의
        val spinnerItemSelectedListener = SpinnerItemSelectedListener()
        vaccineYearSpinner.onItemSelectedListener = spinnerItemSelectedListener
        vaccineMonthSpinner.onItemSelectedListener = spinnerItemSelectedListener
        vaccineDaySpinner.onItemSelectedListener = spinnerItemSelectedListener
        vaccineHourSpinner.onItemSelectedListener = spinnerItemSelectedListener
        vaccineMinuteSpinner.onItemSelectedListener = spinnerItemSelectedListener

        // 성별(남성) 클릭 시
        petMale.setOnTouchListener { _, event ->
            if(event?.action == MotionEvent.ACTION_DOWN) {
                petGender = when (petGender) {
                    2 -> { // male인 경우
                        petMale.setImageResource(R.drawable.message_petmale)
                        petFemale.setImageResource(R.drawable.message_petfemaleclick)
                        1
                    }
                    else -> {
                        petMale.setImageResource(R.drawable.message_petmaleclick)
                        petFemale.setImageResource(R.drawable.message_petfemale)
                        2
                    }
                }
            }
            false
        }

        // 성별(여성) 클릭 시
        petFemale.setOnTouchListener { _, event ->
            if(event?.action == MotionEvent.ACTION_DOWN) {
                petGender = when (petGender) {
                    1 -> { // female인 경우
                        petMale.setImageResource(R.drawable.message_petmaleclick)
                        petFemale.setImageResource(R.drawable.message_petfemale)
                        2
                    }
                    else -> {
                        petMale.setImageResource(R.drawable.message_petmale)
                        petFemale.setImageResource(R.drawable.message_petfemaleclick)
                        1
                    }
                }
            }
            false
        }

        // 예약 등록 클릭 시
        reservationBtn.setOnTouchListener { _, event ->
            if(event?.action == MotionEvent.ACTION_DOWN) {
                if (checkOwnerInfo() && checkPetInfo() && checkReservationInfo()) {
                    // 전송
                    val reservationService: ReservationService = Retrofit.Builder()
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .baseUrl(SERVER_URL)
                            .client(OkHttpClient())
                            .build()
                            .create(ReservationService::class.java)

                    val vaccineData = VaccineReservationData(KAKAO_ID, ownerName!!, ownerHP!!, petName!!,
                            petAge!!, petWeight!!, petGender, vaccineName!!, vaccineDate!!, vaccineTime!!)

                    reservationService.resultVaccineRepos(vaccineData)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                // 서버 통신 성공
                                if (it.result == 1) {
                                    Log.d(TAG, "예약 성공!")
                                    Toast.makeText(applicationContext, "예약 성공! 저장 성공!", Toast.LENGTH_LONG).show()
                                    setResult(Activity.RESULT_OK)
                                    finish()
                                }
                                else {
                                    Log.d(TAG, "예약 실패!")
                                    setResult(Activity.RESULT_OK)
                                    finish()
                                    }
                            }, {
                                // 서버 통신 실패
                                Log.d(TAG, "Error : ${it.message}")
                            })
                }
                false
            }
            false
        }
    }

    /*
    주인 정보 edittext 값 체크
    @return : boolean(true : 통과, false : 실패)
     */
    fun checkOwnerInfo(): Boolean {
        if (checkEditText(etOwnerName) && checkEditText(etOwnerPhoneNum1) && checkEditText(etOwnerPhoneNum2) && checkEditText(etOwnerPhoneNum3)) {
            ownerName = etOwnerName.text.toString()
            ownerHP = "${etOwnerPhoneNum1.text}-${etOwnerPhoneNum2.text}-${etOwnerPhoneNum3.text}"
            return true
        }
        toastCheckResult("주인의 정보")
        return false
    }

    /*
    주인 정보 edittext 값 체크
    @return : boolean(true : 통과, false : 실패)
     */
    fun checkPetInfo(): Boolean {
        if (checkEditText(etPetName) && checkEditText(etPetAge) && checkEditText(etPetWeight)) {
            if (petGender != 0) {
                petName = etPetName.text.toString()
                petAge = Integer.valueOf(etPetAge.text.toString())
                petWeight = etPetWeight.text.toString()
                return true
            }
        }
        toastCheckResult("반려동물의 정보")
        return false
    }

    /*
    주인 정보 edittext 값 체크
    @return : boolean(true : 통과, false : 실패)
     */
    fun checkReservationInfo(): Boolean {
        if (checkEditText(etVaccineName)) {
            vaccineYear.let { vaccineMonth.let { vaccineDay.let {
                vaccineHour.let { vaccineMinute.let {
                    vaccineName = etVaccineName.text.toString()
                    vaccineDate = "$vaccineYear.$vaccineMonth.$vaccineDay"
                    vaccineTime = "${vaccineHour}시 ${vaccineMinute}분"
                } }
            } } }
        }
        toastCheckResult("예방접종에 대한 정보")
        return false
    }

    /*
    Spinner Item Select Listener 처리하는 클래스
     */
    internal inner class SpinnerItemSelectedListener : AdapterView.OnItemSelectedListener {

        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            val spinner = parent as Spinner
            when (spinner.id) {
                R.id.vaccineYearSpinner -> vaccineYear = parent.getItemAtPosition(position).toString()
                R.id.vaccineMonthSpinner -> vaccineMonth = aaMonth!!.getItem(position)!!.toString()
                R.id.vaccineDaySpinner -> vaccineDay = aaDay!!.getItem(position)!!.toString()
                R.id.vaccineHourSpinner -> vaccineHour = aaHour!!.getItem(position)!!.toString()
                R.id.vaccineMinuteSpinner -> vaccineMinute = aaMinute!!.getItem(position)!!.toString()
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>) {

        }
    }

    /*
    Spinner 년, 월, 일 ArrayList 초기화
     */
    private fun setDateArrayForSpinner() {
        // year
        val year = Calendar.getInstance().get(Calendar.YEAR)

        yearArray.add("년")
        monthArray.add("월")
        dayArray.add("일")

        for (i in year - 20..year) {
            yearArray.add(i.toString())
        }

        // month
        for (i in 1..12) {
            monthArray.add(i.toString())
        }

        // day
        for (i in 1..31) {
            dayArray.add(i.toString())
        }
    }

    /*
    Spinner 시간, 분 ArrayList 초기화
     */
    private fun seTimeForSpinner() {

        // month
        for (i in 0..23) {
            hourArray.add(i.toString())
        }

        // day
        for (i in 0..59) {
            minuteArray.add(i.toString())
        }
    }

    private fun toastCheckResult(msg: String?) {
        Toast.makeText(applicationContext, "$msg 내 값을 잘못 입력하셨습니다.", Toast.LENGTH_LONG).show()
    }
}
