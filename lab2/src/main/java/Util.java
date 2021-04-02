import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Util {

	public static ParsedInput parse() throws IOException {
		return parse(null);
	}

	public static ParsedInput parse(String path) throws IOException {
		BufferedReader br = setBufferedReader(path);

		//handle first few
		int numberOfBaskets = Integer.parseInt(br.readLine());

		double s = Double.parseDouble(br.readLine());

		int numberOfBucketsAvailable = Integer.parseInt(br.readLine());


		List<List<Integer>> list = new ArrayList<>();

		int items = 0;

		for (String line = br.readLine(); line != null; line = br.readLine()) {

			List<Integer> basket = new ArrayList<>();

			String[] split = line.split("\\s+");

			for (String str : split) {
				int i = Integer.parseInt(str);
				items = Math.max(items, i);
				basket.add(i);
			}
			list.add(basket);
		}

		br.close();


		return new ParsedInput(numberOfBaskets, s, numberOfBucketsAvailable, items, list);
	}

	private static BufferedReader setBufferedReader(String path) throws FileNotFoundException {
		BufferedReader br;
		if (path == null) {
			br = new BufferedReader(new InputStreamReader(System.in));
		} else {
			br = new BufferedReader(new FileReader(path));
		}
		return br;
	}

	public static class ParsedInput {
		private final int numberOfBaskets;

		private final double s;

		private final List<List<Integer>> list;

		private final int numberOfBucketsAvailable;

		private final int numberOfProducts;

		public ParsedInput(
				int numberOfBaskets,
				double s,
				int numberOfBucketsAvailable,
				int numberOfProducts,
				List<List<Integer>> list) {
			this.numberOfBaskets = numberOfBaskets;
			this.s = s;
			this.numberOfBucketsAvailable = numberOfBucketsAvailable;
			this.numberOfProducts = numberOfProducts;
			this.list = list;
		}

		public int getNumberOfDifferentProducts() {
			return this.numberOfProducts;
		}

		public int getNumberOfBaskets() {
			return this.numberOfBaskets;
		}

		public double getS() {
			return this.s;
		}

		public List<List<Integer>> getList() {
			return this.list;
		}

		public int getNumberOfBucketsAvailable() {
			return this.numberOfBucketsAvailable;
		}
	}
}
