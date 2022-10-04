import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

public class Main {
	static int suma[];
	static ArrayList<Integer> list;
	public static CyclicBarrier barrier;

	public static void main(String[] args) {
		int N = Integer.parseInt(args[0]);
		int P = Integer.parseInt(args[1]);
		barrier = new CyclicBarrier(P);

		suma = new int[P];
		list = new ArrayList<>(N);

		for (int i = 0; i < N; i++) {
			list.add((i * 67) % 10);
			System.out.print(" i = " + list.get(i));
		}
		System.out.println();
		for (int i = 0; i < P; i++) {
			suma[i] = 0;
		}

		java.lang.Thread[] threads = new java.lang.Thread[P];

		for(int i = 0; i < P; ++i) {
			threads[i] = new java.lang.Thread(new Thread(i, N, P));
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
