import java.util.*;
import java.io.*;

public class BpTree {
    private static Node root;
    private static int order;
    public void printTree(Node node, int level) {//테스트용 printTree
        if (node == null) {
            System.out.println("empty!");
            return;
        }
        System.out.printf("Level %d : ", level);
        for (int i = 0; i < level - 1; i++) {
            System.out.printf("         ");
        }
        for (int i = 0; i < node.kv.keyCnt; i++) {
            System.out.printf("k:%d ", node.kv.key[i]);
        }
        System.out.println();
        level++;
        for (int i = 0; i < node.childCnt; i++) {
            printTree(node.child[i], level);
        }
    }
    public void searchSingleKey(Node node, int key){//search single key key를 찾는 와중에 거치는 key값도 출력해야한다
        Node tmpNode = node;
        int tmpKeyPos;
        boolean flag = false;
        if (node == null) return;
        if (tmpNode.isLeaf == false) {
            for (tmpKeyPos = 0; tmpKeyPos < tmpNode.kv.keyCnt; tmpKeyPos++) {
                if (key < node.kv.key[tmpKeyPos]) {
                    flag = true;
                    break;
                }
            }
            if(!flag){
                System.out.print(node.kv.key[tmpKeyPos-1] + ",");

            }else{
                System.out.print(node.kv.key[tmpKeyPos] + ",");
                flag = false;
            }
            searchSingleKey(tmpNode.child[tmpKeyPos], key);

        }
        else{//if tmpNode == leaf
            int keyPos;
            if((keyPos = Arrays.binarySearch(node.kv.key, 0, node.kv.keyCnt, key)) < 0){
                System.out.println("NOT FOUND");
            }

            else {
                System.out.println(node.kv.val[keyPos]);
            }
        }
    }

    public void searchRangeKey(Node node, int from, int to){
        Node tmpNode = node;
        int tmpKeyPos;
        if (node == null) return;
        if (tmpNode.isLeaf == false) {
            for (tmpKeyPos = 0; tmpKeyPos < tmpNode.kv.keyCnt; tmpKeyPos++) {
                if (from < node.kv.key[tmpKeyPos]) {
                    break;
                }
            }
            tmpNode = tmpNode.child[tmpKeyPos];
        }
        else{//if tmpNode == leaf
            if(node.kv.key[0] > to) return;
            if (node.kv.key[node.kv.keyCnt - 1] < from) {
                tmpNode = node.next;
            }
            else {
                for (int i = 0; i < node.kv.keyCnt; i++) {
                    if (node.kv.key[i] >= from && node.kv.key[i] <= to) {
                        System.out.println(node.kv.key[i] + "," + node.kv.val[i]);
                    }
                }
            }
            if(node.next != null) {
                tmpNode = node.next;
            }
        }
        searchRangeKey(tmpNode, from, to);
    }
    public void printTreeNext(Node node){//이건 next가 잘 되어있는지 확인하기 위한 테스트용
        if(node == null) return;
        if(node.isLeaf){
            for(int i= 0 ;i<node.kv.keyCnt;i++){
                System.out.print("("+node.kv.key[i]+"|"+node.kv.val[i]+")");
            }
            System.out.print("|");
            printTreeNext(node.next);
        }else{
            Node tmpNode = node;
            while(tmpNode.child[0] != null){
                tmpNode = tmpNode.child[0];
            }
            printTreeNext(tmpNode);
        }
    }

    public BpTree(int order) {//make BpTree then set root node
        root = new Node(order);
        BpTree.order = order;
        root.isLeaf = true;
        //root.isRoot = true;
    }

    //형식상 insert 1단계
    public void insert(int key, int value) {
        root = insertNode(key, value, 0, root, root);
    }

    //재귀가능한 insert 2단계
    public Node insertNode(int key, int value, int posInPar, Node node, Node parent) {
        int keyPos;//insert할 key값의 pos 기록
        for (keyPos = 0; keyPos < node.kv.keyCnt; keyPos++) {
            if (key < node.kv.key[keyPos]) {
                break;
            }
        }
        //key 삽입 후 overflow면 splitNode실행(각 Node는 1의 여유 key공간이 존재)
        if (node.isLeaf) {
            //정렬
            for (int i = node.kv.keyCnt; i > keyPos; i--) {
                node.kv.key[i] = node.kv.key[i - 1];
                node.kv.val[i] = node.kv.val[i - 1];
                node.child[i + 1] = node.child[i];
            }
            //삽입
            node.kv.key[keyPos] = key;
            node.kv.val[keyPos] = value;
            node.kv.plusCnt();
            if (node.kv.keyCnt == order) {
                node = splitNode(posInPar, node, parent);
            }
        } else {//if not leaf
            node.child[keyPos] = insertNode(key, value, keyPos, node.child[keyPos], node);
            if (node.kv.keyCnt == order) {
                node = splitNode(posInPar, node, parent);
            }
        }
        return node;
    }

    //split Node insert 3단계
    public Node splitNode(int posInParent, Node node, Node parNode) {//nonLeaf split할 때 자꾸 오류생김
        int midPos = node.kv.keyCnt / 2;
        Node newRightNode = new Node(order);
        newRightNode.isLeaf = node.isLeaf;
        //newRightNode.next = node.next;
        //node.next = newRightNode;
        /*
        2 = midPos
        0 1 2 3
        2:: <= newParNode or 기존 parNode에 insert
        node = > 01: // 23: <= new right Node
         */
        //split new Node
        int tmpCnt = node.kv.keyCnt;
        int tmpMidPos = midPos;
        if (node.isLeaf == false) tmpMidPos = midPos + 1;
        for (int i = tmpMidPos; i < tmpCnt; i++) {
            newRightNode.kv.key[i - tmpMidPos] = node.kv.key[i];
            newRightNode.kv.val[i - tmpMidPos] = node.kv.val[i];
            newRightNode.kv.plusCnt();
            node.kv.minusCnt();
        }
        if (node.isLeaf == false) node.kv.minusCnt();

        //non-leaf node를 split할때 next는 필요없음
        if (!node.isLeaf) {
            tmpCnt = node.childCnt;
            for (int i = tmpMidPos; i < tmpCnt; i++) {
                newRightNode.child[i - tmpMidPos] = node.child[i];
                newRightNode.childCnt++;
                node.childCnt--;
            }
        } else {//node가 leaf node이면 linkedlist로 연결해야함
            newRightNode.next = node.next;
            node.next = newRightNode;
        }
        //parentNode setting 분리할때 위로 올릴 노드 설정
        if (node == root) {
            Node newParNode = new Node(order);
            newParNode.kv.key[0] = node.kv.key[midPos];
            newParNode.kv.val[0] = node.kv.key[midPos];
            newParNode.child[0] = node;
            newParNode.child[1] = newRightNode;//0915 12:26 추가
            newParNode.kv.plusCnt();
            newParNode.childCnt = 2;
            /*
            newRightNode.next = node.next;
            node.next = newRightNode;필요 없을 것 같아서 삭제*/
            return newParNode;
        } else {
            for (int i = parNode.kv.keyCnt; i > posInParent; i--) {
                parNode.kv.key[i] = parNode.kv.key[i - 1];
                parNode.kv.val[i] = parNode.kv.val[i - 1];
                parNode.child[i + 1] = parNode.child[i];
            }
            parNode.kv.key[posInParent] = node.kv.key[midPos];
            parNode.kv.val[posInParent] = node.kv.val[midPos];
            parNode.kv.plusCnt();
            //node.kv.minusCnt(); 0914 12:19수정 이거 안해도 될 것 같은데?
            parNode.child[posInParent + 1] = newRightNode;

            parNode.childCnt++;
            /*
            newRightNode.next = node.next;
            node.next = newRightNode;필요없을 것 같아서 삭제*/
            return node;
        }
        //return node;
    }

    public void delete(Node node, int key) {
        Node tmpNode = node;
        int tmpKeyPos;
        if (tmpNode.isLeaf == false) {
            for (tmpKeyPos = 0; tmpKeyPos < tmpNode.kv.keyCnt; tmpKeyPos++) {
                if (key < node.kv.key[tmpKeyPos]) {
                    break;
                }
            }
            delete(tmpNode.child[tmpKeyPos], key);//나중에 수정할지도//////////////////
            if(tmpNode.child[tmpKeyPos].kv.keyCnt < root.minKeySize) {//주로 오류 나는 장소**************
                if (tmpKeyPos == 0 && tmpNode.child[tmpKeyPos + 1].kv.keyCnt > node.minKeySize) {//
                    borrowFromRight(tmpNode, key, tmpKeyPos);
                } else if (tmpKeyPos == 0 && tmpNode.child[tmpKeyPos + 1].kv.keyCnt == node.minKeySize) {
                    mergeNode(tmpNode, key, tmpKeyPos, true);
                } else if (tmpKeyPos != 0 && tmpNode.child[tmpKeyPos - 1].kv.keyCnt > node.minKeySize) {
                    borrowFromLeft(tmpNode, key, tmpKeyPos);
                } else if (tmpKeyPos != 0 && tmpNode.child[tmpKeyPos - 1].kv.keyCnt == node.minKeySize) {
                    mergeNode(tmpNode, key, tmpKeyPos, false);
                }
            }
        } else {//ifLeaf
            //일단 삭제한다
            int deletePos;
            deletePos = Arrays.binarySearch(node.kv.key, 0, node.kv.keyCnt, key);
           // System.out.println(key);//이거는 수정해야된다
            for (int i = deletePos; i < node.kv.keyCnt; i++) {
                node.kv.key[i] = node.kv.key[i + 1];
                node.kv.val[i] = node.kv.val[i + 1];
            }
            node.kv.minusCnt();
            return;

        }
    }

    public void mergeNode(Node parNode, int key, int posInPar, boolean left) {
        Node mainNode;//왼쪽 node delete할 node가 어느쪽이든 왼쪽으로 붙일 것
        Node mergedNode;//오른쪽 node

        if (left) {//delete한 node는 left이며 right랑 merge
            mainNode = parNode.child[posInPar];
            mergedNode = parNode.child[posInPar + 1];

            if(mainNode.isLeaf == false){
                //non-leaf node의 merge일 경우 par에서 key를 가져온다
                mainNode.kv.key[mainNode.kv.keyCnt] = parNode.kv.key[posInPar];
                //mainNode.kv.val[mainNode.kv.keyCnt] = parNode.kv.val[posInPar];
                //mainNode.kv.plusCnt();
                mainNode.kv.keyCnt++;
            }
        } else {//delete한 node는 right이며 left랑 merge
            mainNode = parNode.child[posInPar - 1];
            mergedNode = parNode.child[posInPar];

            if(mergedNode.isLeaf == false){
                //non-leaf node의 merge일 경우 par에서 key를 가져온다
                mainNode.kv.key[mainNode.kv.keyCnt] = parNode.kv.key[posInPar-1];
                mainNode.kv.val[mainNode.kv.keyCnt] = parNode.kv.val[posInPar-1];
                mainNode.kv.plusCnt();
            }
        }
        //merge : non-leaf든 아니든 왼쪽 노드 main node에 오른쪽 노드 merged node key를 넣는다
        int tmpIdx = mainNode.kv.keyCnt;
        if(mainNode.isLeaf) {
            for (int i = tmpIdx; i < mainNode.kv.keyCnt + mergedNode.kv.keyCnt; i++) {
                mainNode.kv.key[i] = mergedNode.kv.key[i - tmpIdx];
                mainNode.kv.val[i] = mergedNode.kv.val[i - tmpIdx];
            }
            mainNode.kv.keyCnt += mergedNode.kv.keyCnt;
            mainNode.kv.valCnt += mergedNode.kv.valCnt;
        }else{
            for (int i = tmpIdx; i < mainNode.kv.keyCnt + mergedNode.kv.keyCnt; i++) {
                mainNode.kv.key[i] = mergedNode.kv.key[i - tmpIdx];
            }
            //mergedNode의 Cnt 정리
            mainNode.kv.keyCnt += mergedNode.kv.keyCnt;
        }





        //leaf node의 경우 next도 알맞게 옮긴다
        if(mainNode.isLeaf){
            //leaf node의 경우
            mainNode.next = mergedNode.next;
        } else{        //Non-Leaf Node의 경우 child도 수정해야한다.
            //merge child
            for(int i = mainNode.childCnt; i<mainNode.childCnt + mergedNode.childCnt;i++){
                mainNode.child[i] = mergedNode.child[i-mainNode.childCnt];
            }
            mainNode.childCnt += mergedNode.childCnt;
        }

        //parNode 수정 + 정리
        //parNode가 root node이고 key가 하나밖에 없을 때 merge할 경우 mainNode(left)가 root가 된다.
        if(parNode == BpTree.root && parNode.kv.keyCnt == 1){
            BpTree.root = mainNode;
            return;
        }
        //그외 parNode 정리
        if(left){
            //key를 당기고 == parNode의 idx를 지운다
            for(int i = posInPar; i<parNode.kv.keyCnt;i++){
                parNode.kv.key[i] =parNode.kv.key[i+1];
                //parNode.kv.val[i] = parNode.kv.val[i+1];
            }
            //child도 당긴다
            for(int i = posInPar+1; i < parNode.childCnt;i++){
                parNode.child[i] = parNode.child[i+1];
            }
        }else{
            //key를 당기고
            for(int i = posInPar-1; i<parNode.kv.keyCnt;i++){
                parNode.kv.key[i] = parNode.kv.key[i+1];
                //parNode.kv.val[i] = parNode.kv.val[i+1];
            }
            //child도 당긴다
            for(int i = posInPar; i< parNode.childCnt;i++){
                parNode.child[i] = parNode.child[i+1];
            }
        }
       // parNode.kv.minusCnt();
        parNode.kv.keyCnt--;
        parNode.childCnt--;
    }

    public void borrowFromRight(Node parNode, int key, int posInPar) {
        Node deleteNode = parNode.child[posInPar];
        Node borrowedNode = parNode.child[posInPar + 1];

        //borrow해서 추가하기
        int addPos = deleteNode.kv.keyCnt;
        if (deleteNode.isLeaf == true) {
            deleteNode.kv.key[addPos] = borrowedNode.kv.key[0];
            deleteNode.kv.val[addPos] = borrowedNode.kv.key[0];

            deleteNode.kv.plusCnt();
            for (int i = 0; i <borrowedNode.kv.keyCnt ; i++) {//당기기
                borrowedNode.kv.key[i] = borrowedNode.kv.key[i+1];
                borrowedNode.kv.val[i] = borrowedNode.kv.val[i+1];
            }
            borrowedNode.kv.minusCnt();
            parNode.kv.key[posInPar] = borrowedNode.kv.key[0];
        } else {//non-leaf node에서의BFR
            //value는 딱히 신경X
            //key는 parNode에서, child는 borrowed node에서 가져온다
            deleteNode.kv.key[addPos] = parNode.kv.key[posInPar];
            //deleteNode.kv.val[addPos] = parNode.kv.val[posInPar];
            parNode.kv.key[posInPar] = borrowedNode.kv.key[0];
           // parNode.kv.val[posInPar] = borrowedNode.kv.val[0];
            //deleteNode.kv.plusCnt();
            deleteNode.kv.keyCnt++;
            //borrowed node에서의 삭제
            for (int i = 0 ; i <borrowedNode.kv.keyCnt; i++) {
                borrowedNode.kv.key[i] = borrowedNode.kv.key[i+1];
               // borrowedNode.kv.val[i] = borrowedNode.kv.val[i + 1];
            }
           // borrowedNode.kv.minusCnt();
            borrowedNode.kv.keyCnt--;
            deleteNode.child[deleteNode.childCnt] = borrowedNode.child[0];
            deleteNode.childCnt++;

            for(int i = 0; i<borrowedNode.childCnt;i++){
                borrowedNode.child[i] = borrowedNode.child[i+1];
            }
            borrowedNode.childCnt--;
        }
    }

    public void borrowFromLeft(Node parNode, int key, int posInPar) {
        Node deleteNode = parNode.child[posInPar];
        Node borrowedNode = parNode.child[posInPar - 1];

        //borrow해서 추가하기
        if (deleteNode.isLeaf == true) {
            for(int i = deleteNode.kv.keyCnt; i>0;i--){//밀기
                deleteNode.kv.key[i] = deleteNode.kv.key[i-1];
                deleteNode.kv.val[i] = deleteNode.kv.val[i-1];
            }
            deleteNode.kv.key[0] = borrowedNode.kv.key[borrowedNode.kv.keyCnt-1];//빌려오기
            deleteNode.kv.val[0] = borrowedNode.kv.val[borrowedNode.kv.valCnt-1];
            deleteNode.kv.plusCnt();
            borrowedNode.kv.minusCnt();
            parNode.kv.key[posInPar-1] = deleteNode.kv.key[0];
        } else {//non-leaf node에서의BFL
            //value는 딱히 신경X
            //key는 parNode에서, child는 borrowed node에서 가져온다
            for(int i = deleteNode.kv.keyCnt; i>0; i--){
                deleteNode.kv.key[i] = deleteNode.kv.key[i-1];
                //deleteNode.kv.val[i] = deleteNode.kv.val[i-1];
            }
            deleteNode.kv.key[0] = parNode.kv.key[posInPar - 1];
            //deleteNode.kv.val[0] = parNode.kv.val[posInPar - 1];
            //parNode의 idx는 빌려온 노드의 제일 큰 키로 설정
            parNode.kv.key[posInPar-1] = borrowedNode.kv.key[borrowedNode.kv.keyCnt-1];
            //parNode.kv.val[posInPar-1] = borrowedNode.kv.val[borrowedNode.kv.valCnt-1];
            //deleteNode.kv.plusCnt();
            deleteNode.kv.keyCnt++;
            for(int i = deleteNode.childCnt ;i>0;i--){
                deleteNode.child[i] = deleteNode.child[i-1];
            }
            //borrowednode에서 child 가져오기
            deleteNode.child[0] = borrowedNode.child[borrowedNode.childCnt-1];
            deleteNode.childCnt++;
            //borrowedNode.child[borrowedNode.childCnt-1] = borrowedNode.child[borrowedNode.childCnt];
            borrowedNode.childCnt--;
            //borrowedNode는 key하나를 올려보냈기 때문에 키 하나를 줄여야함
            //borrowedNode.kv.key[borrowedNode.kv.keyCnt-1] = 0;
            //borrowedNode.kv.val[borrowedNode.kv.valCnt-1] = 0;
            //borrowedNode.kv.minusCnt();
            borrowedNode.kv.keyCnt--;
        }
    }
    public static void writeDAT3(String path, String inputStr, boolean append){ //실제로 file에 쓰는 method
        BufferedWriter bw = null;
        try {
            if(append){
                bw = new BufferedWriter(new FileWriter(path, true));
            }else {
                bw = new BufferedWriter(new FileWriter(path));
            }
            bw.write(inputStr);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void writeDAT2(String path, Node node) { //bptree 정보를 dat파일에 string 형태로 저장해야한다
        if(node != null){
            if(!node.isLeaf){
                Node tmpNode = node;
                for(int i =0 ;i<tmpNode.childCnt;i++) {
                    writeDAT2(path, tmpNode.child[i]);
                }
                writeDAT3(path, tmpNode.toString()+ "\n", true);
            }else{
                writeDAT3(path, node.toString()+"\n", true);
            }
        }
    }
    public static void writeDAT1(String path, Node node){
        String inputStr = "order "+ order;
        BpTree.writeDAT3(path,inputStr+"\n", false);
        BpTree.writeDAT2(path, node);
    }
    public static BpTree readDAT(String path){//bptree 정보를 dat파일에서 가져와서 tree를 만들어야 한다
        BpTree bpTree  = null;
        BufferedReader in = null;

        Node[] tmpChild = null;//non-leaf에 child 붙이기용도
        Node forNext = null;
        Node[][] nLeafChild = null;
        //Node[] tmpNLeafChild = null;
        int[] tmpNodeCnt = null;
        int tmpKVCnt = 0;
        int height = -1;
        int[] keys = new int[0];
        int[] values = new int[0];
        int order = 0;

        try{//트리의 밑에서부터 루트까지 읽어나가는 구조
            in = new BufferedReader(new FileReader(path));
            String line;
            String[] sKeys;
            String[] sNodes;
            Node tmpRoot = null;
            while((line = in.readLine())!= null) {
                sNodes = line.split(" ");
                if(sNodes[0].equals("order")){
                    order = Integer.parseInt(sNodes[1]);
                    bpTree = new BpTree(order);//tree 생성
                    keys = new int[order];
                    values = new int[order];
                    tmpChild = new Node[order+1];
                    if(order < 8) {nLeafChild = new Node[order*order*order + 1][order + 1];
                        tmpNodeCnt = new int[order*order*order+1];}
                    else {nLeafChild = new Node[order + 1][order + 1];
                    tmpNodeCnt = new int[order+1];}
                    //tmpNLeafChild = new Node[order+1];
                }
                else if(sNodes[0].equals("Leaf")){
                    //key
                    if(sNodes.length <3){//아직 아무것도 만들어지지 않은 상태
                        return bpTree;
                    }
                    sKeys = sNodes[1].split(",");
                    tmpKVCnt = sKeys.length;
                    for(int i = 0;i<tmpKVCnt;i++){
                        keys[i] = Integer.parseInt(sKeys[i]);
                    }
                    //value
                    sKeys = sNodes[2].split(",");
                    tmpKVCnt = sKeys.length;
                    for(int i = 0;i<tmpKVCnt;i++){
                        values[i] = Integer.parseInt(sKeys[i]);
                    }
                    //leafNode 생성 후 정보 전달
                    Node leafNode = new Node(order);
                    leafNode.isLeaf = true;
                    for(int i =0 ; i<tmpKVCnt;i++){
                        leafNode.kv.key[i] = keys[i];
                        keys[i] = 0;
                    }
                    leafNode.kv.keyCnt = tmpKVCnt;
                    for(int i = 0;i<tmpKVCnt;i++){
                        leafNode.kv.val[i] = values[i];
                        values[i] = 0;
                    }
                    leafNode.kv.valCnt = tmpKVCnt;
                    if(forNext != null){//leafnode next 지정
                        forNext.next = leafNode;
                        forNext =leafNode;
                    }else{
                        forNext = leafNode;
                    }
                    for(int i =0; i<tmpChild.length;i++){
                        if (tmpChild[i] == null) {
                            tmpChild[i] = leafNode;
                            break;
                        }
                    }
                    height = -1;
                }//leaf end
                else if(sNodes[0].equals("NLeaf")){
                    sKeys = sNodes[1].split(",");
                    tmpKVCnt = sKeys.length;

                    for(int i= 0;i<tmpKVCnt;i++){
                        keys[i] = Integer.parseInt(sKeys[i]);
                    }
                    //non-leafnode 생성 후 정보 전달
                    Node nLeafNode = new Node(order);
                    for(int i =0 ;i<tmpKVCnt;i++){
                        nLeafNode.kv.key[i] = keys[i];
                        keys[i] = 0;
                    }
                    nLeafNode.kv.keyCnt = tmpKVCnt;
                    if(++height == 0) {
                        for (int i = 0; i < tmpChild.length; i++) {
                            if (tmpChild[i] != null) {
                                nLeafNode.child[i] = tmpChild[i];
                                nLeafNode.childCnt++;
                                tmpChild[i] = null;
                            }
                        }
                    }
                    //non-leaf 다음에 non-leaf == 부모 non-leaf
                    for (int i = 0; i < height+1; i++) {
                        for (int j = 0; j < order+1; j++) {
                            if (height == 0 && nLeafChild[i][j] == null){//leaf 다음에 오는 non-leaf들
                                nLeafChild[0][j] = nLeafNode;
                                tmpNodeCnt[0]++;
                                break;
                            }
                            else if(height >0 && tmpKVCnt+1 == tmpNodeCnt[height-1] && j<tmpNodeCnt[height-1]) {//부모노드라면
                                nLeafNode.child[j] = nLeafChild[height - 1][j];
                                nLeafChild[height - 1][j] = null;
                                nLeafNode.childCnt++;
                            }
                            if(i<height) continue;
                            else if(height >0&& nLeafChild[i][j]==null){
                                nLeafChild[i][j] = nLeafNode;
                                tmpNodeCnt[i]++;
                                break;
                            }
                        }
                        if(height == 0) break;
                        tmpNodeCnt[height - 1] = 0;
                    }
                    tmpRoot = nLeafNode;
                }
            }
            BpTree.root = tmpRoot;
        } catch(IOException e){
            e.printStackTrace();
        }
        return bpTree;
    }
    public void readCSVFile(String fileName, BpTree tree, boolean insTdelF){
        try{
            BufferedReader read = new BufferedReader((new FileReader(fileName)));
            String line = "";
            if(insTdelF) {
                while ((line = read.readLine()) != null) {
                    String[] tmp = line.split(",");
                    int key = Integer.parseInt(tmp[0]);
                    int val = Integer.parseInt(tmp[1]);
                    tree.insert(key, val);
                }
                read.close();
            }
            else{
                while ((line = read.readLine()) != null) {
                    int key = Integer.parseInt(line);
                    tree.delete(tree.root, key);
                }
                read.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {//진짜 main 만드는중
        BpTree tree = null;
        String input = args[0].substring(1);
        switch(input){
            case "c":
                int order = Integer.parseInt(args[2]);
                String path = args[1];
                tree = new BpTree(order);
                writeDAT1(path, root);
                return;
            case "i":
                tree = readDAT(args[1]);
                if(tree == null){
                    System.out.println("tree 초기화 필요");
                }
                tree.readCSVFile(args[2],tree, true);
                tree.writeDAT1(args[1],root);
                return;
            case "d":
                tree = readDAT(args[1]);
              //  tree.printTree(tree.root, 1);
                if(tree == null){
                    System.out.println("tree 초기화 필요");
                }
                tree.readCSVFile(args[2], tree, false);
                tree.writeDAT1(args[1], root);
                return;
            case "s":
                tree = readDAT(args[1]);
                if(tree == null){
                    System.out.println("tree 초기화 필요");
                }
                tree.searchSingleKey(tree.root, Integer.parseInt(args[2]));
                return;
            case "r":
                tree = readDAT(args[1]);
                if(tree == null){
                    System.out.println("tree 초기화 필요");
                }
                tree.searchRangeKey(tree.root, Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                return;
        }
    }
    /*
    public static void main(String[] args) {
        //추후 입력 출력 수정할 것
        Scanner scan = new Scanner(System.in);
        System.out.println("order :");
        order = scan.nextInt();
        BpTree bptree = new BpTree(order);

        while (true) {
            System.out.println("Test console");
            System.out.println("1.search");
            System.out.println("2.insert");
            System.out.println("3.delete");
            System.out.println("4.printTree");
            System.out.println("5.rangeSearch");
            System.out.println("0.close");
            int tmpInput = scan.nextInt();
            switch (tmpInput) {
                case 0:
                    return;
                case 1:
                    int key;
                    System.out.print("search할 key를 입력: ");
                    key = scan.nextInt();
                    bptree.searchSingleKey(root, key);
                    break;
                case 2:
                    int tmpKey, tmpVal;
                    System.out.println("key and value : ");
                    for(int i = 0; i<50;i++){

                        tmpKey = Integer.parseInt(scan.next());
                    tmpVal = Integer.parseInt(scan.next());
                        bptree.insert(tmpKey, tmpVal);
                    }
                    bptree.printTree(root, 1);//만들면서 테스트용
                    bptree.printTreeNext(root);
                    System.out.println();
                    break;
                case 3:
                    int deleteKey;
                    System.out.println("delete key : ");
                    for(int i =0 ; i< 30;i++) {
                        deleteKey = Integer.parseInt(scan.next());
                        bptree.delete(bptree.root, deleteKey);
                        bptree.printTree(root, 1);//테스트용
                        bptree.printTreeNext(root);
                        System.out.println();
                    }
                    break;
                case 4:
                    //bptree.printTree(root);
                    bptree.printTree(root, 1);
                    return;
                case 5:
                    int from, to;
                    System.out.println("from 과 to를 순서대로 입력:");
                    from = scan.nextInt();
                    to = scan.nextInt();
                    bptree.searchRangeKey(root, from, to);
                    break;
                default:
                    System.out.println("시험용");
            }
        }
    }
     */
}

