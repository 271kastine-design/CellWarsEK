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
        if(Wcount <= Lcount){
            MyAIDefense defense = new MyAIDefense(MyID);
            Location defenseLocation = defense.select(grid);
            if(defenseLocation != null) {
                return defenseLocation;
            }
        }
        else{
            MyAIOffense offence = new MyAIOffense(MyID);
            Location offenceLocation = offence.select(grid);
            if(offenceLocation != null) {
                return offenceLocation;
            }
        }
        
        //targets a 3 in a row ocilator
        for(int r = 0; r < grid.getRows(); r++) {
            for (int c = 0; c < grid.getCols(); c++) {
                if (Ids[r][c] != MyID && Ids[r][c] != -1) {
                    int neighbors = GridFunctions.getNeighbors(r, c, grid);
                    MyAIOscilator ocilator = new MyAIOscilator(MyID);
                    Location ocilatorLocation = ocilator.select(grid);
                    if (ocilatorLocation != null) {
                        return ocilatorLocation;
                    }
                
                    if(neighbors == 3) {
                        MyAIOffense stillLife = new MyAIOffense(MyID);
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
                        if(GridFunctions.mostCommonNeighbor(r, c, grid) != MyID) {
                            if(c == 0 || r == 0){
                                return new Location(r, c + 2);
                            }
                            else if(c == 0 || r == grid.getRows() - 1) {
                                return new Location(r, c + 2);
                            }
                            else if(c == grid.getCols() - 1 || r == 0) {
                                return new Location(r, c - 2);
                            }
                            else if(c == grid.getCols() - 1 || r == grid.getRows() - 1) {
                                return new Location(r, c - 2);
                            }
                            else{
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
}
