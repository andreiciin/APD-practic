#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>

#include "pthread_barrier_mac.h"

#define MAX 50

int count; // count == N
int list[MAX], rank_list[50] = {0}, sort_list[50] = {0};
long cores;
pthread_barrier_t barrier;

void *calc_rank(void *arg) {

   int i,j;
   long thread_id = *(long *)arg;

	int start = thread_id * (double) count / cores;
	int end = (thread_id + 1) * (double) count / cores;
    if (end > count) {
		end = count;
	}

    printf("id %ld start %d end %d \n", thread_id, start, end);

    //Calculate the rank for each element in the arr_list/

	for(i=start; i<end; i++)
	{
		for(j=0; j<i; j++)
		{
			if(list[i] >= list[j])
				rank_list[i]++;
			else
				rank_list[j]++;
		}	
	}	

    pthread_barrier_wait(&barrier);

    // afisare un singur thread
    if (thread_id == 0) {
        printf("\nRank for each item in the unsorted list\n");
        printf("\n\tElement\tRank\n");
        for(long i=0; i<count; i++)
            printf("\t%d\t%d\n",list[i],rank_list[i]+1);
    }

    for(long i=start; i<end; i++)
		sort_list[rank_list[i]] = list[i];

    pthread_barrier_wait(&barrier);
    // afisare un sg thread
    if (thread_id == 0) {
	    printf("\nSorted listed according to the rank\n\t");
	    for(long i=0; i<count; i++)
		    printf("%d ",sort_list[i]);
	    printf("\n");	    
    }
}



int main(int argc, char *argv[])
{
	//printf("\nEnter the number of items (1<=X<=50): ");
	//scanf("%d",&count);
    count = atoi(argv[1]);
    cores = atoi(argv[2]);
	for(long i=0; i<count; i++)
		scanf("%d",&list[i]);

    // de aici incep cu threaduri
    //cores = sysconf(_SC_NPROCESSORS_CONF);
	printf("Number of cores is: %ld\r\n", cores);

    int r;
    void *status;
	pthread_t threads[cores]; // sau num_threads == P
	long arguments[cores];

    r = pthread_barrier_init(&barrier, NULL, cores);

	if (r != 0) {
		printf("Barrier init failed!");

		return -1;
	} 

    //calc_rank();
    
    for (long i = 0; i < cores; i++) {
		arguments[i] = i;
		r = pthread_create(&threads[i], NULL, calc_rank, &arguments[i]);

		if (r) {
			printf("Eroare la crearea thread-ului %ld\n", i);
			exit(-1);
		}
	}

	for (long i = 0; i < cores; i++) {
		r = pthread_join(threads[i], &status);

		if (r) {
			printf("Eroare la asteptarea thread-ului %ld\n", i);
			exit(-1);
		}
	}

    r = pthread_barrier_destroy(&barrier);

	if (r != 0) {
		printf("Barrier destroy failed!");

		return -1;
	} 
	//pthread_exit(NULL);
    return 0;
}