import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Scanner;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.*;
import java.net.InetAddress;

/**
 * Code for HW5
 * This class process ameshousing.csv
 * @author Ram
 */
public class AmesHousingDB {
public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException{
		
	    SimpleDataSource.init("/Users/ram/Desktop/anku/database.properties");
	    
	    try(Connection conn = SimpleDataSource.getConnection();Statement stat = conn.createStatement()) 
	    {
	    	try{
	           stat.execute("DROP TABLE House");
	        }
	        catch(SQLException e){
				  // get exception if table doesn't exist yet
				  e.printStackTrace();
	        }  
	    	stat.execute("CREATE TABLE House (PID INT, Neighborhood VARCHAR(20), YearBuilt INT, SQFT INT, NumBeds INT, NumBaths Decimal(6,1), Price INT)");
	    
	    	Scanner in = new Scanner(new File("/Users/ram/Desktop/anku/ameshousing.csv")); // Reading "amesrestaurants.csv"
	    	in.nextLine(); // skip header of the csv file
            while (in.hasNextLine()){ // reading the csv file line by line
				
				String line = in.nextLine();
				String[] linewords = line.split(",");
				
				String pid = linewords[0];
				String neighb = linewords[1];
				String yr = linewords[2];
				String sq = linewords[3];
				String numbd = linewords[4];
				String numba = linewords[5];
				String price = linewords[6];

				// Prepare the SQL statement in String format to insert the current house
				String sql = "INSERT INTO House(PID, Neighborhood, YearBuilt, SQFT, NumBeds, NumBaths, Price) "+"VALUES(?,?,?,?,?,?,?)";
				// Execute the SQL statement
				PreparedStatement myStmt = conn.prepareStatement(sql);
				myStmt.setString(1,pid);
				myStmt.setString(2,neighb);
				myStmt.setString(3,yr);
				myStmt.setString(4,sq);
				myStmt.setString(5,numbd);
				myStmt.setString(6,numba);
				myStmt.setString(7,price);
				myStmt.executeUpdate();
			}

            in.close();
	    	
            System.out.println("##############################################################");
    		System.out.println("########           AMES House Search Program          ########");
    		System.out.println("##############################################################");
            
            Scanner console = new Scanner(System.in);
            boolean continueProgram = true;
            while(continueProgram){
          	  System.out.println("Select from the following options");
          	  System.out.println("(Q) Quit");
          	  System.out.println("(A) Add a house");
          	  System.out.println("(C) Calculate average house price");
          	  System.out.println("(P) Print a subset of the houses based on SQFT");
          	  System.out.println("(B) Print a subset of the houses based on numBeds and numBaths");
          	  String select = console.next();
          	  
          	  if(select.equals("Q")){ // Q option is complete. No need to change.
          		  continueProgram = false;
          	  }else if(select.equals("A")){ // Complete A option
				System.out.print("PID:"); 	
				Integer pid  = console.nextInt();
				System.out.print("Neighborhood:"); 
					String neighb = console.next();
					System.out.print("YearBuilt:"); 
					Integer yr = console.nextInt();
					System.out.print("SQFT:"); 
					Integer sq = console.nextInt();
					System.out.print("NumBeds:"); 
					Integer numbd = console.nextInt();
					System.out.print("NumBaths:"); 
					Float numba = console.nextFloat();
					System.out.print("Price:"); 
					Integer price = console.nextInt();
					String sql = "INSERT INTO House(PID, Neighborhood, YearBuilt, SQFT, NumBeds, NumBaths, Price) "+"VALUES(?,?,?,?,?,?,?)";
				// Execute the SQL statement
				PreparedStatement myStmt = conn.prepareStatement(sql);
				myStmt.setInt(1,pid);
				myStmt.setString(2,neighb);
				myStmt.setInt(3,yr);
				myStmt.setInt(4,sq);
				myStmt.setInt(5,numbd);
				myStmt.setFloat(6,numba);
				myStmt.setInt(7,price);
				   myStmt.executeUpdate();
				   
          	  }else if(select.equals("C")){ // Complete C option
					String sql = "SELECT AVG(Price) FROM HOUSE";
					Statement stmt = conn.createStatement();
					try{
						ResultSet result = stmt.executeQuery(sql);
						if(result.next()){
						String s = result.getString(1);
						System.out.println("Average price in Ames:" +s);
						}
					}
					catch(SQLException e){
						e.printStackTrace();
				  }

          		  
          	  }else if(select.equals("P")){ // Complete P option
				System.out.print("Lower bound on sqft:");  
				Integer sqf = console.nextInt();
				String sql = "SELECT * FROM HOUSE WHERE SQFT>"+sqf;
				Statement stmt = conn.createStatement();
				try{
					ResultSet result = stmt.executeQuery(sql);
					while(result.next()){
					String id = result.getString(1);
					String n = result.getString(2);
					String y = result.getString(3);
					String s = result.getString(4);
					String nb = result.getString(5);
					String na = result.getString(6);
					String p = result.getString(7);
					System.out.println(id+" "+n+" "+y+" "+s+" "+nb+" "+na+" "+p);
					}
				}
				catch(SQLException e){
					e.printStackTrace();
			  }

          		  
          		  
			  }else if(select.equals("B")){ // Complete B option
				System.out.print("Lower bound on Beds:");  
				Integer bds = console.nextInt();
				System.out.print("Lower bound on Baths:");  
				Integer bts = console.nextInt();
				String sql = "SELECT * FROM HOUSE WHERE NumBeds>"+bds+"AND NumBaths>"+bts;
				Statement stmt = conn.createStatement();
				try{
					ResultSet result = stmt.executeQuery(sql);
					while(result.next()){
					String id = result.getString(1);
					String n = result.getString(2);
					String y = result.getString(3);
					String s = result.getString(4);
					String nb = result.getString(5);
					String na = result.getString(6);
					String p = result.getString(7);
					System.out.println(id+" "+n+" "+y+" "+s+" "+nb+" "+na+" "+p);
					}
				}
				catch(SQLException e){
					e.printStackTrace();
			  }
        		  
        		  
        		  
      	      }else { // No need to change. If not one of Q,A,C,P,B, then print error message.
      	    	  System.out.println("Incorrect menu. Select again.");
      	      }
          	  
          	  System.out.println();
          	  System.out.println();
            }
            
            console.close();
	    }
		
		
	}

}
