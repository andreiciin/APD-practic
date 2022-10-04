import java.util.Arrays;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadLocalRandom;

public class Testapd {
	static Integer vQSort[];
	static Integer M[][];
	public static CyclicBarrier barrier;

	// initializare matrice
	static Integer[][] init(int L) {
		Integer M[][] = new Integer[L][L];

		for (int i = 0; i < L; i++) {
			for (int j = 0; j < L; j++) {
				M[i][j] = ThreadLocalRandom.current().nextInt(1, 20 + 1);
				System.out.print(M[i][j] + " ");
			}
			System.out.println();
		}
		return M;
	}

	static Integer[] copy_matrix_in_vector(Integer M[][], int L) {
		int i, j;
		Integer v[] = new Integer[L * L];
		for (i = 0; i < L; i++) {
			if (i % 2 == 0) {
				for (j = 0; j < L; j++) {
					v[i * L + j] = M[i][j];
				}
			} else {
				for (j = L; j > 0; j--) {
					v[i * L + (L - j)] = M[i][j-1];
				}
			}
		}
		return v;
	}

	public static void main(String[] args) {
		int L = Integer.parseInt(args[0]);
		int P = Integer.parseInt(args[1]);
		int N = L * L; // numarul de elemente de sortat
		// initializam matricea M
		M = init(L);

		// se sorteaza etalonul
		vQSort = copy_matrix_in_vector(M, L);
		Arrays.sort(vQSort);
		System.out.println(" vsort[] : "
				+ Arrays.toString(vQSort));

		barrier = new CyclicBarrier(P);

		java.lang.Thread[] threads = new java.lang.Thread[P];

		for(int i = 0; i < P; ++i) {
			threads[i] = new java.lang.Thread(new Thread(i, N, P, L));
			threads[i].start();
		}

		try {
			for(int i = 0; i < P; ++i)
				threads[i].join();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
