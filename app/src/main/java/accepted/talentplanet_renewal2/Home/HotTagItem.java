package accepted.talentplanet_renewal2.Home;

import java.io.Serializable;

public class HotTagItem implements Serializable {
    private String tag;
    private String backgroundID;

    public HotTagItem(String tag, String backgroundID) {
        this.tag = tag;
        this.backgroundID = backgroundID;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getBackgroundID() {
        return backgroundID;
    }

    public void setBackgroundID(String backgroundID) {
        this.backgroundID = backgroundID;
    }
}
