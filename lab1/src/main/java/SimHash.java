import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimHash {
	public static void main(String[] args) throws IOException {
		long start = System.nanoTime();
		SimHash.LinearHashSearching();
		//SimHash.LinearHashSearching("lab1\\src\\main\\resources\\lab1A_primjer\\test2\\R.in");
		System.err.println((System.nanoTime() - start) / Math.pow(10, 9));
	}

	static void LinearHashSearching() throws IOException {
		LinearHashSearching(null);
	}

	static void LinearHashSearching(String path) throws IOException {
		try (BufferedReader br = Util.setBufferedReader(path)) {

			int N = Integer.parseInt(br.readLine());

			List<char[]> hashInBinary = new ArrayList<>(N);

			for (int i = 0; i < N; i++) {
				hashInBinary.add(Util.simhash(br.readLine(), Util.ReturnType.Binary).toCharArray());
			}

			int Q = Integer.parseInt(br.readLine());


			for (int i = 0; i < Q; i++) {
				int similiarDocs = 0;
				String[] line = br.readLine().split("\\s+");
				int thisDocNumber = Integer.parseInt(line[0]);
				char[] thisDocument = hashInBinary.get(thisDocNumber);
				int hammingDistance = Integer.parseInt(line[1]);

				//kroz svaki dokument
				for (char[] other : hashInBinary) {
					if (other == thisDocument) {
						continue;
					}

					similiarDocs = getSimiliarDocs(similiarDocs, thisDocument, hammingDistance, other);
				}
				System.out.println(similiarDocs);
			}
		}
	}


	//byte -> binary

	public static int getSimiliarDocs(int similiarDocs, char[] doc, int hammingDistance, char[] other) {
		int distanceCounter = 0;

		boolean similiar = true;
		for (int k = 0; k < other.length; k++) {
			if (other[k] != doc[k]) {
				distanceCounter++;
				if (distanceCounter > hammingDistance) {
					similiar = false;
					break;
				}
			}
		}
		if (similiar) {
			similiarDocs++;
		}
		return similiarDocs;
	}
}
