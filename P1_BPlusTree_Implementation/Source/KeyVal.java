public class KeyVal {
    public int[] key;
    public int[] val;
    public int keyCnt;
    public int valCnt;

    public KeyVal(int order){
        this.key = new int[order];
        this.val = new int[order];
        keyCnt = 0;
        valCnt = 0;
    }
    public void plusCnt(){
        keyCnt++;
        valCnt++;
    }
    public void minusCnt(){
        keyCnt--;
        valCnt--;
    }
}