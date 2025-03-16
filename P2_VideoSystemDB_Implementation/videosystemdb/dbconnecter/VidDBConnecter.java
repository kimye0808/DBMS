package dbconnecter;

import entity.Usr;
import entity.Video;

import java.sql.*;

public class VidDBConnecter {

    /*
     * Vid get Connection
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
     *  search video by title
     */
    public void srchVidTitle(String title) {
        Connection cnct = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT v_uID " +
                "FROM video " +
                "WHERE vName = ?";
        try{
            cnct = getConnection();
            pstmt = cnct.prepareStatement(sql);
            pstmt.setString(1, title);//parameter ? 1~n
            rs = pstmt.executeQuery();
            System.out.println("--------------------------------------------------");
            System.out.printf("%20s %20s\n", "TITLE", "UPLOADER");
            System.out.println("--------------------------------------------------");
            while(rs.next()){
                String upldr = rs.getString(1);
                System.out.printf("%20s %20s\n", title, upldr);
            }
            System.out.println("--------------------------------------------------");

        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            close(cnct, pstmt, rs);
        }
    }


    /*
     *  search video by uploader's ID
     */
    public void srchVidUpldr(String upldrID) {
        Connection cnct = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT vName " +
                "FROM video " +
                "WHERE v_uID = ?";
        try{
            cnct = getConnection();
            pstmt = cnct.prepareStatement(sql);
            pstmt.setString(1, upldrID);
            rs = pstmt.executeQuery();
            System.out.println("--------------------------------------------------");
            System.out.printf("%20s %20s\n", "TITLE", "UPLOADER");
            System.out.println("--------------------------------------------------");
            while(rs.next()){
                String title = rs.getString(1);
                System.out.printf("%20s %20s\n",title , upldrID);
            }
            System.out.println("--------------------------------------------------");

        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            close(cnct, pstmt, rs);
        }
    }


    /*
     *  search video by video category
     */
    public void srchVidCat(int srchCategory) {
        Connection cnct = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT vName, v_uID " +
                "FROM video " +
                "WHERE vCategory = ?";
        try{
            cnct = getConnection();
            pstmt = cnct.prepareStatement(sql);
            pstmt.setInt(1, srchCategory);
            rs = pstmt.executeQuery();
            System.out.println("--------------------------------------------------");
            System.out.printf("%20s %20s\n", "TITLE", "UPLOADER");
            System.out.println("--------------------------------------------------");
            while(rs.next()){
                String title = rs.getString(1);
                String upldrID = rs.getString(2);
                System.out.printf("%20s %20s\n",title , upldrID);
            }
            System.out.println("--------------------------------------------------");

        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            close(cnct, pstmt, rs);
        }
    }


    /*
     *  get video by video name
     */
    public Video getVid(String vidName) {
        Connection cnct = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * " +
                "FROM video " +
                "WHERE vName = ?";
        try{
            cnct = getConnection();
            pstmt = cnct.prepareStatement(sql);
            pstmt.setString(1, vidName);
            rs = pstmt.executeQuery();
            if(rs.next()){
                String tmpvID = rs.getString(1);
                String tmpvName = rs.getString(2);
                int tmpviews = rs.getInt(3);
                int tmplength = rs.getInt(4);
                int tmpvCategory = rs.getInt(5);
                int tmpageLimit = rs.getInt(6);
                Timestamp tmpupldDate = rs.getTimestamp(7);
                double tmpviewPerTime = rs.getDouble(8);
                String tmpv_mID = rs.getString(9);
                String tmpv_uID = rs.getString(10);
                String tmpv_adID = rs.getString(11);
                return new Video(tmpvID, tmpvName, tmpviews, tmplength,
                        tmpvCategory, tmpageLimit, tmpupldDate, tmpviewPerTime,
                        tmpv_mID, tmpv_uID, tmpv_adID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close(cnct, pstmt, rs);
        }
        return null;
    }

    /*
     *  upload video
     */
    public int uploadVid(String vID, String vName, int length, int vCategory, int ageLimit, Date upldDate, String v_uID) {
        Connection cnct=  null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "INSERT INTO video(vID, vName, vLength, vCategory, ageLimit, upldDate, v_uID, v_adID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        String sqlcnctAd = "UPDATE video SET v_adID = ? WHERE vName = ?";

        try{
            //upload video
            cnct = getConnection();
            pstmt = cnct.prepareStatement(sql);
            pstmt.setString(1, vID);
            pstmt.setString(2, vName);
            pstmt.setInt(3, length);
            pstmt.setInt(4, vCategory);
            pstmt.setInt(5, ageLimit);
            pstmt.setDate(6, upldDate);
            pstmt.setString(7, v_uID);
            pstmt.setString(8, "HYUHYUHYU0");//tmp ad ID value
            int result1 = pstmt.executeUpdate();
            //connect AD and video
            pstmt = cnct.prepareStatement(sqlcnctAd);
            String tmpadID = getRandomAd(vCategory);
            pstmt.setString(1, tmpadID);
            pstmt.setString(2, vName);

            int result2 = pstmt.executeUpdate();

            if(result1 >0 && result2 > 0) return 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            close(cnct, pstmt, rs);
        }
        return -1;
    }
    /*
     *  get random AD ID for connecting video
     */
    public String getRandomAd(int vCategory){
        Connection cnct = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT adID FROM ad WHERE adCategory = ? ORDER BY RAND() LIMIT 1";

        String sql2 = "SELECT adID FROM ad ORDER BY RAND() LIMIT 1";

        try{
            cnct = getConnection();
            pstmt = cnct.prepareStatement(sql);
            pstmt.setInt(1, vCategory);
            rs = pstmt.executeQuery();
            if(rs.next()){
                return rs.getString(1);// if same category ad exists, return
            }

            pstmt = cnct.prepareStatement(sql2);
            rs = pstmt.executeQuery();
            if(rs.next()){
                return rs.getString(1);//if not exists, return random
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            close(cnct, pstmt, rs);
        }
        return null;
    }

    /*
     *  show hot videos upto 10
     */
    public void showHotVid() {
        Connection cnct = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT vName, views, ageLimit, v_uID FROM video ORDER BY viewPerTime DESC LIMIT 7";
        try{
            cnct = getConnection();
            pstmt = cnct.prepareStatement(sql);
            rs = pstmt.executeQuery();

            System.out.println("-----------------------HOT VIDEOS-------------------------AGELIMIT|0:All, 1: 15, 2: 19|");
            int i = 1;
            while(rs.next()){
                String tmpvName = rs.getString(1);
                int tmpviews = rs.getInt(2);
                int tmpageLimit = rs.getInt(3);
                String tmpv_uID = rs.getString(4);
                System.out.println(i+". VIDEO: "+ tmpvName +"|"+"UPLOADER: "+tmpv_uID+"|"+"VIEWS: "+tmpviews+"|"+"AGELIMIT: "+tmpageLimit);
                i++;
            }
            System.out.println();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * report video
     */
    public int reportVid(Usr usr, Video video, int reason) {
        Connection cnct = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String preSql = "SELECT * FROM report WHERE EXISTS (SELECT rpt_uID FROM report WHERE rpt_uID = ? )";

        String sql = "INSERT INTO report(rpt_uID, rpt_vID, reason)" +
                "VALUES (?,?,?) ";

        try{
            cnct = getConnection();
            pstmt = cnct.prepareStatement(preSql);
            pstmt.setString(1, usr.getuID());
            rs = pstmt.executeQuery();
            if(rs.next()){
                System.out.println("You already reported this video!");
                return -1;
            }
            pstmt = cnct.prepareStatement(sql);
            pstmt.setString(1, usr.getuID());
            pstmt.setString(2, video.getvID());
            pstmt.setInt(3, reason);

            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /*
     * show uploaded video list
     */
    public void showUploadedVidList(Usr usr) {
        Connection cnct = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT vName " +
                "FROM video " +
                "WHERE v_uID = ?";
        try{
            cnct = getConnection();
            pstmt = cnct.prepareStatement(sql);
            pstmt.setString(1, usr.getuID());
            rs = pstmt.executeQuery();
            System.out.println("--------------------------------------------------");
            System.out.printf("%20s %20s", "TITLE", "UPLOADER");
            System.out.println();
            System.out.println("--------------------------------------------------");
            while(rs.next()){
                String tmpvName = rs.getString(1);
                System.out.printf("%20s %20s", tmpvName, usr.getuID());
                System.out.println();
            }
            System.out.println();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close(cnct, pstmt, rs);
        }
    }

    /*
     * delete uploaded video
     */
    public int deleteUploadedVid(Usr usr, Video dvideo) {
        Connection cnct = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "DELETE FROM video WHERE vID = ? AND v_uID = ?";

        try{
            cnct = getConnection();
            pstmt = cnct.prepareStatement(sql);
            pstmt.setString(1, dvideo.getvID());
            pstmt.setString(2, usr.getuID());

            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
