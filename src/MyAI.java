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

    int bestScore = Integer.MIN_VALUE;
    Location bestMove = null;

    for (int r = 0; r < grid.getRows(); r++) {
        for (int c = 0; c < grid.getCols(); c++) {

            if (currentBoard[r][c] == -1
                    && GridFunctions.mostCommonNeighbor(r, c, grid) != MyID) {

                int[][] newBoard = copyBoard(currentBoard);

                newBoard[r][c] = MyID;

                int[][] futureBoard = calculateNextBoard(newBoard, MyID);

                int score = alphaBetaPruning(
                    futureBoard,
                    1,
                    Integer.MIN_VALUE,
                    Integer.MAX_VALUE,
                    false
                );

                if (score > bestScore) {
                    bestScore = score;
                    bestMove = new Location(r, c);
                }
            }
        }
    }

    // Safety fallback
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
    public int[][] copyBoard(int[][] board) {
        int[][] newBoard = new int[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                newBoard[i][j] = board[i][j];
            }
        }
        return newBoard;
    }
    public int alphaBetaPruning(int[][] board, int depth, int alpha, int beta, boolean isMaximizing) {
        if (depth == 0) {
            return evaluateBoard(board);
        }
        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;

            for (int r = 0; r < board.length; r++) {
                for (int c = 0; c < board[0].length; c++) {
                     int[][] newBoard = new int[board.length][board[0].length];

                     for (int i = 0; i < board.length; i++) {
                         for (int j = 0; j < board[0].length; j++) {
                             newBoard[i][j] = board[i][j];
                         }
                     }
                     newBoard[r][c] = MyID;
                     int[][] futureBoard = calculateNextBoard(newBoard, MyID);

                     int score = alphaBetaPruning(futureBoard, depth - 1, alpha, beta, false);
                     bestScore = Math.max(bestScore, score);
                     alpha = Math.max(alpha, bestScore);
                     if (alpha >= beta) {
                         break;
                     } 
                }
                if (alpha >= beta) {
                            break;
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;

            for (int r = 0; r < board.length; r++) {
                for (int c = 0; c < board[0].length; c++) {
                     int[][] newBoard = new int[board.length][board[0].length];

                     for (int i = 0; i < board.length; i++) {
                         for (int j = 0; j < board[0].length; j++) {
                             newBoard[i][j] = board[i][j];
                         }
                     }
                     newBoard[r][c] = MyID;
                     int[][] futureBoard = calculateNextBoard(newBoard, MyID);

                     int score = alphaBetaPruning(futureBoard, depth - 1, alpha, beta, true);
                     bestScore = Math.min(bestScore, score);
                     beta = Math.min(beta, bestScore);
                     if (alpha >= beta) {
                         break;
                     } 
                }
                if (alpha >= beta) {
                            break;
                }
            }
            return bestScore; 
        }
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

                        if (nr >= 0 && nr < rows
                                && nc >= 0 && nc < cols
                                && board[nr][nc] != -1) {

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