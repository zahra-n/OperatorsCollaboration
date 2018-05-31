package utilities;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

public class Methods {
	
	public static ArrayList<Passenger> defaultOpDinfer (ArrayList<Passenger> passengers, ArrayList<Vehicle> vehicles, ArrayList<Operator> operators, double threshold)
	{
		
		for (int i = 0 ; i < passengers.size() ; i++)
		{ 
			double finalDistance = threshold;
			if (passengers.get(i).defaultOperator == 0)
			{
				int [] operatorCounter = new int [operators.size()];
					
				Point passengerCoord = passengers.get(i).coordinate;
				
				for (int j = 0 ; j < vehicles.size() ; j++)
				{
					Point vehicleCoord = vehicles.get(j).coordinate;
					
					if (Math.abs(passengerCoord.getX() - vehicleCoord.getX()) <= threshold && Math.abs(passengerCoord.getY() - vehicleCoord.getY()) <= threshold )
					{
						double distance = passengerCoord.distance( vehicleCoord );
						
						if (distance < threshold )
						{
							operatorCounter[vehicles.get(j).operator - 1]++;
							finalDistance = distance;
							passengers.get(i).setDefaultOperator(vehicles.get(j).operator);
							
						}
					}
				}
				
				int operatorCode = 0;
				int maxCount = 0;
				for (int o = 0 ; o < operatorCounter.length ; o++)
				{
					if (operatorCounter[o] > maxCount )
					{
						operatorCode = o;
						maxCount = operatorCounter[o];
					}
				}
				
				if (operatorCounter[operatorCode] > 0)
					passengers.get(i).setDefaultOperator(operatorCode + 1);
			}
		}
		
		return passengers;
		
	}

}
