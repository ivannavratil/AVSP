public class Test {
	public static void main(String[] args) {

		System.out.println(CF.calculatePearsonCorrelationCoefficient(
				new Double[]{2 / 3.0, (double) 0, (double) 0, 5 / 3.0, -7 / 3.0, (double) 0, (double) 0},
				new Double[]{(double) 1 / 3.0, (double) 1 / 3.0, (double) -2 / 3.0, (double) 0, (double) 0, (double) 0, (double) 0}
		));

		System.out.println(CF.calculatePearsonCorrelationCoefficient(
				new Double[]{2 / 3.0, (double) 0, (double) 0, 5 / 3.0, -7 / 3.0, (double) 0, (double) 0},
				new Double[]{(double) 0, (double) 0, (double) 0, -5 / 3.0, 1 / 3.0, 4 / 3.0, (double) 0}
		));


		//		System.out.println(CF.calculatePearsonCorrelationCoefficient(
		//				new double[]{2 / 3.0},
		//				0,
		//				new double[]{1 / 3.0},
		//				0
		//		));
		//
		//		System.out.println(CF.calculatePearsonCorrelationCoefficient(
		//				new double[]{5 / 3.0, -7 / 3.0},
		//				0,
		//				new double[]{-5 / 3.0, 1 / 3.0},
		//				0
		//		));
	}
}
