import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

public class NodeRank {

	public static void main(String[] args) throws IOException {
		long start = System.nanoTime();
		NodeRank.start();
		//NodeRank.start("lab4\\src\\main\\resources\\a\\mtest2\\R.in");
		System.err.println((System.nanoTime() - start) / Math.pow(10, 9));
	}

	static void start() throws IOException {
		start(null);
	}

	private static void start(String path) throws IOException {
		UtilA.ParsedInput input = UtilA.parse(path);

		int N = input.forwards.size();
		double[][] rj = new double[101][N];
		Arrays.fill(rj[0], 1.0 / N);

		//broj iteracije
		for (int i = 0; i < 100; i++) {
			double S = 0;

			for (int j = 0; j < N; j++) {
				double sum = 0;
				List<Integer> ints = input.backwards.get(j);

				for (Integer integer : ints) {
					sum = sum + input.beta * (rj[i][integer] / input.forwards.get(integer).length);
				}

				rj[i + 1][j] = sum;
				S = S + sum;
			}
			for (int j = 0; j < N; j++) {
				rj[i + 1][j] += (1 - S) / N;
			}
		}

		//		for (int[] ints : input.queries) {
		//			System.out.printf("%.10f\n", rj[ints[1]][ints[0]]);
		//		}

		OutputStream out = new BufferedOutputStream(System.out);
		for (int[] ints : input.queries) {
			out.write(String.format("%.10f\n", rj[ints[1]][ints[0]]).getBytes());
		}
		out.flush();
	}
}
