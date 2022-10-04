import java.util.concurrent.CyclicBarrier;

public class Testapd {
		static int v[];
		static int rank[];
		static int sorted[];
		public static CyclicBarrier barrier;

		public static void main(String[] args) {
			int N = Integer.parseInt(args[0]);
			int P = Integer.parseInt(args[1]);
			barrier = new CyclicBarrier(P);

			v = new int[N];
			rank = new int[N];
			sorted = new int[N];

			for (int i = 0; i < N; i++) {
				v[i] = (i * 167) % 13;
				System.out.print(" i = " + v[i]);
			}
			System.out.println();

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
