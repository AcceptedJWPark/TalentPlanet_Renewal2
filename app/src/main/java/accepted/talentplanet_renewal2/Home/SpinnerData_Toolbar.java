package accepted.talentplanet_renewal2.Home;

public class SpinnerData_Toolbar {
    String text;
    int imageID;

    public SpinnerData_Toolbar(String text, int imageID) {
        this.text = text;
        this.imageID = imageID;
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
}
