import java.util.concurrent.CyclicBarrier;

public class Main {
	static int servetele[];

	public static CyclicBarrier barrier;

	public static void main(String[] args) {
		int P = Integer.parseInt(args[0]);
		int N = Integer.parseInt(args[1]);
		barrier = new CyclicBarrier(P);

		servetele = new int[N];
		for (int i = 0; i < N; i++) servetele[i] = 1;

		java.lang.Thread[] threads = new java.lang.Thread[P];

		for(int i = 0; i < P; ++i) {
			threads[i] = new java.lang.Thread(new Thread(i, P, N));
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
