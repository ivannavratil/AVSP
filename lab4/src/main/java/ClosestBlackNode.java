import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.stream.IntStream;

public class ClosestBlackNode {

	static Structure[] solutions;

	public static void main(String[] args) throws IOException {
		long start = System.nanoTime();
		ClosestBlackNode.start();
		//ClosestBlackNode.start("lab4\\src\\main\\resources\\b\\btest2\\R.in");
		System.err.println((System.nanoTime() - start) / Math.pow(10, 9));
	}

	private static void start(String path) throws IOException {
		UtilB.ParsedInput input = UtilB.parse(path);
		int size = input.transitions.size();
		solutions = new Structure[size];

		IntStream.range(0, size).parallel().forEach(i -> BFS.search(i, input.transitions, input.blackVertices));

		OutputStream out = new BufferedOutputStream(System.out);
		for (Structure solution : solutions) {
			out.write((solution.closestBlack + " " + solution.distance + "\n").getBytes());
		}
		out.flush();
	}

	static void start() throws IOException {
		start(null);
	}

	static class Structure {
		int closestBlack;

		int distance;

		public Structure(int closestBlack, int distance) {
			this.closestBlack = closestBlack;
			this.distance = distance;
		}
	}
}
