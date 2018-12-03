package com.example.b00682737.maryportholidaypark;

public class Caravan {

    String caravanId;
    String caravanCheckIn;
    String caravanBedrooms;
    String caravanCheckOut;
    String extras;


    public Caravan(){

    }

    public Caravan(String caravanId, String caravanCheckIn, String extras, String caravanBedrooms, String caravanCheckOut) {
        this.caravanId = caravanId;
        this.caravanCheckIn = caravanCheckIn;
        this.caravanBedrooms = caravanBedrooms;
        this.caravanCheckOut = caravanCheckOut;
        this.extras = extras;
    }

   public String getCaravanId() {
        return caravanId;
   }

    public String getCaravanCheckIn() {
        return caravanCheckIn;
    }

   public String getCaravanBedrooms() { return caravanBedrooms;}

    public String getCaravanCheckOut() { return caravanCheckOut; }

    public String extras() { return extras; }
}
