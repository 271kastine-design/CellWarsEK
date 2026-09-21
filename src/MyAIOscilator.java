public class MyAIOscilator {
    private int myID;

    public MyAIOscilator(int myID) {
        this.myID = myID;
    }

    public Location select(Grid grid) {
        int MyID = this.myID;
        int[][] Ids = new int[grid.getRows()][grid.getCols()];
        for (int r = 0; r < grid.getRows(); r++) {
            for (int c = 0; c < grid.getCols(); c++) {
                Ids[r][c] = grid.getCell(r, c);
            }
        }

        //targets a 3 in a row ocilator
        for (int r = 0; r < grid.getRows(); r++) {
            for (int c = 0; c < grid.getCols(); c++) {
                if (Ids[r][c] != MyID && Ids[r][c] != -1) {
                    int neighbors = GridFunctions.getNeighbors(r, c, grid);
                    if (neighbors == 2) {
                        if (GridFunctions.mostCommonNeighbor(r, c, grid) != MyID) {
                            if (r - 1 >= 0 && r + 1 < grid.getRows()) {
                                if (Ids[r - 1][c] != -1 && Ids[r + 1][c] != -1
                                        && Ids[r - 1][c] != MyID && Ids[r + 1][c] != MyID) {
                                    return new Location(r, c);
                                } else if (c - 1 >= 0 && c + 1 < grid.getCols()) {
                                    if (Ids[r][c - 1] != -1 && Ids[r][c + 1] != -1
                                            && Ids[r][c - 1] != MyID && Ids[r][c + 1] != MyID) {
                                        return new Location(r, c);
                                    }
                                }
                            } else if (c - 1 >= 0 && c + 1 < grid.getCols()) {
                                if (Ids[r][c - 1] != -1 && Ids[r][c + 1] != -1
                                        && Ids[r][c - 1] != MyID && Ids[r][c + 1] != MyID) {
                                    return new Location(r, c);
                                }
                            }
                        }
                    }
                }
            }
        }

        return null;
    }
}
