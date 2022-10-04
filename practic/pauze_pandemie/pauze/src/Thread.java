import java.util.concurrent.BrokenBarrierException;

public class Thread implements Runnable {

	int thread_id;
	int P;
	int N;

	Thread(int id, int p, int n) {
		thread_id = id;
		P = p;
		N = n;
	}

	@Override
	public void run() {

		System.out.println("Hello from thread with id " + thread_id);

		int start = (int) (thread_id * (double) N / P);
		int end = (int) Math.min((thread_id + 1) * (double) N / P, N);

		if (thread_id != 0) {
			for (int i = start; i < end; ++i) {
				if (Main.servetele[i] == 1) {
					Main.servetele[i]--;
				}
			}
		}

		// barrier
		try {
			Main.barrier.await();
		} catch (BrokenBarrierException | InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("N = " + N);
		for (int i = 0; i < N; i++) System.out.print("i = " + Main.servetele[i] + " ");
		System.out.println();

		if (thread_id == 0) {
			int ok = 0;
			for (int i = 1; i < N; i++)
				if (Main.servetele[i] == 0)
					ok++;
			if (ok == N-1) System.out.println("Toti elevii au luat servetele!");
			else System.out.println("Nu toti!");
		}
	}
}
