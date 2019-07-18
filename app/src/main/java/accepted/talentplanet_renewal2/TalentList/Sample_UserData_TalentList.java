package accepted.talentplanet_renewal2.TalentList;

public class Sample_UserData_TalentList {
    private String userName;
    private String userGender;
    private String userAge;
    private String hashtag;
    private String distance;


    public Sample_UserData_TalentList(String userName, String userGender, String userAge, String hashtag, String distance) {
        this.userName = userName;
        this.userGender = userGender;
        this.userAge = userAge;
        this.hashtag = hashtag;
        this.distance = distance;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
