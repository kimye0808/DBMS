package entity;


import java.sql.Date;
import java.sql.Timestamp;

public class Video {
    private String vID;
    private String vName;
    private int views;
    private int length;
    private int vCategory;
    private int ageLimit;
    private Timestamp upldDate;
    private double viewPerTime;
    private String v_mID;
    private String v_uID;
    private String v_adID;

    public Video(String vID, String vName, int views, int length,
            int vCategory, int ageLimit, Timestamp upldDate, double viewPerTime,
             String v_mID, String v_uID, String v_adID){
        this.vID = vID;
        this.vName = vName;
        this.views =views;
        this.length =length;
        this.vCategory =vCategory;
        this.ageLimit = ageLimit;
        this.upldDate = upldDate;
        this.viewPerTime = viewPerTime;
        this.v_mID = v_mID;
        this.v_uID = v_uID;
        this.v_adID = v_adID;
    }

    public Timestamp getUpldDate() {
        return upldDate;
    }

    public int getAgeLimit() {
        return ageLimit;
    }

    public int getLength() {
        return length;
    }

    public int getvCategory() {
        return vCategory;
    }

    public double getViewPerTime() {
        return viewPerTime;
    }

    public int getViews() {
        return views;
    }

    public String getV_mID() {
        return v_mID;
    }

    public String getV_uID() {
        return v_uID;
    }

    public String getvID() {
        return vID;
    }

    public String getvName() {
        return vName;
    }

    public String getV_adID() {
        return v_adID;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public void updateViewPerTime() {
        Timestamp nowDate = new java.sql.Timestamp(new java.util.Date().getTime());
        viewPerTime = (double)(views+1)/((nowDate.getTime() - upldDate.getTime()));//초 차이로 나눔, view는 아직 update x
    }
}//video class end
