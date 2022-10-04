import com.sun.tools.javac.Main;

import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ThreadLocalRandom;

public class Thread implements Runnable {
	int thread_id;
	int P;

	Thread(int id, int p) {
		thread_id = id;
		P = p;
	}

	void play(int randomNum) {
		String choice = "pp";
		if (randomNum == 0) choice = "HARTIE";
		if (randomNum == 1) choice = "FOARFECA";
		if (randomNum == 2) choice = "PIATRA";

		if (thread_id != P-1 && thread_id >= 0) {
			int vec_id = thread_id + 1;
			while (Testapd.v[vec_id] < 0 && vec_id < P-1) vec_id++;

			if (vec_id >= 0 && Testapd.v[vec_id] >= 0) {
				String choiceEn = "bb";
				if (Testapd.v[vec_id] == 0) choiceEn = "HARTIE";
				if (Testapd.v[vec_id] == 1) choiceEn = "FOARFECA";
				if (Testapd.v[vec_id] == 2) choiceEn = "PIATRA";

				System.out.println("Threadul " + thread_id + " care a ales " + choice +
						" se bate cu threadul " + vec_id + " care a ales " + choiceEn);
				if (Testapd.v[thread_id] > Testapd.v[vec_id]) {
					Testapd.v[vec_id] = -1;
					Testapd.rounds[thread_id]++;
				} else {
					Testapd.v[thread_id] = -1;
					Testapd.rounds[vec_id]++;
				}
			}
		} else if (thread_id == P-1 && thread_id >= 0) {
			int vec_id = 0;
			while (Testapd.v[vec_id] < 0 && vec_id < P-2) vec_id++;
			String choiceEn1 = "aa";
			if (Testapd.v[vec_id] == 0) choiceEn1 = "HARTIE";
			if (Testapd.v[vec_id] == 1) choiceEn1 = "FOARFECA";
			if (Testapd.v[vec_id] == 2) choiceEn1 = "PIATRA";

			System.out.println("Threadul " + thread_id + " care a ales " + choice +
					" se bate cu threadul " + vec_id + " care a ales " + choiceEn1);
			if (Testapd.v[thread_id] > Testapd.v[vec_id]) {
				Testapd.v[vec_id] = -1;
				Testapd.rounds[thread_id]++;
			} else {
				Testapd.v[thread_id] = -1;
				Testapd.rounds[vec_id]++;
			}
		}

	}

	@Override
	public void run() {

//		System.out.println("Hello from thread with id " + thread_id);

		int randomNum = ThreadLocalRandom.current().nextInt(0, 2 + 1);
		Testapd.v[thread_id] = randomNum;

		// barrier
		try {
			Testapd.barrier.await();
		} catch (BrokenBarrierException | InterruptedException e) {
			e.printStackTrace();
		}

		play(randomNum);

		// barrier
		try {
			Testapd.barrier.await();
		} catch (BrokenBarrierException | InterruptedException e) {
			e.printStackTrace();
		}

		randomNum = ThreadLocalRandom.current().nextInt(0, 2 + 1);
		if (Testapd.v[thread_id] >= 0) Testapd.v[thread_id] = randomNum;
		play(randomNum);

		try {
			Testapd.barrier.await();
		} catch (BrokenBarrierException | InterruptedException e) {
			e.printStackTrace();
		}

		randomNum = ThreadLocalRandom.current().nextInt(0, 2 + 1);
		if (Testapd.v[thread_id] >= 0) Testapd.v[thread_id] = randomNum;
		play(randomNum);

		try {
			Testapd.barrier.await();
		} catch (BrokenBarrierException | InterruptedException e) {
			e.printStackTrace();
		}

		if (thread_id == 0) {
			ArrayList<Integer> list = new ArrayList<>(5);
			for (int i = 0; i < P; i++) {
				//System.out.println(Testapd.v[i]);
				if (Testapd.v[i] >= 0) list.add(i);
			}
			if (list.size() == 1) System.out.println("Threadul " + list.get(0) + " a castigat turneul dupa " +
					Testapd.rounds[list.get(0)] + " runde");
		}
	}
}
