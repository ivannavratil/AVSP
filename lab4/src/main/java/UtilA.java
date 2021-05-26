import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UtilA {

	public static ParsedInput parse() throws IOException {
		return parse(null);
	}

	public static ParsedInput parse(String path) throws IOException {
		BufferedReader br = setBufferedReader(path);

		String[] firstLine = br.readLine().split("\\s+");

		int numberOfVertices = Integer.parseInt(firstLine[0]);
		double beta = Double.parseDouble(firstLine[1]);

		List<int[]> graphMap = new ArrayList<>(numberOfVertices);

		List<List<Integer>> graphMapReverse = new ArrayList<>(numberOfVertices);
		for (int i = 0; i < numberOfVertices; i++) {
			graphMapReverse.add(new ArrayList<>(15));
		}


		for (int i = 0; i < numberOfVertices; i++) {
			String[] split = br.readLine().split("\\s+");
			int[] ints = Arrays.stream(split)
			                   .mapToInt(Integer::parseInt)
			                   .toArray();

			graphMap.add(ints);

			for (int anInt : ints) {
				graphMapReverse.get(anInt).add(i);
			}
		}

		int numberOfQueries = Integer.parseInt(br.readLine());

		List<int[]> queries = new ArrayList<>(numberOfQueries);

		for (int i = 0; i < numberOfQueries; i++) {
			queries.add(Arrays.stream(br.readLine().split("\\s+"))
			                  .mapToInt(Integer::parseInt)
			                  .toArray());
		}

		br.close();

		return new ParsedInput(beta, graphMap, graphMapReverse, queries);
	}

	private static BufferedReader setBufferedReader(String path) throws FileNotFoundException {
		return path == null ? new BufferedReader(new InputStreamReader(System.in)) : new BufferedReader(new FileReader(path));
	}

	public static class ParsedInput {

		List<int[]> queries;

		double beta;

		List<int[]> forwards;

		List<List<Integer>> backwards;

		public ParsedInput(double beta, List<int[]> forwards, List<List<Integer>> backwards, List<int[]> queries) {
			this.queries = queries;
			this.backwards = backwards;
			this.beta = beta;
			this.forwards = forwards;
		}
	}
}
