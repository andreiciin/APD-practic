import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ThreadLocalRandom;

public class Thread implements Runnable {
	int thread_id;
	int N;
	int P;
	int L;

	Thread(int id, int n, int p, int l) {
		thread_id = id;
		N = n;
		P = p;
		L = l;
	}

	@Override
	public void run() {

		System.out.println("Hello from thread with id " + thread_id);

		for (int k = 0; k < Math.sqrt(N) + 1; ++k) {
			for (int i = thread_id; i < L; i += P) {
				if (i % 2 == 1) {
					Arrays.sort(Testapd.M[i], Collections.reverseOrder());
				} else {
					Arrays.sort(Testapd.M[i]);
				}
			}
			// barrier
			try {
				Testapd.barrier.await();
			} catch (BrokenBarrierException | InterruptedException e) {
				e.printStackTrace();
			}

			for (int i = thread_id; i < L; i += P) {
				Integer aux[] = new Integer[L];
				for (int j = 0; j < L; j++) {
					aux[j] = Testapd.M[j][i];
				}
				Arrays.sort(aux);

				for (int j = 0; j < L; j++) {
					Testapd.M[j][i] = aux[j];
				}
			}

			// barrier
			try {
				Testapd.barrier.await();
			} catch (BrokenBarrierException | InterruptedException e) {
				e.printStackTrace();
			}
		}

		// barrier
		try {
			Testapd.barrier.await();
		} catch (BrokenBarrierException | InterruptedException e) {
			e.printStackTrace();
		}

		if (thread_id == 0) {

			Integer v[] = Testapd.copy_matrix_in_vector(Testapd.M, L);
			System.out.println(" v[] : "
					+ Arrays.toString(v));
			int ok = 1;
			for (int i = 0; i < N; i++) {
				if (v[i] != Testapd.vQSort[i]) {
					System.out.println("Sortare incorecta!");
					ok = 0;
					break;
				}
			}

			if (ok == 1) System.out.println("Sortare corecta!");
		}
	}
}
