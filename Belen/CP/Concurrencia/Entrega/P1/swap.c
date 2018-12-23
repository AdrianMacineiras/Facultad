#include <errno.h>
#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include "options.h"

struct buffer {
	int *data;
	int size;
	pthread_mutex_t *mutex; // array de mutex
};

struct thread_info {
	pthread_t       thread_id;        /* ID returned by pthread_create() */
	int             thread_num;       /* Application-defined thread # */
};

struct args {
	int 			thread_num;       /* Application-defined thread # */
	int 	        delay;			  /* delay between operations */
	int				*iterations; 	// Compartir iterations igual que el buffer (con el puntero)
	struct buffer   *buffer;		  /* Shared buffer */
	pthread_mutex_t *mutex;
};

void *swap(void *ptr)
{
	struct args *args =  ptr;
	int success;

	while (1) {
		pthread_mutex_lock(args->mutex);
		if (!(*args->iterations)) { // pasar la condicion para dentro para facilitar el lock y el unlock
			pthread_mutex_unlock(args->mutex);
			pthread_exit(args->mutex);
		} else {
			(*args->iterations)--;
			pthread_mutex_unlock(args->mutex);


			int i, j, tmp;

			i = rand() % args->buffer->size;
			j = rand() % args->buffer->size;

			if (i == j) {
				printf("i y j son iguales.\n");
				continue;
			}
			success = 0;
			do {
				pthread_mutex_lock(&args->buffer->mutex[i]);

				if (pthread_mutex_trylock(&args->buffer->mutex[j]) == 0) {
					tmp = args->buffer->data[i];
					if (args->delay) usleep(args->delay); // Let other threads in...

					args->buffer->data[i] = args->buffer->data[j];
					if (args->delay) usleep(args->delay);

					args->buffer->data[j] = tmp;
					if (args->delay) usleep(args->delay);
					pthread_mutex_unlock(&args->buffer->mutex[j]);
					success = 1;
				}

				pthread_mutex_unlock(&args->buffer->mutex[i]);
			} while (!success);
		}
	}
	return NULL;
}

void print_buffer(struct buffer buffer) {
	int i;

	for (i = 0; i < buffer.size; i++)
		printf("%i ", buffer.data[i]);
	printf("\n");
}

void start_threads(struct options opt)
{
	int i;
	struct thread_info *threads;
	struct args *args;
	struct buffer buffer;


	srand(time(NULL));

	if ((buffer.data = malloc(opt.buffer_size * sizeof(int))) == NULL) {
		printf("Not enough memory\n");
		exit(1);
	}

	if ((buffer.mutex = malloc(opt.buffer_size * sizeof(pthread_mutex_t))) == NULL) {
		printf("Not enough memory\n");
		exit(1);
	}

	buffer.size = opt.buffer_size;

	for (i = 0; i < buffer.size; i++) {
		if (pthread_mutex_init(&buffer.mutex[i], NULL) != 0) {
			exit(0);
		}
		buffer.data[i] = i;
	}

	printf("creating %d threads\n", opt.num_threads);
	threads = malloc(sizeof(struct thread_info) * opt.num_threads);
	args = malloc(sizeof(struct args) * opt.num_threads);

	if ((args->mutex = malloc(opt.buffer_size * sizeof(pthread_mutex_t))) == NULL) {
		printf("Not enough memory\n");
		exit(1);
	}
	if (pthread_mutex_init(args->mutex, NULL) != 0) {
		exit(0);
	}

	if (threads == NULL || args == NULL) {
		printf("Not enough memory\n");
		exit(1);
	}

	printf("Buffer before: ");
	print_buffer(buffer);


	/* Create independent threads running void *swap() */
	for (i = 0; i < opt.num_threads; i++) {
		threads[i].thread_num = i;

		args[i].thread_num = i;
		args[i].buffer     = &buffer;
		args[i].delay      = opt.delay;
		args[i].iterations = &opt.iterations;
		args[i].mutex = args->mutex;

		if ( 0 != pthread_create(&threads[i].thread_id, NULL,
		                         swap, &args[i])) {
			printf("Failing creating thread %d", i);
			exit(1);
		}
	}

	for (i = 0; i < opt.num_threads; i++)
		pthread_join(threads[i].thread_id, NULL);

	/* Print the buffer */
	printf("Buffer after:  ");
	print_buffer(buffer);

	pthread_mutex_destroy(args->mutex);
	for (i = 0; i < buffer.size; i++) {
		pthread_mutex_destroy(&buffer.mutex[i]);
	}
	pthread_exit(NULL);
}

int main (int argc, char **argv)
{
	struct options opt;

	opt.num_threads = 10;
	opt.buffer_size = 10;
	opt.iterations = 100;
	opt.delay = 0;

	read_options(argc, argv, &opt);

	start_threads(opt);

	exit (0);
}
