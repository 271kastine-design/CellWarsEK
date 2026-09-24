/**
 * STUDENT FILE
 *
 * Name: Elliott Kastin
 * AI Code Name: ChipAI
 *
 * Strategy Description:
 * Replace this comment with a short explanation of the strategy your AI uses.
 * Your final strategy must be fundamentally different from the sample AIs.
 */
public class MyAI extends CellAI {

    @Override
    public String getAIName() {
        return "MyAI - ChipAI";
    }
    /* 
    @Override
    public Location select(Grid grid) {
        /*
         * Replace this starter strategy.
         *
         * Helpful information:
         *   getID()                     -> your cell ID
         *   grid.getRows()              -> number of rows
         *   grid.getCols()              -> number of columns
         *   grid.getCell(r, c)          -> -1 if dead, otherwise an AI ID
         *   GridFunctions.getNeighbors  -> number of living neighbors
         *   GridFunctions.mostCommonNeighbor -> most common neighboring AI
         *   randomInt(bound)            -> reproducible random integer
        
        int MyID = getID();
        int Wcount = 0;
        int Lcount = 0;
        int[][] Ids = new int[grid.getRows()][grid.getCols()];
        for (int r = 0; r < grid.getRows(); r++) {
            for (int c = 0; c < grid.getCols(); c++) {
                Ids[r][c] = grid.getCell(r, c);
                if(Ids[r][c] == MyID) {
                    Wcount++;
                } else if(Ids[r][c] != -1) {
                    Lcount++;
                }
            }
        }
        if (Wcount <= Lcount) {
            MyAIDefense defense = new MyAIDefense(MyID);
            Location defenseLocation = defense.select(grid);
            if (defenseLocation != null) {
                return defenseLocation;
            }
        } else {
            MyAIOffense offence = new MyAIOffense(MyID);
            Location offenceLocation = offence.select(grid);
            if (offenceLocation != null) {
                return offenceLocation;
            }
        }

        // targets a 3-in-a-row oscillator
        for (int r = 0; r < grid.getRows(); r++) {
            for (int c = 0; c < grid.getCols(); c++) {
                if (Ids[r][c] != MyID && Ids[r][c] != -1) {
                    int neighbors = GridFunctions.getNeighbors(r, c, grid);
                    MyAIOscilator ocilator = new MyAIOscilator(MyID);
                    Location ocilatorLocation = ocilator.select(grid);
                    if (ocilatorLocation != null) {
                        return ocilatorLocation;
                    }

                    if (neighbors == 3) {
                        MyAIOffense stillLife = new MyAIOffense(MyID);
                        Location stillLifeLocation = stillLife.select(grid);
                        if (stillLifeLocation != null) {
                            return stillLifeLocation;
                        }
                    }
                }
            }
        }

        for (int r = 0; r < grid.getRows(); r++) {
            for (int c = 0; c < grid.getCols(); c++) {
                if (Ids[r][c] != -1 && Ids[r][c] != MyID) {
                    if (GridFunctions.getNeighbors(r, c, grid) == 3) {
                        if (GridFunctions.mostCommonNeighbor(r, c, grid) != MyID) {
                            if (c == 0 || r == 0) {
                                return new Location(r, c + 2);
                            } else if (c == 0 || r == grid.getRows() - 1) {
                                return new Location(r, c + 2);
                            } else if (c == grid.getCols() - 1 || r == 0) {
                                return new Location(r, c - 2);
                            } else if (c == grid.getCols() - 1 || r == grid.getRows() - 1) {
                                return new Location(r, c - 2);
                            } else {
                                return new Location(r, c + 2);
                            }
                        }
                    }
                    return new Location(r, c);
                }
            }
        }

        return new Location(randomInt(grid.getRows()), randomInt(grid.getCols()));
    }
 */
    int MyID = getID();

    @Override
    public Location select(Grid grid) {
        int[][] currentBoard = new int[grid.getRows()][grid.getCols()];

        for (int r = 0; r < grid.getRows(); r++) {
            for (int c = 0; c < grid.getCols(); c++) {
                currentBoard[r][c] = grid.getCell(r, c);
            }
        }

        int[] cellCounts = countCells(currentBoard, MyID);

        int[] bestQuickScores = {
            Integer.MIN_VALUE,
            Integer.MIN_VALUE,
            Integer.MIN_VALUE,
            Integer.MIN_VALUE,
            Integer.MIN_VALUE
        };

        Location[] bestQuickMoves = new Location[5];

        for (int r = 0; r < grid.getRows(); r++) {
            for (int c = 0; c < grid.getCols(); c++) {
                if (currentBoard[r][c] == -1) {
                    int score = quickScore(
                        currentBoard,
                        r,
                        c,
                        MyID,
                        cellCounts[0],
                        cellCounts[1]
                    );

                    for (int i = 0; i < 5; i++) {
                        if (score > bestQuickScores[i]) {
                            for (int j = 4; j > i; j--) {
                                bestQuickScores[j] = bestQuickScores[j - 1];
                                bestQuickMoves[j] = bestQuickMoves[j - 1];
                            }

                            bestQuickScores[i] = score;
                            bestQuickMoves[i] = new Location(r, c);
                            break;
                        }
                    }
                }
            }
        }

        int bestScore = Integer.MIN_VALUE;
        Location bestMove = null;

        for (int i = 0; i < 5; i++) {
            if (bestQuickMoves[i] != null) {
                Location move = bestQuickMoves[i];
                int[][] newBoard = copyBoard(currentBoard);
                newBoard[move.getRow()][move.getCol()] = MyID;

                int[][] futureBoard = calculateNextBoard(newBoard, MyID);
                int score = alphaBetaPruning(
                    futureBoard,
                    5,
                    Integer.MIN_VALUE,
                    Integer.MAX_VALUE,
                    false
                );

                if (score > bestScore) {
                    bestScore = score;
                    bestMove = move;
                }
            }
        }

        if (bestMove == null) {
            for (int r = 0; r < grid.getRows(); r++) {
                for (int c = 0; c < grid.getCols(); c++) {
                    if (currentBoard[r][c] == -1) {
                        return new Location(r, c);
                    }
                }
            }
        }

        return bestMove;
    }

    private int[] countCells(int[][] board, int playerID) {
        int myCells = 0;
        int opponentCells = 0;

        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[0].length; c++) {
                if (board[r][c] == playerID) {
                    myCells++;
                } else if (board[r][c] != -1) {
                    opponentCells++;
                }
            }
        }

        return new int[] { myCells, opponentCells };
    }

    private int quickScore(
        int[][] board,
        int r,
        int c,
        int playerID,
        int myCells,
        int opponentCells
    ) {
        int myNeighbors = 0;
        int opponentNeighbors = 0;
        for (int ar = -1; ar <= 1; ar++) {
            for (int ac = -1; ac <= 1; ac++) {
                if (ar == 0 && ac == 0) {
                    continue;
                }

                int br = r + ar;
                int bc = c + ac;

                if (br >= 0 && br < board.length
                        && bc >= 0 && bc < board[0].length
                        && board[br][bc] != -1) {
                    if (board[br][bc] == playerID) {
                        myNeighbors++;
                    } else {
                        opponentNeighbors++;
                    }
                }
            }
        }
        if(opponentCells * 10 < myCells) {
            return opponentNeighbors * 4 - myNeighbors;
        }
        else if(opponentCells * 5 < myCells) {
            return opponentNeighbors * 3 + myNeighbors;
        } else if(opponentCells * 2 < myCells) {
            return opponentNeighbors * 2 + myNeighbors;
        } else if (myCells > opponentCells) {
            return myNeighbors*2 + opponentNeighbors*2;
        }
        else{
            return myNeighbors*3 - opponentNeighbors;
        }

    }

    public int[][] copyBoard(int[][] board) {
        int[][] newBoard = new int[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                newBoard[i][j] = board[i][j];
            }
        }
        return newBoard;
    }
    public Location[] top5Moves(int[][] board, int playerID) {
        Location[] topMoves = new Location[5];
        int[] scores = new int[5];
        int[] cellCounts = countCells(board, playerID);

        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[0].length; c++) {
                if (board[r][c] == -1) {
                    int score = quickScore(
                        board,
                        r,
                        c,
                        playerID,
                        cellCounts[0],
                        cellCounts[1]
                    );
                    for (int i = 0; i < 5; i++) {
                        if (score > scores[i]) {
                            for (int j = 4; j > i; j--) {
                                scores[j] = scores[j - 1];
                                topMoves[j] = topMoves[j - 1];
                            }
                            scores[i] = score;
                            topMoves[i] = new Location(r, c);
                            break;
                        }
                    }
                }
            }
        }
        return topMoves;
    }
    public int alphaBetaPruning(int[][] board, int depth, int alpha, int beta, boolean isMaximizing) {
        if (depth == 0) {
            return evaluateBoard(board);
        }

        int opponentID = findOpponentID(board);

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            Location[] moves = top5Moves(board, MyID);

            for (int i = 0; i < moves.length; i++) {
                if (moves[i] == null) {
                    continue;
                }

                int r = moves[i].getRow();
                int c = moves[i].getCol();

                if (board[r][c] == -1) {
                    int[][] newBoard = copyBoard(board);
                    newBoard[r][c] = MyID;

                    int[][] futureBoard = calculateNextBoard(newBoard, MyID);
                    int score = alphaBetaPruning(futureBoard, depth - 1, alpha, beta, false);

                    bestScore = Math.max(bestScore, score);
                    alpha = Math.max(alpha, bestScore);

                    if (alpha >= beta) {
                        return bestScore;
                    }
                }
            }

            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            Location[] moves = top5Moves(board, opponentID);

            for (int i = 0; i < moves.length; i++) {
                if (moves[i] == null) {
                    continue;
                }

                int r = moves[i].getRow();
                int c = moves[i].getCol();

                if (board[r][c] == -1) {
                    int[][] newBoard = copyBoard(board);
                    newBoard[r][c] = opponentID;

                    int[][] futureBoard = calculateNextBoard(newBoard, opponentID);
                    int score = alphaBetaPruning(futureBoard, depth - 1, alpha, beta, true);

                    bestScore = Math.min(bestScore, score);
                    beta = Math.min(beta, bestScore);

                    if (alpha >= beta) {
                        return bestScore;
                    }
                }
            }

            return bestScore;
        }
    }

    public int findOpponentID(int[][] board) {
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[0].length; c++) {
                if (board[r][c] != -1 && board[r][c] != MyID) {
                    return board[r][c];
                }
            }
        }
        return -1;
    }

    public int evaluateBoard(int[][] board) {
        int myCells = 0;
        int opponentCells = 0;

        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[0].length; c++) {
                if (board[r][c] == MyID) {
                    myCells++;
                } else if (board[r][c] != -1) {
                    opponentCells++;
                }
            }
        }

        return myCells - opponentCells;
    }

    private int[][] calculateNextBoard(int[][] board, int myIDLocal) {
        int rows = board.length;
        int cols = board[0].length;
        int[][] nextBoard = new int[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int neighbors = 0;
                int myNeighbors = 0;
                int opponentNeighbors = 0;
                int opponentID = -1;

                for (int dr = -1; dr <= 1; dr++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        if (dr == 0 && dc == 0) {
                            continue;
                        }

                        int nr = r + dr;
                        int nc = c + dc;

                        if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && board[nr][nc] != -1) {
                            neighbors++;

                            if (board[nr][nc] == myIDLocal) {
                                myNeighbors++;
                            } else {
                                opponentNeighbors++;
                                opponentID = board[nr][nc];
                            }
                        }
                    }
                }

                if (board[r][c] != -1) {
                    if (neighbors == 2 || neighbors == 3) {
                        nextBoard[r][c] = board[r][c];
                    } else {
                        nextBoard[r][c] = -1;
                    }
                } else {
                    if (neighbors == 3) {
                        if (myNeighbors > opponentNeighbors) {
                            nextBoard[r][c] = myIDLocal;
                        } else {
                            nextBoard[r][c] = opponentID;
                        }
                    } else {
                        nextBoard[r][c] = -1;
                    }
                }
            }
        }

        return nextBoard;
    }
}