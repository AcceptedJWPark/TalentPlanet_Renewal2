package accepted.talentplanet_renewal2.Classes;

public class TalentObject_Home {
    private String title;
    private int backgroundResourceID;
    private int iconResourceID;

    public TalentObject_Home(String title, int backgroundResourceID, int iconResourceID) {
        this.title = title;
        this.backgroundResourceID = backgroundResourceID;
        this.iconResourceID = iconResourceID;
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
}
