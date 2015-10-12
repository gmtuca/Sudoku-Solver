import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Set;
import java.util.HashSet;

public class SudokuSolver
{
  private static int[][] initialSudoku = new int[9][9];
  private static int[][] solvingSudoku = new int[9][9];
  private static Set<Integer>[][] prohibitedNumber = new Set[9][9];

  public static void main(String[] args) throws Exception
  {
    BufferedReader input = new BufferedReader(new FileReader(args[0]));

    String[] lineString;
    for(int row = 0; row < 9; row++)
    {
      lineString = input.readLine().split(" ");

      for(int col = 0; col < 9; col++)
        initialSudoku[row][col] = Integer.parseInt(lineString[col]);
    }//for

    for(int row = 0; row < 9; row++)
      for(int col = 0; col < 9; col++)
        solvingSudoku[row][col] = initialSudoku[row][col];

    for(int row = 0; row < 9; row++)
      for(int col = 0; col < 9; col++) 
        prohibitedNumber[row][col] = new HashSet<Integer>();

    solve(0,0);
    printResult(solvingSudoku);
  }//main

  public static boolean checkRow(int guess, int rowNumber)
  {
    for(int col = 0; col < 9; col++)
      if(solvingSudoku[rowNumber][col] == guess)
        return false;

    return true;
  }//checkRow

  public static boolean checkCol(int guess, int colNumber)
  {
    for(int row = 0; row < 9; row++)
      if(solvingSudoku[row][colNumber] == guess)
        return false;

    return true;
  }//checkCol

  public static boolean checkBox(int guess, int rowNumber, int colNumber)
  {
    int boxRow = (rowNumber / 3) * 3 ;
    int boxCol = (colNumber / 3) * 3 ;

    for(int r = 0; r < 3; r++)
     for(int c = 0; c < 3; c++)
       if(solvingSudoku[boxRow+r][boxCol+c] == guess)
          return false;

    return true;
  }//checkBox

  public static boolean checkProhibition(int guess,
                                         int rowNumber, int colNumber)
  {
    if(prohibitedNumber[rowNumber][colNumber].contains(guess))
      return false;

    return true;
  }//checkProhibition

  public static void solve(int rowNumber, int colNumber)
  {
    if(rowNumber > 8)
      return;

    if(initialSudoku[rowNumber][colNumber] == 0)
    {
      for(int guess = 1; guess <= 9; guess++)
        if(checkRow(guess,rowNumber)
           && checkCol(guess,colNumber)
             && checkBox(guess,rowNumber,colNumber)
               && checkProhibition(guess,rowNumber,colNumber))
        {
          solvingSudoku[rowNumber][colNumber] = guess;
          break;
        }//if

      if(solvingSudoku[rowNumber][colNumber] == 0)
        previousField(rowNumber,colNumber);
      else
        nextField(rowNumber,colNumber);
    }//if
    else
      nextField(rowNumber, colNumber);
  }//solve

  public static void nextField(int rowNumber, int colNumber)
  {
    //printResult(solvingSudoku);
    //System.out.println("next (" + rowNumber + "," + colNumber + ")");

    if(colNumber == 8)
      solve(rowNumber + 1, 0); 
    else
      solve(rowNumber, colNumber + 1);
  }//nextField

  public static void previousField(int rowNumber, int colNumber)
  {
    //printResult(solvingSudoku);
    //System.out.println("prev(" + rowNumber + "," + colNumber + ")");
    int prevRowNumber = rowNumber;
    int prevColNumber = colNumber;

    if(colNumber == 0)
    {
      prevColNumber = 8;
      prevRowNumber--;
    }//if
    else
      prevColNumber--;

    prohibitedNumber[rowNumber][colNumber] = new HashSet<Integer>();

    if(initialSudoku[prevRowNumber][prevColNumber] != 0)
      previousField(prevRowNumber,prevColNumber);
    else
    {
      prohibitedNumber[prevRowNumber][prevColNumber].add
                    (new Integer(solvingSudoku[prevRowNumber][prevColNumber]));
      solvingSudoku[prevRowNumber][prevColNumber] = 0;

      //System.out.println("(" + prevRowNumber + "," + prevColNumber + ") - "
      //+ prohibitedNumber[prevRowNumber][prevColNumber]);

      solve(prevRowNumber,prevColNumber);
    }//else
  }//previousField

  public static void printResult(int[][] solvedSudoku)
  {
    for(int row = 0; row < 9; row++)
    {
      System.out.println();

      if(row > 0 && row % 3 == 0)
        System.out.println(" - - - | - - - | - - -");

      for(int col = 0; col < 9; col++)
      {
        if(col > 0 && col % 3 == 0)
          System.out.print(" |");

        System.out.print(" " + solvedSudoku[row][col]);

      }//for
    }//for

    System.out.println();
    System.out.println();

  }//printResult

}//SudokuSolver
