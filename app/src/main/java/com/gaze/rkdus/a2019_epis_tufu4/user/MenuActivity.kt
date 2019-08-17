package com.gaze.rkdus.a2019_epis_tufu4.user

import android.Manifest
import android.annotation.TargetApi
import android.app.ActivityManager
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.support.v4.view.ViewCompat.canScrollHorizontally
import android.support.v4.view.ViewCompat.canScrollVertically
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import com.gaze.rkdus.a2019_epis_tufu4.BaseActivity
import com.gaze.rkdus.a2019_epis_tufu4.CommunityActivity
import com.gaze.rkdus.a2019_epis_tufu4.R
import com.gaze.rkdus.a2019_epis_tufu4.adapter.PackageListAdapter
import com.gaze.rkdus.a2019_epis_tufu4.item.PackageListItem
import com.gaze.rkdus.a2019_epis_tufu4.popup.SerialPopupActivity
import com.gaze.rkdus.a2019_epis_tufu4.utils.userUtil.Companion.checkEditText
import kotlinx.android.synthetic.main.activity_menu.*
import org.json.JSONArray
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException

class MenuActivity : BaseActivity() {
    private var switchIntent = Intent()
    var adapter: PackageListAdapter? = null
    private var itemList = ArrayList<PackageListItem>()
    private var serialWord: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        // 권한 확인 및 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkCameraPermission()
            checkLocationPermission()
        }

        // 터치 리스너
        whatIsPetRegImg.setOnTouchListener(touchListener)
        howToPetRegImg.setOnTouchListener(touchListener)
        regReviseGuideImg.setOnTouchListener(touchListener)
        searchHospitalImg.setOnTouchListener(touchListener)
        myPageImg.setOnTouchListener(touchListener)
        communityImg.setOnTouchListener(touchListener)
        reservationImg.setOnTouchListener(touchListener)
        searchBtn.setOnTouchListener(touchListener)

        // 서비스 시작(임시)
//        if (!isServiceRunning()) {
//            val intent = Intent(this, ReservationCheckService::class.java)
//            intent.putExtra("NICKNAME", NICKNAME)
//            startService(intent)
//        }

        // 패키지 상품 리스트 설정
        setPackageItems()


        val gridLayoutManager = customLayoutManager(this, 2)
        packageRecyclerView.layoutManager = gridLayoutManager

        adapter = PackageListAdapter(itemList)
        adapter!!.resetAll(itemList)
        packageRecyclerView.adapter = adapter
        adapter!!.notifyDataSetChanged()

        // RecyclerView 클릭 이벤트 초기화
        setRecyclerViewItemClick(itemList, adapter!!)
        tvUserName.text = NICKNAME  // 닉네임 설정

        // 일련번호 자판에서 검색 버튼 클릭 시
        searchEditText.setOnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    searchSerial()
                    false
                }
                else -> false
            }
        }

        // 예약 건수 불러오기
        var jsonStr = loadJSONFile("myReservation.json")
        tvReservationCount.text = "예약 : 0건"
        if (!TextUtils.isEmpty(jsonStr)) {
            var reservationArray = JSONArray(jsonStr)
            reservationArray.let {
                tvReservationCount.text = "예약 : ${reservationArray.length()}건"
            }
        }
    }

    private val touchListener = View.OnTouchListener { v, event ->
        when (v.id) {
            R.id.whatIsPetRegImg -> {
                switchIntent = Intent(applicationContext, WhatIsRegActivity::class.java)
                startActivity(switchIntent)
            }
            R.id.howToPetRegImg -> {
                switchIntent = Intent(applicationContext, TypeActivity::class.java)
                startActivity(switchIntent)
            }
            R.id.regReviseGuideImg -> {
                switchIntent = Intent(applicationContext, ReviseActivity::class.java)
                startActivity(switchIntent)
            }
            R.id.searchHospitalImg -> {
                switchIntent = Intent(applicationContext, SearchActivity::class.java)
                startActivity(switchIntent)
            }
            R.id.myPageImg -> {
                switchIntent = Intent(applicationContext, MyPageActivity::class.java)
                startActivity(switchIntent)
            }
            R.id.communityImg -> {
                switchIntent = Intent(applicationContext, CommunityActivity::class.java)
                startActivity(switchIntent)
            }
            R.id.reservationImg -> { // 예약 아이콘 클릭
                switchIntent = Intent(applicationContext, MyPageActivity::class.java)
                startActivity(switchIntent)
            }
            R.id.searchBtn -> {
                Toast.makeText(applicationContext, "일련번호 조회를 시작합니다.", Toast.LENGTH_SHORT).show()
                searchSerial()
            }
        }
        false
    }

    class customLayoutManager(context: Context?, spanCount: Int) : GridLayoutManager(context, spanCount) {
        override fun canScrollVertically(): Boolean { // 세로스크롤 막기
            return false
        }

        override fun canScrollHorizontally(): Boolean { //가로 스크롤막기
            return false
        }
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
    일련번호 조회를 위한 함수
     */
    private fun searchSerial() {
        if (checkEditText(searchEditText)) {
            serialWord = searchEditText.text.toString()
            var serialIntent = Intent(this, SerialPopupActivity::class.java)
            serialIntent.putExtra("serialWord", serialWord)
            startActivity(serialIntent)
        }
    }

    /*
    RecyclerView Item 개별 클릭 리스너 설정하는 함수
     */
    private fun setRecyclerViewItemClick(result: java.util.ArrayList<PackageListItem>, packageListAdapter: PackageListAdapter) {
        packageListAdapter.setItemClick { view, position ->
            //해당 위치의 Data get
            val resultData = result[position]
            Toast.makeText(applicationContext, "${resultData.name} 는 현재 준비중입니다. ", Toast.LENGTH_SHORT).show()
        }
    }
    /*
    임시 뷰로 보여줄 아이템 값 설정
     */
    private fun setPackageItems() {
        itemList.add(PackageListItem("강아지 올패키지", 20, 96000, 120000, R.drawable.package_1))
        itemList.add(PackageListItem("강아지 간식 패키지", 20, 64000, 80000, R.drawable.package_2))
        itemList.add(PackageListItem("강아지 장난감 패키지", 15, 110500, 130000, R.drawable.package_3))
        itemList.add(PackageListItem("강아지 산책 패키지", 30, 84000, 120000, R.drawable.package_4))
    }

    /*
    서비스가 현재 작동중인지 확인하는 함수
    @return : true(작동중), false(작동중이 아님.)
     */
    fun isServiceRunning(): Boolean {
        val manager = applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (ReservationCheckService::class.java.name == service.service.className)
                return true
        }
        return false
    }

    /*
    Camera 관련 권한 체크 상태 확인 함수
    @return : boolean(true : 체크한 경우, false : 체크 안한 경우)
     */
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun checkCameraPermission() {
        Log.d(TAG, "checkLocationPermission start ")
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            // 권한 팝업에서 한번이라도 거부한 경우 true 리턴.
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                // ...
            }
            requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1)
        }
    }

    /*
   Location 관련 권한 체크 상태 확인 함수
   @return : boolean(true : 체크한 경우, false : 체크 안한 경우)
    */
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun checkLocationPermission() {
        Log.d(TAG, "checkLocationPermission start ")
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            // 권한 팝업에서 한번이라도 거부한 경우 true 리턴.
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                // ...
            }
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                    1)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        Log.d(TAG, "onRequestPermissionsResult start ")
        if (requestCode == 1) {
            if (grantResults.isNotEmpty()) {
                for (i in grantResults.indices) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        // 하나라도 거부한다면.
                        AlertDialog.Builder(this).setTitle("알림").setMessage("권한을 허용해주셔야 해당 서비스를 이용하실 수 있습니다.")
                                .setPositiveButton("종료") { dialog, _ ->
                                    dialog.dismiss()
                                    finish()
                                }.setNegativeButton("권한 설정") { dialog, _ ->
                                    dialog.dismiss()
                                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                            .setData(Uri.parse("package:" + applicationContext.packageName))
                                    applicationContext.startActivity(intent)
                                }.setCancelable(false).show()
                        return
                    }
                }
            }
        }
    }
}