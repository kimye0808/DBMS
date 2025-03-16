package action;

import dbconnecter.MgrDBConnecter;
import entity.Ad;
import entity.Manager;
import entity.Video;

import java.util.Scanner;

public class MgrAction {
    Manager mgr;
    MgrDBConnecter mgrDBC = new MgrDBConnecter();
    Scanner scan = new Scanner(System.in);


    /*
     * Mgr login
     */
    public Manager login(){
        while(true) {
            System.out.print("ID: ");
            String ID = scan.next();
            System.out.print("PW: ");
            String PW = scan.next();
            String result = mgrDBC.login(ID, PW);
            if(result == null){
                System.out.println("You are not registered!");
                return null;
            }
            switch (result) {
                case "SUCCESS":
                    System.out.println("Welcome " + ID);
                    return mgr = mgrDBC.getMgr(ID);
                case "PWFAIL":
                    System.out.println("invalid PW!");
                    return  null;
            }
        }
    }

    /*
     * register New Mgr
     */
    public int regNewMgr() {
        System.out.println("SIGN UP phase");
        System.out.print("ID: ");
        String ID = scan.next();
        System.out.print("PW: ");
        String PW = scan.next();
        System.out.println("(13 integers, except slash and blank)");
        System.out.print("SSN: ");
        String SSN = scan.next();

        int result = mgrDBC.regNewMgr(ID, PW, SSN);

        if(result >= 0) {
            System.out.println("Register Success\n");
            return 1;
        }
        return -1;
    }


    /*
     * show reported videos
     */
    public void showReportedVideo(Manager mgr) {
        mgrDBC.showReportedVideo(mgr);
    }

    /*
     * show reported videos
     */
    public void showAllUsr(Manager mgr) {
        mgrDBC.showAllUsr(mgr);
    }

    /*
     * show user upload record
     */
    public void showUsrUploadRecord(Manager mgr) {
        System.out.println("User ID : ");
        String uID = scan.next();
        if(mgrDBC.showUsrUploadRecord(mgr, uID) < 0){
            System.out.println("Maybe wrong input!");
        }
    }

    /*
     * show user upload record
     */
    public void showUsrWatchRecord(Manager mgr) {
        System.out.println("User ID : ");
        String uID = scan.next();
        if(mgrDBC.showUsrWatchRecord(mgr, uID) < 0){
            System.out.println("Maybe wrong input!");
        }
    }

    /*
     * block user
     */
    public void blockUsr(Manager mgr) {
        System.out.println("User ID : ");
        String uID = scan.next();
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
        if(mgrDBC.blockUsr(mgr, uID, reason) < 0){
            System.out.println("Maybe wrong input!");
        }
    }

    /*
     * unblock user
     */
    public void unblockUsr(Manager mgr) {
        System.out.println("User ID : ");
        String uID = scan.next();
        if(mgrDBC.unblockUsr(mgr, uID) < 0){
            System.out.println("Maybe wrong input!");
        }
    }

    /*
     * remove user
     */
    public void removeUsr(Manager mgr) {
        System.out.println("User ID : ");
        String uID = scan.next();
        if(mgrDBC.removeUsr(mgr, uID) < 0){
            System.out.println("Maybe wrong input!");
        }
    }

    /*
     * remove user
     */
    public void showAllVideo(Manager mgr) {
        mgrDBC.showAllVideo(mgr);
    }

    /*
     * block video
     */
    public void blockVideo(Manager mgr) {
        Video tmpVid = new VidAction().getVid();
        if(tmpVid == null){
            System.out.println("You got a wrong input! return");
            return;
        }
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
        if(mgrDBC.blockVideo(mgr, tmpVid, reason) < 0){
            System.out.println("Maybe wrong input!");
        }
    }

    /*
     * unblock video
     */
    public void unblockVideo(Manager mgr) {
        Video tmpVid = new VidAction().getVid();
        if(tmpVid == null){
            System.out.println("You got a wrong input! return");
            return;
        }
        if(mgrDBC.unblockVideo(mgr, tmpVid) < 0){
            System.out.println("Maybe wrong input!");
        }
    }

    /*
     * remove video
     */
    public void removeVid(Manager mgr) {
        Video tmpVid = new VidAction().getVid();
        if(tmpVid == null){
            System.out.println("You got a wrong input! return");
            return;
        }
        if(mgrDBC.removeVid(mgr, tmpVid) < 0){
            System.out.println("Maybe wrong input!");
        }
    }

    /*
     * add AD
     */
    public void addAd(Manager mgr) {
        System.out.print("AD name : ");
        String adName = scan.next();
        System.out.println("0 - comedy");
        System.out.println("1 - toy");
        System.out.println("2 - gaming");
        System.out.println("3 - education");
        System.out.println("4 - music");
        System.out.println("5 - eating");
        System.out.println("6 - beauty");
        System.out.println("7 - sports");
        System.out.print("AD category: ");
        int adCategory = scan.nextInt();
        if(!(0<=adCategory && adCategory <=7)){
            System.out.println("you chose a wrong input! return");
            return;
        }
        System.out.print("AD profit per view : ");
        int profitPerView = scan.nextInt();
        String adID = EigenValue.getEigenValue();
        if(mgrDBC.addAd(mgr, adName, adID, adCategory, profitPerView)>0){
            System.out.println("insert complete!");
        }
    }

    /*
     * show all ad
     */
    public void showAllAd(Manager mgr) {
        mgrDBC.showAllAd(mgr);
    }

    /*
     * remove ad
     */
    public void removeAd(Manager mgr) {
        System.out.println("ad name : ");
        String adName = scan.next();
        if(mgrDBC.removeAd(mgr, adName)>0){
            System.out.println("remove complete");
        }
    }


    /*
     * show blocked video
     */
    public void showBlockedVideo(Manager mgr) {
        mgrDBC.showBlockedVideo(mgr);
    }

    /*
     * show blocked user
     */
    public void showBlockedUser(Manager mgr) {
        mgrDBC.showBlockedUser(mgr);
    }
}
