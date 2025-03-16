package entity;

public class Usr {
    private String uID;
    private String uPW;
    private String uSSN;
    private int grade;//1이면 광고 완전 제거 0이면 광고 시청
    private int interests;
    /*interests는 관심분야. 번호로 지정
0 - comedy 1 - toy 2 - gaming 3 - educational 4 - music 5 - eating 6 - beauty 7 - sports */


    public Usr(String uID, String uPW, String uSSN, int grade, int interests){
        this.uID = uID;
        this.uPW = uPW;
        this.uSSN = uSSN;
        this.grade = grade;
        this.interests = interests;
    }

    public String toString(){
        String newLine = System.getProperty("line.separator");
        return "your ID: " + uID + newLine +
                "your PW: " + uPW + newLine +
                "Your SSN: " + uSSN + newLine +
                "Your Grade: " + grade + newLine +
                "Your interests: " + interests + newLine;
    }

    public String getuID(){
        return uID;
    }

    public String getuPW(){
        return uPW;
    }

    public int getGrade(){
        return grade;
    }
    public String getuSSN(){
        return uSSN;
    }
    public int getInterests(){
        return interests;
    }

    public void setGrade(){
        grade = 1;
    }
}
