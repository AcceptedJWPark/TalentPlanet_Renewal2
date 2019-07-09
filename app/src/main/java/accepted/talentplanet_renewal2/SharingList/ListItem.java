package accepted.talentplanet_renewal2.SharingList;

/**
 * Created by Accepted on 2017-09-29.
 */

public class ListItem {

    private String matchedFlag;
    private String userName;
    private String userID;
    private String hashtag;
    private int matchingID;
    private String sFilePath;
    private String filePath;
    private int talentCateCode;
    private String creationDate;
    private String talentFlag;

    public String getMatchedFlag() {
        return matchedFlag;
    }

    public void setMatchedFlag(String matchedFlag) {
        this.matchedFlag = matchedFlag;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public int getMatchingID() {
        return matchingID;
    }

    public void setMatchingID(int matchingID) {
        this.matchingID = matchingID;
    }

    public String getsFilePath() {
        return sFilePath;
    }

    public void setsFilePath(String sFilePath) {
        this.sFilePath = sFilePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getTalentCateCode() {
        return talentCateCode;
    }

    public void setTalentCateCode(int talentCateCode) {
        this.talentCateCode = talentCateCode;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getTalentFlag() {
        return talentFlag;
    }

    public void setTalentFlag(String talentFlag) {
        this.talentFlag = talentFlag;
    }

    public ListItem(String matchedFlag, String userName, String userID, String hashtag, int matchingID, String sFilePath, String filePath, int talentCateCode, String creationDate, String talentFlag) {
        this.matchedFlag = matchedFlag;
        this.userName = userName;
        this.userID = userID;
        this.hashtag = hashtag;
        this.matchingID = matchingID;
        this.sFilePath = sFilePath;
        this.filePath = filePath;
        this.talentCateCode = talentCateCode;
        this.creationDate = creationDate;
        this.talentFlag = talentFlag;
    }
}
