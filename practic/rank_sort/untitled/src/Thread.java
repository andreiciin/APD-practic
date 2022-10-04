import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ThreadLocalRandom;

public class Thread implements Runnable {
	int thread_id;
	int P;
	int N;

	Thread(int id, int n, int p) {
		thread_id = id;
		N = n;
		P = p;
	}

	@Override
	public void run() {

		System.out.println("Hello from thread with id " + thread_id);

		int start = (int) (thread_id * (double) N / P);
		int end = (int) Math.min((thread_id + 1) * (double) N / P, N);

		for(int i = start; i < end; i++)
		{
			for(int j = 0; j < i; j++)
			{
				if(Testapd.v[i] >= Testapd.v[j])
					Testapd.rank[i]++;
				else
					Testapd.rank[j]++;
			}
		}
		// barrier
		try {
			Testapd.barrier.await();
		} catch (BrokenBarrierException | InterruptedException e) {
			e.printStackTrace();
		}

		// afisare un singur thread
		if (thread_id == 0) {
			System.out.println("Rank for each item in the unsorted list:");
			System.out.println("Element\tRank");
			for(int i = 0; i < N; i++)
				System.out.println(Testapd.v[i] + "\t" + (Testapd.rank[i]+1));
		}

		for(int i=start; i<end; i++)
			Testapd.sorted[Testapd.rank[i]] = Testapd.v[i];

		// barrier
		try {
			Testapd.barrier.await();
		} catch (BrokenBarrierException | InterruptedException e) {
			e.printStackTrace();
		}

		// afisare un sg thread
		if (thread_id == 0) {
			System.out.println("Sorted listed according to the rank:");
			for(int i=0; i < N; i++)
				System.out.print(Testapd.sorted[i] + " ");
			System.out.println();
		}
	}
}