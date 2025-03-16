package entity;

public class Ad {
    private int adCategory;
    private double adView;
    private double profitPerView;
    private String adName;
    private String adID;
    private String ad_mID;

    public Ad(int adCategory, double adView, double profitPerView,
              String adName, String adID, String ad_mID){
        this.adCategory = adCategory;
        this.adView = adView;
        this.profitPerView = profitPerView;;
        this.adName = adName;
        this.adID = adID;
        this.ad_mID = ad_mID;
    }

    public int getAdCategory() {
        return adCategory;
    }

    public double getAdView() {
        return adView;
    }

    public double getProfitPerView() {
        return profitPerView;
    }

    public String getAdName() {
        return adName;
    }

    public String getAd_mID() {
        return ad_mID;
    }

    public String getAdID() {
        return adID;
    }

    public void setAdView(double adView) {
        this.adView = adView;
    }
}
