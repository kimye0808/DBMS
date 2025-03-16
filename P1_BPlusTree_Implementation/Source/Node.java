public class Node {

    public Node next;
    public boolean isLeaf;
    final public int minKeySize;
    final public int maxKeySize;
    final public int minChildSize;
    final public int maxChildSize;
    public Node[] child;
    public int childCnt;
    public KeyVal kv;

    public Node(int order){
        //key, child size setting
        if(order%2 == 0) {
            minKeySize = order / 2 - 1;
        }else {
            minKeySize = order / 2;
        }
        maxKeySize = order - 1;
        maxChildSize = order;
        minChildSize = minKeySize+1;

        //array
        kv = new KeyVal(order);
        child = new Node[maxChildSize+1];//overflow시 편하게 하기 위해서 크기가 하나 더 큰 배열로 만든다
        /*
        for(int i = 0;i<maxChildSize;i++){
            child[i] = new Node(order);
        }*/
        childCnt = 0;
    }

    public String toString(){
        String inputStr = "";
        if(!this.isLeaf){
            inputStr += "NLeaf ";
            for(int i= 0 ;i< kv.keyCnt; i++){
                inputStr += kv.key[i]+ ",";
            }
            return inputStr;
        }else{
            inputStr += "Leaf ";
            for(int i = 0;i<kv.keyCnt; i++){
                inputStr += kv.key[i] +",";
            }
            inputStr+=" ";
            for(int i = 0;i<kv.valCnt;i++){
                inputStr += kv.val[i] + ",";
            }
            return inputStr;
        }
    }
}
