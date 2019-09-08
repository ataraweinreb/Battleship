public class Grid
{
    private Location[][] grid;
    private int points;

    public static final int NUM_ROWS = 10;
    public static final int NUM_COLS = 10;
    
    public Grid()
    {
        if (NUM_ROWS > 26)
        {
            throw new IllegalArgumentException("ERROR! NUM_ROWS CANNOT BE > 26");
        }
        
        grid = new Location[NUM_ROWS][NUM_COLS];
        
        for (int row = 0; row < grid.length; row++)
        {
            for (int col = 0; col < grid[row].length; col++)
            {
                Location tempLoc = new Location();
                grid[row][col] = tempLoc;
            }
        }
    }

    public void markHit(int row, int col)
    {
        grid[row][col].markHit();
        points++;
    }

    public void markMiss(int row, int col)
    {
        grid[row][col].markMiss();
    }

    public void setStatus(int row, int col, int status)
    {
        grid[row][col].setStatus(status);
    }

    public int getStatus(int row, int col)
    {
        return grid[row][col].getStatus();
    }

    public boolean alreadyGuessed(int row, int col)
    {
        return !grid[row][col].isUnguessed();
    }

    public void setShip(int row, int col, boolean val)
    {
        grid[row][col].setShip(val);
    }

    public boolean hasShip(int row, int col)
    {
        return grid[row][col].hasShip();
    }

    public Location get(int row, int col)
    {
        return grid[row][col];
    }

    public int numRows()
    {
        return NUM_ROWS;
    }

    public int numCols()
    {
        return NUM_COLS;
    }
    
    public void printStatus()
    {
        generalPrintMethod(0);
    }
    
    public void printShips()
    {
        generalPrintMethod(1);
    }
    
    public void printCombined()
    {
        generalPrintMethod(2);
    }
    
    public boolean hasLost()
    {
        if (points >= 17)
            return true;
        else
            return false;
    }
    
    public void addShip(Ship s)
    {
        int row = s.getRow();
        int col = s.getCol();
        int length = s.getLength();
        int dir = s.getDirection();
        
        if (!(s.isDirectionSet()) || !(s.isLocationSet()))
            throw new IllegalArgumentException("ERROR! direction or location is unset.");

        if (dir == 0)
        {
            for (int i = col; i < col+length; i++)
            {
                grid[row][i].setShip(true);
                grid[row][i].setLengthOfShip(length);
                grid[row][i].setDirectionOfShip(dir);
            }
        }
        else if (dir == 1)
        {
            for (int i = row; i < row+length; i++)
            {
                grid[i][col].setShip(true);
                grid[i][col].setLengthOfShip(length);
                grid[i][col].setDirectionOfShip(dir);
            }
        }
    }
    
    // 0 for status, 1 for ships, 2 for combined.
    private void generalPrintMethod(int type)
    {
        System.out.println();
        System.out.print("  ");
        for (int i = 1; i <= NUM_COLS; i++)
        {
            System.out.print(i + " ");
        }
        System.out.println();

        int endLetterForLoop = (NUM_ROWS - 1) + 65;
        for (int i = 65; i <= endLetterForLoop; i++)
        {
            char theChar = (char) i;
            System.out.print(theChar + " ");
            
            for (int j = 0; j < NUM_COLS; j++)
            {
                if (type == 0)
                {
                    if (grid[switchCounterToIntegerForArray(i)][j].isUnguessed())
                        System.out.print("- ");
                    else if (grid[switchCounterToIntegerForArray(i)][j].checkMiss())
                        System.out.print("O ");
                    else if (grid[switchCounterToIntegerForArray(i)][j].checkHit())
                        System.out.print("X ");
                }
                else if (type == 1)
                {
                    if (grid[switchCounterToIntegerForArray(i)][j].hasShip())
                    {
                        if (grid[switchCounterToIntegerForArray(i)][j].getLengthOfShip() == 2)
                        {
                            System.out.print("D ");
                        }
                        else if (grid[switchCounterToIntegerForArray(i)][j].getLengthOfShip() == 3)
                        {
                            System.out.print("C ");
                        }
                        else if (grid[switchCounterToIntegerForArray(i)][j].getLengthOfShip() == 4)
                        {
                            System.out.print("B ");
                        }
                        else if (grid[switchCounterToIntegerForArray(i)][j].getLengthOfShip() == 5)
                        {
                            System.out.print("A ");
                        }
                    }
                        
                    else if (!(grid[switchCounterToIntegerForArray(i)][j].hasShip()))
                    {
                        System.out.print("- ");
                    }
                        
                }
                else
                {
                    if (grid[switchCounterToIntegerForArray(i)][j].checkHit())
                        System.out.print("X ");
                    else if (grid[switchCounterToIntegerForArray(i)][j].hasShip())
                    {
                        if (grid[switchCounterToIntegerForArray(i)][j].getLengthOfShip() == 2)
                        {
                            System.out.print("D ");
                        }
                        else if (grid[switchCounterToIntegerForArray(i)][j].getLengthOfShip() == 3)
                        {
                            System.out.print("C ");
                        }
                        else if (grid[switchCounterToIntegerForArray(i)][j].getLengthOfShip() == 4)
                        {
                            System.out.print("B ");
                        }
                        else if (grid[switchCounterToIntegerForArray(i)][j].getLengthOfShip() == 5)
                        {
                            System.out.print("A ");
                        }
                    }
                    else if (!(grid[switchCounterToIntegerForArray(i)][j].hasShip()))
                    {
                        System.out.print("- ");
                    }  
                }
            }
            System.out.println();
        }
    }
    
    public int switchCounterToIntegerForArray (int val)
    {
        int toReturn = -1;
        switch (val)
        {
            case 65:    toReturn = 0;
                        break;
            case 66:    toReturn = 1;
                        break;
            case 67:    toReturn = 2;
                        break;
            case 68:    toReturn = 3;
                        break;
            case 69:    toReturn = 4;
                        break;
            case 70:    toReturn = 5;
                        break;
            case 71:    toReturn = 6;
                        break;
            case 72:    toReturn = 7;
                        break;
            case 73:    toReturn = 8;
                        break;
            case 74:    toReturn = 9;
                        break;
            case 75:    toReturn = 10;
                        break;
            case 76:    toReturn = 11;
                        break;
            case 77:    toReturn = 12;
                        break;
            case 78:    toReturn = 13;
                        break;
            case 79:    toReturn = 14;
                        break;
            case 80:    toReturn = 15;
                        break;
            case 81:    toReturn = 16;
                        break;
            case 82:    toReturn = 17;
                        break;
            case 83:    toReturn = 18;
                        break;
            case 84:    toReturn = 19;
                        break;
            case 85:    toReturn = 20;
                        break;
            case 86:    toReturn = 21;
                        break;
            case 87:    toReturn = 22;
                        break;
            case 88:    toReturn = 23;
                        break;
            case 89:    toReturn = 24;
                        break;
            case 90:    toReturn = 25;
                        break;
            default:    toReturn = -1;
                        break;
        }
        
        if (toReturn == -1)
        {
            throw new IllegalArgumentException("ERROR happened in switch.");
        }
        else
        {
            return toReturn;
        }
    }   
}
