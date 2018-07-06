package utilities;

import java.util.Random;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		for (int i = 0 ; i < 100 ; i++)
		{
		Random rnd = new Random();
		double radius = Math.abs(rnd.nextGaussian() * 0.125 + 0.5) * 3000 ;
		double radius1 = Math.abs(rnd.nextGaussian() * 0.25) * 3000 ;
		System.out.println(radius + ", " + radius1);
		}

	}

}
