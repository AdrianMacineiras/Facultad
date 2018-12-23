/*
mpicc mandel.c -o mandel -lm
mpirun -np 1 mandel > mandelp.txt
python2 view.py mandelp.txt
*/



/*The Mandelbrot set is a fractal that is defined as the set of points c
in the complex plane for which the sequence z_{n+1} = z_n^2 + c
with z_0 = 0 does not tend to infinity.*/

/*This code computes an image of the Mandelbrot set.*/
#include <stdlib.h>
#include <stdio.h>
#include <sys/time.h>
#include <mpi.h> // Librería mpi
#include <math.h> // Librería mpi

#define DEBUG 1

#define         X_RESN  1024     /* x resolution */
#define         Y_RESN  1024    /* y resolution */
#define         X_MIN   -2.0	/*Boundaries of the mandelbrot set*/
#define         X_MAX    2.0
#define         Y_MIN   -2.0
#define         Y_MAX    2.0
#define		maxIterations	1000 /*Cuanto mayor, más detalle en la imagen y mayor coste computacional*/

typedef struct complextype
{
	float real, imag;
} Compl;


int main (int argc, char *argv[])   //contador de argumentos; puntero al array donde empiezan los argumentos
{

	/* Mandelbrot variables */
	int i, j, k, acu;
	Compl   z, c;
	float   lengthsq, temp;
	struct timeval  ti, tf;
	int microseconds;
	double flopsp=0; // (duda) porque flops double y no int? Flopsp no deberia ser un vector: int flops[numprocs];
	double flops=0; // (duda) porque flops double y no int? Flopsp no deberia ser un vector: int flops[numprocs];

	//INICIALIZAMOS
	MPI_Status status;
	int numprocs, rank; // numero de procesos e id del proceso

	int res[X_RESN][Y_RESN]; // Matriz resultado

	MPI_Init(&argc, &argv);
	MPI_Comm_size(MPI_COMM_WORLD, &numprocs);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);

	
	int filas_proceso = ceil((float) X_RESN / numprocs); //almacenamos el numero de filas que le toca a cada proceso	
	int n_elem_proc; //vamos a llevar la cuenta de los elementos que va a contener cada proceso
	

	int* desplazamiento = (int *)malloc(numprocs * sizeof(int)); //el array desplaz tendra tantas posiciones como procesos existan
	int* cantidad = (int *)malloc(numprocs * sizeof(int)); //el array cantidad tendra tantas posiciones como procesos existan
			

	//int sendBuff[X_RESN/numprocs],recvBuff[X_RESN/numprocs];
	/*
	Estas funciones devuelven MPI_SUCCESS cuando se producen de manera exitosa.
	MPI_init
		-> Inicializa el entorno de ejecución del MPI
		-> Le pasas el numero de argumentos y el puntero al array de argumentos.
	MPI_Comm_size
		-> averiguar el numero de proc que participan
		-> le pasas:
			- MPI_COMM_WORLD: conjunto de todos los procesos que estan ejecutando una aplicacion
			- numprocs: aqui se almacena el numero de procesos que tiene
	MPI_Comm_rank
		-> para que cada proceso averigue su dirección (identificador) dentro de la colección de procesos que componen la aplicación
		-> le pasas:
			- MPI_COMM_WORLD: conjunto de todos los procesos que estan ejecutando una aplicacion
			- rank: aqui se almacena el id
	*/


	int min, max;
	//almeceno en max la ultima fila correspondiente a cada proceso
	//ATENCION: LAS FILAS EMPIEZAN A CONTAR EN 1
	if (rank == numprocs - 1) {
		max = X_RESN;
		// printf("max %d\n", max );
	} else {
		max = ((rank + 1) * filas_proceso);
		//printf("max %d\n", max );
	}
	//almeceno en min la ultima fila correspondiente a cada proceso
	min = rank * filas_proceso;
	// printf("min %d\n", min );



	// MPI_Scatter -> lo lo necesitamos porque la imagen la leen todos los procesos
	/*
	ENTRADA:
		sendbuf 	Dirección inicial del buffer de salida, solo útil para el proceso raíz, el resto de procesos ignoran este parámetro.
		sendcount 	Número de elementos que se envía a cada proceso del comunicador (entero que sólo tiene sentido en el raíz).
		sendtype 	Tipo de dato que se va a enviar, solo lo tendrá en cuenta la raíz (Como por ejemplo MPI_INT).
		recvcount 	Número de elementos que espera recibir cada proceso (int).
		recvtype 	Tipo de datos de los elementos a recibir (Como por ejemplo MPI_INT).
		root 		Rango (rank) del proceso raíz (el que realizará el envío).
		comm 		Comunicador por el que realizar la comunicación.
	SALIDA:
		recvbuf 	Direción del buffer de recepción (para todos los procesos, incluido el proceso raíz).
	*/

	/* Start measuring time */
	//MEDIR EL TIEMPO DE COMPUTACION!!!
	MPI_Barrier(MPI_COMM_WORLD); // Con mpi_barrier espero a que todos los procesos lleguen a este punto
	gettimeofday(&ti, NULL);


		int i_rel = 0 ;
		/* Calculate and draw points */
		for (i = min; i < max ; i++) {
			for (j = 0; j < Y_RESN; j++) {
				z.real = z.imag = 0.0;
				c.real = X_MIN + j * (X_MAX - X_MIN) / X_RESN;
				c.imag = Y_MAX - i * (Y_MAX - Y_MIN) / Y_RESN;
				flopsp+=4;
				k = 0;
				do  {    /* iterate for pixel color */
					temp = z.real * z.real - z.imag * z.imag + c.real;
					z.imag = 2.0 * z.real * z.imag + c.imag;
					z.real = temp;
					lengthsq = z.real * z.real + z.imag * z.imag;
					k++;
					flopsp+=10;
				} while (lengthsq < 4.0 && k < maxIterations);

				if (k >= maxIterations)
					res[i][j] = 0;
				else
					res[i][j] = k;
			}
		}
		//calcular los trozos para gatherv
		//int n_elem_proc = Y_RESN * filas_proceso;
		//calculo el array de cantidades(numero de filas que tendra cada proceso)
		if (rank == 0) { //este calculo lo realizara solo el proceso 0
			for (i = 0; i < numprocs; i++) {
				desplazamiento[i] = i * filas_proceso * Y_RESN; //calculo el array de desplazamiento(numero de fia en la que empieza cada proceso)
				if (i != numprocs - 1) { //si no soy el ultimo proceso
					//calculo el numero de filas que tendre y lo almaceno en el array cantidad
					cantidad[i] = filas_proceso * Y_RESN ;
				} else { //si soy el ultimo proceso me tocan estas filas
					cantidad[i] = ( X_RESN - filas_proceso * (numprocs - 1)) * Y_RESN;
				}
			}


			for (i = 0; i < numprocs; i++) {
				fprintf(stderr,"array cantidad -> soy el proceso %d y tengo %d elementos\n", i, cantidad[i] );
			}
			for (i = 0; i < numprocs; i++) {
				fprintf(stderr,"array desplazamiento -> soy el proceso %d y mi desplazamiento es %d\n", i, desplazamiento[i]);
			}
		fprintf(stderr,"\n");
		}

	gettimeofday(&tf, NULL);
    microseconds = (tf.tv_usec - ti.tv_usec)+ 1000000 * (tf.tv_sec - ti.tv_sec);//aqui muestro el resultado para cada proceso
    fprintf (stderr, "(PERF) Tiempo computacion (seconds) para el proceso %d = %lf\n", rank, (double) microseconds/1E6);

    MPI_Reduce(&microseconds, &acu, 1, MPI_INT, MPI_SUM, 0, MPI_COMM_WORLD);//aqui calculo en el proceso 0 la media del tiempo de computacion
    if (rank==0){
        fprintf (stderr, "Media de tiempo de computacion = %lf, calculado por el proceso: %d\n\n", (((double)acu)/((double)numprocs))/1E6, rank);
        fflush(stderr);
    }


    //TIEMPO DE COMUNICACION   
    MPI_Barrier(MPI_COMM_WORLD);//espero a los procesos para medir el tiempo de comunicacion por separado
    /* Start measuring time//empiezo a medir el tiempo de comunicacion */
    gettimeofday(&ti, NULL);

    //calculo el numero de elementos que tiene cada proceso
	if (rank == numprocs - 1) {
		n_elem_proc = (Y_RESN - rank * filas_proceso) * Y_RESN;
	} else {
		n_elem_proc = filas_proceso * Y_RESN;
	}

	//usar GATHERV
	MPI_Gatherv(res[rank * filas_proceso], n_elem_proc, MPI_INT, res , cantidad, desplazamiento, MPI_INT, 0, MPI_COMM_WORLD);
	/*
	ENTRADA:
		sendbuf
		    dirección inicial de memoria de envío (opción) 
		sendcount
		    número de elementos de almacenamiento intermedio de envío (número entero) 
		sendtype
		    tipo de datos de elementos de memoria de envío (mango) 
		recvcounts
		    matriz de enteros (del tamaño del grupo de longitud) que contiene el número de elementos que se reciben de cada proceso (significativo sólo en la raíz) 
		displs
		    matriz de enteros (del tamaño del grupo de longitud). I entrada especifica el desplazamiento relativo de recvbuf en el que colocar los datos de entrada de proceso i (significativo sólo en la raíz) 
		recvtype
		    tipo de datos de elementos de amortiguación recv (significativo sólo en la raíz) (mango) 
		raíz
		    rango de proceso de recepción (número entero) 
		comm
		    comunicador (mango) 
			
	SALIDA:
		recvbuf
    		dirección del búfer de recepción (opción, significativo sólo en la raíz) 
	*/

	/* End measuring time */
	gettimeofday(&tf, NULL);
	microseconds = (tf.tv_usec - ti.tv_usec) + 1000000 * (tf.tv_sec - ti.tv_sec); //aqui muestro el resultado para cada proceso
	fprintf (stderr, "(PERF) Tiempo de comunicacion (seconds) del proceso = %lf y pertenezco al proceso: %d\n", (double) microseconds / 1E6, rank); //para q lo imprima en el canal d errores y no se mezcle con lo que imprime x pantalla

	

	MPI_Reduce(&microseconds, &acu, 1, MPI_INT, MPI_SUM, 0, MPI_COMM_WORLD);//calculo en el proceso 0 la media del tiempo de comunicacion
    if (rank==0){
        fprintf (stderr, "Media de tiempo de comunicacion = %lf, calculado por el proceso %d\n\n", (((double)acu)/((double)numprocs))/1E6, rank);
    }


    MPI_Allreduce(&flopsp, &flops, 1, MPI_DOUBLE, MPI_SUM, MPI_COMM_WORLD);//el allreduce le pasa a todos los procesos el calculo de la suma del buffer, es como el reduce pero distribuye el resultado de la suma para todos, aqui le paso a todos los flops totales, para luego hacer el ratio correspondiente, los que menos trabajan tendran el ratio mas alto (entre 60 o asi)
    /*
    Combines values from all processes and distributes the result back to all processes 
    ENTRADA:
		sendbuf: starting address of send buffer (choice) 
		count: number of elements in send buffer (integer) 
		datatype: data type of elements of send buffer (handle) 
		op: operation (handle) 
		comm: communicator (handle) 
	SALIDA:
		recvbuf: starting address of receive buffer (choice) 
    */
    MPI_Barrier(MPI_COMM_WORLD);//hago barriers para que me los pinte todos juntos
    fprintf(stderr,"Numero de flops para el proceso (%d) = %lf \n", rank, flopsp);//pinto los flops de cada proceso
    MPI_Barrier(MPI_COMM_WORLD);
    fprintf(stderr,"El balanceo de carga para el proceso (%d) = %lf \n", rank, flops/(flopsp * numprocs));//pinto el ratio para cada proceso



	if ( DEBUG && (rank == 0) ) {
		for (i = 0; i < X_RESN; i++) {
			for (j = 0; j < Y_RESN; j++)
				printf("%d\t", res[i][j]);
			printf("\n");
		}
	}

	MPI_Finalize();
	return  0;
}

