public class MyAIStillLife {
    private final int myID;

    public MyAIStillLife(int myID) {
        this.myID = myID;
    }

    public Location select(Grid grid) {
        
        int MyID = myID;
        int[][] Ids = new int[grid.getRows()][grid.getCols()];
        for(int r = 0; r < grid.getRows(); r++) {
            for (int c = 0; c < grid.getCols(); c++) {
                Ids[r][c] = grid.getCell(r, c);
                if (grid.getCell(r, c) != MyID && grid.getCell(r, c) != -1) {
                    int neighbors = GridFunctions.getNeighbors(r, c, grid);
                    if(neighbors == 3){
                        if(GridFunctions.mostCommonNeighbor(r, c, grid) != MyID) {
                            if(r-1 >= 0 && r+1 < grid.getRows() && c-1 >= 0 && c+1 < grid.getCols()) {
                                if(Ids[r+1][c] != -1 && Ids[r-1][c] == -1 && Ids[r][c-1] == -1 && Ids[r][c+1] != -1) {
                                    return new Location(r+2, c);
                                }
                                else if(Ids[r-1][c] != -1 && Ids[r+1][c] == -1 && Ids[r][c-1] != -1 && Ids[r][c+1] == -1) {
                                    return new Location(r-2, c);
                                }
                                else if(Ids[r][c+1] != -1 && Ids[r][c-1] == -1 && Ids[r-1][c] == -1 && Ids[r+1][c] != -1) {
                                    return new Location(r, c+2);
                                }
                                else if(Ids[r][c-1] != -1 && Ids[r][c+1] == -1 && Ids[r-1][c] != -1 && Ids[r+1][c] == -1) {
                                    return new Location(r, c-2);
                                }
                            }
                            else if(r-1 >= 0 && r+1 < grid.getRows() && !(c-1 >=0)) {
                                if(Ids[r+1][c] != -1 && Ids[r-1][c] == -1 && Ids[r][c+1] != -1 && Ids[r+1][c+1] == -1) {
                                    return new Location(r+2, c);
                                }
                            }
                            else if(c-1 >= 0 && c+1 < grid.getCols() && !(r-1 >=0)) {
                                if(Ids[r][c+1] != -1 && Ids[r][c-1] == -1 && Ids[r+1][c] != -1 && Ids[r+1][c+1] == -1) {
                                    return new Location(r, c+2);
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
