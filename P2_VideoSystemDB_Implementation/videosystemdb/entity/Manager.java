package entity;

public class Manager {
    private String mID;
    private String mPW;
    private String mSSN;

    public Manager(String mID, String mPW, String mSSN){
        this.mID = mID;
        this.mPW = mPW;
        this.mSSN = mSSN;
    }

    public String getmID() {
        return mID;
    }

    public String getmPW() {
        return mPW;
    }

    public String getmSSN() {
        return mSSN;
    }
}

