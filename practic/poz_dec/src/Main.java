import java.util.concurrent.CyclicBarrier;

public class Main {
    static int v[];
    static int rez[];
    static int sum[];

    public static CyclicBarrier barrier;

    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int P = Integer.parseInt(args[1]);
        int X = Integer.parseInt(args[2]);
        barrier = new CyclicBarrier(P);

        v = new int[N];
        rez = new int[P];
        sum = new int[P];

//        for(int i = 0; i < args.length; i++) {
//            System.out.println(args[i]);
//        }
//        System.out.println();

        // init vec
        for (int i = 0; i < N; i++)
            v[i] = (i + 3) % 5;

//        for (int i = 0; i < N; i++)
//            System.out.print(v[i] + " ");

        int cores = Runtime.getRuntime().availableProcessors();

        // aici cu P in loc de cores
        java.lang.Thread[] threads = new java.lang.Thread[P];

        for(int i = 0; i < P; ++i) {
            threads[i] = new java.lang.Thread(new Thread(i, N, P, X));
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
