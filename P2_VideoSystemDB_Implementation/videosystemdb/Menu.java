
import action.*;
    import entity.*;

    import java.util.Scanner;


    public class Menu {
        Scanner scan = new Scanner(System.in);
        UsrAction usrAct = new UsrAction();
        MgrAction mgrAct = new MgrAction();
        VidAction vidAct = new VidAction();
        PlAction plAct = new PlAction();
        AdAction adAct = new AdAction();

        /*
        * sign up or sign in
        */
        public void signUpOrIn(int usr1mgr2) {
            while (true) {
                System.out.println("0. Return");
                System.out.println("1. Sign up");
                System.out.println("2. Sign in");
                int loginMenuNum = scan.nextInt();
            System.out.println();
            switch (loginMenuNum) {
                case 0:
                    return;//return start phase

                case 1:
                    if (usr1mgr2 == 2) {//if mgr
                        mgrAct.regNewMgr();
                    } else {
                        usrAct.regNewUsr();
                    }
                    break;
                case 2:
                    if (usr1mgr2 == 2) {//if mgr
                        Manager mgr = null;
                        if ((mgr = mgrAct.login()) != null) {
                            mgrMenu(mgr);
                            continue;
                        }
                    }else {
                        Usr usr = null;
                        if ((usr = usrAct.login()) != null) {
                            usrMenu(usr);
                            continue;
                        }
                        break;
                    }
                default:
                    System.out.println("You got a wrong choice!");
                    break;
            }//switch end
        }//while end
    }//signUpOrIn end


    /*
     * manager menu
     */
    public void mgrMenu(Manager mgr){
        while(true){
            System.out.println("0. Return");
            System.out.println("1. Manage user");
            System.out.println("2. Manage video");
            System.out.println("3. Manage ad");
            System.out.println("4. Show reported videos");
            int usrInputNum = scan.nextInt();
            System.out.println();

            switch(usrInputNum){
                case 0:
                    return;
                case 1:
                    manageUser(mgr);
                    break;
                case 2:
                    manageVideo(mgr);
                    break;
                case 3:
                    manageAd(mgr);
                    break;
                case 4:
                    mgrAct.showReportedVideo(mgr);
                    break;
                default:
                    System.out.println("You got a wrong choice!");
                    break;
            }//switch end
        }//while end
    }//mgrMenu method end

    /*
     * manage user menu from mgrMenu
     */
    public void manageUser(Manager mgr){
        while(true){
            System.out.println("0. Return");
            System.out.println("1. Show all users");
            System.out.println("2. Show user's upload record");
            System.out.println("3. Show user's watch record");
            System.out.println("4. Block User");
            System.out.println("5. Unblock User");
            System.out.println("6. Remove User");
            System.out.println("7. Show blocked user");

            int usrInputNum = scan.nextInt();
            System.out.println();

            switch(usrInputNum){
                case 0:
                    return;
                case 1:
                    mgrAct.showAllUsr(mgr);
                    break;
                case 2:
                    mgrAct.showUsrUploadRecord(mgr);
                    break;
                case 3:
                    mgrAct.showUsrWatchRecord(mgr);
                    break;
                case 4:

                    mgrAct.blockUsr(mgr);
                    break;
                case 5:
                    mgrAct.unblockUsr(mgr);
                    break;
                case 6:
                    mgrAct.removeUsr(mgr);
                    break;
                case 7:
                    mgrAct.showBlockedUser(mgr);
                    break;
                default:
                    System.out.println("You got a wrong choice!");
                    break;
            }//switch end
        }//while end
    }//manageUser method end

    /*
     * manage video menu from mgrMenu
     */
    public void manageVideo(Manager mgr){
        while(true){
            System.out.println("0. Return");
            System.out.println("1. Show all vidoes");
            System.out.println("2. Block video");
            System.out.println("3. Unblock video");
            System.out.println("4. Remove video");
            System.out.println("5. Show blocked video");
            int usrInputNum = scan.nextInt();
            System.out.println();

            switch(usrInputNum){
                case 0:
                    return;
                case 1:
                    mgrAct.showAllVideo(mgr);
                    break;
                case 2:
                    mgrAct.blockVideo(mgr);
                    break;
                case 3:
                    mgrAct.unblockVideo(mgr);
                    break;
                case 4:
                    mgrAct.removeVid(mgr);
                    break;
                case 5:
                    mgrAct.showBlockedVideo(mgr);
                    break;

                default:
                    System.out.println("You got a wrong choice!");
                    break;
            }//switch end
        }//while end
    }//manageVideo method end

    /*
     * manage ad menu from mgrMenu
     */
    public void manageAd(Manager mgr){
        while(true){
            System.out.println("0. Return");
            System.out.println("1. Show all ads");
            System.out.println("2. Add ad");//video에 ad도 추가시켜야함, CATEGORY 같은거 같으면 pPv 큰거
            System.out.println("3. Remove ad");
            int usrInputNum = scan.nextInt();
            System.out.println();

            switch(usrInputNum){
                case 0:
                    return;
                case 1:
                    mgrAct.showAllAd(mgr);
                    break;
                case 2:
                    mgrAct.addAd(mgr);
                    break;
                case 3:
                    mgrAct.removeAd(mgr);
                    break;
                case 4:
                    mgrAct.showReportedVideo(mgr);
                    break;
                default:
                    System.out.println("You got a wrong choice!");
                    break;
            }//switch end
        }//while end
    }//manageAd method end



    /*
     * user menu
     */
    public void usrMenu(Usr usr){
        while(true){
            System.out.println("0. Return");
            System.out.println("1. Video");
            System.out.println("2. User Information");
            System.out.println("3. User Playlist");
            System.out.println("4. Grade up");

            int usrInputNum = scan.nextInt();
            System.out.println();

            switch(usrInputNum){
                case 0:
                    return;
                case 1:
                    videoMenu(usr);
                    break;
                case 2:
                    System.out.println(usr);
                    break;
                case 3:
                    playlistMenu(usr);
                    break;
                case 4:
                    usrAct.gradeUp(usr);
                    break;
                default:
                    System.out.println("You got a wrong choice!");
                    break;
            }//switch end
        }//while end
    }//usrScreen end


    /*
     * user video menu from user menu
     */
    private void videoMenu(Usr usr) {
        while(true){
            System.out.println("0. Return");
            System.out.println("1. Show Popular Videos");
            System.out.println("2. Search Video By Title");
            System.out.println("3. Search Video By Uploader's ID");//추가
            System.out.println("4. Show Video in a specific Category");//추가
            System.out.println("5. Watch Video");
            System.out.println("6. Upload Video");
            System.out.println("7. Show uploaded Video list");
            System.out.println("8. Delete uploaded video");
            System.out.println("9. Report Video");
            int usrInputNum = scan.nextInt();
            System.out.println();

            switch(usrInputNum){
                case 0:
                    return;
                case 1:
                    vidAct.showHotVid();
                    break;
                case 2:
                    vidAct.srchVidTitle();
                    break;
                case 3:
                    vidAct.srchVidUpldr();
                    break;
                case 4:
                    vidAct.srchVidCategory();
                    break;
                case 5:
                    Video video = vidAct.getVid();//getvid에서 vid이름 입력
                    if(video == null){
                        System.out.println("it does not exists!");
                        break;
                    }
                    if(usr.getGrade() == 1){
                        usrAct.watchVidWithoutAd(usr, video);
                    }else if(usr.getGrade() == 0){
                        Ad ad = adAct.getAd(video);
                        if(ad != null) {
                            usrAct.watchVidWithAd(usr, video, ad);
                        }
                    }
                    break;
                case 6:
                    vidAct.uploadVid(usr);
                    break;
                case 7:
                    vidAct.showUploadedVidList(usr);
                    break;
                case 8:
                    Video dvideo = vidAct.getVid();//getvid에서 vid이름 입력
                    if(dvideo == null){
                        System.out.println("it does not exists!");
                        break;
                    }
                    vidAct.deleteUploadedVid(usr, dvideo);
                    break;
                case 9:
                    Video rVideo = vidAct.getVid();
                    if(rVideo == null){
                        System.out.println("it does not exists!");
                        break;
                    }
                    vidAct.reportVid(usr, rVideo);
                    break;
                default:
                    System.out.println("You got a wrong choice!");
                    break;

            }
        }
    }


    /*
     * user playlist menu from user menu
     */
    private void playlistMenu(Usr usr) {
        while(true){
            System.out.println("0. Return");
            System.out.println("1. Show playlists");
            System.out.println("2. Make a new playlist");
            System.out.println("3. Delete a playlist");
            System.out.println("4. Show a specific playlist");
            int usrInputNum = scan.nextInt();
            System.out.println();

            switch(usrInputNum) {
                case 0:
                    return;
                case 1:
                    plAct.showAllPlaylists(usr);
                    break;
                case 2:
                    plAct.makePlaylist(usr);
                    break;
                case 3:
                    plAct.deletePlaylist(usr);
                    break;
                case 4:
                    Playlist plist = plAct.getPlaylist(usr);
                    if (plist == null) {
                        System.out.println("getPlaylist error");
                        break;
                    }
                    specificPlaylistMenu(usr,plist);
                    break;
                default:
                    System.out.println("You got a wrong choice!");
                    break;
            }//switch end
        }//while end
    }//plmenu end


    /*
     * show a specific playlist menu from playlist menu
     */
    private void specificPlaylistMenu(Usr usr, Playlist plist) {
        while(true){
            System.out.println("0. Return");
            System.out.println("1. Show All Videos In Playlist");
            System.out.println("2. Add Video In Playlist");
            System.out.println("3. Delete Video In Playlist");
            int userInputNum = scan.nextInt();
            System.out.println();

            switch(userInputNum){
                case 0:
                    return;
                case 1:
                    plAct.showVideosInPl(usr, plist);
                    break;
                case 2:
                    plAct.addVidInList(usr, plist);
                    break;
                case 3:
                    plAct.deleteVidInList(usr, plist);
                    break;
                default:
                    System.out.println("You got a wrong choice!");
                    break;
            }//switch end
        }//while end
    }//sPLM method end


}//class Menu end


