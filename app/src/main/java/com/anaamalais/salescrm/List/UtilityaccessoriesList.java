package com.anaamalais.salescrm.List;

public class UtilityaccessoriesList {

    private String utility_accessory_id;
    private String utility_accessory_name;
    private String utility_accessory_price;
    private boolean isOn = false;


    public String getUtility_accessory_id() {
        return utility_accessory_id;
    }

    public void setUtility_accessory_id(String utility_accessory_id) {
        this.utility_accessory_id = utility_accessory_id;
    }

    public String getUtility_accessory_name() {
        return utility_accessory_name;
    }

    public void setUtility_accessory_name(String utility_accessory_name) {
        this.utility_accessory_name = utility_accessory_name;
    }

    public String getUtility_accessory_price() {
        return utility_accessory_price;
    }

    public void setUtility_accessory_price(String utility_accessory_price) {
        this.utility_accessory_price = utility_accessory_price;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }
}
