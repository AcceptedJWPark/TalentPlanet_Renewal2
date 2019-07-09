package accepted.talentplanet_renewal2.SharingList;

/**
 * Created by Accepted on 2017-12-15.
 */

public class Spn_ListItem {
    private String spinnerItem1;
    private String spinnerItem2;
    private String spinnerItem3;

    public String getspinnerItem1() {
        return spinnerItem1;
    }
    public String getspinnerItem2() {
        return spinnerItem2;
    }
    public String getspinnerItem3(){
        return spinnerItem3;
    }

    public void setspinnerItem1(String spinnerItem1) {
        this.spinnerItem1 = spinnerItem1;
    }
    public void setspinnerItem2(String spinnerItem2) {
        this.spinnerItem2 = spinnerItem2;
    }
    public void setspinnerItem3(String spinnerItem3) {
        this.spinnerItem3 = spinnerItem3;
    }



    public Spn_ListItem(String spinnerItem1, String spinnerItem2, String spinnerItem3)
    {
        this.spinnerItem1 = spinnerItem1;
        this.spinnerItem2 = spinnerItem2;
        this.spinnerItem3 = spinnerItem3;
    }


}
