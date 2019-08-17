package com.gaze.rkdus.a2019_epis_tufu4.user

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
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
import com.gaze.rkdus.a2019_epis_tufu4.item.HealthCheckupReservationData
import com.gaze.rkdus.a2019_epis_tufu4.item.MyReservationListData
import com.gaze.rkdus.a2019_epis_tufu4.utils.ReservationService
import com.gaze.rkdus.a2019_epis_tufu4.utils.userUtil.Companion.setSpinnerMaxHeight
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_health_checkup_message_acitivty.*
import kotlinx.android.synthetic.main.activity_vaccine_message.*
import kotlinx.android.synthetic.main.activity_vaccine_message.etOwnerName
import kotlinx.android.synthetic.main.activity_vaccine_message.etOwnerPhoneNum1
import kotlinx.android.synthetic.main.activity_vaccine_message.etOwnerPhoneNum2
import kotlinx.android.synthetic.main.activity_vaccine_message.etOwnerPhoneNum3
import kotlinx.android.synthetic.main.activity_vaccine_message.etPetAge
import kotlinx.android.synthetic.main.activity_vaccine_message.etPetName
import kotlinx.android.synthetic.main.activity_vaccine_message.etPetWeight
import kotlinx.android.synthetic.main.activity_vaccine_message.petFemale
import kotlinx.android.synthetic.main.activity_vaccine_message.petMale
import kotlinx.android.synthetic.main.activity_vaccine_message.reservationBtn
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class HealthCheckupMessageActivity : BaseActivity() {
    private val SPINNER_HEIGHT: Int = 350
    var hospitalKey: Int? = null
    var hospitalName: String? = null
    var ownerName: String? = null
    var ownerHP: String? = null
    var petName: String? = null
    var petAge: Int? = null
    var petWeight: String? = null
    var petGender: Int = 0  // 0: default,  1: female,  2: male
    var healthCheckYear: String? = null
    var healthCheckMonth: String? = null
    var healthCheckDay: String? = null
    var healthCheckHour: String? = null
    var healthCheckMinute: String? = null
    var healthCheckDate: String? = null
    var healthCheckTime: String? = null

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

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health_checkup_message_acitivty)

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
        healthCheckYearSpinner.adapter = aaYear
        healthCheckYearSpinner.setSelection(0)
        healthCheckMonthSpinner.adapter = aaMonth
        healthCheckMonthSpinner.setSelection(0)
        healthCheckDaySpinner.adapter = aaDay
        healthCheckDaySpinner.setSelection(0)
        healthCheckHourSpinner.adapter = aaHour
        healthCheckMinuteSpinner.adapter = aaMinute

        // spinner 최대크기 정하기
        setSpinnerMaxHeight(healthCheckYearSpinner, SPINNER_HEIGHT)
        setSpinnerMaxHeight(healthCheckMonthSpinner, SPINNER_HEIGHT)
        setSpinnerMaxHeight(healthCheckDaySpinner, SPINNER_HEIGHT)
        setSpinnerMaxHeight(healthCheckHourSpinner, SPINNER_HEIGHT)
        setSpinnerMaxHeight(healthCheckMinuteSpinner, SPINNER_HEIGHT)

        // Spinner 선택 리스너 정의
        val spinnerItemSelectedListener = SpinnerItemSelectedListener()
        healthCheckYearSpinner.onItemSelectedListener = spinnerItemSelectedListener
        healthCheckMonthSpinner.onItemSelectedListener = spinnerItemSelectedListener
        healthCheckDaySpinner.onItemSelectedListener = spinnerItemSelectedListener
        healthCheckHourSpinner.onItemSelectedListener = spinnerItemSelectedListener
        healthCheckMinuteSpinner.onItemSelectedListener = spinnerItemSelectedListener

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
                    val reservationService: ReservationService = Retrofit.Builder()
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .baseUrl(SERVER_URL)
                            .client(OkHttpClient())
                            .build()
                            .create(ReservationService::class.java)

                    val healthCheckupData = HealthCheckupReservationData(KAKAO_ID, ownerName!!, ownerHP!!, petName!!,
                            petAge!!, petGender, petWeight!!, healthCheckDate!!, healthCheckTime!!)

                    reservationService.resultHealthcheckupRepos(healthCheckupData)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                // 서버 통신 성공
                                if (it.result == 1) {
                                    Log.d(TAG, "예약 성공!")
                                    Toast.makeText(applicationContext, "예약 성공! 저장 성공!", Toast.LENGTH_LONG).show()
                                    saveReservationFile(healthCheckupData)
                                    setResult(Activity.RESULT_OK)
                                    finish()
                                }
                                else {
                                    Log.d(TAG, "예약 실패!")
                                    setResult(Activity.RESULT_CANCELED)
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveReservationFile(data: HealthCheckupReservationData) {
        val filename = "myReservation.json"
        val fileText = loadJSONFile(filename)
        var fileOutputStream: FileOutputStream? = null
        fileOutputStream = openFileOutput(filename, Context.MODE_PRIVATE) // MODE_PRIVATE : 다른 앱에서 해당 파일 접근 못함

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val date = current.format(formatter)
        var resultData = MyReservationListData("WAIT", 3, hospitalKey!!, hospitalName!!, date!!)
        // TODO : 지금은 일단 리스트 출력에 필요한 정보만 넣어놓기 위함.
//        val gson = Gson()
//        var tempStr: String? = gson.toJson(data)

        var jsonArray: JSONArray

        jsonArray = if (!TextUtils.isEmpty(fileText))
            JSONArray(fileText)
        else
            JSONArray()

        jsonArray.put(resultData)
        fileOutputStream.write(jsonArray.toString().toByteArray())   // Json 쓰기
        fileOutputStream.flush()
        fileOutputStream.close()
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
    json 파일 불러와서 String으로 리턴하기
     */
    private fun loadJSONFile(filename: String): String? {
        Log.d(TAG, "loadJSONFIle start")
        var result: String? = null

        var fileinputStream: FileInputStream? = null
        try {
            fileinputStream = openFileInput(filename)
            val size = fileinputStream!!.available()
            val buffer = ByteArray(size)
            fileinputStream.read(buffer)
            fileinputStream.close()
            result = String(buffer, charset("UTF-8"))
        } catch (e: FileNotFoundException) {
            Log.d(TAG, "사전에 등록증을 저장한 내역이 없습니다.")
            return null
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return result
    }

    /*
    주인 정보 edittext 값 체크
    @return : boolean(true : 통과, false : 실패)
     */
    fun checkReservationInfo(): Boolean {
        healthCheckYear.let { healthCheckMonth.let { healthCheckDay.let {
            healthCheckHour.let { healthCheckMinute.let {
                healthCheckDate = "$healthCheckYear-$healthCheckMonth-$healthCheckDay"
                healthCheckTime = "${healthCheckHour}시 ${healthCheckMinute}분"
                } }
            } } }

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
                R.id.healthCheckYearSpinner -> healthCheckYear = parent.getItemAtPosition(position).toString()
                R.id.healthCheckMonthSpinner -> healthCheckMonth = aaMonth!!.getItem(position)!!.toString()
                R.id.healthCheckDaySpinner -> healthCheckDay = aaDay!!.getItem(position)!!.toString()
                R.id.healthCheckHourSpinner -> healthCheckHour = aaHour!!.getItem(position)!!.toString()
                R.id.healthCheckMinuteSpinner -> healthCheckMinute = aaMinute!!.getItem(position)!!.toString()
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
    Spinner 년, 월, 일 ArrayList 초기화
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

    /*
    EditText의 값 중 공백 확인
    @return : boolean(true : 앞 뒤 공백 또는 전체 공백이 아님. false : 둘 중 하나라도 해당하는 경우)
     */
    private fun checkEditText(editText: EditText): Boolean {
        val editStr = editText.text.toString()    // 검색어 임시 변수에 저장.
        if (TextUtils.isEmpty(editStr.trim())) { // 공백처리
            return false
        }
        return editStr == editStr.trim()
    }

    private fun toastCheckResult(msg: String?) {
        Toast.makeText(applicationContext, "$msg 내 값을 잘못 입력하셨습니다.", Toast.LENGTH_LONG).show()
    }
}
