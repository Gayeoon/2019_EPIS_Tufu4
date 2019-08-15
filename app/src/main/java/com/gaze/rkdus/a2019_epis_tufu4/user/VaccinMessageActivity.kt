package com.gaze.rkdus.a2019_epis_tufu4.user

import android.os.Bundle
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.gaze.rkdus.a2019_epis_tufu4.BaseActivity
import com.gaze.rkdus.a2019_epis_tufu4.R
import com.gaze.rkdus.a2019_epis_tufu4.adapter.MessageSpinnerAdapter
import kotlinx.android.synthetic.main.activity_vaccine_message.*
import java.util.*

class vaccineMessageActivity : BaseActivity() {
    var hospitalKey: Int? = null
    var hospitalName: String? = null
    var ownerName: String? = null
    var ownerHP: String? = null
    var petName: String? = null
    var petAge: String? = null
    var petWeight: String? = null
    var petGender: Int = 0
    var vaccineName: String? = null
    var vaccineYear: String? = null
    var vaccineMonth: String? = null
    var vaccineDay: String? = null
    var vaccineHour: String? = null
    var vaccineMinute: String? = null
    var vaccineDate: String? = null
    var vaccineTime: String? null

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
        vaccineHourSpinner.setSelection(0)
        vaccineMinuteSpinner.adapter = aaMinute
        vaccineMinuteSpinner.setSelection(0)

        // Spinner 선택 리스너 정의
        val spinnerItemSelectedListener = SpinnerItemSelectedListener()
        vaccineYearSpinner.onItemSelectedListener = spinnerItemSelectedListener
        vaccineMonthSpinner.onItemSelectedListener = spinnerItemSelectedListener
        vaccineDaySpinner.onItemSelectedListener = spinnerItemSelectedListener
        vaccineHourSpinner.onItemSelectedListener = spinnerItemSelectedListener
        vaccineMinuteSpinner.onItemSelectedListener = spinnerItemSelectedListener

        // 예약 등록 클릭 시
        reservationBtn.setOnTouchListener { _, event ->
            if(event?.action == MotionEvent.ACTION_DOWN) {
                if (checkOwnerInfo() && checkPetInfo() && checkReservationInfo())
                    // 전송
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
                petAge = etPetAge.text.toString()
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
                    vaccineTime = "$vaccineHour.$vaccineMinute"
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
                R.id.hourSpinner -> vaccineHour = aaHour!!.getItem(position)!!.toString()
                R.id.minuteSpinner -> vaccineMinute = aaMinute!!.getItem(position)!!.toString()
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
        // year
        val year = Calendar.getInstance().get(Calendar.YEAR)

        hourArray.add("시")
        monthArray.add("분")

        // month
        for (i in 1..24) {
            hourArray.add(i.toString())
        }

        // day
        for (i in 1..60) {
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
