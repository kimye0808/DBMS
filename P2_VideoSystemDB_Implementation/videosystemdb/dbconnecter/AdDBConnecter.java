package dbconnecter;

import entity.Ad;

import java.sql.*;

public class AdDBConnecter {
    /*
     * Ad get Connection
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

    public Ad getAd(String adID) {
        Connection cnct = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * "+
                "FROM ad " +
                "WHERE adID = ?";
        try{
            cnct = getConnection();
            pstmt = cnct.prepareStatement(sql);
            pstmt.setString(1, adID);
            rs = pstmt.executeQuery();
            if(rs.next()){
                String tmpadName = rs.getString(1);
                String tmpadID = rs.getString(2);
                int tmpadCategory = rs.getInt(3);
                int tmpadView = rs.getInt(4);
                int tmpprofitPerView = rs.getInt(5);
                String tmpad_mID = rs.getString(6);
                return new Ad(tmpadCategory, tmpadView, tmpprofitPerView,
                        tmpadName, tmpadID, tmpad_mID);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
               close(cnct, pstmt, rs);
        }
        return null;
    }
}
