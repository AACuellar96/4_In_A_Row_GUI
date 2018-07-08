public class Board {
    // 2d array representation of board.
    private char[][] board;
    // Number of possible moves left on board.
    private int possMoves;
    // utility value of the board.
    private int utility;
    // last move made in this board.
    private String lastMove;
    // Boolean representing if utility value has been generated or not.
    private boolean utilityGen;
    // Whose turn it is.
    private char turn;

    /**
     * Constructor.
     * @param board The board the game will be played on.
     * @param moves Number of moves that can be made.
     * @param last Last move made on this board.
     * @param turn Whose turn it is.
     */
    public Board(char[][] board,int moves,String last,char turn){
        this.board=board;
        this.possMoves=moves;
        utility=0;
        lastMove=last;
        utilityGen=false;
        this.turn=turn;
    }

    /**
     * If utility has not been changed, generates it and returns it. Otherwise just returns it.
     * @return utility
     */
    public int getUtility() {
        if(utilityGen)
            return utility;
        else
            return generateUtility();
    }

    /**
     * Gets amount of possible moves left.
     * @return Possible Moves
     */
    public int getPossMoves() {
        return possMoves;
    }

    /**
     * Changes whose turn it is.
     */
    private void changeTurn(){
        if(turn=='o')
            turn='x';
        else
            turn='o';
    }

    /**
     * Gets the 2d array representing the board.
     * @return board
     */
    public char[][] getBoard() {
        return board;
    }

    /**
     * Gets last move made in this board.
     * @return Last move made
     */
    public String getLastMove() {
        return lastMove;
    }

    /**
     * Sets utility value to the parameter. Changes utilityGen to true.
     * @param utility The new value for utility
     */
    public void setUtility(int utility) {
        this.utility = utility;
        utilityGen=true;
    }

    /**
     * Makes the recorded move, decrements possible moves, and changes whose turn it is.
     * @param row Row of the move
     * @param col Col of the move.
     * @param player player making the move.
     */
    public void makeMove(int row,int col,char player){
        board[row][col]=player;
        lastMove=""+row+""+col;
        possMoves--;
        changeTurn();
    }

    /**
     * Checks if either player has won.
     * @return 0 if no winner. -1 if human wins, 1 if computer wins.
     */
    public int checkTerminal(){
        for(int row=0;row<8;row++) {
            for (int col = 0; col < 5; col++) {
                if(board[row][col]!='-' && board[row][col]==board[row][col+1] && board[row][col+1]==board[row][col+2]
                        && board[row][col+2]==board[row][col+3]){
                    if(board[row][col]=='X'){
                        return 1;
                    }
                    else{
                        return -1;
                    }
                }
            }
        }
        for(int row=0;row<5;row++) {
            for (int col = 0; col < 8; col++) {
                if(board[row][col]!='-' && board[row][col]==board[row+1][col] && board[row+1][col]==board[row+2][col]
                        && board[row+2][col]==board[row+3][col]){
                    if(board[row][col]=='X'){
                        return 1;
                    }
                    else{
                        return -1;
                    }
                }
            }
        }
        return 0;
    }

    /**
     * Utility function. Sets utility to the recorded value. Makes more weight for a possible player victory
     * or computer victory depending on whose turn it is.
     * @return Utility value.
     */
    public int generateUtility(){
        int possXAW=0;
        int possXW=0;
        int possOAW=0;
        int possOW=0;
        for(int row=0;row<8;row++) {
            for(int col=0;col<5;col++){
                int blank=0;
                int X=0;
                int O=0;
                for (int colC = col; (colC<(col+4)); colC++) {
                    if(board[row][colC]=='-')
                        blank++;
                    else if(board[row][colC]=='X')
                        X++;
                    else
                        O++;
                }
                if(blank==1 && X==3)
                    possXAW++;
                else if(blank==2 && X==2)
                    possXW++;
                else if(blank==1 && O==3)
                    possOAW++;
                else if(blank==2 && O==2)
                    possOW++;
                else if(X==4)
                    return 99;
                else if(O==4)
                    return -99;
            }
        }
        for(int col=0;col<8;col++){
            for(int row=0;row<5;row++) {
                int blank=0;
                int X=0;
                int O=0;
                for (int rowC = row; (rowC<(row+4)); rowC++) {
                    if(board[rowC][col]=='-')
                        blank++;
                    else if(board[rowC][col]=='X')
                        X++;
                    else
                        O++;
                }
                if(blank==1 && X==3)
                    possXAW++;
                else if(blank==2 && X==2)
                    possXW++;
                else if(blank==1 && O==3)
                    possOAW++;
                else if(blank==2 && O==2)
                    possOW++;
                else if(X==4)
                    return 99;
                else if(O==4)
                    return -99;
            }
        }

        if(turn=='o'){
            if(possOAW>0) {
                utility=-98;
                utilityGen=true;
                return -98;
            }
            else if(possXAW>0) {
                utility=98;
                utilityGen=true;
                return 98;
            }
            else{
                utility=possXW-possOW;
                utilityGen=true;
                return utility;
            }
        }
        else{
            if(possXAW>0) {
                utility=98;
                utilityGen=true;
                return 98;
            }
            else if(possOAW>0){
                utility=-98;
                utilityGen=true;
                return -98;
            }
            else{
                utility=possXW-possOW;
                utilityGen=true;
                return utility;
            }
        }
    }

    /**
     * Method that generates all possible successors of this board.
     * @param player The player who would be making a move from this board.
     * @return List of all possible moves that can be done by the player.
     */
    public Board[] successors(char player){
        Board[] possibleResponses=new Board[possMoves];
        char nTurn='x';
        if(player=='x')
            nTurn='o';

        int index=0;
        for(int row=0;row<8;row++){
            for(int col=0;col<8;col++){
                if(board[row][col]=='-'){
                    char[][] copy = new char[8][8];
                    for(int r=0;r<8;r++)
                        for(int c=0;c<8;c++)
                            copy[r][c]=board[r][c];

                    copy[row][col]=player;
                    String last=""+row+""+col;
                    possibleResponses[index]=new Board(copy,(possMoves-1),last,nTurn);
                    index++;
                }
            }
        }
        return possibleResponses;
    }

}
