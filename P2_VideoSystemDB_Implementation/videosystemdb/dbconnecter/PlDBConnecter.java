package dbconnecter;

import entity.Playlist;
import entity.Usr;
import entity.Video;

import java.sql.*;

public class PlDBConnecter {


    /*
     * playlist get Connection
     */
    public Connection getConnection(){
        try{
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/videosystemdb", "root", "dudgus123");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     *  close all connect
     */
    private void close(Connection cnct, PreparedStatement pstmt, ResultSet rs) {
        try {
            if(rs != null) {
                rs.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if(pstmt != null) {
                pstmt.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if(cnct != null) {
                cnct.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * make a playlist
     */
    public int makePlaylist(Usr usr, String plName) {
        Connection cnct = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        String sql = "INSERT INTO playlist(pName, pl_uID, plCategory)" +
                "VALUES (?, ?, ?)";
        try{
            cnct = getConnection();
            pstmt = cnct.prepareStatement(sql);
            pstmt.setString(1, plName);
            pstmt.setString(2, usr.getuID());
            pstmt.setInt(3, usr.getInterests());//default category 는 usr의 interest
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close(cnct, pstmt, rs);
        }
        return -1;
    }


    /*
     * delete a playlist
     */
    public int deletePlaylist(Usr usr, String plName) {
        Connection cnct = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        String sql = "DELETE FROM playlist " +
                "WHERE pName = ? AND pl_uID = ?";
        try{
            cnct = getConnection();
            pstmt = cnct.prepareStatement(sql);
            pstmt.setString(1, plName);
            pstmt.setString(2, usr.getuID());
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close(cnct, pstmt, rs);
        }
        return -1;
    }

    /*
     * delete a playlist
     */
    public Playlist getPlaylist(Usr usr, String plName) {
        Connection cnct = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM playlist " +
                "WHERE pName = ? AND pl_uID = ?";
        try{
            cnct =getConnection();
            pstmt = cnct.prepareStatement(sql);
            pstmt.setString(1, plName);
            pstmt.setString(2, usr.getuID());
            rs = pstmt.executeQuery();
            if(rs.next()){
                String tmpPlName = rs.getString(1);
                String tmpPUID = rs.getString(2);
                int tmpPlCategory = rs.getInt(3);
                return new Playlist(tmpPlName, tmpPUID, tmpPlCategory);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            close(cnct, pstmt, rs);
        }
        return null;

    }

    /*
     * delete a playlist
     */
    public void showAllPlaylists(Usr usr) {
        Connection cnct = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT pName, plCategory FROM playlist WHERE pl_uID = ?";
        try{
            cnct = getConnection();
            pstmt = cnct.prepareStatement(sql);
            pstmt.setString(1, usr.getuID());
            rs = pstmt.executeQuery();
            System.out.println("----------------------------------------");
            System.out.printf("%5s %20s\n", "Playlist", "Category");
            System.out.println("----------------------------------------");
            while(rs.next()){
                String pName = rs.getString(1);
                String plCategory = rs.getString(2);
                System.out.printf("%5s %20s\n", pName, plCategory);
            }
            System.out.println("----------------------------------------");
            return;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            close(cnct, pstmt,rs);
        }
    }


    /*
     * show all videos in a playlist
     */
    public void showVideosInPl(Usr usr, Playlist plist) {
        Connection cnct = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT vName, v_uID, upldDate, views "+
                "FROM video, registerPl usr WHERE vID = reg_vID AND reg_pName = ?";
        try{
            cnct = getConnection();
            pstmt = cnct.prepareStatement(sql);
            pstmt.setString(1, plist.getpName());
            rs = pstmt.executeQuery();
            System.out.println("------------------------------------------------------------------------------------------");
            System.out.printf("%20s %20s %20s %20s" , "VIDEO", "UPLOADER", "UPLOAD DATE", "VIEWS");
            System.out.println();
            System.out.println("------------------------------------------------------------------------------------------");
            while(rs.next()){
                String vName = rs.getString(1);
                String uploader = rs.getString(2);
                Timestamp upldDate = rs.getTimestamp(3);
                int tmpviews = rs.getInt(4);
                System.out.printf("%20s %20s %20s %20d\n", vName, uploader, upldDate, tmpviews);
            }
            System.out.println("------------------------------------------------------------------------------------------");
            return;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            close(cnct, pstmt, rs);
        }
    }//showvinpl method end

    /*
     * get a video in a playlist
     */
    public Video getVideoInPl(Usr usr, Playlist plist, String vName) {
        Connection cnct = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT vID, vName, views, vLength, vCategory, ageLimit, upldDate, viewPerTime, v_mID, v_uID, v_adID " +
                "FROM video, playlist, registerPl WHERE reg_pName = pName AND reg_pl_uID = pl_uID AND reg_vID = vID " +
                "AND pName = ? AND vName = ?";
        try{
            cnct = getConnection();
            pstmt = cnct.prepareStatement(sql);
            pstmt.setString(1, plist.getpName());
            pstmt.setString(2, vName);
            rs = pstmt.executeQuery();
            if(rs.next()){
                String tmpvID = rs.getString(1);
                String tmpvName = rs.getString(2);
                int tmpviews = rs.getInt(3);
                int tmplength = rs.getInt(4);
                int tmpvCategory = rs.getInt(5);
                int tmpageLimit = rs.getInt(6);
                Timestamp tmpupldDate = rs.getTimestamp(7);
                int tmpviewPerTime = rs.getInt(8);
                String tmpv_mID = rs.getString(9);
                String tmpv_uID = rs.getString(10);
                String tmpv_adID = rs.getString(11);
                return new Video(tmpvID, tmpvName, tmpviews, tmplength, tmpvCategory,
                        tmpageLimit,tmpupldDate,tmpviewPerTime, tmpv_mID,
                        tmpv_uID, tmpv_adID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            close(cnct, pstmt, rs);
        }
        return null;
    }//getVinPl method end


    /*
     * delete a video in a playlist
     */
    public int deleteVidInList(Usr usr, Playlist plist, Video video) {
        Connection cnct = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "DELETE FROM registerPl WHERE reg_vID = ? AND reg_pName = ?";
        try{
            cnct = getConnection();
            pstmt = cnct.prepareStatement(sql);
            pstmt.setString(1, video.getvID());
            pstmt.setString(2, plist.getpName());

            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            close(cnct, pstmt, rs);
        }
        return 0;
    }


    /*
     * add a video in a playlist
     */
    public int addVidInList(Usr usr, Playlist plist, Video video) {
        Connection cnct = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "INSERT INTO registerPl "+
                "VALUES(?, ?, ?)";
        try{
            cnct = getConnection();
            pstmt = cnct.prepareStatement(sql);
            pstmt.setString(1, plist.getpName());
            pstmt.setString(2, usr.getuID());
            pstmt.setString(3, video.getvID());
            if(pstmt.executeUpdate() > 0) return 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            close(cnct, pstmt, rs);
        }
        return -1;
    }
}//dbconnecter end