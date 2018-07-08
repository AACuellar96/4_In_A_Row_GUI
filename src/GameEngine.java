import java.util.Scanner;
public class GameEngine {
    // The representation of the game board.
    private static Board defState;
    // Scanner to take input
    private static Scanner sc;
    // time limit imposed by user for each move.
    private static int timeLimit;

    /**
     * Main Method that starts everything. Creates board and sets up the game.
     */
    public static void run(){
        char[][] initial = new char[8][8];
        for(int row=0;row<8;row++) {
            for (int col = 0; col < 8; col++) {
                initial[row][col]='-';
            }
        }
        System.out.println("Please enter who will be going first.");
        sc= new Scanner(System.in);
        char first=sc.nextLine().toLowerCase().charAt(0);
        while(first!='o'&& first!='x'){
            System.out.println("Invalid player, please enter again.");
            first=sc.nextLine().toLowerCase().charAt(0);
        }
        defState = new Board(initial,64,"",first);
        System.out.println("Please enter the desired time limit.");
        timeLimit=sc.nextInt();
        if(timeLimit>30)
            timeLimit=30;
        timeLimit*=1000;
        sc.nextLine();
        if(first=='o'){
            while(defState.checkTerminal()==0 && defState.getPossMoves()>0) {
                printBoard();
                boolean valid=playerTurn();
                while(!valid){
                    System.out.println("Invalid move, please input another move.");
                    valid=playerTurn();
                }
                if(defState.checkTerminal()!=0 || defState.getPossMoves()==0)
                    break;
                printBoard();
                computerTurn();
            }
        }
        else{
            while(defState.checkTerminal()==0 && defState.getPossMoves()>0) {
                printBoard();
                computerTurn();
                if(defState.checkTerminal()!=0 || defState.getPossMoves()==0)
                    break;
                printBoard();
                boolean valid=playerTurn();
                while(!valid){
                    System.out.println("Invalid move, please input another move.");
                    valid=playerTurn();
                }
            }
        }
        printBoard();
        if(defState.checkTerminal()==1)
            System.out.println("I win!");
        else if(defState.checkTerminal()==-1)
            System.out.println("You win!");
        else
            System.out.println("Tie.");
    }

    /**
     * Method to record the player's move.
     * @return True if player makes a valid move, false otherwise.
     */
    private static boolean playerTurn(){
        System.out.println("Please enter your move.");
        System.out.println("");
        String move=sc.nextLine().toLowerCase();
        if(move.charAt(0)>104 || move.charAt(0)<97 || move.charAt(1)<49 || move.charAt(1)>56)
            return false;
        int row=move.charAt(0)-97;
        int col=move.charAt(1)-49;
        if(defState.getBoard()[row][col]!='-')
            return false;
        defState.makeMove(row,col,'O');
        return true;
    }

    /**
     * Method to make the computer's move.
     */
    private static void computerTurn(){
        String move=ABSearch.search(defState,timeLimit);
        int row=move.charAt(0)-48;
        int col=move.charAt(1)-48;
        defState.makeMove(row,col,'X');
        row+=97;
        System.out.println("My move is: "+ (char) (row)+""+(col+1));
        System.out.println("");
    }

    /**
     * Method to print out the board.
     */
    private static void printBoard(){
        System.out.println("  1 2 3 4 5 6 7 8");
        for(int row=0;row<8;row++){
            System.out.print((char) (row+65)+" ");
            for(int col=0;col<8;col++){
                System.out.print(defState.getBoard()[row][col]+" ");
            }
            System.out.println("");
        }
    }

}
