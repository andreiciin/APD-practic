import java.util.concurrent.BrokenBarrierException;

public class Thread implements Runnable {

    int thread_id;
    int P;

    Thread(int id, int p) {
        thread_id = id;
        P = p;
    }

    @Override
    public void run() {

        //System.out.println("Hello from thread with id " + thread_id);

        if (thread_id != 0) {
            try {
                java.lang.Thread.sleep(Main.p_sleep_time[thread_id] * 100);
                Main.v[thread_id] = 1;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        boolean waitMe = true;
        int ok[] = new int[P];
        if (thread_id == 0) {
            while(waitMe) {
                int count = 0;
                // un fel de busy waiting
                for (int i = 1; i < P; i++) {
                    if (Main.v[i] == 1) {
                        System.out.println("Thread-ul " + i + " a terminat");
                        Main.v[i] = 0;
                        ok[i] = 1;
                    }
                    if (ok[i] == 1) count++;
                    if (count == P-1) waitMe = false;
                }
            }
        }
    }
}
