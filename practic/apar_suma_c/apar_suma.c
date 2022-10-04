#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

#include "pthread_barrier_mac.h"

#define NUM_THREADS 2

#define min(a,b) (((a)<(b))?(a):(b))

int N, suma = 0, par_sum = 0; // nr elem, suma de verificare, suma paralela
int arr[10000], sum[10000]; // vector elem

pthread_barrier_t barrier;

void *f(void *arg)
{
	int thread_id = *(int *)arg;

    int start = thread_id * N-1 / NUM_THREADS;
    int end = min(N-1, (thread_id + 1) * N-1 / NUM_THREADS);

    int loc_sum = 0;
    for (int i = start; i < end; ++i)
    {
        loc_sum += arr[i];
    }
    sum[thread_id] += loc_sum;

	pthread_barrier_wait(&barrier);

    if (thread_id == 0) {
        int par_sum = 0;
        for (int i = 0; i < NUM_THREADS; ++i)
                par_sum += sum[i];

        printf("Suma paral e %d \n", par_sum);
        // verificare rezultat
        int afis = (N * (N+1))/2 - par_sum;
        printf("Nr lipsa e %d \n", afis);   
    }     

	pthread_exit(NULL);
}

int main(int argc, char **argv)
{
	int i, r;
	void *status;
	pthread_t threads[NUM_THREADS];
	int arguments[NUM_THREADS];

	r = pthread_barrier_init(&barrier, NULL, NUM_THREADS);

	if (r != 0) {
		printf("Barrier init failed!");
		return -1;
	}

    scanf("%d", &N); // citire nr threaduri
    suma = N * (N+1) / 2;
    printf("Suma precalc e %d \n", suma);
    for (int i = 0; i < N-1; i++) // citire N-1 numere
        scanf("%d", &arr[i]); 

	for (i = 0; i < NUM_THREADS; i++) {
		arguments[i] = i;
		r = pthread_create(&threads[i], NULL, f, &arguments[i]);

		if (r) {
			printf("Eroare la crearea thread-ului %d\n", i);
			exit(-1);
		}
	}

	for (i = 0; i < NUM_THREADS; i++) {
		r = pthread_join(threads[i], &status);

		if (r) {
			printf("Eroare la asteptarea thread-ului %d\n", i);
			exit(-1);
		}
	}

	r = pthread_barrier_destroy(&barrier);

	if (r != 0) {
		printf("Barrier destroy failed!");

		return -1;
	} 

	return 0;
}
