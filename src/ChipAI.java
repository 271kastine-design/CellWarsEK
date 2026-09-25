/**
 * STUDENT FILE
 *
 * Name: Elliott Kastin
 * AI Code Name: ChipAI
 *
 * Strategy Description:
 * This AI tries to combine fast local move evaluation with a deeper minimax-style lookahead.
 * It ranks the strongest empty spaces using a heuristic based on nearby friendly versus enemy cells,
 * then evaluates only the top candidates several turns ahead to choose a move that improves board
 * position while still reacting to the opponent's likely responses.
 */
public class ChipAI extends CellAI {

    @Override
    public String getAIName() {
        return "ChipAI";
    }

    // Store the AI's assigned player ID once so all later scoring and board checks use the same identity.
    int MyID = getID();

    @Override
    public Location select(Grid grid) {
        // Copy the live game board into a normal 2D array so we can inspect and simulate moves
        // without mutating the actual game state.
        int[][] currentBoard = new int[grid.getRows()][grid.getCols()];

        for (int r = 0; r < grid.getRows(); r++) {
            for (int c = 0; c < grid.getCols(); c++) {
                currentBoard[r][c] = grid.getCell(r, c);
            }
        }

        // Count total owned cells for this AI and the opponent to use in heuristic balance checks.
        int[] cellCounts = countCells(currentBoard, MyID);

        // Keep the highest-scoring nearby moves in a small shortlist before doing deeper search.
        int[] bestQuickScores = {
            Integer.MIN_VALUE,
            Integer.MIN_VALUE,
            Integer.MIN_VALUE,
            Integer.MIN_VALUE,
            Integer.MIN_VALUE
        };

        Location[] bestQuickMoves = new Location[5];

        // Evaluate every open square and rank the best ones by local pressure and board control.
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

                    // Insert the move into the ranked list only if it beats one of the current top entries.
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

        // Search the top candidates several moves ahead and pick the one that leads to the best result.
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

        // In the unlikely event that no move qualified, just take the first legal open cell.
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

    // Counts how many spaces belong to this AI and how many are held by the opponent.
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

    // Quick heuristic for a single empty cell. It measures the local neighborhood and adjusts its value
    // depending on whether the AI is currently ahead or behind in total territory.
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

        // Check all eight surrounding tiles for the candidate move.
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

        // The scoring shifts depending on whether the AI is leading or trailing in total cell count.
        if (opponentCells * 10 < myCells) {
            return opponentNeighbors * 4 - myNeighbors;
        } else if (opponentCells * 5 < myCells) {
            return opponentNeighbors * 3 + myNeighbors;
        } else if (opponentCells * 2 < myCells) {
            return opponentNeighbors * 2 + myNeighbors;
        } else if (myCells > opponentCells) {
            return myNeighbors * 2 + opponentNeighbors * 2;
        } else {
            return myNeighbors * 3 - opponentNeighbors;
        }
    }

    // Helper to duplicate a board before exploring hypothetical next states.
    public int[][] copyBoard(int[][] board) {
        int[][] newBoard = new int[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                newBoard[i][j] = board[i][j];
            }
        }
        return newBoard;
    }

    // Builds a ranked list of the best five legal moves for a given player using the same heuristic.
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

                    // Insert the move into the top-five list in descending order of score.
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

    // Big-O magnitude: this search is roughly O(b^d), where b is the branching factor (about the top moves
    // considered at each step) and d is the lookahead depth. With a board of size R x C and a top-5 shortlist,
    // each level scans the board and simulates candidate moves, so the practical cost is still exponential in depth
    //D = 5 and b = 5, but much smaller than exploring every legal move on every turn.
    // but much smaller than exploring every legal move on every turn.
    // Alpha-beta pruning is used to reduce unnecessary search branches in the minimax tree.
    // It assumes the AI is maximizing its evaluation while the opponent minimizes it.
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

    // Finds any non-empty cell that is not controlled by this AI, which is used as the opponent ID.
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

    // Evaluation function at a leaf node: reward cells owned by this AI and penalize opponent-owned cells.
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

    // Simulates a full board update using the game's rules: cells with 2 or 3 neighbors survive,
    // empty cells become occupied only when exactly 3 neighbors exist, and if a birth happens the
    // majority surrounding color decides ownership for that space.
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

                // Count all neighboring occupied cells and split them into friendly vs enemy ownership.
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

                // Existing cells survive only with exactly two or three live neighbors; otherwise they die.
                if (board[r][c] != -1) {
                    if (neighbors == 2 || neighbors == 3) {
                        nextBoard[r][c] = board[r][c];
                    } else {
                        nextBoard[r][c] = -1;
                    }
                } else {
                    // Empty cells are born only with exactly three adjacent occupied cells.
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