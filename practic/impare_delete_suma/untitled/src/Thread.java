import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;

public class Thread implements Runnable {
	int thread_id;
	int N;
	int P;

	Thread(int id, int n, int p) {
		thread_id = id;
		N = n;
		P = p;
	}

	synchronized void deleteL(ArrayList<Integer> rem) {
		for (int i = 0; i < rem.size(); i++) {
			System.out.println("toRemove = " + rem.get(i));
			for (int j = 0; j < Main.list.size(); j++) {
				if (Main.list.get(j) == rem.get(i)) {
					Main.list.remove(j);
				}
			}
		}
	}

	@Override
	public void run() {

		System.out.println("Hello from thread with id " + thread_id);

		int start = (int) (thread_id * (double) Main.list.size() / P);
		int end = (int) Math.min((thread_id + 1) * (double) Main.list.size() / P, Main.list.size());

		ArrayList<Integer> toRemove = new ArrayList<>();
		for (int i = start; i < end; ++i) {
			if (Main.list.get(i) % 2 != 0) {
				//Main.list.remove(i);
				toRemove.add(Main.list.get(i));
			} else {
				Main.suma[thread_id] += Main.list.get(i);
			}
		}

		// barrier
		try {
			Main.barrier.await();
		} catch (BrokenBarrierException | InterruptedException e) {
			e.printStackTrace();
		}

		// mutex
		deleteL(toRemove);

		// barrier
		try {
			Main.barrier.await();
		} catch (BrokenBarrierException | InterruptedException e) {
			e.printStackTrace();
		}

		if (thread_id == 0) {
			int totalSuma = 0;
			for (int i = 0; i < P; i++) totalSuma += Main.suma[i];
			System.out.println("Suma pare = " + totalSuma);
			System.out.println("Elem lista pare: ");

			for (int i = 0; i < Main.list.size(); i++) {
				System.out.println(Main.list.get(i));
			}

		}
	}
}
