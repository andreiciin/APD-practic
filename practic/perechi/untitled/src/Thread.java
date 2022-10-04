import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ThreadLocalRandom;

public class Thread implements Runnable {
	int thread_id;
	int P;
	int mk;
	int val[] = new int[12];

	Thread(int id, int p) {
		thread_id = id;
		P = p;
	}

	synchronized void check() {
		for (int j = 0; j < 11; j++) {
			if (this.val[j] > Testapd.v[j])
				Testapd.v[j] = this.val[j];
		}
	}

	@Override
	public void run() {

//		System.out.println("Hello from thread with id " + thread_id);
//		int mk = ThreadLocalRandom.current().nextInt(2, 10 + 1);
		int mk = ThreadLocalRandom.current().nextInt(2, 3 + 1);
		this.mk = mk;

		for (int i = 0; i < this.mk; i++) {
			int k = ThreadLocalRandom.current().nextInt(1, 10 + 1);
//			int v = ThreadLocalRandom.current().nextInt(1, 100000 + 1);
			int v = ThreadLocalRandom.current().nextInt(1, 100 + 1);
			int aux = this.val[k];
			if (aux < v) this.val[k] = v;
			System.out.println("[Thread " + thread_id + "] Am generat perechea (" + k + ", " + v +")");

			check();

			try {
				java.lang.Thread.sleep( 1000);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

		// barrier
		try {
			Testapd.barrier.await();
		} catch (BrokenBarrierException | InterruptedException e) {
			e.printStackTrace();
		}

		if (thread_id == 0) {
			for (int i = 1; i < 11; i++)
				System.out.println("(" + i + ", " + Testapd.v[i] + ")");
		}
	}
}
