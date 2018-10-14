/*********************************************************************************************************************
  * SandLabFiles.java
  *********************************************************************************************************************/
import java.io.*;
import java.util.*;

public class SandLabFiles {
   static FileWriter  fw;
   static PrintWriter pw;
   
   static FileReader fr;
   static BufferedReader br;
   
   static int nbrOfRows = 220;
   static int nbrOfCols = 180;
  
   
   public static void main(String[] args) throws IOException {
      System.out.println("SandLabFiles.main");
      readFile("SandLabFile.txt");
   }
   
   public static void writeFile(int [][] sandLabArray, String fileName)  {
      System.out.println("Starting SandLabFiles.writeFile()");
      try {
         fw = new FileWriter(fileName, false);
         pw = new PrintWriter(fw);
         
         for (int r = 0; r < nbrOfRows; r++) {
            for (int c = 0; c < nbrOfCols; c++) {
               pw.print(sandLabArray[r][c]);
            }
            pw.println();
         }
         pw.flush();
         pw.close();
         System.out.println("SandLabFiles.writeFile() is complete");
      }
      catch (Exception e) {
         System.err.println("Error writing file. Ending program.");
         System.exit(-1);
      } 
   }
   
   public static void setRowsAndCols(int rows, int cols){
      nbrOfRows = rows;
      nbrOfCols = cols;
   }
   
   //////////////////////////////////////////////////////////////////////////////////////////
   
   public static int[][] readFile(String fileName) {
      int [][] grid = new int[nbrOfRows][nbrOfCols]; 
      String value = "";
      
      try { 
         fr = new FileReader(fileName);
         br = new BufferedReader(fr);
         
         for (int r = 0; r < nbrOfRows; r++) {
            value = br.readLine();
            for (int c = 0; c < nbrOfCols; c++) {
               grid[r][c] = Character.getNumericValue(value.charAt(c));
            }
         }
         
         br.close(); 
      }
      catch (Exception e) {
         System.err.println("Error writing file. Ending program.");
         System.exit(-1);
      } 
      return grid;      
   }
}