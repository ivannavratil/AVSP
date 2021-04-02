import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

public class SimHashBuckets {

	private static final int BANDS = 8;

	private static final int HASH_LENGTH = 128;

	private static final int bandSize = HASH_LENGTH / BANDS;

	public static void main(String[] args) throws IOException {
		long start = System.nanoTime();
		//SimHashBuckets.start("lab1\\src\\main\\resources\\lab1B_primjer\\test0\\R.in");
		//SimHashBuckets.start("lab1\\src\\main\\resources\\lab1B_primjer\\test2\\R.in");
		SimHashBuckets.start();
		System.err.println((System.nanoTime() - start) / Math.pow(10, 9));
	}

	static void start() throws IOException {
		start(null);
	}

	static void start(String path) throws IOException {
		try (BufferedReader br = Util.setBufferedReader(path)) {

			int N = Integer.parseInt(br.readLine());

			List<char[]> hashInBinary = new ArrayList<>(N);


			for (int i = 0; i < N; i++) {
				hashInBinary.add(Util.simhash(br.readLine(), Util.ReturnType.Binary).toCharArray());
			}

			Map<Integer, Set<Integer>> kandidati = LSH(N, hashInBinary);

			int Q = Integer.parseInt(br.readLine());


			for (int i = 0; i < Q; i++) {
				int similiarDocs = 0;
				String[] line = br.readLine().split("\\s+");
				int thisDocNumber = Integer.parseInt(line[0]);
				char[] thisDocument = hashInBinary.get(thisDocNumber);
				int hammingDistance = Integer.parseInt(line[1]);


				Set<Integer> similiarCandidates = kandidati.get(thisDocNumber);

				//kroz svaki dokument
				for (Integer candidate : similiarCandidates) {

					//redom po bitovima
					char[] other = hashInBinary.get(candidate);

					similiarDocs = SimHash.getSimiliarDocs(similiarDocs, thisDocument, hammingDistance, other);
				}
				System.out.println(similiarDocs);
			}
		}
	}

	private static Map<Integer, Set<Integer>> LSH(int N, List<char[]> hashInBinary) {
		//redni broj dokumenta -> slicni dokumenti
		Map<Integer, Set<Integer>> kandidati = new HashMap<>();

		//For each band b
		for (int band = 1; band <= BANDS; band++) {

			//bucket -> svi dokumenti u tom bucketu (bucket = pretinac)
			HashMap<Integer, Set<Integer>> pretinci = new HashMap<>();

			//za svaki dokument
			for (int trenutni_id = 0; trenutni_id < N; trenutni_id++) {
				//izracunaj hash cijelog dokumenta
				char[] hash = hashInBinary.get(trenutni_id);

				//uzmi odredeni dio hash-a i pretvori ga u int i taj int je bucket u koji taj dok ide za ovaj band
				int val = hash2int(band, new String(hash));

				//dohvati indekse tekstova u tom pretincu
				Set<Integer> tekstovi_u_pretincu;

				//ako vec postoje dokumenti u tom pretincu
				if (pretinci.containsKey(val)) {
					tekstovi_u_pretincu = pretinci.get(val);

					for (Integer tekstID : tekstovi_u_pretincu) {
						kandidati.putIfAbsent(trenutni_id, new HashSet<>());
						kandidati.get(trenutni_id).add(tekstID);
						kandidati.putIfAbsent(tekstID, new HashSet<>());
						kandidati.get(tekstID).add(trenutni_id);
					}
				} else {
					//ako ne postoje dokumenti u tom pretincu
					tekstovi_u_pretincu = new HashSet<>();
				}
				//dodaj sebe u taj pretinac
				tekstovi_u_pretincu.add(trenutni_id);
				pretinci.put(val, tekstovi_u_pretincu);
			}
		}
		return kandidati;
	}

	private static int hash2int(int band, String hash) {

		int end = HASH_LENGTH - (band - 1) * bandSize;
		int start = end - bandSize;

		String hashPart = hash.substring(start, end);

		return Integer.parseInt(new BigInteger(hashPart, 2).toString(10));
	}
}
