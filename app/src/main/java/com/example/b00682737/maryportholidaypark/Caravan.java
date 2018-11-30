package com.example.b00682737.maryportholidaypark;

public class Caravan {

    String caravanId;
    String caravanName;
    String caravanBeds;

    public Caravan(){

    }

    public Caravan(String caravanId, String caravanName, String caravanBeds) {
        this.caravanId = caravanId;
        this.caravanName = caravanName;
        this.caravanBeds = caravanBeds;
    }

    public String getCaravanId() {
        return caravanId;
    }

    public String getCaravanName() {
        return caravanName;
    }

    public String getCaravanBeds() {
        return caravanBeds;
    }
}
