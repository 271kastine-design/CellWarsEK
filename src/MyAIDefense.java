public class MyAIDefense {
    private final int myID;

    public MyAIDefense(int myID) {
        this.myID = myID;
    }

    public Location select(Grid grid) {
        final int myIDLocal = myID;
        int[][] ids = new int[grid.getRows()][grid.getCols()];

        for (int r = 0; r < grid.getRows(); r++) {
            for (int c = 0; c < grid.getCols(); c++) {
                ids[r][c] = grid.getCell(r, c);
            }
        }

        for (int r = 0; r < grid.getRows(); r++) {
            for (int c = 0; c < grid.getCols(); c++) {
                if (grid.getCell(r, c) == myIDLocal) {
                    int neighbors = GridFunctions.getNeighbors(r, c, grid);

                    if (neighbors == 2 && GridFunctions.mostCommonNeighbor(r, c, grid) == myIDLocal) {
                        if (r + 3 < grid.getRows() && c - 2 >= 0 && c + 2 < grid.getCols()) {
                            boolean bracketPattern = ids[r][c] != -1
                                    && ids[r][c - 1] == -1 && ids[r][c + 1] == -1
                                    && ids[r + 1][c] == -1 && ids[r + 1][c - 1] != -1 && ids[r + 1][c + 1] != -1
                                    && ids[r + 2][c] == -1 && ids[r + 2][c - 1] != -1 && ids[r + 2][c + 1] != -1
                                    && ids[r + 3][c] != -1 && ids[r + 3][c - 1] == -1 && ids[r + 3][c + 1] == -1
                                    && ids[r][c - 2] == -1 && ids[r + 1][c - 2] == -1
                                    && ids[r + 2][c - 2] == -1 && ids[r + 3][c - 2] == -1
                                    && ids[r][c + 2] == -1 && ids[r + 1][c + 2] == -1
                                    && ids[r + 2][c + 2] == -1 && ids[r + 3][c + 2] == -1;

                            if (bracketPattern) {
                                return new Location(r + 1, c);
                            }
                        }

                        if (r + 2 < grid.getRows() && c - 1 >= 0 && c + 3 < grid.getCols()) {
                            boolean sidewaysBracketPattern =
                                    ids[r][c - 1] == -1
                                    && ids[r][c] != -1
                                    && ids[r][c + 1] != -1
                                    && ids[r][c + 2] == -1
                                    && ids[r][c + 3] == -1
                                    && ids[r + 1][c - 1] == -1
                                    && ids[r + 1][c] == -1
                                    && ids[r + 1][c + 1] == -1
                                    && ids[r + 1][c + 2] != -1
                                    && ids[r + 1][c + 3] == -1
                                    && ids[r + 2][c - 1] == -1
                                    && ids[r + 2][c] != -1
                                    && ids[r + 2][c + 1] != -1
                                    && ids[r + 2][c + 2] == -1
                                    && ids[r + 2][c + 3] == -1;

                            if (sidewaysBracketPattern) {
                                return new Location(r + 1, c);
                            }
                        }
                    }

                }
            }
        }
        for (int r = 0; r < grid.getRows(); r++) {
            for (int c = 0; c < grid.getCols(); c++) {
                if (r >= 1 && r + 3 < grid.getRows()
                        && c >= 1 && c + 3 < grid.getCols()) {
                    boolean boatPattern =
                            ids[r - 1][c - 1] == -1
                            && ids[r - 1][c] == -1
                            && ids[r - 1][c + 1] == -1
                            && ids[r - 1][c + 2] == -1
                            && ids[r - 1][c + 3] == -1
                            && ids[r][c - 1] == -1
                            && ids[r][c] == -1
                            && ids[r][c + 1] != -1
                            && ids[r][c + 2] != -1
                            && ids[r][c + 3] == -1
                            && ids[r + 1][c - 1] == -1
                            && ids[r + 1][c] != -1
                            && ids[r + 1][c + 1] == -1
                            && ids[r + 1][c + 2] != -1
                            && ids[r + 1][c + 3] == -1
                            && ids[r + 2][c - 1] == -1
                            && ids[r + 2][c] == -1
                            && ids[r + 2][c + 1] != -1
                            && ids[r + 2][c + 2] == -1
                            && ids[r + 2][c + 3] == -1
                            && ids[r + 3][c - 1] == -1
                            && ids[r + 3][c] == -1
                            && ids[r + 3][c + 1] == -1
                            && ids[r + 3][c + 2] == -1
                            && ids[r + 3][c + 3] == -1;

                    if (boatPattern) {
                        return new Location(r + 1, c);
                    }
                }
            }
        }

        return null;
    }
}
