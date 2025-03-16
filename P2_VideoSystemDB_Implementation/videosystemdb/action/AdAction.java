package action;

import dbconnecter.AdDBConnecter;
import entity.Ad;
import entity.Video;

public class AdAction {
    AdDBConnecter adDBC = new AdDBConnecter();

    public Ad getAd(Video video) {
        String adID = video.getV_adID();

        return adDBC.getAd(adID);
    }
}
