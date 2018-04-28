package com.softechfoundation.municipal.Pojos;

/**
 * Created by yubar on 4/25/2018.
 */

public class MinistryPojo {
    String ministryName;
    String ministerName;
    String contactNumber;
    String ministerImage;
    String ministerEmail;
    String party;

    public String getMinistryName() {
        return ministryName;
    }

    public String getMinisterName() {
        return ministerName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getMinisterImage() {
        return ministerImage;
    }

    public String getParty() {
        return party;
    }

    public String getMinisterEmail() {
        return ministerEmail;
    }

    public void setMinisterEmail(String ministerEmail) {
        this.ministerEmail = ministerEmail;
    }

    public void setMinistryName(String ministryName) {
        this.ministryName = ministryName;
    }

    public void setMinisterName(String ministerName) {
        this.ministerName = ministerName;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setMinisterImage(String ministerImage) {
        this.ministerImage = ministerImage;
    }

    public void setParty(String party) {
        this.party = party;
    }
}
