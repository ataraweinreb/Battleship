import java.util.Scanner;

public class Battleship
{
    public static Scanner reader = new Scanner(System.in);

    public static void main(String[] args)
    {
        System.out.println("Welcome to Tara's Battleship Game");

        System.out.println("\nPlayer SETUP:");
        Player userPlayer = new Player();
        setup(userPlayer);

        System.out.println("Computer setup is finished.");
        System.out.println("Please press ENTER to continue.");
        reader.nextLine();
        reader.nextLine();
        Player computer = new Player();
        setupComputer(computer);
        computer.playerGrid.printShips();

        String result = "";
        while(true)
        {
            System.out.println(result);
            System.out.println("\nUser makes a guess:");
            result = askForGuess(userPlayer, computer);

            if (userPlayer.playerGrid.hasLost())
            {
                System.out.println("Computer wins, user loses!");
                break;
            }
            else if (computer.playerGrid.hasLost())
            {
                System.out.println("User wins, computer loses!");
                break;
            }

            System.out.println("\nComputer is guessing...");


            compMakeGuess(computer, userPlayer);
        }
    }

    private static void compMakeGuess(Player comp, Player user)
    {
        Randomizer rand = new Randomizer();
        int row = rand.nextInt(0, 9);
        int col = rand.nextInt(0, 9);

        // If the computer has already guessed this position, computer guesses again.
        while (comp.oppGrid.alreadyGuessed(row, col))
        {
            row = rand.nextInt(0, 9);
            col = rand.nextInt(0, 9);
        }

        if (user.playerGrid.hasShip(row, col))
        {
            comp.oppGrid.markHit(row, col);
            user.playerGrid.markHit(row, col);
            System.out.println("Computer hit at " + convertIntToLetter(row) + convertCompColToRegular(col));
        }
        else
        {
            comp.oppGrid.markMiss(row, col);
            user.playerGrid.markMiss(row, col);
            System.out.println("Computer missed at " + convertIntToLetter(row) + convertCompColToRegular(col));
        }


        System.out.println("\nYour board... Please press ENTER to continue.");
        reader.nextLine();
        user.playerGrid.printCombined();
        System.out.println("Please press ENTER to continue.");
        reader.nextLine();
    }

    private static String askForGuess(Player p, Player opp)
    {
        System.out.println("Viewing my guesses:");
        p.oppGrid.printStatus();

        int row = -1;
        int col = -1;

        String oldRow = "Z";
        int oldCol = -1;

        while(true)
        {
            System.out.print("Type in a row (A-J): ");
            String userInputRow = reader.next();
            userInputRow = userInputRow.toUpperCase();
            oldRow = userInputRow;
            row = convertLetterToInt(userInputRow);

            System.out.print("Type in a column (1-10): ");
            col = reader.nextInt();
            oldCol = col;
            col = convertUserColToProCol(col);

            if (col >= 0 && col <= 9 && row != -1)
                break;

            System.out.println("Invalid location!");
        }

        if (opp.playerGrid.hasShip(row, col))
        {
            p.oppGrid.markHit(row, col);
            opp.playerGrid.markHit(row, col);
            return "** User hit at " + oldRow + oldCol + " **";
        }
        else
        {
            p.oppGrid.markMiss(row, col);
            opp.playerGrid.markMiss(row, col);
            return "** User missed at " + oldRow + oldCol + " **";
        }
    }

    private static void setup(Player p)
    {
        p.playerGrid.printShips();
        System.out.println();
        int counter = 1;
        int normCounter = 0;
        while (p.numOfShipsLeft() > 0)
        {
            for (Ship s: p.ships)
            {
                System.out.println("\nShip #" + counter + ": Length-" + s.getLength());
                int row = -1;
                int col = -1;
                int dir = -1;
                while(true)
                {
                    System.out.print("Type in a row (A-J): ");
                    String userInputRow = reader.next();
                    userInputRow = userInputRow.toUpperCase();
                    row = convertLetterToInt(userInputRow);

                    System.out.print("Type in a column (1-10): ");
                    col = reader.nextInt();
                    col = convertUserColToProCol(col);

                    System.out.print("Type in a direction (0-H, 1-V): ");
                    dir = reader.nextInt();

                    //check for invalid inputs
                    if (col >= 0 && col <= 9 && row != -1 && dir != -1)
                    {
                        if (!hasErrors(row, col, dir, p, normCounter))
                        {
                            break;
                        }
                    }

                    System.out.println("Invalid location!");
                }

                p.ships[normCounter].setLocation(row, col);
                p.ships[normCounter].setDirection(dir);
                p.playerGrid.addShip(p.ships[normCounter]);
                p.playerGrid.printShips();
                System.out.println();
                System.out.println("You have " + p.numOfShipsLeft() + " remaining ships to place.");

                normCounter++;
                counter++;
            }
        }
    }

    private static void setupComputer(Player p)
    {
        System.out.println();
        int counter = 1;
        int normCounter = 0;

        Randomizer rand = new Randomizer();

        while (p.numOfShipsLeft() > 0)
        {
            for (Ship s: p.ships)
            {
                int row = rand.nextInt(0, 9);
                int col = rand.nextInt(0, 9);
                int dir = rand.nextInt(0, 1);


                while (hasErrorsComp(row, col, dir, p, normCounter))
                {
                    row = rand.nextInt(0, 9);
                    col = rand.nextInt(0, 9);
                    dir = rand.nextInt(0, 1);
                }

                p.ships[normCounter].setLocation(row, col);
                p.ships[normCounter].setDirection(dir);
                p.playerGrid.addShip(p.ships[normCounter]);

                normCounter++;
                counter++;
            }
        }
    }

    private static boolean hasErrors(int row, int col, int dir, Player p, int count)
    {
        int length = p.ships[count].getLength();

        //Check if the ship is placed out of bounds.
        if (dir == 0)
        {
            int checker = length + col;
            if (checker > 10)
            {
                System.out.println("SHIP DOES NOT FIT!");
                return true;
            }
        }

        //Check if the ship is placed out of bounds.
        if (dir == 1)
        {
            int checker = length + row;
            if (checker > 10)
            {
                System.out.println("SHIP DOES NOT FIT!");
                return true;
            }
        }

        // Check if overlapping with another ship
        if (dir == 0)
        {
            for (int i = col; i < col+length; i++)
            {
                if(p.playerGrid.hasShip(row, i))
                {
                    System.out.println("ERROR! There is already another ship in this location.");
                    return true;
                }
            }
        }
        else if (dir == 1)
        {
            for (int i = row; i < row+length; i++)
            {
                if(p.playerGrid.hasShip(i, col))
                {
                    System.out.println("ERROR! There is already another ship in this location.");
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean hasErrorsComp(int row, int col, int dir, Player p, int count)
    {
        int length = p.ships[count].getLength();

        if (dir == 0) //check if out of bounds
        {
            int checker = length + col;
            if (checker > 10)
            {
                return true;
            }
        }

        if (dir == 1)
        {
            int checker = length + row;
            if (checker > 10)
            {
                return true;
            }
        }

        // Check if ships overlap
        if (dir == 0)
        {
            for (int i = col; i < col+length; i++)
            {
                if(p.playerGrid.hasShip(row, i))
                {
                    return true;
                }
            }
        }
        else if (dir == 1)
        {
            for (int i = row; i < row+length; i++)
            {
                if(p.playerGrid.hasShip(i, col))
                {
                    return true;
                }
            }
        }

        return false;
    }

    private static int convertLetterToInt(String val)
    {
        int toReturn = -1;
        switch (val)
        {
            case "A":   toReturn = 0;
                break;
            case "B":   toReturn = 1;
                break;
            case "C":   toReturn = 2;
                break;
            case "D":   toReturn = 3;
                break;
            case "E":   toReturn = 4;
                break;
            case "F":   toReturn = 5;
                break;
            case "G":   toReturn = 6;
                break;
            case "H":   toReturn = 7;
                break;
            case "I":   toReturn = 8;
                break;
            case "J":   toReturn = 9;
                break;
            default:    toReturn = -1;
                break;
        }

        return toReturn;
    }

    private static String convertIntToLetter(int val)
    {
        String toReturn = "Z";
        switch (val)
        {
            case 0:   toReturn = "A";
                break;
            case 1:   toReturn = "B";
                break;
            case 2:   toReturn = "C";
                break;
            case 3:   toReturn = "D";
                break;
            case 4:   toReturn = "E";
                break;
            case 5:   toReturn = "F";
                break;
            case 6:   toReturn = "G";
                break;
            case 7:   toReturn = "H";
                break;
            case 8:   toReturn = "I";
                break;
            case 9:   toReturn = "J";
                break;
            default:    toReturn = "Z";
                break;
        }

        return toReturn;
    }

    private static int convertUserColToProCol(int val)
    {
        int toReturn = -1;
        switch (val)
        {
            case 1:   toReturn = 0;
                break;
            case 2:   toReturn = 1;
                break;
            case 3:   toReturn = 2;
                break;
            case 4:   toReturn = 3;
                break;
            case 5:   toReturn = 4;
                break;
            case 6:   toReturn = 5;
                break;
            case 7:   toReturn = 6;
                break;
            case 8:   toReturn = 7;
                break;
            case 9:   toReturn = 8;
                break;
            case 10:   toReturn = 9;
                break;
            default:    toReturn = -1;
                break;
        }

        return toReturn;
    }

    private static int convertCompColToRegular(int val)
    {
        int toReturn = -1;
        switch (val)
        {
            case 0:   toReturn = 1;
                break;
            case 1:   toReturn = 2;
                break;
            case 2:   toReturn = 3;
                break;
            case 3:   toReturn = 4;
                break;
            case 4:   toReturn = 5;
                break;
            case 5:   toReturn = 6;
                break;
            case 6:   toReturn = 7;
                break;
            case 7:   toReturn = 8;
                break;
            case 8:   toReturn = 9;
                break;
            case 9:   toReturn = 10;
                break;
            default:    toReturn = -1;
                break;
        }

        return toReturn;
    }
}
