public class MyAIDefense {
    private final int myID;

    public MyAIDefense(int myID) {
        this.myID = myID;
    }

    public Location select(Grid grid) {
        int bestScore = -1000000;
        Location bestMove = null;

        int rows = grid.getRows();
        int cols = grid.getCols();

        int[][] ids = new int[rows][cols];

        // Copy the current board
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                ids[r][c] = grid.getCell(r, c);
            }
        }

        // Try every possible move
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {

                if (ids[r][c] == -1) {

                    // Make a copy of the board
                    int[][] testBoard = new int[rows][cols];

                    for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < cols; j++) {
                            testBoard[i][j] = ids[i][j];
                        }
                    }

                    // Pretend we make this move
                    testBoard[r][c] = myID;

                    // Simulate one generation
                    int[][] futureBoard = calculateNextBoard(testBoard, myID);

                    int myFutureCells = 0;
                    int opponentFutureCells = 0;

                    int myVulnerableCells = 0;
                    int opponentVulnerableCells = 0;

                    int myBirths = 0;
                    int opponentBirths = 0;

                    // Analyze the future board
                    for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < cols; j++) {

                            if (futureBoard[i][j] == myID) {
                                myFutureCells++;

                                // Count neighbors around our cell
                                int neighbors = countNeighbors(futureBoard, i, j);

                                // Our cell will die next generation
                                if (neighbors < 2 || neighbors > 3) {
                                    myVulnerableCells++;
                                }

                            } else if (futureBoard[i][j] != -1) {
                                opponentFutureCells++;

                                int neighbors = countNeighbors(futureBoard, i, j);

                                // Opponent cell will die next generation
                                if (neighbors < 2 || neighbors > 3) {
                                    opponentVulnerableCells++;
                                }

                            } else {
                                // Empty cell: check whether it will create a new cell
                                int neighbors = countNeighbors(futureBoard, i, j);

                                if (neighbors == 3) {

                                    int myNeighbors = 0;
                                    int opponentNeighbors = 0;

                                    for (int dr = -1; dr <= 1; dr++) {
                                        for (int dc = -1; dc <= 1; dc++) {

                                            if (dr == 0 && dc == 0) {
                                                continue;
                                            }

                                            int nr = i + dr;
                                            int nc = j + dc;

                                            if (nr >= 0 && nr < rows
                                                    && nc >= 0 && nc < cols
                                                    && futureBoard[nr][nc] != -1) {

                                                if (futureBoard[nr][nc] == myID) {
                                                    myNeighbors++;
                                                } else {
                                                    opponentNeighbors++;
                                                }
                                            }
                                        }
                                    }

                                    if (myNeighbors > opponentNeighbors) {
                                        myBirths++;
                                    } else {
                                        opponentBirths++;
                                    }
                                }
                            }
                        }
                    }

                    /*
                     * SCORE:
                     *
                     * +10 for every one of our future cells
                     * +6  for every possible future birth for us
                     * +4  for every opponent cell that is vulnerable
                     *
                     * -8  for every one of our vulnerable cells
                     * -5  for every opponent future cell
                     * -4  for every opponent birth opportunity
                     */
                    int score =
                            (myFutureCells * 10)
                          + (myBirths * 6)
                          + (opponentVulnerableCells * 4)
                          - (myVulnerableCells * 8)
                          - (opponentFutureCells * 5)
                          - (opponentBirths * 4);

                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = new Location(r, c);
                    }
                }
            }
        }

        return bestMove;
    }

    private int countNeighbors(int[][] board, int r, int c) {
        int neighbors = 0;

        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {

                if (dr == 0 && dc == 0) {
                    continue;
                }

                int nr = r + dr;
                int nc = c + dc;

                if (nr >= 0 && nr < board.length
                        && nc >= 0 && nc < board[0].length
                        && board[nr][nc] != -1) {

                    neighbors++;
                }
            }
        }

        return neighbors;
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

                // Existing cell
                if (board[r][c] != -1) {

                    if (neighbors == 2 || neighbors == 3) {
                        nextBoard[r][c] = board[r][c];
                    } else {
                        nextBoard[r][c] = -1;
                    }

                // Empty cell
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