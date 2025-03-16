package action;

import dbconnecter.UsrDBConnecter;
import entity.Ad;
import entity.Usr;
import entity.Video;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class UsrAction {
    Scanner scan = new Scanner(System.in);
    Usr usr;
    UsrDBConnecter usrDBC = new UsrDBConnecter();

    /*
     * Usr login
     */
    public Usr login(){
        System.out.print("ID: ");
        String ID = scan.next();
        if(checkIfUsrBlocked(ID) == 1){
            System.out.println("You are blocked");
            return null;
        }
        System.out.print("PW: ");
        String PW = scan.next();
        String result = usrDBC.login(ID, PW);
        if(result == null){
            System.out.println("You are not registered!");
            return null;
        }
        switch (result) {
            case "SUCCESS":
                System.out.println("Welcome " + ID);
                return usr = usrDBC.getUsr(ID);
            case "PWFAIL":
                System.out.println("invalid PW!");
                return null;
        }
        return null;
    }
    /*
     * check if user blocked
     */
    public int checkIfUsrBlocked(String uID){
        return usrDBC.checkIfUsrBlocked(uID);
    }

    /*
     * register New Usr
     */
    public int regNewUsr() {
        System.out.println("SIGN UP phase");
        System.out.print("ID: ");
        String ID = scan.next();
        System.out.print("PW: ");
        String PW = scan.next();
        System.out.println("(13 integers, except slash and blank)");
        System.out.print("SSN: ");
        String SSN = scan.next();
        if(SSN.length() != 13){
            System.out.println("You got a wrong input!");
            return -1;
        }
        //grade 는 default로 0
        System.out.println("What are your interests?");
        System.out.println("0 - comedy");
        System.out.println("1 - toy");
        System.out.println("2 - gaming");
        System.out.println("3 - education");
        System.out.println("4 - music");
        System.out.println("5 - eating");
        System.out.println("6 - beauty");
        System.out.println("7 - sports");
        int interests = scan.nextInt();
        if(interests < 0 || interests >7){
            System.out.println("You got a wrong input!");
            return -1;
        }
        int result = usrDBC.regNewUsr(ID, PW, SSN, 0, interests);

        if(result >= 0) {
            System.out.println("Register Success\n");
            return 1;
        }
        return -1;
    }


    /*
     * watch video with ad
     */
    public void watchVidWithAd(Usr usr, Video video, Ad ad) {
        if(checkIfVideoBlocked(video) == 1){
            System.out.println("the video is blocked");
            return;
        }
        String vName = video.getvName();
        String uploader = video.getV_uID();
        Timestamp uploadDate = video.getUpldDate();
        int vLength = video.getLength();
        int views = video.getViews();

        boolean isSkip;

        System.out.println("------------------------------------------------------------------------------------------------------------");
        System.out.printf("%20s %20s %20s %20d %20d\n", vName, uploader, uploadDate,vLength, views);
        System.out.println("------------------------------------------------------------------------------------------------------------");


        System.out.println("[AD TIME 10S : "+ ad.getAdName() + "]");
        for(int i = 0; i < 3;i++) {
            try {
                System.out.print("AD~");
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("SKIP : skip , CONTINUE : ANYTHING");
        String usrInput = null;
        while(true){
            usrInput = scan.next();
            if(usrInput != null) break;
        }
        if(!usrInput.equalsIgnoreCase("skip")){
            isSkip = false;
            for(int i = 0; i < 7;i++) {
                try {
                    System.out.print("AD~");
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        isSkip = true;
        System.out.println();
        System.out.println("VIDEO PLAYING");
        usrDBC.watchVidWithAd(usr, video, ad, isSkip);
        System.out.println("VIDEO FINISH");
    }

    /*
     * watch video without ad
     */
    public void watchVidWithoutAd(Usr usr, Video video){
        if(checkIfVideoBlocked(video) == 1){
            System.out.println("the video is blocked");
            return;
        }
        String vName = video.getvName();
        String uploader = video.getV_uID();
        Timestamp uploadDate = video.getUpldDate();
        int vLength = video.getLength();
        int views = video.getViews();


        System.out.println("------------------------------------------------------------------------------------------------------------");
        System.out.printf("%20s %20s %20s %20d %20d\n", vName, uploader, uploadDate,vLength, views);
        System.out.println("------------------------------------------------------------------------------------------------------------");

        System.out.println("VIDEO PLAYING");
        usrDBC.watchVidWithoutAd(usr, video);
        System.out.println("VIDEO FINISH");
    }

    /*
     * check if video blocked
     */
    public int checkIfVideoBlocked(Video video){
        return usrDBC.checkIfVideoBlocked(video);
    }
    /*
     * grade up
     */
    public void gradeUp(Usr usr) {
        System.out.println("grade up : 1000 WON");
        System.out.println("0. Return");
        System.out.println("1. Pay");
        int usrInputNum = scan.nextInt();
        System.out.println();

        switch(usrInputNum){
            case 0:
                return;
            case 1:
                if(usr.getGrade() == 1){
                    System.out.println("You already graded up! return");
                    return;
                }
                usrDBC.gradeUp(usr);
                break;
            default:
                System.out.println("You chose a wrong number! return");
                return;
        }
    }


}
