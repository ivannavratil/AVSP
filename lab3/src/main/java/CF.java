import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class CF {

	public static void main(String[] args) throws IOException {

		long start = System.nanoTime();
		CF.start();
		//CF.start("lab3\\src\\main\\resources\\test_example\\R.in");
		System.err.println((System.nanoTime() - start) / Math.pow(10, 9));
	}

	static void start() throws IOException {
		start(null);
	}

	private static void start(String path) throws IOException {
		Util.ParsedInput input = Util.parse(path);

		Double[][] normal = input.getUserItemMatrix();
		Double[][] transposed = transpose(normal);

		List<int[]> queries = input.getQueries();

		DecimalFormat df = new DecimalFormat("#.000");

		for (int[] query : queries) {
			Double[][] matrix;
			int i_input, j_input;
			if (query[2] == 0) {
				matrix = normal;
				i_input = query[0];
				j_input = query[1];
			} else {
				matrix = transposed;
				j_input = query[0];
				i_input = query[1];
			}

			int max = query[3];

			List<Pair> list = new ArrayList<>();
			for (int i = 0; i < matrix.length; i++) {
				//ne trebamo racunati slicnost sami sa sobom
				if (i != i_input) {
					list.add(new Pair(i, calculatePearsonCorrelationCoefficient(matrix[i_input], matrix[i])));
				}
			}

			Pair[] pairs = list.stream()
			                   //samo ako su slicni
			                   .filter(a -> a.sim >= 0)
			                   //samo ako su ocjenili taj item
			                   .filter(pair -> matrix[pair.index][j_input] != null)
			                   //sortiraj po slicnosti silazno
			                   .sorted((o1, o2) -> o2.sim.compareTo(o1.sim))
			                   //daj mi najvise max
			                   .limit(max)
			                   .toArray(Pair[]::new);


			double v = predictItemValue(matrix, pairs, j_input);

			BigDecimal bd = new BigDecimal(v);
			BigDecimal res = bd.setScale(3, RoundingMode.HALF_UP);

			System.out.println(df.format(res));
		}
	}

	public static Double[][] transpose(Double[][] matrix) {
		int rows = matrix.length;
		int columns = matrix[0].length;
		Double[][] transposed = new Double[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				transposed[i][j] = matrix[j][i];
			}
		}
		return transposed;
	}

	public static double calculatePearsonCorrelationCoefficient(Double[] x, Double[] y) {
		int numberOfElements = x.length;

		Double[] xx = new Double[x.length];
		Double[] yy = new Double[y.length];

		for (int i = 0; i < xx.length; i++) {
			xx[i] = x[i];
			yy[i] = y[i];
		}

		double rxAvg = Arrays.stream(xx).filter(Objects::nonNull).mapToDouble(a -> a).summaryStatistics().getAverage();
		double ryAvg = Arrays.stream(yy).filter(Objects::nonNull).mapToDouble(a -> a).summaryStatistics().getAverage();

		for (int i = 0; i < numberOfElements; i++) {
			if (xx[i] != null) {
				xx[i] = xx[i] - rxAvg;
			}
			if (yy[i] != null) {
				yy[i] = yy[i] - ryAvg;
			}
		}

		double brojnik = IntStream.range(0, numberOfElements).filter(a -> xx[a] != null && yy[a] != null).mapToDouble(i -> xx[i] * yy[i]).sum();

		double xSum = 0, ySum = 0;
		for (int i = 0; i < numberOfElements; i++) {
			if (xx[i] != null) {
				xSum = xSum + xx[i] * xx[i];
			}
			if (yy[i] != null) {
				ySum = ySum + yy[i] * yy[i];
			}
		}

		double nazivnik = Math.sqrt(xSum * ySum);

		return brojnik / nazivnik;
	}

	public static double predictItemValue(Double[][] matrix, Pair[] topPairs, int index) {
		double brojnik = 0;
		double nazivnik = 0;

		for (Pair p : topPairs) {
			//brojnik = brojnik + slicnost user x i usera y * ocjena korisnika y za item i
			brojnik = brojnik + p.sim * matrix[p.index][index];
			nazivnik = nazivnik + p.sim;
		}

		return brojnik / nazivnik;
	}

	public static class Pair {
		int index;

		Double sim;

		public Pair(int index, Double sim) {
			this.index = index;
			this.sim = sim;
		}
	}
}
