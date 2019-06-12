package accepted.talentplanet_renewal2.Classes;

public class TalentObject_Home implements Comparable<TalentObject_Home> {
    private String title;
    private int backgroundResourceID;
    private int iconResourceID;
    private int talentCount;
    private int cateCode;

    public TalentObject_Home(String title, int backgroundResourceID, int iconResourceID, int talentCount) {
        this.title = title;
        this.backgroundResourceID = backgroundResourceID;
        this.iconResourceID = iconResourceID;
        this.talentCount = talentCount;
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
}
