package test;

import java.awt.Point;
import java.awt.Toolkit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import utilities.Vehicle;
import utilities.Generators;
import utilities.Operator;
import utilities.Passenger;
import utilities.ZahraUtility;

public class TestOnePlatformDiffSpace {
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		String outputDir = "C:\\Users\\znavidikasha\\eclipse\\OperatorsCompetition\\test\\";
		// TODO Auto-generated method stub
		
		/*
		 * setting global variables
		 */
		double xLimit = 3000; //in meter
		double yLimit = 3000; //in meter
		int area = (int) (xLimit * yLimit / Math.pow(10, 6)); //(m^2)
		String passDist = "N"; 
		String vehDist = "N"; 
		int passengerNumber = 900;
		int successThreshold = 5;
		int probIteration = 100;
		int iterations = 100;
		int iterationWrite = 50;
		double reachMeasure = 100; // in meter
		double potentialUtil = 5.0;
		double vehUtilLowerThres = 2.0;
		double vehUtilUpperThres = 4.0;
		double passUtilThres = 2.0;
		int vehicleCapacity = 1;
		int passInterestThres = 5;
		String mainDir = outputDir + "output\\OnePlatform\\diffSpaceL\\" + area + "sqkm\\" ;
		String probabilityFilePath = mainDir + "One-diff-" + area + "sqkm_probabilities.csv";
		Files.createDirectories(Paths.get(mainDir));
		ArrayList<Operator> operators = new ArrayList<Operator>();
		int operatorsNumber = ZahraUtility.csvLineCounter("input\\operators.csv");
		int scenarios = ZahraUtility.csvColCounter("input\\operators.csv") ;
		String[][] operatorsList = ZahraUtility.Data(operatorsNumber, scenarios, "input\\operators.csv");
		
		
		/*
		 * openning file for probability writing
		 */		
		BufferedWriter pBufferedWriter = new BufferedWriter(new FileWriter(new File(probabilityFilePath), true));
		
		/*
		 * starting for loops for passengers and vehicles
		 */
		for (int s = 2 ; s < scenarios ; s++ )
		{
			operators.clear();
			for (int i = 1 ; i < operatorsList.length ; i++)
			{
				Operator tempOp = new Operator(Integer.parseInt(operatorsList[i][0]), operatorsList[i][1], Double.parseDouble(operatorsList[i][s]));
				operators.add(tempOp);
			}
			pBufferedWriter.write("Area_sqKm,Population,Population_distribution,Vehicles_added_each_iteration,Vehicles_distribution");
			for (int o = 0 ; o < operators.size() ; o++)
				pBufferedWriter.write(",SP_" + operators.get(o).getMarketShare() + "_" + operators.get(o).getName());
			
			for (int v = 300 ; v < 901 ; v+=10000)// here set the min and max of vehicle and the increment
			{
				double [] probability = new double [operators.size()]; //counting successful instances
//				int [] prob5Per = new int [operators.size()];
//				int [] prob10Per = new int [operators.size()];
//				int [] prob15Per = new int [operators.size()];
//				int [] prob20Per = new int [operators.size()];
				int allIterations = 0; // counting all instances
				
				int [] vehAddedInIteration = new int [operators.size()] ;
				int interestedPassengers = 0;
				int matchedPassengers = 0 ;
				double [] matchedVehicles = new double [operators.size()];
				

				for (int probItcounter = 0 ; probItcounter < probIteration ; probItcounter++)
				{
					for (int k = 0 ; k < operators.size() ; k++)
						vehAddedInIteration[k] = (int) (v * operators.get(k).getMarketShare());
					
					ArrayList<Passenger> passengers = new ArrayList <Passenger>();
					ArrayList<Vehicle> vehicles = new ArrayList <Vehicle>();
					double [] vehicleUtilSum = new double[operators.size()];
					//directory for each probability iteration
					String dir = mainDir + passDist + vehDist + "\\" + "O"+ (operators.size()) + "-" + operators.get(0).getMarketShare() + "-P" + passDist + "_V" + vehDist + "\\" + probItcounter + "\\" ;
					Files.createDirectories(Paths.get(dir));
					
					//opening file for writing the aggregated data
					String aggregatedFilePath = dir + "P" + passengerNumber + passDist + "-V" + v + vehDist + "-" + vehicleCapacity + "C" + ".csv" ; 
					
					BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(aggregatedFilePath), false));
					bufferedWriter.write("Iteration,Passengers' mean utility,Interested passengers,% Interested passengers,"
							+ "Matched passengers,% Matched passengers,Total Vehicles");
					for (int o = 0 ; o < operators.size() ; o++)
						bufferedWriter.write("," + operators.get(o).getName() + " mean utility,"+ operators.get(o).getName() 
								+ " matched vehicels, %" + operators.get(o).getName() + " matched vehicle");
					bufferedWriter.write("\n");
		

					//iterations start
					for (int k = 1 ; k <= iterations ; k++)
					{
						//generating the population with a normal distribution
						passengers = Generators.passengerGenerator(passDist, passengerNumber, operators, xLimit, yLimit);
						vehicles = Generators.vehicleLocalGenerator(vehicles, vehDist, vehAddedInIteration, operators, xLimit, yLimit, vehicleCapacity);

						int [] operatorsFleetSize = new int [operators.size()];
						double [] meanVehUtil = new double [operators.size()];
						double passengerUtilSum = 0.0;
						matchedPassengers = 0 ;
						ZahraUtility.allList2ZeroD(matchedVehicles);
						ZahraUtility.allList2ZeroD(vehicleUtilSum);
						interestedPassengers = 0;
						
						
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
									
									if (distance < reachMeasure)
									{
										passengers.get(i).neighbour++;
										 
										if (distance < finalDist && vehicles.get(j).capacity > 0 )
										{
											finalDist = distance;
											passengers.get(i).setMtcheVehID(j);	
										}
									}
								}
								if (i == 0)
									operatorsFleetSize[vehicles.get(j).operator - 1]++;
								
							}// end of vehicle loop
													
							 
							
							//assigning the previously identified nearest vehicle to interested passengers
							if (passengers.get(i).mtcheVehID > -1 )
							{
								int thisVehID = passengers.get(i).mtcheVehID;
								double util = (reachMeasure - finalDist) / reachMeasure * potentialUtil;
								passengers.get(i).setUtility(util);
								vehicles.get(thisVehID).setUtility(util);
								vehicles.get(thisVehID).capacity-- ;
								matchedPassengers++;
								matchedVehicles[vehicles.get(thisVehID).operator - 1]++;
								vehicleUtilSum[vehicles.get(thisVehID).operator - 1] += util; //vehicles.get(thisVehID).utility;
							}
							
							else
								passengers.get(i).setUtility(0.0);
							
							passengerUtilSum += passengers.get(i).utility;		
	
						}// end of passenger loop
						

						
						//====================calculating the mean utility====================

						double meanPassengersUtil = passengerUtilSum / matchedPassengers;					
//						double [] meanVehUtil = new double [operators.size()];
						//ZahraUtility.allList2ZeroD(meanVehUtil);
						double [] matchedVehPercent = new double [operators.size()];
						
						for (int o = 0 ; o < operators.size() ; o++)
						{
							meanVehUtil[o] = (vehicleUtilSum[o] / operatorsFleetSize[o] );
							if (matchedVehicles[o] > 0)
								matchedVehPercent [o] = matchedVehicles[o] / vehAddedInIteration[o] * 100.0;
						}
							
						
						double matchedPassPercent = (double) matchedPassengers/passengers.size() * 100;
						 
						
						if (k % iterationWrite == 0 || k == 1 )
						{
							StringBuilder fileContentP = new StringBuilder();
							StringBuilder fileContentV = new StringBuilder();
							
							fileContentP.append("Iteration,Passenger_ID,X,Y,Utility,Neighbours,Interest, Default Operator"+ "\n");
							for (int i = 0 ; i < passengers.size(); ++i)
								fileContentP.append(k + "," + passengers.get(i).toString() + "\n");
							
							fileContentV.append("Iteration,Vehicle_ID,X,Y,Utility,Capacity,Neighbours,Operator,interest" + "\n");
							for (int i = 0 ; i < vehicles.size(); ++i)
								fileContentV.append(k + "," +vehicles.get(i).toString() + "\n");
							
							ZahraUtility.write2File(fileContentP.toString(), dir + k + "-passengers-P" + passengerNumber + passDist 
									+ "-V" + v + vehDist + "-" + vehicleCapacity + "C" + ".csv");
							ZahraUtility.write2File(fileContentV.toString(), dir + k + "-vehicles-P" + passengerNumber + passDist + "-V" 
									+ v + vehDist + "-" + vehicleCapacity + "C" + ".csv");
						}

						bufferedWriter.write(k + "," + meanPassengersUtil + "," + interestedPassengers + "," 
								+ (double)interestedPassengers/passengers.size() * 100 + "%," 
								+ matchedPassengers + "," +  matchedPassPercent + "%," + vehicles.size() );
						for (int o = 0 ; o < operators.size() ; o++)
							bufferedWriter.write("," + meanVehUtil[o] + "," + matchedVehicles[o] + "," + matchedVehPercent[o] );
						bufferedWriter.write("\n");
						
						//identifying and removing vehicles with low utility
						for (int i = 0 ; i < vehicles.size() ; i++)
						{
							
							if (vehicles.get(i).utility < vehUtilLowerThres)
							{
								vehicles.remove(i);
								i--;
							}
							else
							{
								vehicles.get(i).setUtility(0.0);
								vehicles.get(i).setCapacity(vehicleCapacity);
								vehicles.get(i).setId(i);	
							}
						}
					
					    
					    // modifying the size of each operator's fleet based on their average utility
					    for (int o = 0 ; o < operators.size() ; o++)
					    {
							if(meanVehUtil[o] < vehUtilLowerThres)
								vehAddedInIteration[o] *= 0.9;
							
							else if (meanVehUtil[o] >= vehUtilUpperThres)
								vehAddedInIteration[o] *= 1.1;
					    }
					    
					    if (meanPassengersUtil > passUtilThres)
					    	passengerNumber *= 1.1;
					    
					    if (meanPassengersUtil > passUtilThres)
					    	passengerNumber *= 0.9;
					   

					}// end of iterations
					
					for (int o = 0 ; o < operators.size() ; o++)
					{
						if (matchedVehicles[o] > successThreshold )
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

				}
			
			}// end of scenarios
		
		pBufferedWriter.close();
		System.out.println("DONE");
		Toolkit.getDefaultToolkit().beep();
		
	}//end of main

}//end of class
