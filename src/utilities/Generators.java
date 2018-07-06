package utilities;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Generators {
	
	
	public static ArrayList<Passenger> passengerGenerator (String distribution, int passengerNumber, ArrayList<Operator> operatorList, double xLimit, double yLimit){
		
		ArrayList<Passenger> passengers = new ArrayList<Passenger>();
				
		if (distribution.equals("N"))
		{
			int passIdCounter = 1; // this is to have a continues unique ID for passengers
			for (int o = 0 ; o < operatorList.size() ; o++)
			{
				for (int i = 0 ; i < passengerNumber * operatorList.get(o).getMarketShare(); i++ )
				{
					Random rnd = new Random();
					Point passengerCoord = new Point();
					passengerCoord.setLocation((rnd.nextGaussian()*0.125 + 0.5) * xLimit, (rnd.nextGaussian()*0.125 + 0.5) * yLimit);
					Passenger tempPassenger = new Passenger (passIdCounter , passengerCoord, 0.0, 0, 0, -1, 0);
					passengers.add(tempPassenger);
					passIdCounter++ ;
				}
			}
		}
		
		else if (distribution.equals("U"))
		{
			int passIdCounter = 1; // this is to have a continues unique ID for passengers
			for (int o = 0 ; o < operatorList.size() ; o++)
			{
				for (int i = 0 ; i < passengerNumber * operatorList.get(o).getMarketShare(); i++ )
				{
					Random rnd = new Random();
					Point passengerCoord = new Point();
					passengerCoord.setLocation(rnd.nextDouble() * xLimit, rnd.nextDouble() * yLimit);
					Passenger tempPassenger = new Passenger (passIdCounter , passengerCoord, 0.0, 0, 0, -1, operatorList.get(o).getCode());
					passengers.add(tempPassenger);
					passIdCounter++ ;
				}
			}
		}
				
		return passengers;
	}
	
//======================================================================================================================================
	
	public static ArrayList<Vehicle> vehicleGenerator (ArrayList<Vehicle> vehicles, String distribution, int [] vehAddedInIteration,
			ArrayList<Operator> operatorList, double xLimit, double yLimit, int vehicleCapacity){
		
		if (distribution.equals("N"))
		{			
			int startingID = -1; // this is necessary to have unique id for vehicles through all iterations
			if (vehicles.size() > 0)
				startingID = vehicles.get(vehicles.size()- 1).id;
			int vehicleIdCounter = startingID + 1 ;
			for (int o = 0 ; o < operatorList.size() ; o++)
			{
				for (int i = 0 ; i < vehAddedInIteration[o] ; i++ )
				{
					Random rnd = new Random();
					Point vehicleCoord = new Point();
					vehicleCoord.setLocation((rnd.nextGaussian()*0.125 + 0.5) * xLimit, (rnd.nextGaussian()*0.125 + 0.5) * yLimit);
					Vehicle tempVehicle = new Vehicle (vehicleIdCounter, vehicleCoord, 0.0, 0, vehicleCapacity, operatorList.get(o).getCode(), 1);
					vehicles.add(tempVehicle);
					vehicleIdCounter++ ;
				}
			}
		}
		
		else if (distribution.equals("U"))
		{
			int startingID = -1; // this is necessary to have unique id for vehicles through all iterations
			if (vehicles.size() > 0)
				startingID = vehicles.get(vehicles.size()- 1).id;
			int vehicleIdCounter = startingID + 1 ;
			for (int o = 0 ; o < operatorList.size() ; o++)
			{
				for (int i = 0 ; i < vehAddedInIteration[o] ; i++ )
				{
					Random rnd = new Random();
					Point vehicleCoord = new Point();
					vehicleCoord.setLocation(rnd.nextDouble() * xLimit, rnd.nextDouble() * yLimit);
					Vehicle tempVehicle = new Vehicle (vehicleIdCounter, vehicleCoord, 0.0, 0, vehicleCapacity, operatorList.get(o).getCode(), 1);
					vehicles.add(tempVehicle);
					vehicleIdCounter++ ;
				}
			}
		}
		
		return vehicles;
	}
	
	public static ArrayList<Vehicle> vehicleLocalGenerator (ArrayList<Vehicle> vehicles, String distribution, int [] vehAddedInIteration,
			ArrayList<Operator> operatorList, double xLimit, double yLimit, int vehicleCapacity){

		if (distribution.equals("N"))
		{			
			int startingID = -1; // this is necessary to have unique id for vehicles through all iterations
			if (vehicles.size() > 0)
				startingID = vehicles.get(vehicles.size()- 1).id;
			int vehicleIdCounter = startingID + 1 ;
			double startPoint = 0;
			for (int o = 0 ; o < operatorList.size() ; o++)
			{
				double xLimitPartial = operatorList.get(o).getMarketShare() * xLimit;

				for (int i = 0 ; i < vehAddedInIteration[o] ; i++ )
				{
					Random rnd = new Random();
					Point vehicleCoord = new Point();
					vehicleCoord.setLocation((rnd.nextGaussian()*0.125 + 0.5) * xLimitPartial + startPoint, (rnd.nextGaussian()*0.125 + 0.5) * yLimit);
					Vehicle tempVehicle = new Vehicle (vehicleIdCounter, vehicleCoord, 0.0, 0, vehicleCapacity, operatorList.get(o).getCode(), 1);
					vehicles.add(tempVehicle);
					vehicleIdCounter++ ;
				}
				startPoint += xLimitPartial;
			}
		}
		
		else if (distribution.equals("U"))
		{
			int startingID = -1; // this is necessary to have unique id for vehicles through all iterations
			if (vehicles.size() > 0)
				startingID = vehicles.get(vehicles.size()- 1).id;
			int vehicleIdCounter = startingID + 1 ;
			double startPoint = 0;
			for (int o = 0 ; o < operatorList.size() ; o++)
			{
				double xLimitPartial = operatorList.get(o).getMarketShare() * xLimit;
				for (int i = 0 ; i < vehAddedInIteration[o] ; i++ )
				{
					Random rnd = new Random();
					Point vehicleCoord = new Point();
					vehicleCoord.setLocation(rnd.nextDouble() * xLimitPartial + startPoint, rnd.nextDouble() * yLimit);
					Vehicle tempVehicle = new Vehicle (vehicleIdCounter, vehicleCoord, 0.0, 0, vehicleCapacity, operatorList.get(o).getCode(), 1);
					vehicles.add(tempVehicle);
					vehicleIdCounter++ ;
				}
				startPoint += xLimitPartial;
			}
		}
		
		return vehicles;
	}
	
	public static ArrayList<Vehicle> vehiclePolarGenerator (ArrayList<Vehicle> vehicles, String distribution, int [] vehAddedInIteration,
			ArrayList<Operator> operatorList, double xLimit, double yLimit, int vehicleCapacity){

		
		if (distribution.equals("N"))
		{			
			int startingID = -1; // this is necessary to have unique id for vehicles through all iterations
			if (vehicles.size() > 0)
				startingID = vehicles.get(vehicles.size()- 1).id;
			int vehicleIdCounter = startingID + 1 ;
			double startPoint = 0;
			for (int o = 0 ; o < operatorList.size() ; o++)
			{
				double tetha = Math.toRadians(360 * operatorList.get(o).getMarketShare());
				
				for (int i = 0 ; i < vehAddedInIteration[o] ; i++ )
				{
					Random rnd = new Random();
					Point vehicleCoord = new Point();
					double alpha = Math.random() * tetha + startPoint;
					double radius = Math.abs(rnd.nextGaussian() * 0.125 ) * xLimit ;
					vehicleCoord.setLocation(radius * Math.cos(alpha) + xLimit/2 , radius * Math.sin(alpha) + yLimit/2 );
					Vehicle tempVehicle = new Vehicle (vehicleIdCounter, vehicleCoord, 0.0, 0, vehicleCapacity, operatorList.get(o).getCode(), 1);
					vehicles.add(tempVehicle);
					vehicleIdCounter++ ;
				}
				startPoint += tetha; 
			}
		}
		
		else if (distribution.equals("U"))
		{
			int startingID = -1; // this is necessary to have unique id for vehicles through all iterations
			if (vehicles.size() > 0)
				startingID = vehicles.get(vehicles.size()- 1).id;
			int vehicleIdCounter = startingID + 1 ;
			double startPoint = 0;
			for (int o = 0 ; o < operatorList.size() ; o++)
			{
				double tetha = Math.toRadians(360 * operatorList.get(o).getMarketShare());
				
				for (int i = 0 ; i < vehAddedInIteration[o] ; i++ )
				{
					Random rnd = new Random();
					Point vehicleCoord = new Point();
					double alpha = Math.random() * tetha + startPoint;
					double radius = rnd.nextDouble() * xLimit/2 ;
					vehicleCoord.setLocation(radius * Math.cos(alpha) + xLimit/2 , radius * Math.sin(alpha) + yLimit/2 );
					Vehicle tempVehicle = new Vehicle (vehicleIdCounter, vehicleCoord, 0.0, 0, vehicleCapacity, operatorList.get(o).getCode(), 1);
					vehicles.add(tempVehicle);
					vehicleIdCounter++ ;
				}
				startPoint += tetha;
			}
		}
		
		return vehicles;
	}
		

}//end of CompetitionMethods
