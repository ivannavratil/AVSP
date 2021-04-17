import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Util {

	public static ParsedInput parse() throws IOException {
		return parse(null);
	}

	public static ParsedInput parse(String path) throws IOException {
		BufferedReader br = setBufferedReader(path);

		String[] firstLine = br.readLine().split("\s++");

		int numberOfItems = Integer.parseInt(firstLine[0]);
		int numberOfUsers = Integer.parseInt(firstLine[1]);

		Double[][] userItemMatrix = new Double[numberOfItems][numberOfUsers];

		for (int i = 0; i < numberOfItems; i++) {
			Double[] ints = new Double[numberOfUsers];
			String[] elementInLine = Arrays.stream(br.readLine().split("\\s++")).map(String::strip).toArray(String[]::new);
			for (int j = 0; j < elementInLine.length; j++) {
				ints[j] = elementInLine[j].equals("X") ? null : (Double.parseDouble(elementInLine[j]));
			}
			userItemMatrix[i] = ints;
		}

		int numberOfQueries = Integer.parseInt(br.readLine());

		List<int[]> queries = new ArrayList<>(numberOfQueries);

		for (int i = 0; i < numberOfQueries; i++) {
			int[] ints = Arrays.stream(br.readLine().split("\s++")).mapToInt(Integer::parseInt).toArray();
			ints[0] = ints[0] - 1;
			ints[1] = ints[1] - 1;
			queries.add(ints);
		}

		br.close();

		return new ParsedInput(userItemMatrix, queries);
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

		/**
		 * item - retci
		 * <p>
		 * user - stupci
		 */
		private final Double[][] userItemMatrix;

		/**
		 * upiti, oznake u polju:
		 *
		 * <p>
		 * 1. jedna stavka u matrici (za koju stavku racunamo vrijednost preporuke)
		 *
		 * <p>
		 * 2. jednog korisnika u matrici (za kojeg korisnika racunamo vrijendost preporuke)
		 *
		 * <p>
		 * 3. tip algoritama - 0 za item-item pristup suradnickog filtriranja, 1 za user-user pristup
		 *
		 * <p>
		 * 4. maksimalni kardinalni broj skupa slicnih stavki/korisnka koje sustav preporuke razma prilikom racunanja vrijednost preporuka
		 */
		private final List<int[]> queries;

		public ParsedInput(Double[][] userItemMatrix, List<int[]> queries) {
			this.userItemMatrix = userItemMatrix;
			this.queries = queries;
		}

		public Double[][] getUserItemMatrix() {
			return this.userItemMatrix;
		}

		public List<int[]> getQueries() {
			return this.queries;
		}
	}
}
