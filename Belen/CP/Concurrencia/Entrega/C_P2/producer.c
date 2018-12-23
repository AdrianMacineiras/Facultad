
#include <errno.h>
#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <stdbool.h>
#include "options.h"
#include "lista.h"





/**
 * liberar: libera un puntero
 *	@ptr: puntero que se va a liberar
 */
void liberar(void * ptr) {
	// printf("Hola\n");
	free(ptr);
}

/* creation and consumption of elements */

/**
 * Element: Describe one of the elements that we work with
 *
 * @producer: which thread has created the element
 * @value: What is the producer value
 * @time: how long it took to generate the value in microseconds
 */
struct element {
	int producer;
	int value;
	int time;
};

pthread_mutex_t create_mutex = PTHREAD_MUTEX_INITIALIZER; // PTHREAD_COND_INITIALIZER es una macro
int created_elements = 0;;

/**
 * element_create: creates a new element
 *
 * @producer: thread that produces it
 * @seed: this is needed for the random generator to work
 */
struct element *element_create(int producer, unsigned int *seed)
{
	struct element *e = malloc(sizeof(*e));
	if (e == NULL) {
		printf("Out of memory");
		exit(-1);
	}
	e->producer = producer;
	e->value = rand_r(seed) % 1000;// numero entre 0 y 999. Coge un numero entre 0 y max integer y le aplica el modulo.
	e->time = rand_r(seed) % 100000; // numero entre 0 y 99999.
	usleep(e->time);

	pthread_mutex_lock(&create_mutex);
	created_elements++;
	pthread_mutex_unlock(&create_mutex);
	return e;
}

pthread_mutex_t consume_mutex = PTHREAD_MUTEX_INITIALIZER;
int consumed_elements = 0;;

/**
 * element_consume: creates a new element
 *
 * @producer: thread that produces it
 * @seed: this is needed for the random generator to work
 */
void element_consume(struct element *e)
{
	usleep(e->time);
	free(e);
	pthread_mutex_lock(&consume_mutex);
	consumed_elements++;
	pthread_mutex_unlock(&consume_mutex);
}

/* queue implementation */

/**
 * Queue: Queue of elements
 *
 * @elements: array of elements that would store the values
 * @count: number of used elements
 * @size: size in elements of elements array
 * @insertions: how many insertions have we got in the whole lifetime
 */
struct queue {
	struct element **elements; // puntero a punteros de elementos
	int count; //numero que tiene dentro
	int size; 	// tamaÃ±o de la cola
	int insertions; // producidos
};

/**
 * queue_create: create a new queue
 *
 * @size: size of the queue
 */
static struct queue *queue_create(int size) // funcion que crea una cola para manipularla.
{
	struct queue *queue =  malloc(sizeof(struct queue));
	queue->size = size;
	queue->count = 0;
	queue->insertions = 0;
	queue->elements = malloc(sizeof(struct element) * size);
	return queue;
}

/**
 * queue_destroy: destroy a queue
 *Âº
 * Destroy the queue and frees its memory
 *
 * @queue: queue to destroy
 */
static void queue_destroy(struct queue *queue)
{
	if (queue->count != 0) {
		printf("queue still has %d elements\n", queue->count);
	}
	free(queue->elements);
	free(queue);
}

/**
 * queue_is_empty: returns true if the queue is empty
 *
 * @queue: queue to check
 */
static bool queue_is_empty(struct queue *queue)
{
	return queue->count == 0;
}

/**
 * queue_is_full: returns true if the queue is full
 *
 * @queue: queue to check
 */
static bool queue_is_full(struct queue *queue)
{
	return queue->count == queue->size;
}

/**
 * queue_insertions: returns the number of instertions
 *
 * @queue: queue to check
 */
static int queue_insertions(struct queue *queue)
{
	return queue->insertions;
}

/**
 * queue_insert: inserts a new element on the queue
 *
 * @queue: queue where to insert
 * @element: element to insert
 */
static void queue_insert(struct queue *queue, struct element *element)
{
	if (queue->count == queue->size) {
		printf("buffer is full\n");
		exit(-1);
	}
	queue->elements[queue->count] = element;
	queue->count++;
	queue->insertions++;
}

/**
 * queue_remove: remove an element in the queue
 *
 * Returns the element removed
 *
 * @queue: queue where to remove
 */
struct element *queue_remove(struct queue *queue)
{
	if (queue->count == 0) {
		printf("buffer is empty\n");
		exit(-1);
	}
	queue->count--;
	return queue->elements[queue->count];
}

pthread_cond_t buffer_full = PTHREAD_COND_INITIALIZER;
pthread_cond_t buffer_empty = PTHREAD_COND_INITIALIZER;
pthread_mutex_t buffer_mutex = PTHREAD_MUTEX_INITIALIZER;

/**
 * thread_info: Information needed for each created thread
 *
 * @thread_id: pthread identifier for the thread
 * @thread_num: numerical identifier for our application
 */

struct thread_info {    /* Used as argument to thread_start() */
	pthread_t thread_id;        /* ID returned by pthread_create() */
	int       thread_num;       /* Application-defined thread # */
};


/**
 * producer_args: Arguments passed to each producer thread
 *
 * @thread_num: numerical identifier for our application
 * @queue: queue into which insert produced elements
 */

struct producer_args {
	int thread_num;
	struct queue *queue;
	int *iterations; // iterations producer
	int tiempo_medio;
	long veces_cola_llena;
	int producidos;
	pthread_mutex_t *mutexp; // mutex para las iteraciones de los productores
	pthread_cond_t *controlFlujoI_cond; // crear consumidores
	//Contador global de cola llena + mutex inicializado por main
	pthread_mutex_t *colaLlena_mutex;
	int *control_cola_llena;
	//pthread_mutex_t mutexColaLlena;//ejercicio 4
};

/**
 * producer_function: function executed by each producer thread
 *
 * @ptr: producer_args that needs to be passed as void
 */
void *producer_function(void *ptr)
{
	struct producer_args *args = ptr;
	unsigned int seed = args->thread_num;
	printf("producer thread %d\n", args->thread_num);
	bool is_empty; //sacado de dentro del while para que no genere una variable cada vez que se ejecuta el bucle; no malgastamos mem

	while (1) {
		pthread_mutex_lock(args->mutexp);
		if (*(args->iterations) <= 0) {
			printf("fin producers: %d\n", args->thread_num);
			pthread_mutex_unlock(args->mutexp);
			pthread_exit(NULL); //termina el subproceso de llamada. Ojo, puede no aparecer en las estadisticas.
			break;
		} else {
			int iteraciones = *(args->iterations);
			printf("iterations producers %d\n", iteraciones);
			printf("thread_num %d\n", args->thread_num);
			int a = *(args->iterations);
			a--;
			*(args->iterations) = a;
			// printf("%d", a);
			pthread_mutex_unlock(args->mutexp);
			struct element *e = element_create(args->thread_num, &seed);
			args->producidos++; // Contador de elementos creados
			args->tiempo_medio = (args->tiempo_medio) + (e->time);
			printf("%d: produces %d in %d microseconds\n", args->thread_num, e->value, e->time);


			pthread_mutex_lock(&buffer_mutex);
			while (queue_is_full(args->queue)) {
				args->veces_cola_llena++;
				pthread_mutex_lock(args->colaLlena_mutex);
				*(args->control_cola_llena) = *(args->control_cola_llena) + 1;
				//int colaLlena = *(args->control_cola_llena);

				printf("\n");
				printf("%d CONTADOR COLA LLENA \n",  *(args->control_cola_llena));
				printf("\n");
				if ( *(args->control_cola_llena) == 10) { //habia ++cola
					//Despertamos al hilo controFlujoI
					//colaLlena = 0;
					//*(args->control_cola_llena) = 0;
					pthread_cond_signal(args->controlFlujoI_cond);
				}
				pthread_mutex_unlock(args->colaLlena_mutex);
				pthread_cond_wait(&buffer_full, &buffer_mutex);
			}
			is_empty = queue_is_empty(args->queue);
			queue_insert(args->queue, e);
			if (is_empty)
				pthread_cond_broadcast(&buffer_empty);
			pthread_mutex_unlock(&buffer_mutex);
		}
	} //cierre while
	return NULL;
}

/**
 * consumer_args: Arguments passed to each consumer thread
 *
 * @thread_num: numerical identifier for our application
 * @queue: queue from where to remove elements
 */
struct consumer_args {
	int thread_num;
	struct queue *queue;
	int *iterations; //iterations consumer
	long veces_cola_vacia;
	int tiempo_medio;
	int consumidos;
	pthread_mutex_t *mutexc;
	//Contador global de cola vacia + mutex inicializado por main
	pthread_cond_t *controlFlujoII_cond;
	pthread_mutex_t *colaVacia_mutex;
	int *control_cola_vacia;
	int finalizar; // esto es el flag para saber si un consumidor tiene que parar.
	//flag == 0 -> consumiendo
	//flag == 1 -> para de consumir
	//pthread_mutex_t mutexColaVacia;

};
/**
 * consumer_function: function executed by each consumer thread
 *
 * @ptr: consumer_args that needs to be passed as void
 */
void *consumer_function(void *ptr) // esto es un hilo; condiciones para ser un hilo: ser void *
{
	struct consumer_args *args = ptr;

	printf("consumer thread %d\n", args->thread_num);

	while (!args->finalizar) {
		struct element *e;
		bool is_full;

		pthread_mutex_lock(args->mutexc);

		if (*(args->iterations) <= 0) {
			printf("fin consumers %d\n", args->thread_num);
			pthread_mutex_unlock(args->mutexc);
			pthread_exit(NULL); //termina el subproceso de llamada
			break;
		}
		printf("iterations consumers %d\n", *(args->iterations));
		printf("thread_num %d\n", args->thread_num);
		int a = *(args->iterations);
		a--;
		*(args->iterations) = a;
		pthread_mutex_unlock(args->mutexc);
		pthread_mutex_lock(&buffer_mutex);
		if (queue_is_empty(args->queue)) {
			args->veces_cola_vacia = args->veces_cola_vacia + 1;
			//printf("En este punto la cola estÃ¡ vacÃ­a.\n");
		}
		while (queue_is_empty(args->queue)) {
			pthread_mutex_lock(args->colaVacia_mutex);
			*(args->control_cola_vacia) = *(args->control_cola_vacia) + 1;
			//int colaVacia;
			//colaVacia = *(args->control_cola_vacia);

			printf("\n");
			printf("%d CONTADOR COLA VACIA \n", *(args->control_cola_vacia));
			printf("\n");
			if (*(args->control_cola_vacia) == 10) { //habia ++cola
				//Despertamos al hilo controFlujoIf
				pthread_cond_signal(args->controlFlujoII_cond);
			}
			pthread_mutex_unlock(args->colaVacia_mutex);
			pthread_cond_wait(&buffer_empty, &buffer_mutex);// se le pasa la condicion por la que esta esperando y el mutex
		}
		is_full = queue_is_full(args->queue);
		if (is_full)
			pthread_cond_broadcast(&buffer_full);

		e = queue_remove(args->queue);

		printf("%d: consumes %d in %d microseconds\n", args->thread_num, e->value, e->time);
		args->tiempo_medio = (args->tiempo_medio) + (e->time);
		pthread_mutex_unlock(&buffer_mutex);
		element_consume(e);
		args->consumidos++;
	}
	return NULL;
}

/**
 * producer_create: creates a new producer
 *
 * @info: thread information of created producer
 * @args: args passed to created thread
 * @thread_num: number id of created thread
 * @queue: where to insert producer elements
 */
void producer_create(struct thread_info *info, struct producer_args *args,
                     int thread_num, struct queue *queue, pthread_mutex_t *mutex_t, int *iteraciones, int* control_cola_llena, pthread_mutex_t *mutex_t2, pthread_cond_t *mutex_cond)
{
	info->thread_num = thread_num;
	args->thread_num = thread_num;
	args->queue = queue;
	args->producidos = 0;
	args->tiempo_medio = 0;
	args->veces_cola_llena = 0;
	args->mutexp = mutex_t;
	args->iterations = iteraciones;
	args->colaLlena_mutex = mutex_t2;
	args->control_cola_llena = control_cola_llena;
	args->controlFlujoI_cond = mutex_cond;

	if (pthread_create(&info->thread_id, NULL,
	                   producer_function, args)) {
		printf("Failing creating consumer thread %d", thread_num);
		exit(1);
	}
}

/**
 * consumer_create: creates a new consumer
 *
 * @info: thread information of created consumer
 * @args: args passed to created thread
 * @thread_num: number id of created thread
 * @queue: where to remove elements
 */
void consumer_create(struct thread_info *info, struct consumer_args *args,
                     int thread_num, struct queue *queue, pthread_mutex_t *mutex_t, int *iteracionesc, int* control_cola_vacia, pthread_mutex_t *mutex_t2, pthread_cond_t *mutex_cond)
{
	info->thread_num = thread_num;
	args->thread_num = thread_num;
	args->queue = queue;
	args->consumidos = 0;
	args->tiempo_medio = 0;
	args->veces_cola_vacia = 0;
	args->mutexc = mutex_t;
	args->iterations = iteracionesc;
	args->colaVacia_mutex = mutex_t2;
	args->finalizar = 0;
	args->control_cola_vacia = control_cola_vacia;
	args->controlFlujoII_cond = mutex_cond;

	if (pthread_create(&info->thread_id, NULL, consumer_function, args)) {
		printf("Failing creating consumer thread %d", thread_num);
		exit(1);
	}
}

void imprimir_estadisticas_productor(struct producer_args *args) {
	int tiempo_medio = args->tiempo_medio;
	int producidos = args->producidos;
	printf("Elementos producidos: %d\n", producidos);
	printf("Tiempo medio en producir cada elemento: %f \n", (float) (tiempo_medio / producidos));
	printf("Veces cola llena: %ld\n", args->veces_cola_llena );
}

void imprimir_estadisticas_consumidor(struct consumer_args *args) {
	int tiempo_medio = args->tiempo_medio;
	int consumidos = args->consumidos;
	printf("Elementos consumidos: %d\n", consumidos);
	printf("Tiempo medio en consumir cada elemento: %f \n", (float) (tiempo_medio / consumidos));
	printf("Veces cola vacia: %ld\n", args->veces_cola_vacia );
}

void imprimir_estadisticas(struct options *opt, lista *lproducers, lista *lconsumers, struct queue *queue) {
	printf("\n \n");
	int global_producidos = 0;
	void * p;

	for (p = primero(*lproducers); finLista(lproducers, p) == 0; p = siguiente(p))
	{
		struct producer_args *prod = obtenerDato(p);
		printf("\n");
		printf("\n");
		printf("NÃºmero de productor: %d\n", prod->thread_num);
		imprimir_estadisticas_productor(prod);
		int producidos = prod->producidos;
		global_producidos = global_producidos + producidos;

	}
	printf("\n \n");
	int global_consumidos = 0;
	for (p = primero(*lconsumers); finLista(lconsumers, p) == 0; p = siguiente(p))
	{
		struct consumer_args *cons = obtenerDato(p);
		printf("\n");
		printf("\n");
		printf("NÃºmero de consumidor: %d\n", cons->thread_num);
		imprimir_estadisticas_consumidor(cons);
		int consumidos = cons->consumidos;
		global_consumidos = global_consumidos + consumidos;
	}
	int producidos_globales = created_elements;
	// int producidos_globales = producer_args->queue->insertions;
	printf("NÃºmero de elementos insertados en la cola: %d \n", producidos_globales);
	printf("Numero de elementos producidos: %d \n", global_producidos);
	printf("NÃºmero de elementos consumidos: %d \n", global_consumidos);
	printf("Â¿EstÃ¡ la cola vacÃ­a?: %s\n", queue_is_empty(queue) ? "SÃ­" : "No");
	printf("Fin de la lista.\n");

}
struct control_flujo_args { // estructura que le pasamos a control de flujo
	struct options *opt;
	lista lconsumers;
	lista lconsumers_info;
	struct queue *queue;
	int *iteracionesc;
	int *control_cola_vacia;
	int *control_cola_llena;
	pthread_mutex_t *mutexp;
	pthread_mutex_t *mutexc;
	pthread_mutex_t *colaVacia_mutex;
	pthread_cond_t *controlFlujoII_cond;
	pthread_mutex_t *colaLlena_mutex;
	pthread_cond_t *controlFlujoI_cond;
	int finalizar_programa;
	int num_empty;
	int num_full;
} ;

void *controlFlujoI(void *ptr) { // el argumento tiene que ser un void*
	struct control_flujo_args* args = ptr;
	struct options* opt = args->opt;
	lista lconsumers = args->lconsumers;
	lista lconsumers_info = args->lconsumers_info;
	struct queue* queue = args->queue;
	int *iteracionesc = args->iteracionesc;
	struct thread_info* consumer_infos;
	struct consumer_args* consumer_args;
	//struct producer_args* producer_args;
	printf("\n");
	printf("NUM_FULL %d\n", args->num_full );
	printf("\n");

	while (1) {
		if ((consumer_infos = malloc(sizeof(struct thread_info))) == NULL) {
			printf("Error al reservar memoria en consumer_infos.\n");
			exit(1);
		}
		if ((consumer_args = malloc(sizeof(struct consumer_args))) == NULL) {
			printf("Error al reservar memoria en consumer_args.\n");
			exit(1);
		}
		pthread_setcancelstate(PTHREAD_CANCEL_DISABLE, NULL);
		pthread_mutex_lock(args->colaLlena_mutex);
		while (*(args->control_cola_llena) < args->num_full && args->finalizar_programa == 0) //AÃ‘ADIDO lunes a mayores
			pthread_cond_wait(args->controlFlujoI_cond, args->colaLlena_mutex);
		*(args->control_cola_llena) = 0;

		pthread_mutex_unlock(args->colaLlena_mutex);

		pthread_setcancelstate(PTHREAD_CANCEL_ENABLE, NULL);
		pthread_testcancel();

		printf("--------------------Detectada 10 veces la cola llena--------------------\n");

		insertarDato(consumer_args,  ultimo(lconsumers));
		insertarDato(consumer_infos, ultimo(lconsumers_info));
		consumer_create(consumer_infos, consumer_args, opt->num_consumers++, queue, args->mutexc, iteracionesc, args->control_cola_vacia, args->colaVacia_mutex, args->controlFlujoII_cond);
	}
	return NULL;
}

void *controlFlujoII(void *ptr) {
	struct control_flujo_args* args = ptr;
	printf("\n");
	printf("NUM_EMPTY %d\n", args->num_empty);
	printf("\n");
	while (1) {
		//struct consumer_args* consumer_args;
		pthread_setcancelstate(PTHREAD_CANCEL_DISABLE, NULL);

		pthread_mutex_lock(args->colaVacia_mutex);
		while (*(args->control_cola_vacia) < args->num_empty && args->finalizar_programa == 0 ) { //&&  falg?? ultim elem??)
			pthread_cond_wait(args->controlFlujoII_cond, args->colaVacia_mutex);
		}
		*(args->control_cola_vacia) = 0;

		pthread_mutex_unlock(args->colaVacia_mutex);

		pthread_setcancelstate(PTHREAD_CANCEL_ENABLE, NULL);
		pthread_testcancel();

		printf("--------------------Detectada 10 veces la cola vacia--------------------\n");
		// paramos el primer consumidor que este activo
		int encontrado = 1;
		void * p;
		p = primero(args->lconsumers); // inicializamos la busqueda empezando por el principio
		while ((siguiente(p) != NULL) && (encontrado))  {
			struct consumer_args *ca = obtenerDato(p); // obtenemos el dato del nodo
			if (ca->finalizar == 0) {
				ca->finalizar = 1; // Activamos el flag
				printf("Eliminando consumidor: %d\n", ca->thread_num);
				encontrado = 0;
			}
			else p = siguiente(p); // avanzamos al siguiente para no volver a empezar a buscar por el principio. Tonteria
		}
	}
	return NULL;
}

/*
 * producers_consumer: creates and wait to finish for producers and consumers
 *
 * @opt: command line options
 */

void producers_consumers(struct options *opt)
{
	printf("productores,consumidores,buffer,iterations %d,%d,%d,%d\n", opt->num_producers, opt->num_consumers, opt->buffer_size,
	       opt->iterations);
	int i;
	struct thread_info *producer_infos = NULL;
	struct thread_info *consumer_infos = NULL;
	struct producer_args *producer_args = NULL;
	struct consumer_args *consumer_args = NULL;
	struct queue *queue;
	int iteraciones = opt->iterations; // iteraciones productor
	int iteracionesc = opt->iterations; // iteraciones consumidor
	//Hilo y CondiciÃ³n + mutex para controlFlujoI
	pthread_t hiloControlFlujoI;
	pthread_t hiloControlFlujoII;

	lista lproducers;
	lista lconsumers;
	lista lproducers_info;
	lista lconsumers_info;

	pthread_mutex_t *mutex_t;

	if ((mutex_t = malloc(sizeof(pthread_mutex_t))) == NULL) {
		printf("Not enough memory\n");
		exit(1);
	}

	if (pthread_mutex_init(mutex_t, NULL) != 0) {
		exit(0);
	}

	pthread_mutex_t *mutex_t2;

	if ((mutex_t2 = malloc(sizeof(pthread_mutex_t))) == NULL) {
		printf("Not enough memory\n");
		exit(1);
	}

	if (pthread_mutex_init(mutex_t2, NULL) != 0) {
		exit(0);
	}

	pthread_cond_t *mutex_cond;
	if ((mutex_cond = malloc(sizeof(pthread_cond_t))) == NULL) {
		printf("Not enough memory\n");
		exit(1);
	}
	if (pthread_cond_init(mutex_cond, NULL) != 0) {
		exit(0);
	}


	pthread_cond_t *mutex_cond2;
	if ((mutex_cond2 = malloc(sizeof(pthread_cond_t))) == NULL) {
		printf("Not enough memory\n");
		exit(1);
	}
	if (pthread_cond_init(mutex_cond2, NULL) != 0) {
		exit(0);
	}


	if (crearLista(&lproducers) == -1) {
		printf("Error al crear la lista lproducers.\n");
		exit(1);
	}
	if (crearLista(&lproducers_info) == -1) {
		printf("Error al crear la lista lproducers_info.\n");
		exit(1);
	}
	if (crearLista(&lconsumers) == -1) {
		printf("Error al crear la lista lconsumers.\n");
		exit(1);
	}
	if (crearLista(&lconsumers_info) == -1) {
		printf("Error al crear la lista lconsumers_info.\n");
		exit(1);
	}


	printf("creating buffer with %d elements\n", opt->buffer_size);
	queue = queue_create(opt->buffer_size);

	if (queue == NULL) {
		printf("Not enough memory\n");
		exit(1);
	}

	int control_cola_vacia = 0;
	int control_cola_llena = 0;

	printf("Creamos el hilo de controlFlujoI y lo arrancamos.  \n ");

	struct control_flujo_args args;
	args.opt = opt;
	args.lconsumers = lconsumers;
	args.lconsumers_info = lconsumers_info;
	args.queue = queue;
	args.iteracionesc = &iteracionesc;
	args.control_cola_vacia = &control_cola_vacia;
	args.control_cola_llena = &control_cola_llena;
	args.colaVacia_mutex = mutex_t2;
	args.controlFlujoII_cond = mutex_cond;
	args.colaLlena_mutex = mutex_t2;
	args.controlFlujoI_cond = mutex_cond2;
	args.mutexp = mutex_t;
	args.mutexc = mutex_t;
	args.finalizar_programa = 0;
	args.num_empty = opt->num_empty;
	args.num_full = opt->num_full;

	void *ptrs = &args;
	if (pthread_create(&hiloControlFlujoI, NULL, controlFlujoI, (void *) ptrs)) {
		printf("Fallo al crear el hilo de control de flujo I");
		exit(1);
	}
	printf("Creado controlFlujoI. Creating %d producers\n", opt->num_producers);

	printf("Creamos el hilo de controlFlujoII y lo arrancamos.  \n ");

	if (pthread_create(&hiloControlFlujoII, NULL, controlFlujoII, (void *) ptrs)) {
		printf("Fallo al crear el hilo de control de flujo I");
		exit(1);
	}

	/* Create independent threads each of which will execute function */
	for (i = 0; i < opt->num_producers; i++) {
		// mallocs
		if ((producer_infos = malloc(sizeof(struct thread_info))) == NULL) {
			printf("Error al reservar memoria en producer_infos.\n");
			exit(1);
		}
		if ((producer_args = malloc(sizeof(struct producer_args))) == NULL) {
			printf("Error al reservar memoria en producer_args.\n");
			exit(1);
		}
		insertarDato(producer_infos, ultimo(lproducers_info));
		insertarDato(producer_args, ultimo(lproducers));
		producer_create(producer_infos, producer_args, i, queue, mutex_t, &iteraciones, &control_cola_llena, mutex_t2, mutex_cond2);
	}

	printf("creating %d consumers\n", opt->num_consumers);
	consumer_infos = malloc(sizeof(struct thread_info) * opt-> num_consumers);

	for (i = 0; i < opt->num_consumers; i++) {
		if ((consumer_infos = malloc(sizeof(struct thread_info))) == NULL) {
			printf("Error al reservar memoria en consumer_infos.\n");
			exit(1);
		}
		if ((consumer_args = malloc(sizeof(struct consumer_args))) == NULL) {
			printf("Error al reservar memoria en consumer_args.\n");
			exit(1);
		}
		insertarDato(consumer_args, ultimo(lconsumers));
		insertarDato(consumer_infos, ultimo(lconsumers_info));
		consumer_create(consumer_infos, consumer_args, i, queue, mutex_t, &iteracionesc, &control_cola_vacia, mutex_t2, mutex_cond);
	}
	printf("Finalizada la creacion de productores y consumidores\n");


	// cambiar condicion y pasarlo a tipo lista. :)
	void * p;
	for (p = primero(lproducers_info); finLista(&lproducers_info, p) == 0; p = siguiente(p))
	{
		struct thread_info *pinfo = obtenerDato(p);
		printf("Esperando Por productor %d\n", pinfo->thread_num);
		pthread_join(pinfo->thread_id, NULL);
	}

	for (p = primero(lconsumers_info); finLista(&lconsumers_info, p) == 0; p = siguiente(p))
	{
		struct thread_info *cinfo = obtenerDato(p);
		printf("Esperando Por consumidor %d\n", cinfo->thread_num);
		pthread_join(cinfo->thread_id, NULL);
	}
	pthread_mutex_destroy(mutex_t);
	pthread_mutex_destroy(mutex_t2);
	args.finalizar_programa = 1;
	//Forzamos el fin del hilo de controlFlujoI
	pthread_cancel(hiloControlFlujoI);
	pthread_cond_signal(mutex_cond2);
	//Forzamos el fin del hilo de controlFlujoII
	pthread_cancel(hiloControlFlujoII);
	pthread_cond_signal(mutex_cond);

	pthread_join(hiloControlFlujoI, NULL);
	pthread_join(hiloControlFlujoII, NULL);

	imprimir_estadisticas(opt, &lproducers, &lconsumers, queue);
	queue_destroy(queue);
	pthread_cond_destroy(mutex_cond);
	pthread_cond_destroy(mutex_cond2);
	
	// free(producer_infos);
	// free(consumer_infos);
	// free(producer_args);
	// free(consumer_args);
	vaciarLista(&lproducers, liberar);
	vaciarLista(&lproducers_info, liberar);
	vaciarLista(&lconsumers, liberar);
	vaciarLista(&lconsumers_info, liberar);
	// free(lproducers);
	// free(lconsumers);
	// free(lproducers_info);
	// free(lconsumers_info);
}

int main (int argc, char **argv)
{
	struct options opt;

	opt.num_producers = 10;
	opt.num_consumers = 10;
	opt.buffer_size = 5;
	opt.iterations = 100;
	opt.num_full = 10;
	opt.num_empty = 10;

	read_options(argc, argv, &opt);
	producers_consumers(&opt);
	pthread_exit (0);
}