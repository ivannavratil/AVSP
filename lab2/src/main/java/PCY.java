import java.io.IOException;
import java.util.*;

public class PCY {

	public static void main(String[] args) throws IOException {
		long start = System.nanoTime();
		//PCY.start();
		PCY.start("lab2\\src\\main\\resources\\test2\\R.in");
		System.err.println((System.nanoTime() - start) / Math.pow(10, 9));
	}

	private static void start(String path) throws IOException {
		Util.ParsedInput input = Util.parse(path);

		//broj kosara u datoteci
		int numberOfBaskets = input.getNumberOfBaskets();

		//prag podrske
		double s = input.getS();

		List<List<Integer>> listOfBaskets = input.getList();

		int threshold = (int) (s * numberOfBaskets);

		//broj pretinaca na raspolaganju funkciji sazimanja
		int numberOfBucketsAvailable = input.getNumberOfBucketsAvailable();

		int difProducts = input.getNumberOfDifferentProducts();

		//brojac predmeta
		int[] brPredmeta = new int[difProducts];


		//prvi prolaz - brojanje pojavljivanja predmeta
		for (List<Integer> listOfBasket : listOfBaskets) {
			for (Integer item : listOfBasket) {
				brPredmeta[item - 1]++;
			}
		}


		//pretinci za funkciju sazimanja
		int[] pretinci = new int[numberOfBucketsAvailable];

		//drugi prolaz - sazimanje


		for (List<Integer> basket : listOfBaskets) {
			final int basketSize = basket.size();
			for (int i = 0; i < basketSize; i++) {
				int x = basket.get(i);
				if ((brPredmeta[x - 1] < threshold)) {
					continue;
				}
				for (int j = i + 1; j < basketSize; j++) {
					int y = basket.get(j);
					if ((brPredmeta[y - 1] >= threshold)) {
						//ako su dva predmeta cesta, dodaj vrijednost pretinca u koji ti predmeti idu
						pretinci[((x * difProducts) + y) % numberOfBucketsAvailable]++;
					}
				}
			}
		}


		// par predmeta -> vrijednost broja ponavljanja
		HashMap<Pair, Integer> pairsRepeated = new HashMap<>();

		//treci prolaz - brojanje parova
		for (List<Integer> basket : listOfBaskets) {
			for (int i = 0; i < basket.size(); i++) {
				for (int j = i + 1; j < basket.size(); j++) {

					int x = basket.get(i);

					if (brPredmeta[x - 1] < threshold) {
						continue;
					}

					int y = basket.get(j);
					if (brPredmeta[y - 1] >= threshold) {
						if (pretinci[((x * difProducts) + y) % numberOfBucketsAvailable] >= threshold) {
							Pair p = new Pair(x, y);
							if (pairsRepeated.computeIfPresent(p, (key, val) -> val + 1) == null) {
								pairsRepeated.putIfAbsent(p, 1);
							}
						}
					}
				}
			}
		}

		long m = Arrays.stream(brPredmeta).parallel().filter(a -> a >= threshold).count();

		//ukupan broj kandidata cestih parova koje bi brojao algoritam A-priori
		System.out.println((m * (m - 1)) / 2);

		//ukupan broj parova koje prebrojava algoritam main.PCY. Radi se samo o parovima koji se sazimaju u cesti pretinac.
		System.out.println(pairsRepeated.size());

		//silazno sortirani brojevi ponavljanja cestih parova
		pairsRepeated.values().stream().sorted(Comparator.reverseOrder()).forEach(System.out::println);
	}

	static void start() throws IOException {
		start(null);
	}

	private static class Pair {
		int first;

		int second;

		public Pair(int first, int second) {
			this.first = first;
			this.second = second;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || this.getClass() != o.getClass()) {
				return false;
			}
			Pair pair = (Pair) o;
			return this.first == pair.first && this.second == pair.second;
		}

		@Override
		public String toString() {
			return "(" + this.first + "," + this.second + ")";
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.first, this.second);
		}
	}
}
