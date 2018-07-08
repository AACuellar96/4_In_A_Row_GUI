public class ABSearch {
    /**
     * Starts minmax algorithm. Searches for best answer.
     * @param board Board to use and search an answer for.
     * @param timeLimit Time limit imposed by user.
     * @return Optimal move to be made
     */
    public static String search(Board board,long timeLimit){
        long currTime = System.currentTimeMillis();
        int depth=0;
        int depthLimit=-1;
        int val=0;
        Board[] resp=board.successors('X');
        while((System.currentTimeMillis()-currTime)<timeLimit){
            depthLimit++;
            val=searchMaxV(board,resp,-1000,1000,currTime,timeLimit,depth,depthLimit);
        }
        System.out.println("Explored Depths:0-"+(depthLimit));
        for(int i=0;i<resp.length;i++)
            if(resp[i].getUtility()==val)
                return resp[i].getLastMove();
        return board.getLastMove();
    }

    /**
     * Max Function, tries to obtain highest possible move.
     * @param board Board being currently searched.
     * @param succ List of all possible successors of this board.
     * @param alpha Minimum value.
     * @param beta Max value.
     * @param currTime The time that this search was started.
     * @param timeLimit The time limit imposed by user.
     * @param depth Depth of this search.
     * @param dLimit Depth limit before the search must stop.
     * @return Best value that the computer can get.
     */
    private static int searchMaxV(Board board,Board[] succ,int alpha,int beta,long currTime,long timeLimit,int depth,int dLimit){
        if((System.currentTimeMillis()-currTime)>=timeLimit || depth>dLimit || board.getPossMoves()==0)
            return board.getUtility();
        else if(board.checkTerminal()!=0)
            return -99;
        depth++;
        int v=-1000;
        for(int i=0;i<succ.length;i++){
            Board[] succ2;
            if(depth<=dLimit){
                succ2=succ[i].successors('O');
                int ut=searchMinV(succ[i],succ2,alpha,beta,currTime,timeLimit,depth,dLimit);
                v=Math.max(v,ut);
                succ[i].setUtility(ut);
                if(v>=beta) {
                    board.setUtility(v);
                    return v;
                }
                alpha=Math.max(alpha,v);
            }
            else
                return board.getUtility();
        }
        board.setUtility(v);
        return v;
    }

    /**
     * Min Function, tries to obtain lowest possible move.
     * @param board Board being currently searched.
     * @param succ List of all possible successors of this board.
     * @param alpha Minimum value.
     * @param beta Max value.
     * @param currTime The time that this search was started.
     * @param timeLimit The time limit imposed by user.
     * @param depth Depth of this search.
     * @param dLimit Depth limit before the search must stop.
     * @return Best value that the user can get.
     */
    private static int searchMinV(Board board,Board[] succ, int alpha, int beta,long currTime,long timeLimit,int depth,int dLimit){
        if((System.currentTimeMillis()-currTime)>=timeLimit || depth>dLimit || board.getPossMoves()==0)
            return board.getUtility();
        else if(board.checkTerminal()!=0)
            return 99;
        depth++;
        int v=1000;;
        for(int i=0;i<succ.length;i++){
            Board[] succ2;
            if(depth<=dLimit) {
                succ2 = succ[i].successors('X');
                int ut=searchMaxV(succ[i],succ2,alpha,beta,currTime,timeLimit,depth,dLimit);
                v=Math.min(v,ut);
                succ[i].setUtility(ut);
                if(v<=alpha){
                    board.setUtility(v);
                    return v;
                }
                beta=Math.min(beta,v);
            }
            else
                return board.getUtility();
        }
        board.setUtility(v);
        return v;
    }
}
