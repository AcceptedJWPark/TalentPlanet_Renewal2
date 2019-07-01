package accepted.talentplanet_renewal2.Cs.Claim.ClaimList;

/**
 * Created by Accepted on 2017-10-31.
 */

public class ListItem_Answer {

    private String name;
    private String claimType;
    private String date;
    private String content_claim;
    private String content_answer;

    public String getname() {
        return name;
    }

    public String getclaimType() {
        return claimType;
    }

    public String getdate() {
        return date;
    }

    public String getContent_claim() {
        return content_claim;
    }
    public String getanswer() {
        return content_answer;
    }



    public void setname(String name) {
        this.name = name;
    }

    public void setclaimType(String claimType) {
        this.claimType = claimType;
    }

    public void setdate(String date) {
        this.date = date;
    }

    public void setContent_claim(String Claim) {
        this.content_claim = Claim;
    }
    public void setanswer(String answer) {
        this.content_claim = answer;
    }


    public ListItem_Answer(String name, String claimType, String date, String content_claim, String content_answer)
    {
        this.name = name;
        this.claimType = claimType;
        this.date = date;
        this.content_claim = content_claim;
        this.content_answer = content_answer;
    }

}
