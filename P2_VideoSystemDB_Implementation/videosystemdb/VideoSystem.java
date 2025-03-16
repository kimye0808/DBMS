import java.util.Scanner;

public class VideoSystem {

    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);
        Menu menu = new Menu();
        while(true) {
            System.out.println("0. EXIT");
            System.out.println("1. User Menu");
            System.out.println("2. Manager Menu");
            System.out.print("input : ");
            int startNum = scan.nextInt();
            System.out.println();
            switch(startNum){
                case 0:
                    return;//system ends
                case 1:
                    menu.signUpOrIn(startNum);//usr login
                    break;
                case 2:
                    menu.signUpOrIn(startNum);//mgr login
                    break;
                default:
                    System.out.println("You got a wrong choice!");
                    break;
            }
        }
    }
}
