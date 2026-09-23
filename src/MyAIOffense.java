public class MyAIOffense {
    private final int myID;

    public MyAIOffense(int myID) {
        this.myID = myID;
    }

    public Location select(Grid grid) {
        int bestScore = -1000000;
        Location bestMove = null;
        int myIDLocal = myID;
        int[][] ids = new int[grid.getRows()][grid.getCols()];

        for (int r = 0; r < grid.getRows(); r++) {
            for (int c = 0; c < grid.getCols(); c++) {
                ids[r][c] = grid.getCell(r, c);
            }
        }

        for (int r = 0; r < grid.getRows(); r++) {
            for (int c = 0; c < grid.getCols(); c++) {

                // Only consider empty spaces as possible moves
                if (ids[r][c] == -1) {

                    // Make a copy of the current board
                    int[][] testBoard = new int[grid.getRows()][grid.getCols()];

                    for (int i = 0; i < grid.getRows(); i++) {
                        for (int j = 0; j < grid.getCols(); j++) {
                            testBoard[i][j] = ids[i][j];
                        }
                    }

                    // Pretend we make this move
                    testBoard[r][c] = myIDLocal;

                    // Predict the board one turn into the future
                    int[][] futureBoard = calculateNextBoard(testBoard, myIDLocal);

                    // Count our cells and opponent cells
                    int myFutureCells = 0;
                    int opponentFutureCells = 0;

                    // Count opponent cells that are vulnerable
                    int vulnerableOpponentCells = 0;

                    for (int i = 0; i < grid.getRows(); i++) {
                        for (int j = 0; j < grid.getCols(); j++) {

                            if (futureBoard[i][j] == myIDLocal) {
                                myFutureCells++;

                            } else if (futureBoard[i][j] != -1) {
                                opponentFutureCells++;

                                // Check how many neighbors this opponent cell has
                                int neighbors = 0;

                                for (int dr = -1; dr <= 1; dr++) {
                                    for (int dc = -1; dc <= 1; dc++) {

                                        if (dr == 0 && dc == 0) {
                                            continue;
                                        }

                                        int nr = i + dr;
                                        int nc = j + dc;

                                        if (nr >= 0 && nr < grid.getRows()
                                                && nc >= 0 && nc < grid.getCols()
                                                && futureBoard[nr][nc] != -1) {
                                            neighbors++;
                                        }
                                    }
                                }

                                // Fewer than 2 or more than 3 neighbors
                                // means this cell will die
                                if (neighbors < 2 || neighbors > 3) {
                                    vulnerableOpponentCells++;
                                }
                            }
                        }
                    }

                    // Our cells minus opponent cells,
                    // plus vulnerable opponent cells
                    int score = myFutureCells
                              - opponentFutureCells
                              + vulnerableOpponentCells;

                    // Keep the move with the best score
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = new Location(r, c);
                    }
                }
            }
        }

        return bestMove;
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