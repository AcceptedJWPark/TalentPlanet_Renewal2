package accepted.talentplanet_renewal2.Classes;

public class TalentObject_Home implements Comparable<TalentObject_Home> {
    private String title;
    private int backgroundResourceID;
    private int iconResourceID;
    private int talentCount;
    private int cateCode;
    private String talentID;
    private String userID;
    private String TalentDescription;
    private boolean hasFlag;
    private String hashtag;
    private String talentFlag;

    public TalentObject_Home( String title, int backgroundResourceID, int iconResourceID, int talentCount, String talentID) {
        this.title = title;
        this.backgroundResourceID = backgroundResourceID;
        this.iconResourceID = iconResourceID;
        this.talentCount = talentCount;
        this.talentID = talentID;
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

    public int getIconResourceID() {
        return iconResourceID;
    }

    public void setIconResourceID(int iconResourceID) {
        this.iconResourceID = iconResourceID;
    }

    public int getTalentCount() {
        return this.talentCount;
    }

    public void setTalentCount(int talentCount) {
        this.talentCount = talentCount;
    }

    @Override
    public int compareTo(TalentObject_Home o) {
        if (this.talentCount < o.getTalentCount()) {
            return 1;
        } else if (this.talentCount > o.getTalentCount()) {
            return -1;
        }
        return 0;
    }

    public int getCateCode() {
        return cateCode;
    }

    public void setCateCode(int cateCode) {
        this.cateCode = cateCode;
    }

    public String getTalentID() {
        return talentID;
    }

    public void setTalentID(String talentID) {
        this.talentID = talentID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public boolean hasFlag() {
        return hasFlag;
    }

    public void setHasFlag(boolean hasFlag) {
        this.hasFlag = hasFlag;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public String getTalentDescription() {
        return TalentDescription;
    }

    public void setTalentDescription(String talentDescription) {
        TalentDescription = talentDescription;
    }

    public String getTalentFlag() {
        return talentFlag;
    }

    public void setTalentFlag(String talentFlag) {
        this.talentFlag = talentFlag;
    }
}
