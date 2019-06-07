package com.example.rkdus.a2019_epis_tufu4;

public class Client {

    private String user_name;
    private String address;
    private String phone;
    private String type;
    private Pet pet;

    // 반려동물
    public class Pet {
        private String pet_name;
        private String birth;
        private String color;
        private String isNeutral;
        private String isRegist;
        private String kind;
        private String gender;
        private String registNum;

        Pet() {

        }

        void setPet(String pet_name, String birth, String color, String isNeutral, String isRegist, String kind, String gender, String registNum) {
            this.pet_name = pet_name;
            this.birth = birth;
            this.color = color;
            this.isNeutral = isNeutral;
            this.isRegist = isRegist;
            this.kind = kind;
            this.gender = gender;
            this.registNum = registNum;
        }

        public String getBirth() { return birth; }
        public String getColor() { return color; }
        public String getIsNeutral() { return isNeutral; }
        public String getIsRegist() { return isRegist; }
        public String getKind() { return kind; }
        public String getPet_name() { return pet_name; }
        public String getRegistNum() { return registNum; }
        public String getGender() { return gender; }
        public void setBirth(String birth) { this.birth = birth; }
        public void setColor(String color) { this.color = color; }
        public void setIsNeutral(String isNeutral) { this.isNeutral = isNeutral; }
        public void setIsRegist(String isRegist) { this.isRegist = isRegist; }
        public void setKind(String kind) { this.kind = kind; }
        public void setPet_name(String pet_name) { this.pet_name = pet_name; }
        public void setRegistNum(String registNum) { this.registNum = registNum; }
        public void setGender(String gender) { this.gender = gender; }
    }

    Client() {

    }

    Client(String user_name, String address, String phone, String type, Pet pet) {
        this.user_name = user_name;
        this.address = address;
        this.phone = phone;
        this.type = type;
        this.pet = pet;
    }

    public String getUser_name() { return user_name; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public String getType() { return type; }
    public Pet getPet() { return pet; }
    public void setUser_name(String user_name) { this.user_name = user_name; }
    public void setAddress(String address) { this.address = address; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setType(String type) { this.type = type; }
    public void setPet(Pet pet) { this.pet = pet; }
}
