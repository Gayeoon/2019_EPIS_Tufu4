package com.example.rkdus.a2019_epis_tufu4;

import android.util.Log;
import com.google.firebase.database.*;


public class DatabaseCls {

    private static final String TAG = "MainActivity";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = database.getReference();

    DatabaseReference dbClient = dbRef.child("Client");
    DatabaseReference dbHospital = dbRef.child("Hospital");

    DatabaseCls() {

        // DB에 추가할 이벤트 오면 그 때 ㄲ
//        addClient(dbClient, new Client());
//        addHospital(dbHospital, new Hospital());

        // 일단 클라이언트만 읽어보자
//        dbClient.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                Log.d(TAG, "Value is: " + value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });

    }


    // Client 추가
    protected void addClient(Client client) {
        this.addClient(dbClient, client);
    }

    protected void addClient(DatabaseReference dbRef, Client client) {

        DatabaseReference dbCl_new = dbRef.push();
        Client newClient = client;
        Client.Pet newPet = client.getPet();

        dbCl_new.child("user_name").setValue(newClient.getUser_name());
        dbCl_new.child("address").setValue(newClient.getAddress());
        dbCl_new.child("phone").setValue(newClient.getPhone());
        dbCl_new.child("type").setValue(newClient.getType());

        DatabaseReference dbPet= dbCl_new.child("pet");

        dbPet.child("pet_name").setValue(newPet.getPet_name());
        dbPet.child("birth").setValue(newPet.getBirth());
        dbPet.child("color").setValue(newPet.getColor());
        dbPet.child("isNeutral").setValue(newPet.getIsNeutral());
        dbPet.child("isRegist").setValue(newPet.getIsRegist());
        dbPet.child("kind").setValue(newPet.getKind());
        dbPet.child("gender").setValue(newPet.getGender());
        dbPet.child("registNum").setValue(newPet.getRegistNum());


//        dbCl_new.child("user_name").setValue("얘내꺼");
//        dbCl_new.child("address").setValue("99스트릿");
//        dbCl_new.child("phone").setValue("0428221551");
//
//        DatabaseReference dbCl_new_pet = dbCl_new.child("pet");
//
//        dbCl_new_pet.child("pet_name").setValue("주인장치와와");
//        dbCl_new_pet.child("birth").setValue("19961217");
//        dbCl_new_pet.child("color").setValue("BBBblack");
//        dbCl_new_pet.child("isNeutral").setValue("false");
//        dbCl_new_pet.child("isRegist").setValue("true");
//        dbCl_new_pet.child("kind").setValue("치와와치와와");
//        dbCl_new_pet.child("gender").setValue("남견");
//        dbCl_new_pet.child("registNum").setValue("96121712003993");
    }

    // Hospital 추가
    protected void addHospital(Hospital hospital) {
        this.addHospital(dbHospital, hospital);
    }

    protected void addHospital(DatabaseReference dbRef, Hospital hospital) {
        DatabaseReference dbHo_new = dbRef.push();
        Hospital newHospital = hospital;
        Hospital.Notification newNoti = hospital.getNotification();
        Hospital.Notification.Reservation newReserve = hospital.getNotification().getReservation();

        dbHo_new.child("ceo_name").setValue(newHospital.getCeo_name());
        dbHo_new.child("hospital_name").setValue(newHospital.getHospital_name());
        dbHo_new.child("phone").setValue(newHospital.getPhone());
        dbHo_new.child("pw").setValue(newHospital.getPw());

        DatabaseReference dbHo_new_noti = dbHo_new.child("notification");

        DatabaseReference dbHo_new_noti_reserve_in = dbHo_new_noti.child("reservation").child("inner");
        DatabaseReference dbHo_new_noti_reserve_out = dbHo_new_noti.child("reservation").child("outer");

        // id에 해당하는 인덱스 찾아서 가져오자
        dbHo_new_noti_reserve_in.child("IDisIN0ANSKD13BPK3523PKN3").setValue(newReserve.inners.get(0));
        dbHo_new_noti_reserve_out.child("IDisOUT0ANSKD13BPK3523PKN3").setValue(newReserve.outers.get(0));


//        dbHo_new.child("ceo_name").setValue("이거내병원");
//        dbHo_new.child("hospital_name").setValue("이거쟤병원");
//        dbHo_new.child("phone").setValue("0428221551");
//        dbHo_new.child("pw").setValue("480480");
//        DatabaseReference dbHo_new_noti = dbHo_new.child("notification");
//
//        DatabaseReference dbHo_new_noti_reserve_in = dbHo_new_noti.child("reservation").child("inner");
//        DatabaseReference dbHo_new_noti_reserve_out = dbHo_new_noti.child("reservation").child("outer");
//
//        dbHo_new_noti_reserve_in.push().setValue("true");
//        dbHo_new_noti_reserve_in.push().setValue("false");
//        dbHo_new_noti_reserve_out.push().setValue("true");
//        dbHo_new_noti_reserve_out.push().setValue("false");
    }


}
