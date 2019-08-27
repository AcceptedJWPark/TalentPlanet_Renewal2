package com.accepted.acceptedtalentplanet;

import java.io.Serializable;

/**
 * Created by kwonhong on 2017-11-05.
 */

public class MyTalent implements Serializable{
    String Keyword1, Keyword2, Keyword3;
    String Location;
    String Point;
    String Level;
    String talentID;
    String Status;
    boolean compFlag = false;
    GeoPoint gp;

    public MyTalent()
    {

    };
    public void setMyTalent(String Keyword1, String Keyword2, String Keyword3, String Location, String Point, String Level, GeoPoint gp){
        this.Keyword1 = Keyword1;
        this.Keyword2 = Keyword2;
        this.Keyword3 = Keyword3;
        this.Location = Location;
        this.Point = Point;
        this.Level = Level;
        this.compFlag = true;
        this.gp = gp;
    };

    public String[] getKeywordArray(){
        String arr[] = {Keyword1, Keyword2, Keyword3};
        return arr;
    }

    public String getLocation(){
        return Location;
    }

    public int getPoint(){
        try {
            return Integer.parseInt(Point);
        }
        catch(Exception e){
            return 0;
        }
    }

    public String getLevel(){
        switch(Level){
            case "1":
                return "시작단계(Beginner)";
            case "2":
                return "초급(Elementary)";
            case "3":
                return "중급(Intermediate)";
            case "4":
                return "상급(Master)";
            default:
                return "전문가(Professional)";
        }
    }

    public String getStatus(){
        return Status;
    }

    public void setStatus(String Status){
        this.Status = Status;
    }


    public void setTalentID(String talentID){
        this.talentID = talentID;
    }

    public String getTalentID(){
        return this.talentID;
    }

    public int getIntegerLevel(){
        return Integer.parseInt(Level);
    }

    public boolean getCompFlag(){
        return compFlag;
    }

    public GeoPoint getArrGeoPoint() { return gp; }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("TALENT_Keywords = ").append(Keyword1).append(", ").append(Keyword2).append(", ").append(Keyword3).append("\n");
        sb.append("LOCATION = ").append(getLocation());
        return sb.toString();
    }

}
