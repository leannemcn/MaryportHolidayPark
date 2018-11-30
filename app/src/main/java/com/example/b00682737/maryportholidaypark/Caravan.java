package com.example.b00682737.maryportholidaypark;

public class Caravan {

    String caravanId;
    String caravanCheckIn;
    String caravanBedrooms;
    String caravanCheckOut;


    public Caravan(){

    }

    public Caravan(String caravanId, String caravanCheckIn, String caravanBedrooms, String caravanCheckOut) {
        this.caravanId = caravanId;
        this.caravanCheckIn = caravanCheckIn;
        this.caravanBedrooms = caravanBedrooms;
        this.caravanCheckOut = caravanCheckOut;
    }

    public String getCaravanId() {
        return caravanId;
    }

    public String getCaravanCheckIn() {
        return caravanCheckIn;
    }

    public String getCaravanBedrooms() {
        return caravanBedrooms;
    }

    public String getCaravanCheckOut() {
        return caravanCheckOut;
    }
}
