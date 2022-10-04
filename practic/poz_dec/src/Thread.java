import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;

public class Thread implements Runnable {
    int thread_id;
    int search_elem;
    int N;
    int P;

    Thread(int id, int n, int p, int x) {
        thread_id = id;
        N = n;
        P = p;
        search_elem = x;
    }

    @Override
    public void run() {

        //System.out.println("Hello from thread with id " + thread_id);

        int start = (int) (thread_id * (double) N / P);
        int end = (int) Math.min((thread_id + 1) * (double) N / P, N);

        for(int i = start; i < end; ++i) {
            if (Main.v[i] == search_elem) {
                Main.rez[thread_id]++;
                Main.sum[thread_id] += i;
            }
               // System.out.println("poz " + i + " " + Main.v[i] + " " + search_elem);
        }

        // barrier
        try {
            Main.barrier.await();
        } catch (BrokenBarrierException | InterruptedException e) {
            e.printStackTrace();
        }

        if (thread_id == 0) {
            int apar = 0;
            int sum_poz = 0;

            for (int i = 0; i < P; i++) {
                apar += Main.rez[i];
                sum_poz += Main.sum[i];
            }

            System.out.println(apar + " " + sum_poz);

        }
    }
}
