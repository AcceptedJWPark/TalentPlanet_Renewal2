package com.accepted.acceptedtalentplanet.Home;

public class SpinnerData_Toolbar {
    String text;
    int imageID;
    int arrowID;
    String talentFlag;

    public SpinnerData_Toolbar(String text, int imageID, int arrowID, String talentFlag) {
        this.text = text;
        this.imageID = imageID;
        this.arrowID = arrowID;
        this.talentFlag = talentFlag;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public int getArrowID() {
        return arrowID;
    }

    public void setArrowID(int arrowID) {
        this.arrowID = arrowID;
    }

    public String getTalentFlag() {
        return talentFlag;
    }

    public void setTalentFlag(String talentFlag) {
        this.talentFlag = talentFlag;
    }
}
