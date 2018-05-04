package utilities;

import java.awt.Point;

public class RequiredClasses {
	
	public static class Passenger
	{
		public int id;
		public Point coordinate;
		public double utility;
		public int neighbour;
		public int interest;
		public int mtcheVehID;
		public String defaultOperator;
		
		
		public Passenger(int id, Point coordinate, double utility, int neighbour, int interest, int mtcheVehID, String defaultOperator) {
			super();
			this.id = id;
			this.coordinate = coordinate;
			this.utility = utility;
			this.neighbour = neighbour;
			this.interest = interest;
			this.mtcheVehID = mtcheVehID;
			this.defaultOperator = defaultOperator;
		}
		
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public Point getCoordinate() {
			return coordinate;
		}
		public void setCoordinate(Point coordinate) {
			this.coordinate = coordinate;
		}
		public double getUtility() {
			return utility;
		}
		public void setUtility(double utility) {
			this.utility = utility;
		}

		public int getNeighbour() {
			return neighbour;
		}

		public void setNeighbour(int neighbour) {
			this.neighbour = neighbour;
		}

		public int getInterest() {
			return interest;
		}

		public void setInterest(int interest) {
			this.interest = interest;
		}
		
		public int getMtcheVehID() {
			return mtcheVehID;
		}

		public void setMtcheVehID(int mtcheVehID) {
			this.mtcheVehID = mtcheVehID;
		}

		@Override
		public String toString() {
			return  id + "," + coordinate.x + "," + coordinate.y + "," + utility + "," + neighbour + "," + interest + "," + defaultOperator;
		}
				
	}

	public static class Vehicle
	{
		public int id;
		public Point coordinate;
		public double utility;
		public int neighbour;
		public int capacity;
		public String operator;
		
		
		public Vehicle(int id, Point coordinate, double utility, int neighbour, int capacity, String operator) {
			super();
			this.id = id;
			this.coordinate = coordinate;
			this.utility = utility;
			this.neighbour = neighbour;
			this.capacity = capacity;
			this.operator = operator;
		}
		
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public Point getCoordinate() {
			return coordinate;
		}
		public void setCoordinate(Point coordinate) {
			this.coordinate = coordinate;
		}
		public double getUtility() {
			return utility;
		}
		public void setUtility(double utility) {
			this.utility = utility;
		}

		public int getNeighbour() {
			return neighbour;
		}

		public void setNeighbour(int neighbour) {
			this.neighbour = neighbour;
		}
		
		public int getCapacity() {
			return capacity;
		}

		public void setCapacity(int capacity) {
			this.capacity = capacity;
		}

		@Override
		public String toString() {
			return  id + "," + coordinate.x + "," + coordinate.y + "," + utility + "," + capacity + "," + neighbour + "," + operator;
		}
	}
}