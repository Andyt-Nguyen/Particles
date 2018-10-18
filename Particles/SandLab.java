/* 
 * --- Particles Project --
 * Collaborators: Andrew Ty Nguyen && Vince Hunt
 *
 */
import java.awt.*;
import java.util.*;

public class SandLab{
    static final int MAX_ROWS = 220;
    static final int MAX_COLS = 180;
    
    static final String FILE_NAME     = "SandLabFile.txt";  //This is the name of the input file.
    static final String NEW_FILE_NAME = "SandLabFile.txt";  //This is the name of the file you are saving.
    
    //add constants for particle types here
    public static final int EMPTY = 0;
    public static final int METAL = 1;
    public static final int SAVEFILE  = 2;
    public static final int SAND = 3;
    public static final int WATER = 4;
    public static final int OIL = 5;
    public static final int GEN = 6;
    public static final int DESTRUCTOR = 7;
    public static final int RESET = 8;
    public static final int FIRE = 8;
    public static final int VAPE = 20;
    public static final int GLASS = 21;

    //do not add any more fields
    private int[][] grid;
    private LabDisplay display;
    
    //---------------------------------------------------------------------------------------------------------
    
    public static void main(String[] args){
        System.out.println("================= Starting Program =================");
        
        SandLab lab = new SandLab(MAX_ROWS, MAX_COLS);
        lab.run();
    }
    
    //SandLab constructor
    public SandLab(int numRows, int numCols){
        String[] names = new String[9];
        
        names[EMPTY] = "Empty";
        names[METAL] = "Metal";
        names[SAVEFILE] = "SaveFile";
        names[SAND] = "Sand";
        names[WATER] = "Water";
        names[OIL] = "Oil";
        names[FIRE] = "Fire";
        names[GEN] = "Generator";
        names[DESTRUCTOR] = "Destructor";
       // names[RESET] = "Reset";

        display = new LabDisplay("SandLab", numRows, numCols, names);
        grid = new int[numRows][numCols];
        
        if (FILE_NAME != "") {
            System.out.println("loading " + FILE_NAME);
            grid = SandLabFiles.readFile(FILE_NAME);   //uncomment this later to save your file...
        }
    }
    
    //called when the user clicks on a location using the given tool
    private void locationClicked(int row, int col, int tool){
        grid[row][col] = tool;
        //if (tool == RESET)
        //{
          //  grid = new int[MAX_ROWS][MAX_COLS];
        //}
        if (tool == SAVEFILE) {
            SandLabFiles.writeFile(grid, NEW_FILE_NAME);  //uncomment this later to save your file...
        }
    }
    
    //copies each element of grid into the display
    public void updateDisplay(){
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[i].length; j++) {
                if(grid[i][j] == METAL) {
                    display.setColor(i,j,Color.gray);
                } else if(grid[i][j] == SAND) {
                    display.setColor(i,j,Color.yellow);
                } else if(grid[i][j] == WATER) {
                    display.setColor(i,j,Color.blue);
                } else if(grid[i][j] == OIL){
                    display.setColor(i,j,Color.green);
                } else if(grid[i][j] == EMPTY) {
                    display.setColor(i,j,Color.black);
                } else if(grid[i][j] == GEN) {
                    display.setColor(i,j, Color.magenta);
                } else if(grid[i][j] == DESTRUCTOR) {
                    display.setColor(i,j,Color.cyan);
                } else if(grid[i][j] == VAPE) {
                    display.setColor(i,j, Color.pink);
                } else if(grid[i][j] == FIRE) {
                    display.setColor(i,j,Color.red);
                } else if(grid[i][j] == GLASS) {
                    display.setColor(i,j,Color.white);
                }
            }
        }
    }
    
    //called repeatedly.
    //causes one random particle to maybe do something.
    public void step() {
        int randomRow = IR4.getRandomNumber(0, MAX_ROWS -1);
        int randomCol = IR4.getRandomNumber(0, MAX_COLS -1);
        int getRandomDirection = IR4.getRandomNumber(-1, 1);
        runSand(randomRow, randomCol);
        runGlass(randomRow, randomCol);
        runWater(randomRow, randomCol, getRandomDirection);
        runOil(randomRow, randomCol, getRandomDirection);
        runGenerator(randomRow, randomCol);
        runDestructor(randomRow, randomCol);
        runVape(randomRow, randomCol, getRandomDirection);
        runFire(randomRow, randomCol, getRandomDirection);      
    }

    
    public void fallingOver(int particle, int randomRow, int randomCol) {
        int getRandomDirection = IR4.getRandomNumber(-1, 1);
        // int MAX_ROWS = 180;

        if(grid[randomRow][randomCol] == particle) {
            grid[randomRow][randomCol] = EMPTY;
            randomRow = randomRow + 1 > MAX_ROWS - 1 ? 0 : randomRow;
            
            if(grid[randomRow][randomCol] != METAL &&
            grid[randomRow][randomCol] != GEN &&
            grid[randomRow][randomCol] != DESTRUCTOR &&
               grid[randomRow + 1][randomCol] != METAL && 
               grid[randomRow + 1][randomCol] != GEN &&
               grid[randomRow + 1][randomCol] != DESTRUCTOR) {
                if(grid[randomRow + 1][randomCol] == SAND || grid[randomRow + 1][randomCol] == GLASS) {

                    if(getRandomDirection == - 1) {
                        if(randomCol + getRandomDirection < 0) {
                            randomCol = MAX_COLS - 1;
                        }
                        if(grid[randomRow][randomCol] != METAL &&
                        grid[randomRow][randomCol] != DESTRUCTOR &&
                        grid[randomRow][randomCol] != GEN) {

                        
                        if(grid[randomRow + 1][randomCol + getRandomDirection] != METAL && 
                        grid[randomRow + 1][randomCol + getRandomDirection] != SAND &&
                        grid[randomRow + 1][randomCol + getRandomDirection] != GLASS &&
                        grid[randomRow + 1][randomCol + getRandomDirection] != GEN &&
                        grid[randomRow + 1][randomCol + getRandomDirection] != DESTRUCTOR) {
                                grid[randomRow][randomCol] = grid[randomRow + 1][randomCol + getRandomDirection];
                                grid[randomRow + 1][randomCol + getRandomDirection] = particle; 
                        } else {
                            grid[randomRow][randomCol] = particle;
                        }
                    }
                    } else if(getRandomDirection == 1) {
                        if(randomCol + getRandomDirection > MAX_COLS - 1) {
                            randomCol = 0;
                        }
                        if(grid[randomRow][randomCol] != METAL &&
                        grid[randomRow][randomCol] != DESTRUCTOR &&
                        grid[randomRow][randomCol] != GEN) {
                            if(grid[randomRow + 1][randomCol + getRandomDirection] != METAL && 
                            grid[randomRow + 1][randomCol + getRandomDirection] != SAND &&
                            grid[randomRow + 1][randomCol + getRandomDirection] != GLASS &&
                            grid[randomRow + 1][randomCol + getRandomDirection] != GEN &&
                            grid[randomRow + 1][randomCol + getRandomDirection] != DESTRUCTOR) {
                                grid[randomRow][randomCol] = grid[randomRow + 1][randomCol + getRandomDirection];
                                grid[randomRow + 1][randomCol + getRandomDirection] = particle; 
                            } else {
                                grid[randomRow][randomCol] = particle;
                            }
                                
                        }  
                    } else {
                        if(grid[randomRow][randomCol] != METAL) {
                            grid[randomRow][randomCol] = particle;
                        }
                    }





                } else {
                    grid[randomRow][randomCol] = grid[randomRow+1][randomCol];
                    grid[randomRow + 1][randomCol] = particle;
                }


            } else {
                if(randomRow == 0) {
                    randomRow = MAX_ROWS - 1;
                }
                grid[randomRow][randomCol] = particle;
            }
           
        }
    }
    public void runGlass(int randomRow, int randomCol) {
        fallingOver(GLASS, randomRow, randomCol);
    }
    public void runSand(int randomRow, int randomCol) {
        fallingOver(SAND, randomRow, randomCol);
    }


    // public void fallingOver(int particle, int randomRow, int randomCol) {
    //     if(grid[randomRow][randomCol] == particle) {
    //         grid[randomRow+1][randomCol] = particle;
    //     }
    // }

    // public void fallingOver(int particle, int randomRow, int randomCol) {
    //     int getRandomDirection = IR4.getRandomNumber(-1, 1);
    //     // int MAX_ROWS = 180;
    //     // int MAX_COLS = 150;
        
    //     if(grid[randomRow][randomCol] == particle) { 
    //         grid[randomRow][randomCol] = EMPTY;
    //         randomRow = randomRow + 1 > MAX_ROWS - 1 ? 0 : randomRow;
    //         randomCol = randomCol + 1 > MAX_COLS - 1 ?  1 : randomCol;
    //         randomCol = randomCol - 1 < 0 ? MAX_COLS - 2 : randomCol;
            
    //         int curr = grid[randomRow][randomCol];
    //         int currBelow = grid[randomRow + 1][randomCol];
    //         int currLeft = grid[randomRow][randomCol - 1];
    //         int currRight = grid[randomRow][randomCol + 1];
    //         int currBtmRight = grid[randomRow + 1][randomCol + 1];
    //         int currBtmLeft = grid[randomRow+1][randomCol - 1];
    //         int currRandomBtmLeftOrRight = grid[randomRow + 1][randomCol + getRandomDirection];
            
    //         // Conditionals
    //         boolean isCurrBelowSandOrGlass = (currBelow == SAND || currBelow == GLASS);
    //         boolean isCurrBelowEmpty = (currBelow != METAL &&
    //                                     currBelow != SAND  &&
    //                                     currBelow != GEN   &&
    //                                     currBelow != DESTRUCTOR &&
    //                                     currBelow != GLASS);

    //         boolean leftOrRight = (currRandomBtmLeftOrRight != METAL &&
    //                                currRandomBtmLeftOrRight != DESTRUCTOR  &&
    //                                currRandomBtmLeftOrRight != GEN &&
    //                                currRandomBtmLeftOrRight != SAND && 
    //                                currRandomBtmLeftOrRight != GLASS);
            
    //         boolean leftAndBtmLeft = (
    //                                   currBtmRight != SAND && 
    //                                   currBtmRight != GLASS && 
    //                                   currBtmRight != METAL &&
    //                                   currBtmRight != DESTRUCTOR && 
    //                                   currBtmRight != GEN);
            
    //         boolean rightAndBtmRight = (
    //                                     currBtmLeft != SAND   &&
    //                                     currBtmLeft != GLASS  &&
    //                                     currBtmLeft != METAL  &&
    //                                     currBtmLeft != GEN    &&
    //                                     currBtmLeft != DESTRUCTOR);
          
    //         if(grid[randomRow][randomCol] != METAL) {
    //             if(isCurrBelowEmpty) {
    //                 grid[randomRow][randomCol] = currBelow;
    //                 grid[randomRow+1][randomCol] = SAND;
    //             } else if(isCurrBelowSandOrGlass) {
    //                 if(leftOrRight) {
    //                     grid[randomRow][randomCol] = currRandomBtmLeftOrRight;
    //                     grid[randomRow + 1][randomCol + getRandomDirection] = particle;
    //                 } else if(leftAndBtmLeft) {
    //                     grid[randomRow][randomCol] = currBtmRight;
    //                     grid[randomRow + 1][randomCol + 1] = particle;
    //                 } else if(rightAndBtmRight) {
    //                     grid[randomRow][randomCol] = currBtmLeft;
    //                     grid[randomRow + 1][randomCol - 1] = particle;
    //                 } else {
    //                     grid[randomRow][randomCol]= particle;
    //                 }
    //             } else {
    //                 grid[randomRow][randomCol] = particle;
    //             }
    //         } else {
    //             if(randomCol == 0) {
    //                 randomCol = MAX_COLS - 1;
    //             } else if(randomCol == MAX_COLS - 1) {
    //                 randomCol = 1;
    //             }
                
    //             if(randomRow == 0) {
    //                 randomRow = MAX_ROWS - 1;
    //             }
    //             grid[randomRow][randomCol] = particle;
    //         }
    //     }
    // }
    
    
    
    // This function runs the and functionality where the user can draw water
    public void runWater(int randomRow, int randomCol, int getRandomDirection) {
        // int MAX_ROWS = 180;
        // int MAX_COLS = 150;
        // WATER TOOL
        if(grid[randomRow][randomCol] == WATER) {
            int randomRowDir = getRandomDirection == 0 ? 1 : 0;
            int randomColDir = getRandomDirection != 0 ? getRandomDirection : 0;
        
            grid[randomRow][randomCol] = EMPTY;
            randomRow = randomRow + randomRowDir > MAX_ROWS - 1 ? 0 : randomRow;
            if(randomCol + randomColDir > MAX_COLS - 1) randomCol = 0;
            if(randomCol + randomColDir < 0) randomCol = MAX_COLS - 1;
            
            
            int curr = grid[randomRow + randomRowDir][randomCol + randomColDir];
            boolean isCurrBelowEmpty = (curr != METAL && 
                                        curr != SAND && 
                                        curr != WATER && 
                                        curr != GEN && 
                                        curr != DESTRUCTOR && 
                                        curr != GLASS);
            if(grid[randomRow][randomCol] != METAL &&
            grid[randomRow][randomCol] != DESTRUCTOR &&
            grid[randomRow][randomCol] != GEN) {
                if(isCurrBelowEmpty) {
                    grid[randomRow][randomCol] = curr;
                    grid[randomRow + randomRowDir][randomCol + randomColDir] = WATER;
                } else {
                    grid[randomRow][randomCol] = WATER;
                }
            } else {
                if(randomCol == 0) {
                    randomCol = MAX_COLS -1;
                } else if(randomCol == MAX_COLS -1) {
                    randomCol = 0;
                }

                if(randomRow == 0) {
                    randomRow = MAX_ROWS - 1;
                }
                grid[randomRow][randomCol] = WATER;

            }
        }
    }
    
    public void runGenerator(int randomRow, int randomCol) {
        int i = 1;
        boolean overBoundary = (randomCol == 0 || 
                                randomRow == 0 || 
                                randomCol == MAX_COLS - 1 || 
                                randomRow == MAX_ROWS - 1);
        if(overBoundary) return ;
        while (grid[randomRow + i][randomCol] == GEN) 
            i++;
        if (grid[randomRow][randomCol] == GEN) {

            if(grid[0][randomCol] == GEN && grid[MAX_ROWS - 1][randomCol] != EMPTY) {
                    grid[randomRow + i][randomCol] = grid[MAX_ROWS -1][randomCol];
            } else {
                if (grid[randomRow - i][randomCol] != EMPTY && grid[randomRow - i][randomCol] != GEN) {
                    grid[randomRow + i][randomCol] = grid[randomRow - i][randomCol];
                }

            }
        }
    }


    public void runVape(int randomRow, int randomCol, int getRandomDirection) {
        if (grid[randomRow][randomCol] == VAPE) {
            int randomRowDir = getRandomDirection == 0 ? 1 : 0;
            int randomColDir = getRandomDirection != 0 ? getRandomDirection : 0;
        
            if (getRandomNumber(0,100) == 69) {
                grid[randomRow][randomCol] = EMPTY;
                return ;
            }
            
            grid[randomRow][randomCol] = EMPTY;
            if(randomRow - randomRowDir < 0) randomRow = MAX_ROWS - 1;
            if(randomRow - randomRowDir > MAX_ROWS - 1) randomRow = 0;
            if(randomCol - randomColDir > MAX_COLS - 1) randomCol = 0;
            if(randomCol - randomColDir < 0) randomCol = MAX_COLS - 1;

            int curr = grid[(randomRow - randomRowDir)][(randomCol - randomColDir)];
            if (curr != EMPTY) {
                grid[randomRow][randomCol] = EMPTY;
                return ;
            }
            if(curr == EMPTY) {
                grid[randomRow][randomCol] = curr;
                grid[(randomRow - randomRowDir)][(randomCol - randomColDir)] = VAPE;
            }
        }
                  
    }    

    public void runDestructor(int randomRow, int randomCol) {
        if (grid[randomRow][randomCol] == DESTRUCTOR) {
            if(randomRow - 1 < 0) {
                if(grid[0][randomCol] == DESTRUCTOR && grid[MAX_ROWS - 1][randomCol] != EMPTY) {
                grid[MAX_ROWS - 1][randomCol] = VAPE; 
            }
        }else {
                if(grid[randomRow - 1][randomCol] != DESTRUCTOR) {
                    if (grid[randomRow - 1][randomCol] != EMPTY) {
                        grid[randomRow - 1][randomCol] = VAPE;                
                    }
                }
            }
        }
    }
    
    // This function runs the and functionality where the user can draw oil
    public void runOil(int randomRow, int randomCol, int getRandomDirection) {
        // int MAX_ROWS = 180;
        // int MAX_COLS = 150;
        if(grid[randomRow][randomCol] == OIL) {
            int randomRowDir = getRandomDirection == 0 ? 1 : 0;
            int randomColDir = getRandomDirection != 0 ? getRandomDirection : 0;
            
            grid[randomRow][randomCol] = EMPTY;
            if(randomRow + randomRowDir > MAX_ROWS - 1) randomRow = 0;
            if(randomCol + randomColDir > MAX_COLS - 1) randomCol = 0;
            if(randomCol + randomColDir < 0) randomCol = MAX_COLS - 1;
            
            
            int curr = grid[randomRow + randomRowDir][randomCol + randomColDir];
            boolean isCurrBelowEmpty = (curr != METAL && 
                                        curr != SAND && 
                                        curr != OIL && 
                                        curr != WATER && 
                                        curr != GEN && 
                                        curr != DESTRUCTOR && 
                                        curr != GLASS);
            if(grid[randomRow][randomCol] != METAL &&
               grid[randomRow][randomCol] != GEN &&
               grid[randomRow][randomCol] != DESTRUCTOR) {
                if(isCurrBelowEmpty) {
                    grid[randomRow][randomCol] = curr;
                    grid[randomRow + randomRowDir][randomCol + randomColDir] = OIL;
                } else {
                    grid[randomRow][randomCol] = OIL;
                }
            } else {
                if(randomCol == 0) {
                    randomCol = MAX_COLS -1;
                } else if(randomCol == MAX_COLS -1) {
                    randomCol = 0;
                }
                
                if(randomRow == 0) {
                    randomRow = MAX_ROWS - 1;
                }
                grid[randomRow][randomCol] = OIL;

            }
        }
    }

    


    public void runFire(int randomRow, int randomCol, int getRandomDirection) {
        // int MAX_ROWS = 180;
        // int MAX_COLS = 150;
        if(grid[randomRow][randomCol] == FIRE) {
            int randomRowDir = getRandomDirection == 0 ? 1 : 0;
            int randomColDir = getRandomDirection != 0 ? getRandomDirection : 0;
            
            grid[randomRow][randomCol] = EMPTY;
            if(randomRow + randomRowDir > MAX_ROWS - 1) randomRow = 0;
            if(randomCol + randomColDir > MAX_COLS - 1) randomCol = 0;
            if(randomCol + randomColDir < 0) randomCol = MAX_COLS - 1;
            
            int curr = grid[randomRow + randomRowDir][randomCol + randomColDir];
            boolean isCurrBelowEmpty = (curr != METAL && 
                                        curr != FIRE && 
                                        curr != GEN && curr != DESTRUCTOR && 
                                        curr != GLASS);

            if(grid[randomRow][randomCol] != METAL &&
            grid[randomRow][randomCol] != DESTRUCTOR &&
            grid[randomRow][randomCol] != GEN) {
                if(isCurrBelowEmpty) {
                    if(curr == OIL) {
                        grid[randomRow][randomCol] = FIRE;
                        grid[randomRow + randomRowDir][randomCol + randomColDir] = FIRE;
                    } else if(curr == WATER) {
                        grid[randomRow][randomCol] = VAPE;
                        grid[randomRow + randomRowDir][randomCol + randomColDir] = VAPE;
                    } else if(curr == SAND) {
                        grid[randomRow + randomRowDir][randomCol + randomColDir] = GLASS;
                    } else {
                        grid[randomRow][randomCol] = curr;
                        grid[randomRow + randomRowDir][randomCol + randomColDir] = FIRE;
                    }
                } else {
                    grid[randomRow][randomCol] = FIRE;
                }
            }else {
                if(randomCol == 0) {
                    randomCol = MAX_COLS -1;
                } else if(randomCol == MAX_COLS -1) {
                    randomCol = 0;
                }
                
                if(randomRow == 0) {
                    randomRow = MAX_ROWS - 1;
                }
                grid[randomRow][randomCol] = FIRE;

            }
        }
        
    }
    
    
    //DO NOT modify anything below here!!! /////////////////////////////////////////////////////////////////
    public void run(){
        while (true){
            for (int i = 0; i < display.getSpeed(); i++)
                step();
            updateDisplay();
            display.repaint();
            display.pause(1);  //wait for redrawing and for mouse
            int[] mouseLoc = display.getMouseLocation();
            if (mouseLoc != null)  //test if mouse clicked
                locationClicked(mouseLoc[0], mouseLoc[1], display.getTool());
        }
    }
    
    public int getRandomNumber (int low, int high){
        return (int)(Math.random() * (high - low)) + low;
    }
}
