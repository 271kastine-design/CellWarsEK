public class MyAIStillLife {
    private final int myID;

    public MyAIStillLife(int myID) {
        this.myID = myID;
    }

    public Location select(Grid grid) {
        int myIDLocal = myID;
        int[][] ids = new int[grid.getRows()][grid.getCols()];

        for (int r = 0; r < grid.getRows(); r++) {
            for (int c = 0; c < grid.getCols(); c++) {
                ids[r][c] = grid.getCell(r, c);
            }
        }
        for (int r = 0; r < grid.getRows(); r++) {
            for (int c = 0; c < grid.getCols(); c++) {
                if (grid.getCell(r, c) != myIDLocal && grid.getCell(r, c) != -1) {
                    int neighbors = GridFunctions.getNeighbors(r, c, grid);

                    if (neighbors == 3 && GridFunctions.mostCommonNeighbor(r, c, grid) != myIDLocal) {
                        if (r - 1 >= 0 && r + 2 < grid.getRows() &&
                            c - 1 >= 0 && c + 2 < grid.getCols()) {

                            boolean stillLife =
                                ids[r - 1][c - 1] == -1 &&
                                ids[r - 1][c] == -1 &&
                                ids[r - 1][c + 1] == -1 &&
                                ids[r - 1][c + 2] == -1 &&
                                ids[r][c - 1] == -1 &&
                                ids[r][c] != -1 &&
                                ids[r][c + 1] != -1 &&
                                ids[r][c + 2] == -1 &&
                                ids[r + 1][c - 1] == -1 &&
                                ids[r + 1][c] != -1 &&
                                ids[r + 1][c + 1] != -1 &&
                                ids[r + 1][c + 2] == -1 &&
                                ids[r + 2][c - 1] == -1 &&
                                ids[r + 2][c] == -1 &&
                                ids[r + 2][c + 1] == -1 &&
                                ids[r + 2][c + 2] == -1;

                            if (stillLife) {
                                return new Location(r, c - 1);
                            }
                        }
                    }

                    if (neighbors == 2 && GridFunctions.mostCommonNeighbor(r, c, grid) != myIDLocal) {
                        if (r + 3 < grid.getRows() && c - 2 >= 0 && c + 2 < grid.getCols()) {
                            boolean bracketPattern = ids[r][c] != -1
                                    && ids[r][c - 1] == -1 && ids[r][c + 1] == -1
                                    && ids[r + 1][c] == -1 && ids[r + 1][c - 1] != -1 && ids[r + 1][c + 1] != -1
                                    && ids[r + 2][c] == -1 && ids[r + 2][c - 1] != -1 && ids[r + 2][c + 1] != -1
                                    && ids[r + 3][c] != -1 && ids[r + 3][c - 1] == -1 && ids[r + 3][c + 1] == -1
                                    && ids[r][c - 2] == -1 && ids[r + 1][c - 2] == -1 && ids[r + 2][c - 2] == -1 && ids[r + 3][c - 2] == -1
                                    && ids[r][c + 2] == -1 && ids[r + 1][c + 2] == -1 && ids[r + 2][c + 2] == -1 && ids[r + 3][c + 2] == -1;

                            if (bracketPattern) {
                                return new Location(r, c);
                            }
                        }
                    }
                    if (r + 2 < grid.getRows() && c + 3 < grid.getCols() && c - 1 >= 0) {
                        boolean sidewaysBracketPattern =
                                ids[r][c - 1] == -1
                                && ids[r][c] == -1
                                && ids[r][c + 1] != -1
                                && ids[r][c + 2] != -1
                                && ids[r][c + 3] == -1

                                && ids[r + 1][c - 1] == -1
                                && ids[r + 1][c] != -1
                                && ids[r + 1][c + 1] == -1
                                && ids[r + 1][c + 2] == -1
                                && ids[r + 1][c + 3] != -1

                                && ids[r + 2][c - 1] == -1
                                && ids[r + 2][c] == -1
                                && ids[r + 2][c + 1] != -1
                                && ids[r + 2][c + 2] != -1
                                && ids[r + 2][c + 3] == -1;

                        if (sidewaysBracketPattern) {
                            return new Location(r, c + 1);
                        }
                    }
                }
            }
        }

        return null;
    }
}
