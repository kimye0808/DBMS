package dbconnecter;

import entity.Ad;
import entity.Manager;
import entity.Usr;
import entity.Video;

import java.net.ConnectException;
import java.sql.*;

public class UsrDBConnecter {


    /*
     * get connection
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
     * user login
     */
    public String login(String id, String pw){
        Connection cnct = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT uID, uPW FROM usr";

        try{
            cnct = getConnection();
            pstmt = cnct.prepareStatement(sql);
            rs = pstmt.executeQuery(sql);
            while (rs.next()) {
                if(rs.getString(1).equals(id)){
                    if(rs.getString(2).equals(pw)){
                        return "SUCCESS";

                    }else{
                        return "PWFAIL";
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            close(cnct, pstmt, rs);
        }
        return null;
    }

    /*
     * return User after find in DB
     */
    public Usr getUsr(String id){
        Connection cnct = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM usr";

        try{
            cnct = getConnection();
            pstmt = cnct.prepareStatement(sql);
            rs = pstmt.executeQuery(sql);
            while(rs.next()){
                if(rs.getString(1).equals(id)) {
                    String ID = rs.getString(1);
                    String PW = rs.getString(2);
                    String SSN = rs.getString(3);
                    int grade = rs.getInt(4);
                    int interests = rs.getInt(5);
                    return new Usr(ID, PW, SSN, grade, interests);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            close(cnct, pstmt, rs);
        }
        return null;
    }

    /*
     *  reg new usr on db
     */
    public int regNewUsr(String id, String pw, String ssn, int grade, int interests) {
        Connection cnct = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "INSERT INTO usr(uID, uPW, uSSN, grade, interests) VALUES (?, ?, ?, ?, ?)";
        try {
            cnct = getConnection();
            pstmt = cnct.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, pw);
            pstmt.setString(3, ssn);
            pstmt.setInt(4, grade);
            pstmt.setInt(5, interests);
            return pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(cnct, pstmt, rs);
        }
        return -1;
    }


    /*
     *  watch a video without AD
     */
    public void watchVidWithoutAd(Usr usr, Video video) {
        Connection cnct = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "UPDATE video " +
                "SET views = views + 1 " +
                "WHERE vName = ?";

        //update viewPerTime
        video.updateViewPerTime();
        String sqlVPT = "UPDATE video SET viewPerTime = ? WHERE vName = ?";

        String sqlWatchRecord = "INSERT INTO watchV(wv_uID, wv_vID, wDate)" +
                "VALUES (?,?,?)";
        try{
            cnct = getConnection();
            pstmt = cnct.prepareStatement(sql);
            pstmt.setString(1, video.getvName());
            pstmt.executeUpdate();
            video.setViews(video.getViews() + 1);

            //update viewPerTime
            pstmt = cnct.prepareStatement(sqlVPT);
            pstmt.setDouble(1, video.getViewPerTime());
            pstmt.setString(2, video.getvName());
            pstmt.executeUpdate();

            //insert watchRecord
            pstmt = cnct.prepareStatement(sqlWatchRecord);
            pstmt.setString(1, usr.getuID());
            pstmt.setString(2, video.getvID());
            pstmt.setTimestamp(3, new java.sql.Timestamp(new java.util.Date().getTime()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            close(cnct, pstmt, rs);
        }
    }

    /*
     *  watch a video with AD
     */
    public void watchVidWithAd(Usr usr, Video video, Ad ad, boolean isSkip) {
        Connection cnct=  null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        //update view
        String sql = "UPDATE video "+
                "SET views = views + 1 " +
                "WHERE vName = ?";
        //update viewPerTime
        video.updateViewPerTime();
        String sqlVPT = "UPDATE video "+
                "SET viewPerTime = ? " +
                "WHERE vName = ?";
        //updateAddView
        String sqlAd;
        double tmp;
        if(isSkip){
            sqlAd = "UPDATE ad "+
                    "SET adView = adView + 0.5 WHERE adID = ?";
            tmp = 0.5;
        }else{
            sqlAd = "UPDATE ad "+
                    "SET adView = adView + 1 WHERE adID = ?";
            tmp = 1;
        }

        //insert watch record
        String sqlWatchRecord = "INSERT INTO watchV(wv_uID, wv_vID, wDate)" +
                "VALUES (?,?,?)";


        try{
            //update video db
            cnct = getConnection();
            pstmt = cnct.prepareStatement(sql);
            pstmt.setString(1, video.getvName());
            pstmt.executeUpdate();
            video.setViews(video.getViews()+1);
            //update viewPerTime
            pstmt = cnct.prepareStatement(sqlVPT);
            pstmt.setDouble(1, video.getViewPerTime());
            pstmt.setString(2, video.getvName());
            pstmt.executeUpdate();
            //update ad db
            pstmt = cnct.prepareStatement(sqlAd);
            pstmt.setString(1, ad.getAdID());
            pstmt.executeUpdate();
            ad.setAdView(ad.getAdView() + tmp);

            //insert watchRecord
            pstmt = cnct.prepareStatement(sqlWatchRecord);
            pstmt.setString(1, usr.getuID());
            pstmt.setString(2, video.getvID());
            pstmt.setTimestamp(3, new java.sql.Timestamp(new java.util.Date().getTime()));
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            close(cnct, pstmt, rs);
        }
    }


    /*
     *  watch a video with AD
     */
    public void gradeUp(Usr usr) {
        Connection cnct = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "UPDATE usr "+
                "SET grade = grade + 1 WHERE uID = ?";
        try{
            cnct = getConnection();
            pstmt = cnct.prepareStatement(sql);
            pstmt.setString(1, usr.getuID());
            pstmt.executeUpdate();
            usr.setGrade();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            close(cnct, pstmt, rs);
        }
    }

    /*
     *  check if video blocked
     */
    public int checkIfVideoBlocked(Video video) {
        Connection cnct = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT *" +
                "FROM blockV WHERE bv_vID = ?";

        try{
            cnct = getConnection();
            pstmt = cnct.prepareStatement(sql);
            pstmt.setString(1, video.getvID());
            rs = pstmt.executeQuery();
            if(rs.next()){
                return 1;
            }else{
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            close(cnct, pstmt, rs);
        }
        return 0;
    }

    /*
     *  check if user blocked
     */
    public int checkIfUsrBlocked(String uID) {
        Connection cnct = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM blockU WHERE bu_uID = ?";


        try{
            cnct = getConnection();
            pstmt = cnct.prepareStatement(sql);
            pstmt.setString(1, uID);
            rs = pstmt.executeQuery();
            if(rs.next()){
                return 1;
            }else{
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            close(cnct, pstmt, rs);
        }
        return 0;
    }
}
