package artificialEnvironmentMultiPlatform;

import java.awt.Point;
import java.awt.Toolkit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import utilities.Vehicle;
import utilities.Generators;
import utilities.Operator;
import utilities.Passenger;
import utilities.ZahraUtility;

public class NormalDistMultiPlatform {
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		/*
		 * setting global variables
		 */
		double xLimit = 1000; //in meter
		double yLimit = 1000; //in meter
		int area = (int) (xLimit * yLimit / Math.pow(10, 6));
		String passDist = "N"; // for normal distribution
		String vehDist = "N"; // for normal distribution
		int probIteration = 100;
		int iterations = 100;
		int iterationWrite = 20;
		double reachMeasure = 100; // in meter
		double potentialUtil = 5.0;
		double vehUtilLowerThres = 2.0;
		double vehUtilUpperThres = 3.5;
		double passUtilThres = 2.0;
		int vehicleCapacity = 1;
		int passInterestThres = 5;
		String mainDir = "initialRuns\\multiPlatform\\" + area + "sqkm\\" + passDist + vehDist + "\\";
		Files.createDirectories(Paths.get(mainDir));
		ArrayList<Operator> operators = new ArrayList<Operator>();
		String[][] operatorsList = ZahraUtility.Data(4, 3, "input\\operators.csv");
		for (int i = 1 ; i < operatorsList.length ; i++)
		{
			Operator tempOp = new Operator(Integer.parseInt(operatorsList[i][0]), operatorsList[i][1], Double.parseDouble(operatorsList[i][2]));
			operators.add(tempOp);
		}
		
		/*
		 * openning file for probability writing
		 */
		String probabilityFilePath = mainDir + passDist + vehDist + "-" + area + "sqkm_probabilities.csv";
		BufferedWriter pBufferedWriter = new BufferedWriter(new FileWriter(new File(probabilityFilePath), true));
		pBufferedWriter.write("Area_sqKm,Population,Population_distribution,Vehicles_added_each_iteration,Vehicles_distribution");
		for (int o = 0 ; o < operators.size() ; o++)
		pBufferedWriter.write(",SP_" + operators.get(o).getMarketShare() + "_" + operators.get(o).getName());
		
		/*
		 * starting for loops for passengers and vehicles
		 */
		for (int p = 50 ; p < 51 ; p+=10 )
		{
			for (int v = 40; v < 41 ; v+=100)
			{
				double [] probability = new double [operators.size()]; //counting successful instances
//				int prob5Per = 0;
//				int prob10Per = 0;
//				int prob15Per = 0;
//				int prob20Per = 0;
				int allIterations = 0; // counting all instances
				int passengerNumber = p * area;
				int [] vehAddedInIteration = new int [operators.size()] ;
				int interestedPassengers = 0;
				int [] matchedVehicles = new int [operators.size()];
				
				for (int k = 0 ; k < operators.size() ; k++)
				{
					vehAddedInIteration[k] = (int) (v * operators.get(k).getMarketShare());
				}
				

				for (int probItcounter = 0 ; probItcounter < probIteration ; probItcounter++)
				{
					//directory for each probability iteration
					String dir = mainDir + "P" + passDist + passengerNumber + "\\V" + vehDist + v + "\\" + probItcounter + "\\" ;
					
					//opening file for writing the aggregated data
					Files.createDirectories(Paths.get(dir));
					String aggregatedFilePath = dir + "P" + passengerNumber + passDist + "-V" + v + vehDist + "-" + vehicleCapacity + "C" + ".csv" ;
					BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(aggregatedFilePath), false));
					bufferedWriter.write("Iteration,Passengers' mean utility,Vehicles' mean utility,"
							+ "% Interested passengers,% Matched passengers,% Matched Vehicles,"
							+ "Interested passengers,Matched passengers,Matched Vehicles,Total Vehicles\n");
					
					String matchedVehPathFile = dir + "matchedVehicles-P" + passengerNumber + passDist + "-V" + v + vehDist + "-" + vehicleCapacity + "C" + ".csv"; 
					BufferedWriter mvBufferedWriter = new BufferedWriter(new FileWriter(new File(matchedVehPathFile), false));
					mvBufferedWriter.write("Iteration");
					for (int o = 0 ; o < operators.size() ; o++)
					{
						mvBufferedWriter.write("," + operators.get(o).getName());
					}
					//========================================================================================
					
					ArrayList<Passenger> passengers = new ArrayList <Passenger>();
					ArrayList<Vehicle> vehicles = new ArrayList <Vehicle>();
					double [] vehicleUtilSum = new double[operators.size()];
					
					passengers = Generators.passengerGenerator(passDist, passengerNumber, operators, xLimit, yLimit);
					//generating the population with a normal distribution
//					int passIdCounter = 1; // this is to have a continues unique ID for passengers
//					for (int o = 0 ; o < operators.size() ; o++)
//					{
//						for (int i = 0 ; i < passengerNumber * operators.get(o).getMarketShare(); i++ )
//						{
//							Random rnd = new Random();
//							Point passengerCoord = new Point();
//							passengerCoord.setLocation((rnd.nextGaussian()*0.125 + 0.5) * xLimit, (rnd.nextGaussian()*0.125 + 0.5) * yLimit);
//							Passenger tempPassenger = new Passenger (passIdCounter , passengerCoord, 0.0, 0, 0, -1, operators.get(o).getCode());
//							passengers.add(tempPassenger);
//							passIdCounter++ ;
//						}
//					}
//					System.out.println("passengers done: " + passengers.size());
					
			
					//iterations start
					for (int k = 1 ; k <= iterations ; k++)
					{
						ZahraUtility.allList2ZeroI(matchedVehicles);
//						System.out.println( "vehAddedInIteration: " + vehAddedInIteration);
						interestedPassengers = 0;
						double passengerUtilSum = 0.0;
						ZahraUtility.allList2ZeroD(vehicleUtilSum);
						int matchedPassengers = 0 ;
						int unmatchedVehicles = 0;
						
						//generating the vehicles to add in this iteration with a normal distribution
						int startingID = -1; // this is necessary to have unique id for vehicles through all iterations
						if (vehicles.size() > 0)
							startingID = vehicles.get(vehicles.size()- 1).id;
						int vehicleIdCounter = startingID + 1 ;
						for (int o = 0 ; o < operators.size() ; o++)
						{
							for (int i = 0 ; i <= vehAddedInIteration[o] ; i++ )
							{
								Random rnd = new Random();
								Point vehicleCoord = new Point();
								vehicleCoord.setLocation((rnd.nextGaussian()*0.125 + 0.5) * xLimit, (rnd.nextGaussian()*0.125 + 0.5) * yLimit);
								Vehicle tempVehicle = new Vehicle (vehicleIdCounter, vehicleCoord, 0.0, 0, vehicleCapacity, operators.get(o).getCode());
								vehicles.add(tempVehicle);
								vehicleIdCounter++ ;
							}
						}
						
//						System.out.println("passengers: " + passengers.size() + ", vehicles: " + vehicles.size());
						
						for (int i = 0 ; i < passengers.size() ; i++ )
						{
							
							passengers.get(i).setMtcheVehID(-1);
							passengers.get(i).setNeighbour(0);
							
							Point passengerCoord = passengers.get(i).coordinate;
							double finalDist = xLimit;
							
							for (int j = 0 ; j < vehicles.size() ; j++ )
							{
								
								Point vehicleCoord = vehicles.get(j).coordinate;
								
								if (Math.abs(passengerCoord.getX() - vehicleCoord.getX()) <= reachMeasure && Math.abs(passengerCoord.getY() - vehicleCoord.getY()) <= reachMeasure )
								{
									double distance = passengerCoord.distance( vehicleCoord );
									
									if (distance < reachMeasure && vehicles.get(j).getOperator() == passengers.get(i).defaultOperator)
									{
										passengers.get(i).neighbour++;
										
										if (distance < finalDist && vehicles.get(j).capacity > 0 && vehicles.get(j).getOperator() == passengers.get(i).defaultOperator)
										{
											finalDist = distance;
											passengers.get(i).setMtcheVehID(j);	
										}
									}
								}
							}// end of vehicle loop
							
							
							//identifying and counting interested passengers based on their previous experience or available vehicle neighbours
							if (passengers.get(i).neighbour > passInterestThres || passengers.get(i).utility > passUtilThres )
							{
								passengers.get(i).setInterest(1);
								interestedPassengers++;
							}
							else
								passengers.get(i).setInterest(0);
							
							 
							
							//assigning the previously identified nearest vehicle to interested passengers
							if (passengers.get(i).getInterest() == 1 && passengers.get(i).mtcheVehID > -1 )
							{					
								double util = (reachMeasure - finalDist) / reachMeasure * potentialUtil;
								passengers.get(i).setUtility(util);
								vehicles.get(passengers.get(i).mtcheVehID).setUtility(util);
								vehicles.get(passengers.get(i).mtcheVehID).capacity-- ;
								matchedPassengers++;
							}
							else
								passengers.get(i).setUtility(0.0);
							
							passengerUtilSum += passengers.get(i).utility;							
							
						}// end of passenger loop
						
						//identifying and counting unmatched vehicles
						for (int i = 0 ; i < vehicles.size() ; i++)
						{
							if (vehicles.get(i).capacity == vehicleCapacity)
							{
								vehicles.get(i).utility = 0.0;
								unmatchedVehicles++;
							}
						}
						
						
						//====================calculating the mean utility====================

						double meanPassengersUtil = passengerUtilSum / passengers.size();
						
						for (int o = 0 ; o < operators.size() ; o++)
						{
							for (int i = 0 ; i < vehAddedInIteration[o] ; i++)
							{
								vehicleUtilSum[o] += vehicles.get(i).utility;
							}
						}
						
						
						double [] meanVehUtil = new double [operators.size()];
						for (int o = 0 ; o < operators.size() ; o++)
							meanVehUtil[o] = (vehicleUtilSum[o] / vehAddedInIteration[o] );
						
						double matchedPassPercent = (double) matchedPassengers/passengers.size() * 100;
						double matchedVehPercent = (double) (vehicles.size() - unmatchedVehicles)/vehicles.size() * 100;
						
						if (k % iterationWrite == 0 || k == 1)
						{
							StringBuilder fileContentP = new StringBuilder();
							StringBuilder fileContentV = new StringBuilder();
							
							fileContentP.append("Iteration,Passenger_ID,X,Y,Utility,Neighbours,Interest"+ "\n");
							for (int i = 0 ; i < passengers.size(); ++i)
								fileContentP.append(k + "," + passengers.get(i).toString() + "\n");
							
							fileContentV.append("Iteration,Vehicle_ID,X,Y,Utility,Capacity,Neighbours" + "\n");
							for (int i = 0 ; i < vehicles.size(); ++i)
								fileContentV.append(k + "," +vehicles.get(i).toString() + "\n");
							
							ZahraUtility.write2File(fileContentP.toString(), dir + k + "-passengers-P" + passengerNumber + passDist 
									+ "-V" + v + vehDist + "-" + vehicleCapacity + "C" + ".csv");
							ZahraUtility.write2File(fileContentV.toString(), dir + k + "-vehicles-P" + passengerNumber + passDist + "-V" 
									+ v + vehDist + "-" + vehicleCapacity + "C" + ".csv");
						}

						bufferedWriter.write(k + "," + meanPassengersUtil + "," + meanVehUtil + "," 
								+ (double)interestedPassengers/passengers.size() * 100 + "%," 
								+  matchedPassPercent + "%," + matchedVehPercent + "%," + interestedPassengers 
								+ "," + matchedPassengers + "," + (vehicles.size() - unmatchedVehicles) + "," 
								+ vehicles.size() + "\n");
						 
					    //removing the vehicles with low utility
					    for (int i = 0 ; i < vehicles.size(); i ++)
						{
							if (vehicles.get(i).utility < vehUtilLowerThres)
							{
								vehicles.remove(i);
								i--;
							}
							else
							{
								matchedVehicles[vehicles.get(i).getOperator() - 1]++;
								vehicles.get(i).setUtility(0.0);
								vehicles.get(i).setCapacity(vehicleCapacity);
								vehicles.get(i).setId(i);
								
							}
						}
					    
					    mvBufferedWriter.write("\n" + k);
						for (int o = 0 ; o < operators.size() ; o++)
						{
							mvBufferedWriter.write("," + matchedVehicles[o]);
						}
						
					    
					    // modifying the size of each operator's fleet based on their average utility 
					    for (int o = 0 ; o < operators.size() ; o++)
					    {
							if(meanVehUtil[o] < vehUtilLowerThres)
								vehAddedInIteration[o] *= 0.9;
							else if (meanVehUtil[o] >= vehUtilUpperThres)
								vehAddedInIteration[o] *= 1.1;
					    }
					    
//					    System.out.println("matched vehicles of each operator: " + matchedVehicles[0] + ", " +  matchedVehicles[1]);

					}// end of iterations			
					mvBufferedWriter.close();
					
					for (int o = 0 ; o < operators.size() ; o++)
					{
						if (matchedVehicles[o] > 0 )
							probability[o]++ ;
					}
					
					
//					if (interestedPassengers > 0) 
//				    	probability++ ;
//				    if (interestedPassengers >= 0.05 * passengers.size())
//				    	prob5Per++ ;
//				    if (interestedPassengers >= 0.1 * passengers.size())
//				    	prob10Per++ ;
//				    if (interestedPassengers >= 0.15 * passengers.size())
//				    	prob15Per++ ;
//				    if (interestedPassengers >= 0.2 * passengers.size())
//				    	prob20Per++ ;
				    
					allIterations++;
		//			System.out.println("total:" + allIterations);
		//		    bufferedWriter.write(probability);
		//			System.out.println(probIteration);
					
					bufferedWriter.close();
				    
				    
				}//end of probability iteration
				
//				double successProb = (double) probability/allIterations * 100;
//				double successProb5 = (double) prob5Per/allIterations * 100;
//				double successProb10 = (double) prob10Per/allIterations * 100;
//				double successProb15 = (double) prob15Per/allIterations * 100;
//				double successProb20 = (double) prob20Per/allIterations * 100;
//				System.out.println(p + " passengers and " + v + " vehicles: " + successProb + "%");
				pBufferedWriter.write("\n" + area + "," + passengerNumber + "," + passDist + "," + v + "," + vehDist);
				for (int o = 0 ; o < operators.size() ; o++)
					pBufferedWriter.write( "," + probability[o]/allIterations * 100);
				pBufferedWriter.write("\n");

//				pBufferedWriter.write(area + "," + passengerNumber + "," + passDist + "," + v + "," + vehDist + "," +
//										successProb + "," + successProb5 + "," + successProb10 + "," + successProb15 +
//										"," + successProb20 + "\n");
				
				}//end of vehicle forLoop
			}//end of passenger forLoop
		
		pBufferedWriter.close();
		System.out.println("DONE");
		Toolkit.getDefaultToolkit().beep();
	}//end of main

}//end of class
