package action;

import dbconnecter.PlDBConnecter;
import entity.Playlist;
import entity.Usr;
import entity.Video;

import java.util.Scanner;

public class PlAction {
    Scanner scan = new Scanner(System.in);
    PlDBConnecter plDBC = new PlDBConnecter();

    /*
     * make a playlist
     */
    public void makePlaylist(Usr usr) {
        System.out.print("playlist name: ");
        String plName = scan.next();
        if(plDBC.makePlaylist(usr, plName)<0){
            System.out.println("error! check if the name of playlist is duplicated");
        }
    }

    /*
     * delete a playlist
     */
    public void deletePlaylist(Usr usr) {
        System.out.print("playlist name: ");
        String plName = scan.next();
        if(plDBC.deletePlaylist(usr, plName)<0){
            System.out.println("error! check if the name of playlist is wrong");
        }
    }


    /*
     * get a specific playlist
     */
    public Playlist getPlaylist(Usr usr) {
        System.out.print("playlist name: ");
        String plName = scan.next();
        Playlist plist = null;
        if((plist = plDBC.getPlaylist(usr, plName)) == null){
            System.out.println("error! check if the name of playlist is wrong");
            return null;
        }
        return plist;
    }


    /*
     * show all playlists
     */
    public void showAllPlaylists(Usr usr) {
        plDBC.showAllPlaylists(usr);
    }

    /*
     * show video in a playlist
     */
    public void showVideosInPl(Usr usr, Playlist plist) {
        plDBC.showVideosInPl(usr, plist);
    }


    /*
     * delete video in a playlist
     */
    public void deleteVidInList(Usr usr, Playlist plist) {
        Video video = getVideoInPl(usr, plist);
        if(video == null){
            System.out.println("deleteVidInList error");
            return;
        }
        if(plDBC.deleteVidInList(usr, plist, video)==1){
            System.out.println("delete Success");
        }

    }

    /*
     * get a video in a playlist
     */
    public Video getVideoInPl(Usr usr, Playlist plist){
        System.out.println("video name : ");
        String vName = scan.next();
        return plDBC.getVideoInPl(usr, plist, vName);
    }

    /*
     * add video in a specific plist
     */
    public void addVidInList(Usr usr, Playlist plist) {
        Video video = new VidAction().getVid();
        if(video == null){
            System.out.println("it does not exist!");
            return;
        }
        if (plDBC.getVideoInPl(usr, plist, video.getvName()) != null) {
            System.out.println("it already exists!");
            return;
        }
        plDBC.addVidInList(usr, plist, video);
    }
}
