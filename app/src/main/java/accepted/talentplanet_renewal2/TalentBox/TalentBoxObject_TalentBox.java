package accepted.talentplanet_renewal2.TalentBox;

public class TalentBoxObject_TalentBox {
    private String requestType;
    private String code;
    private String SENDER_ID;
    private String RECEIVER_ID;
    private String MentorID;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSENDER_ID() {
        return SENDER_ID;
    }

    public void setSENDER_ID(String SENDER_ID) {
        this.SENDER_ID = SENDER_ID;
    }

    public String getRECEIVER_ID() {
        return RECEIVER_ID;
    }

    public void setRECEIVER_ID(String RECEIVER_ID) {
        this.RECEIVER_ID = RECEIVER_ID;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getMentorID() {
        return MentorID;
    }

    public void setMentorID(String mentorID) {
        MentorID = mentorID;
    }
}
