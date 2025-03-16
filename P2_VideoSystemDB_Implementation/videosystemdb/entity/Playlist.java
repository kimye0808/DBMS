package entity;

public class Playlist {
    private String pName;
    private String pl_uID;
    private int plCategory;

    public Playlist(String pName, String pl_uID, int plCategory){
        this.pName = pName;
        this.pl_uID = pl_uID;
        this.plCategory = plCategory;
    }

    public String getpName(){
        return pName;
    }

    public String getPl_uID(){
        return pl_uID;
    }

    public int getPlCategory(){
        return plCategory;
    }
}
