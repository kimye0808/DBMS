package action;

import dbconnecter.VidDBConnecter;
import entity.Usr;
import entity.Video;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.sql.Date;
import java.util.Scanner;

public class VidAction {
    VidDBConnecter vidDBC = new VidDBConnecter();
    Scanner scan = new Scanner(System.in);

    /*
     * search video by title
     */
    public void srchVidTitle() {
        System.out.print("SEARCH|TITLE|");
        String title = scan.next();
        vidDBC.srchVidTitle(title);
    }


    /*
     * search video by uploader's ID
     */
    public void srchVidUpldr() {
        System.out.print("SEARCH|UPLOADER|");
        String upldrID = scan.next();
        vidDBC.srchVidUpldr(upldrID);
    }


    /*
     * search video by category
     */
    public void srchVidCategory() {
        System.out.println("Category list");
        System.out.println("0 - comedy");
        System.out.println("1 - toy");
        System.out.println("2 - gaming");
        System.out.println("3 - education");
        System.out.println("4 - music");
        System.out.println("5 - eating");
        System.out.println("6 - beauty");
        System.out.println("7 - sports");
        System.out.print("SEARCH|CATEGORY#|");
        int srchCategory = scan.nextInt();
        vidDBC.srchVidCat(srchCategory);
    }


    /*
     * get video info from db
     */
    public Video getVid() {
        System.out.print("video name: ");
        String vName = scan.next();
        return vidDBC.getVid(vName);
    }

    /*
     * upload video
     */
    public void uploadVid(Usr usr) {
        //vID views viewPerTime v_mID v_adID는 먼저 설정하지 않는다
        System.out.print("video name : ");
        String vName = scan.next();
        System.out.print("video length: ");
        int length = scan.nextInt();
        System.out.println("0 - comedy");
        System.out.println("1 - toy");
        System.out.println("2 - gaming");
        System.out.println("3 - education");
        System.out.println("4 - music");
        System.out.println("5 - eating");
        System.out.println("6 - beauty");
        System.out.println("7 - sports");
        System.out.print("video category: ");
        int vCategory = scan.nextInt();
        if(!(0<=vCategory && vCategory <=7)){
            System.out.println("you chose a wrong input! return");
            return;
        }
        System.out.print("video ageLimit(0: ALL, 1: 15, 2: 19) : ");
        int ageLimit = scan.nextInt();
        if(!(0<=ageLimit&&ageLimit<=2)){
            System.out.println("you chose a wrong input! return");
            return;
        }
        String v_uID = usr.getuID();
        String vID = EigenValue.getEigenValue();
        //set upload Date
        Date upldDate = new java.sql.Date(new java.util.Date().getTime());
        int result = vidDBC.uploadVid(vID, vName, length, vCategory, ageLimit, upldDate, v_uID);
        if(result>0){
            System.out.println("Uploading Success");
        }

    }

    /*
     * upload video
     */
    public void showHotVid() {
        vidDBC.showHotVid();
    }

    /*
     * report video
     */
    public void reportVid(Usr usr, Video video) {
        System.out.println("0. inapposite sexual content");
        System.out.println("1. violent content");
        System.out.println("2. hate promotion content");
        System.out.println("3. copyright infringement");
        System.out.println("CHOOSE REASON");
        int reason = scan.nextInt();
        if(reason<0 || reason >3){
            System.out.println("You got a wrong choice! return");
            return;
        }
        if( vidDBC.reportVid(usr, video , reason)>0){
            System.out.println("Thank you to report");
        }
    }

    /*
     * show uploaded video list
     */
    public void showUploadedVidList(Usr usr) {
        vidDBC.showUploadedVidList(usr);
    }

    /*
     * delete uploaded video
     */
    public void deleteUploadedVid(Usr usr, Video dvideo) {
        if(vidDBC.deleteUploadedVid(usr, dvideo)>0){
            System.out.println("delete Success");
            System.out.println();
        }
    }
}
