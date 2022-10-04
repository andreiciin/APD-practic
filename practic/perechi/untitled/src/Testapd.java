import java.util.concurrent.CyclicBarrier;

public class Testapd {
	static int v[];
	public static CyclicBarrier barrier;

	public static void main(String[] args) {
		int P = Integer.parseInt(args[0]);
		barrier = new CyclicBarrier(P);

		v = new int[12];
		for (int i = 0; i < 12; i++) {
			v[i] = 0;
		}

		java.lang.Thread[] threads = new java.lang.Thread[P];

		for(int i = 0; i < P; ++i) {
			threads[i] = new java.lang.Thread(new Thread(i, P));
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
