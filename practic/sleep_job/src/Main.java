import java.util.concurrent.CyclicBarrier;

public class Main {

    static int v[];
    static int p_sleep_time[];

    public static CyclicBarrier barrier;

    public static void main(String[] args) {
        int P = Integer.parseInt(args[0]);
        barrier = new CyclicBarrier(P);

        v = new int[P];
        p_sleep_time = new int[P];

        // citire timpi P-1 threaduri
        for (int i = 0; i < P-1; i++) {
            p_sleep_time[i+1] = Integer.parseInt(args[i+1]);
            v[i] = 0;
        }
        v[P-1] = 0;

        //for (int i = 0; i < P-1; i++)
          //  System.out.println("Thread time " + (i+1) + " " + p_sleep_time[i+1]);

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
