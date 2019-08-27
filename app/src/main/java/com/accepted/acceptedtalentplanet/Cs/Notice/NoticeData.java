package com.accepted.acceptedtalentplanet.Cs.Notice;

public class NoticeData {
    String NOTICE_TITLE;
    String NOTICE_SUMMARY;
    Long CREATION_DATE;

    public String getNOTICE_TITLE() {
        return NOTICE_TITLE;
    }

    public void setNOTICE_TITLE(String NOTICE_TITLE) {
        this.NOTICE_TITLE = NOTICE_TITLE;
    }

    public String getNOTICE_SUMMARY() {
        return NOTICE_SUMMARY;
    }

    public void setNOTICE_SUMMARY(String NOTICE_SUMMARY) {
        this.NOTICE_SUMMARY = NOTICE_SUMMARY;
    }

    public Long getCREATION_DATE() {
        return CREATION_DATE;
    }

    public void setCREATION_DATE(Long CREATION_DATE) {
        this.CREATION_DATE = CREATION_DATE;
    }
}
