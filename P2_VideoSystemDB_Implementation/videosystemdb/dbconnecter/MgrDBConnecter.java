package dbconnecter;

import entity.Manager;
import entity.Video;

import java.sql.*;

public class MgrDBConnecter {

    /*
     * Mgr get Connection
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
     * Mgr login check with db
     */
    public String login(String id, String pw){
        Connection cnct = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT mID, mPW FROM manager";

        try{
            cnct = getConnection();
            pstmt = cnct.prepareStatement(sql);
            rs = pstmt.executeQuery(sql);
            while(rs.next()){
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
     *  return mgr after find in db
     */
    public Manager getMgr(String id){
        Connection cnct = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM manager";

        try{
            cnct = getConnection();
            pstmt = cnct.prepareStatement(sql);
            rs = pstmt.executeQuery(sql);
            while(rs.next()){
                if(rs.getString(1).equals(id)) {
                    String ID = rs.getString(1);
                    String PW = rs.getString(2);
                    String SSN = rs.getString(3);
                    return new Manager(ID, PW, SSN);
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
     *  reg new mgr on db
     */
    public int regNewMgr(String id, String pw, String ssn) {
        Connection cnct = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "INSERT INTO manager(mID, mPW, mSSN) VALUES (?, ?, ?)";
        try {
            cnct = getConnection();
            pstmt = cnct.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, pw);
            pstmt.setString(3, ssn);
            return pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(cnct, pstmt, rs);
        }
        return -1;
    }

    /*
     *  Show reported video
     */
    public void showReportedVideo(Manager mgr) {
        Connection cnct = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT vName, v_uID, reason, rpt_uID "+
                "FROM video, report " +
                "WHERE rpt_vID = vID";

        try{
            cnct = getConnection();
            pstmt = cnct.prepareStatement(sql);
            rs = pstmt.executeQuery();
            System.out.println("---------------------------Reported videos---------------------------------------------");
            System.out.printf("%20s%20s%20s%10s", "VIDEO", "UPLOADER", "REPORTER", "REASON");
            System.out.println();
            while(rs.next()){
                String tmpvName = rs.getString(1);
                String tmpv_uID = rs.getString(2);
                int tmpreason = rs.getInt(3);
                String tmprpt_uID = rs.getString(4);
                System.out.printf("%20s%20s%20s%10s", tmpvName, tmpv_uID, tmprpt_uID, tmpreason);
                System.out.println();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            close(cnct, pstmt, rs);
        }
    }

    /*
     *  Show All Usr
     */
    public void showAllUsr(Manager mgr) {
        Connection cnct = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT uID, uPW, uSSN, grade, interests "+
                "FROM usr ";

        try{
            cnct = getConnection();
            pstmt = cnct.prepareStatement(sql);
            rs = pstmt.executeQuery();
            System.out.println("---------------------------User list----------------------------------------------------------------");
            System.out.printf("%20s%20s%20s%10s%10s", "ID", "PW", "SSN", "GRADE", "INTERESTS");
            System.out.println();
            while(rs.next()){
                String tmpuID = rs.getString(1);
                String tmpuPW = rs.getString(2);
                String tmpuSSN = rs.getString(3);
                int tmpgrade = rs.getInt(4);
                int tmpinterests = rs.getInt(5);
                System.out.printf("%20s%20s%20s%10d%10d", tmpuID, tmpuPW, tmpuSSN, tmpgrade, tmpinterests);
                System.out.println();
            }
            System.out.println("------------------------------------------------------------------------------------------------------");
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            close(cnct, pstmt, rs);
        }
    }

    /*
     *  Show usr upload record
     */
    public int showUsrUploadRecord(Manager mgr, String uID) {
        Connection cnct = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT uID, vName, upldDate "+
                "FROM video, usr " +
                "WHERE uID = ? AND v_uID = uID";
        try{
            cnct = getConnection();
            pstmt = cnct.prepareStatement(sql);
            pstmt.setString(1, uID);
            rs = pstmt.executeQuery();
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.printf("%20s%20s%30s", "ID", "VIDEO", "UPLOAD_DATE");
            System.out.println();
            while(rs.next()){
                String tmpuID = rs.getString(1);
                String tmpvName = rs.getString(2);
                Date tmpupldDate = rs.getDate(3);
                System.out.printf("%20s%20s%30s", tmpuID, tmpvName, tmpupldDate );
                System.out.println();
            }
            System.out.println("-------------------------------------------------------------------------------------");
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            close(cnct, pstmt, rs);
        }
        return -1;
    }

    /*
     *  Show usr watch record
     */
    public int showUsrWatchRecord(Manager mgr, String uID) {
        Connection cnct = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT wv_uID, vName, wDate "+
                "FROM video, watchV " +
                "WHERE wv_uID = ? AND wv_vID = vID";
        try{
            cnct = getConnection();
            pstmt = cnct.prepareStatement(sql);
            pstmt.setString(1, uID);
            rs = pstmt.executeQuery();
            System.out.println("--------------------------------------------------------------------------------------");
            System.out.printf("%20s%20s%20s", "ID", "VIDEO","DATE");
            System.out.println();
            while(rs.next()){
                String tmpuID = rs.getString(1);
                String tmpvName = rs.getString(2);
                Timestamp tmpwDate = rs.getTimestamp(3);
                System.out.printf("%20s%20s%20s", tmpuID, tmpvName, tmpwDate );
                System.out.println();
            }
            System.out.println("---------------------------------------------------------------------------------------");
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            close(cnct, pstmt, rs);
        }
        return -1;
    }

    /*
     *  block User
     */
    public int blockUsr(Manager mgr, String uID, int reason) {
        Connection cnct = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String preSql = "SELECT * FROM blockU WHERE EXISTS (SELECT bu_uID FROM blockU WHERE bu_uID = ?)";

        String sql = "INSERT INTO blockU(bu_reason, bu_mID, bu_uID) VALUES (?, ?, ?)";
        try {
            cnct = getConnection();
            pstmt = cnct.prepareStatement(preSql);
            pstmt.setString(1, uID);
            rs = pstmt.executeQuery();
            if(rs.next()){
                System.out.println("Somebody already blocked this user");
                return -1;
            }

            pstmt = cnct.prepareStatement(sql);
            pstmt.setInt(1, reason);
            pstmt.setString(2, mgr.getmID());
            pstmt.setString(3, uID);
            return pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(cnct, pstmt, rs);
        }
        return -1;
    }

    /*
     *  block User
     */
    public int unblockUsr(Manager mgr, String uID) {
        Connection cnct = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String preSql = "SELECT * FROM blockU WHERE EXISTS (SELECT bu_uID FROM blockU WHERE bu_uID = ?)";

        String sql = "DELETE FROM blockU WHERE bu_uID = ?";
        try {
            cnct = getConnection();
            pstmt = cnct.prepareStatement(preSql);
            pstmt.setString(1, uID);
            rs = pstmt.executeQuery();
            if(!rs.next()){
                System.out.println("This user is not blocked!");
                return -1;
            }

            pstmt = cnct.prepareStatement(sql);
            pstmt.setString(1, uID);
            return pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(cnct, pstmt, rs);
        }
        return -1;
    }

    /*
     *  block User
     */
    public int removeUsr(Manager mgr, String uID) {
        Connection cnct = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "DELETE FROM usr WHERE uID = ?";
        try {
            cnct = getConnection();
            pstmt = cnct.prepareStatement(sql);
            pstmt.setString(1, uID);
            return pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(cnct, pstmt, rs);
        }
        return -1;
    }

    /*
     *  show all video
     */
    public void showAllVideo(Manager mgr) {
        Connection cnct = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT vID, vName, v_uID "+
                "FROM video ";

        try{
            cnct = getConnection();
            pstmt = cnct.prepareStatement(sql);
            rs = pstmt.executeQuery();
            System.out.println("---------------------------Video list--------------------------------------------------");
            System.out.printf("%20s%20s%20s", "VID", "VIDEO", "UPLOADER");
            System.out.println();
            while(rs.next()){
                String tmpvID = rs.getString(1);
                String tmpvName = rs.getString(2);
                String tmpv_uID = rs.getString(3);
                System.out.println();
                System.out.printf("%20s%20s%20s", tmpvID, tmpvName, tmpv_uID);
                System.out.println();
            }
            System.out.println("---------------------------------------------------------------------------------------");
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            close(cnct, pstmt, rs);
        }
    }

    /*
     *  block video
     */
    public int blockVideo(Manager mgr, Video tmpVid, int reason) {
        Connection cnct = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String preSql = "SELECT * FROM blockV WHERE EXISTS (SELECT bv_vID FROM blockV WHERE bv_vID = ?)";

        String sql = "INSERT INTO blockV(bv_reason, bv_mID, bv_vID) VALUES (?, ?, ?)";
        try {
            cnct = getConnection();
            pstmt = cnct.prepareStatement(preSql);
            pstmt.setString(1, tmpVid.getvID());
            rs = pstmt.executeQuery();
            if(rs.next()){
                System.out.println("Somebody already blocked this video!");
                return -1;
            }

            pstmt = cnct.prepareStatement(sql);
            pstmt.setInt(1, reason);
            pstmt.setString(2, mgr.getmID());
            pstmt.setString(3, tmpVid.getvID());
            return pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(cnct, pstmt, rs);
        }
        return -1;
    }

    /*
     *  unblock video
     */
    public int unblockVideo(Manager mgr, Video tmpVid) {
        Connection cnct = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String preSql = "SELECT * FROM blockV WHERE EXISTS (SELECT bv_vID FROM blockV WHERE bv_vID = ?)";

        String sql = "DELETE FROM blockV WHERE bv_vID = ?";
        try {
            cnct = getConnection();
            pstmt = cnct.prepareStatement(preSql);
            pstmt.setString(1, tmpVid.getvID());
            rs = pstmt.executeQuery();
            if(!rs.next()){
                System.out.println("The video is not blocked!");
                return -1;
            }

            pstmt = cnct.prepareStatement(sql);
            pstmt.setString(1, tmpVid.getvID());
            return pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(cnct, pstmt, rs);
        }
        return -1;
    }

    /*
     *  remove video
     */
    public int removeVid(Manager mgr, Video tmpVid) {
        Connection cnct = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "DELETE FROM video WHERE vID = ?";
        try {
            cnct = getConnection();
            pstmt = cnct.prepareStatement(sql);
            pstmt.setString(1, tmpVid.getvID());
            return pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(cnct, pstmt, rs);
        }
        return -1;
    }

    /*
     *  add ad
     */
    public int addAd(Manager mgr, String adName, String adID, int adCategory, int profitPerView) {
        Connection cnct = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "INSERT INTO ad(adCategory, profitPerView, adName, ad_mID, adID) " +
                "VALUES (?, ?, ?, ?, ?)";
        try {
            cnct = getConnection();
            pstmt = cnct.prepareStatement(sql);
            pstmt.setInt(1, adCategory);
            pstmt.setInt(2, profitPerView);
            pstmt.setString(3, adName);
            pstmt.setString(4, mgr.getmID());
            pstmt.setString(5, adID);
            return pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(cnct, pstmt, rs);
        }
        return -1;
    }


    /*
     * show all ad
     */
    public void showAllAd(Manager mgr) {
        Connection cnct = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT adName, adCategory, adView, profitPerView, ad_mID "+
                "FROM ad ";

        try{
            cnct = getConnection();
            pstmt = cnct.prepareStatement(sql);
            rs = pstmt.executeQuery();
            System.out.println("---------------------------Ad list----------------------------------------------------");
            while(rs.next()){
                String tmpadName = rs.getString(1);
                int tmpadCategory = rs.getInt(2);
                double tmpadView = rs.getDouble(3);
                int tmpprofitPerView = rs.getInt(4);
                String tmpad_mID = rs.getString(5);
                System.out.println("adName : "+tmpadName+"|"+"Category : "+tmpadCategory+"|"+"view : "+tmpadView +"|"+
                        "profit/view : "+tmpprofitPerView+"|"+"manager : "+tmpad_mID);
            }
            System.out.println("---------------------------------------------------------------------------------------");
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            close(cnct, pstmt, rs);
        }
    }

    /*
     * remove ad
     */
    public int removeAd(Manager mgr, String adName) { 
        Connection cnct = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "DELETE FROM ad WHERE adName = ?";
        try {
            cnct = getConnection();
            pstmt = cnct.prepareStatement(sql);
            pstmt.setString(1, adName);
            return pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(cnct, pstmt, rs);
        }
        return -1;
    }

    /*
     * show blocked video
     */
    public void showBlockedVideo(Manager mgr) {
        Connection cnct = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT vName, bv_mID, bv_reason "+
                "FROM blockV, video  WHERE vID = bv_vID";

        try{
            cnct = getConnection();
            pstmt = cnct.prepareStatement(sql);
            rs = pstmt.executeQuery();
            System.out.println("---------------------------Video list--------------------------------------------------");
            System.out.printf("%20s%20s%20s", "VID", "BLOCKER", "REASON");
            System.out.println();
            while(rs.next()){
                String tmpvName = rs.getString(1);
                String tmpbv_mID= rs.getString(2);
                int tmpreason = rs.getInt(3);

                System.out.println();
                System.out.printf("%20s%20s%20s", tmpvName, tmpbv_mID, tmpreason);
                System.out.println();
            }
            System.out.println("---------------------------------------------------------------------------------------");
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            close(cnct, pstmt, rs);
        }
    }

    /*
     * show blocked user
     */
    public void showBlockedUser(Manager mgr) {
        Connection cnct = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT bu_uID, bu_mID, bu_reason "+
                "FROM blockU";

        try{
            cnct = getConnection();
            pstmt = cnct.prepareStatement(sql);
            rs = pstmt.executeQuery();
            System.out.println("---------------------------User list---------------------------------------------------");
            System.out.printf("%20s%20s%20s", "UID", "BLOCKER", "REASON");
            System.out.println();
            while(rs.next()){
                String tmpbu_uID = rs.getString(1);
                String tmpbu_mID= rs.getString(2);
                int tmpreason = rs.getInt(3);

                System.out.println();
                System.out.printf("%20s%20s%20s", tmpbu_uID, tmpbu_mID, tmpreason);
                System.out.println();
            }
            System.out.println("---------------------------------------------------------------------------------------");
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            close(cnct, pstmt, rs);
        }
    }
}
