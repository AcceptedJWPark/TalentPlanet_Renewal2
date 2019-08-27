package com.accepted.acceptedtalentplanet;

import java.io.Serializable;

/**
 * Created by kwonhong on 2018-01-16.
 */

public class Friend implements Serializable {
    private String userID;
    private String partnerTalentType;

    public Friend(String userID, String partnerTalentType){
        this.userID = userID;
        this.partnerTalentType = partnerTalentType;
    }

    public String getUserID(){
        return userID;
    }

    public String getPartnerTalentType(){
        return partnerTalentType;
    }
}
