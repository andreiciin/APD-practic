#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

#include "pthread_barrier_mac.h"

#define NUM_THREADS 2
// Number of vertices in the graph
#define V 4
#define INF 99999

#define min(a,b) (((a)<(b))?(a):(b))

int graph[V][V] = { {0,   5,  INF, 10},
                        {INF, 0,   3, INF},
                        {INF, INF, 0,   1},
                        {INF, INF, INF, 0}
                  };

pthread_barrier_t barrier;                      

// A function to print the solution matrix
void printSolution(int dist[][V]);

void *f(void *arg)
{
    int thread_id = *(int *)arg;

    int start = thread_id * V / NUM_THREADS;
    int end = min(V, (thread_id + 1) * V / NUM_THREADS);
    /* dist[][] will be the output matrix
      that will finally have the shortest
      distances between every pair of vertices */
    int dist[V][V], i, j, k;
 
    /* Initialize the solution matrix
      same as input graph matrix. Or
       we can say the initial values of
       shortest distances are based
       on shortest paths considering no
       intermediate vertex. */
    for (i = 0; i < V; i++)
        for (j = 0; j < V; j++)
            dist[i][j] = graph[i][j];

    //  pthread_barrier_wait(&barrier);        
 
    /* Add all vertices one by one to
      the set of intermediate vertices.
      ---> Before start of an iteration, we
      have shortest distances between all
      pairs of vertices such that the shortest
      distances consider only the
      vertices in set {0, 1, 2, .. k-1} as
      intermediate vertices.
      ----> After the end of an iteration,
      vertex no. k is added to the set of
      intermediate vertices and the set
      becomes {0, 1, 2, .. k} */
    for (k = 0; k < V; k++)
    {
        // Pick all vertices as source one by one
        for (i = start; i < end; i++)
        {
            // Pick all vertices as destination for the
            // above picked source
            for (j = 0; j < V; j++)
            {
                // If vertex k is on the shortest path from
                // i to j, then update the value of dist[i][j]
                if (dist[i][k] + dist[k][j] < dist[i][j])
                    dist[i][j] = dist[i][k] + dist[k][j];
            }
        }
    }

    pthread_barrier_wait(&barrier);
    
    if (thread_id == 0) {
        // Print the shortest distance matrix
        printSolution(dist);
    }
    pthread_exit(NULL);
}

/* A utility function to print solution */
void printSolution(int dist[][V])
{
    printf ("The following matrix shows the shortest distances"
            " between every pair of vertices \n");
    for (int i = 0; i < V; i++)
    {
        for (int j = 0; j < V; j++)
        {
            if (dist[i][j] == INF)
                printf("%7s", "INF");
            else
                printf ("%7d", dist[i][j]);
        }
        printf("\n");
    }
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

    for (int i = 1; i < argc; i++) {
        printf("arg %d = %s\n", i, argv[i]);
    }                

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

//   0      5      8      9
//     INF      0      3      4
//     INF    INF      0      1
//     INF    INF    INF      0
