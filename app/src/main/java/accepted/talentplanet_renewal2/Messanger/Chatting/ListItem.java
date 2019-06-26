package accepted.talentplanet_renewal2.Messanger.Chatting;

/**
 * Created by Accepted on 2018-03-05.
 */

public class ListItem {

    private int picture;
    private String message;
    private String date;
    private int messageType;
    private boolean isPicture;
    private boolean isTimeChanged;
    private boolean isDateChanged;


    public ListItem(int picture, String message, String date, int messageType, boolean isPicture, boolean isTimeChanged, boolean isDateChanged) {
        this.picture = picture;
        this.message = message;
        this.date = date;
        this.messageType = messageType;
        this.isPicture = isPicture;
        this.isTimeChanged = isTimeChanged;
        this.isDateChanged = isDateChanged;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public boolean isPicture() {
        return isPicture;
    }

    public void setPicture(boolean picture) {
        this.isPicture = picture;
    }

    public boolean isTimeChanged() {
        return isTimeChanged;
    }

    public void setTimeChanged(boolean timeChanged) {
        this.isTimeChanged = timeChanged;
    }

    public boolean isDateChanged() {
        return isDateChanged;
    }

    public void setDateChanged(boolean dateChanged) {
        this.isDateChanged = dateChanged;
    }
}
