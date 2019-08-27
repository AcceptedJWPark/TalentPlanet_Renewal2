package com.accepted.acceptedtalentplanet.Classes;

public class TalentObject_Profile {
    private String title;
    private int backgroundResourceID;

    public TalentObject_Profile(String title, int backgroundResourceID) {
        this.title = title;
        this.backgroundResourceID = backgroundResourceID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getBackgroundResourceID() {
        return backgroundResourceID;
    }

    public void setBackgroundResourceID(int backgroundResourceID) {
        this.backgroundResourceID = backgroundResourceID;
    }

}
