import java.util.concurrent.CyclicBarrier;

public class Testapd {
	static int v[];
	static int rounds[];

	public static CyclicBarrier barrier;

	public static void main(String[] args) {
		int P = Integer.parseInt(args[0]);
		barrier = new CyclicBarrier(2*P);

		v = new int[2*P];
		rounds = new int[2*P];
		for (int i = 0; i < 2*P; i++) {
			v[i] = -2;
			rounds[i] = 0;
		}

		java.lang.Thread[] threads = new java.lang.Thread[2*P];

		for(int i = 0; i < 2*P; ++i) {
			threads[i] = new java.lang.Thread(new Thread(i, 2*P));
			threads[i].start();
		}

		try {
			for(int i = 0; i < 2*P; ++i)
				threads[i].join();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
