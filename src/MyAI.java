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
         */
        int MyID = getID();
        int[][] Ids = new int[grid.getRows()][grid.getCols()];
        for (int r = 0; r < grid.getRows(); r++) {
            for (int c = 0; c < grid.getCols(); c++) {
                    Ids[r][c] = grid.getCell(r, c);
                }
            }
        //targets a 3 in a row ocilator
        for(int r = 0; r < grid.getRows(); r++) {
            for (int c = 0; c < grid.getCols(); c++) {
                if (Ids[r][c] != MyID && Ids[r][c] != -1) {
                    int neighbors = GridFunctions.getNeighbors(r, c, grid);
                    if (neighbors == 2) {
                        if(GridFunctions.mostCommonNeighbor(r, c, grid) != MyID) {
                            if(r-1 >= 0 && r+1 < grid.getRows()) {
                                if(Ids[r-1][c] != -1 && Ids[r+1][c] != -1 && Ids[r-1][c] != MyID && Ids[r+1][c] != MyID) {
                                    return new Location(r, c);
                                }
                                else if(c-1 >= 0 && c+1 < grid.getCols()) 
                                {
                                    if(Ids[r][c-1] != -1 && Ids[r][c+1] != -1 && Ids[r][c-1] != MyID && Ids[r][c+1] != MyID) {
                                        return new Location(r, c);
                                    }
                                }
                            }
                            else if(c-1 >= 0 && c+1 < grid.getCols()) 
                            {
                                if(Ids[r][c-1] != -1 && Ids[r][c+1] != -1 && Ids[r][c-1] != MyID && Ids[r][c+1] != MyID) {
                                    return new Location(r, c);
                                }
                            }
                        }
                    }
                    if(neighbors == 3) {
                        MyAIStillLife stillLife = new MyAIStillLife(MyID);
                        Location stillLifeLocation = stillLife.select(grid);
                        if (stillLifeLocation != null) {
                            return stillLifeLocation;
                        }
                    }
                }
            }
        }
        for(int r = 0; r < grid.getRows(); r++) {
            for (int c = 0; c < grid.getCols(); c++) {
                if (Ids[r][c] != -1 && Ids[r][c] != MyID) {
                    if(GridFunctions.getNeighbors(r, c, grid) == 3) {
                        return new Location(r, c);
                    }
                }
            }
        }
        return new Location(randomInt(grid.getRows()), randomInt(grid.getCols()));
    }
}
