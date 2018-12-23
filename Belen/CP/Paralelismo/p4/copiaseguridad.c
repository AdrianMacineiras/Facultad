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

#define DEBUG 0

#define         X_RESN  1024     /* x resolution */
#define         Y_RESN  1024   /* y resolution */
#define         X_MIN   -2.0	/*Boundaries of the mandelbrot set*/
#define         X_MAX    2.0
#define         Y_MIN   -2.0
#define         Y_MAX    2.0
#define		maxIterations	1000 /*Cuanto mayor, más detalle en la imagen y mayor coste computacional*/
#define		WORK	1 /*indicar que el proceso tiene que trabajar*/

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
	double flopsp = 0;
	double flops = 0;
	int maestro = 0;
	int filasCalculadas = 0;
	int resParcial[X_RESN];
	int trabajador = 0;
	int fila[X_RESN];
	int  DIE = X_RESN+1; /*indicar que el proceso tiene que trabajar*/
	int pose=0;
	int posr=0;

	//INICIALIZAMOS
	MPI_Status status;
	int numprocs, rank; // numero de procesos e id del proceso

	int res[X_RESN][Y_RESN]; // Matriz resultado

	MPI_Init(&argc, &argv);
	MPI_Comm_size(MPI_COMM_WORLD, &numprocs);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);


	if ( numprocs < 2) {
		fprintf(stderr, "\n Introduzca un numero de procesos >=2\n\n" );
		MPI_Finalize();
	} else {
		/**********CODIGO DEL MAESTRO*************/
		int n_esclavos = numprocs - 1;
		if (rank == maestro) {


			for (j = 0; j < n_esclavos; j++) { //MANDO 1 PRIMERA FILA DE TRABAJO A TODOS LOS PROCESOS
				MPI_Send(&j, 1, MPI_INT, j + 1, pose, MPI_COMM_WORLD); //en j esta la pos de la fila; se lo mandas al proc j+1
				pose++;
			}



			while (filasCalculadas < X_RESN) {
				
				MPI_Recv(&resParcial, Y_RESN, MPI_INT, MPI_ANY_SOURCE, MPI_ANY_TAG, MPI_COMM_WORLD, &status);
				
				filasCalculadas++;
				posr = status.MPI_TAG;    // Guardo en el tag la fila que esta calculada
				trabajador = status.MPI_SOURCE;
				for (int i = 0; i < Y_RESN; i++)
				{
					res[posr][i] = resParcial[i];  // recibes un vector fila
				}


				fprintf(stderr, "Soy el proceso %d y me toca calcular la fila %d\n", trabajador, status.MPI_TAG );

				if (j < Y_RESN) {
					MPI_Send(&j, 1, MPI_INT, trabajador, pose, MPI_COMM_WORLD);
					pose++;
					j++;
				} else  {
					for (i = 1; i <= n_esclavos; i++) {
						MPI_Send(&j, 1, MPI_INT, i, DIE, MPI_COMM_WORLD);
					}
				}






			}
			fprintf(stderr, "\n Acabado de enviar todas las filas\n");


		}

		/**********CODIGO DEL ESCLAVO*************/

		if (rank != maestro) {
			while (1) {
				//Espera la llegada del trabajo encomendado
				MPI_Recv(&j, 1, MPI_INT, maestro, MPI_ANY_TAG, MPI_COMM_WORLD, &status);
				//fprintf(stderr, "Tag del proceso %d = %d\n", rank, status.MPI_TAG);
				if (status.MPI_TAG == DIE) {
					//	fprintf(stderr, "entro en break %d\n", rank);
			/* Start measuring time */
					break;
				}
			gettimeofday(&ti, NULL);
				//Cada proceso calcula la fila asignada por el servidor
				for (i = 0; i < X_RESN; i++) {
					z.real = z.imag = 0.0;
					c.real = X_MIN + j * (X_MAX - X_MIN) / X_RESN;
					c.imag = Y_MAX - i * (Y_MAX - Y_MIN) / Y_RESN;
					flopsp += 4;
					k = 0;
					do  {    /* iterate for pixel color */
						temp = z.real * z.real - z.imag * z.imag + c.real;
						z.imag = 2.0 * z.real * z.imag + c.imag;
						z.real = temp;
						lengthsq = z.real * z.real + z.imag * z.imag;
						k++;
						flopsp += 10;
					} while (lengthsq < 4.0 && k < maxIterations);
					//Si k alcanzó (sin superar el módulo es valor 2) el máximo
					//nº de iteraciones permitidas colocamos 0, si el módulo superó 2
					//antes de alcanzar maxIterations colocamos k
					if (k >= maxIterations)
						fila[i] = 0;
					else
						fila[i] = k;
				}
				//Envía al sevidor los resultados del trabajo encomendado
				MPI_Send(fila, Y_RESN, MPI_INT, 0, j, MPI_COMM_WORLD);

			}
			//Llegó la orden de fin (DIE_TAG) y enviaremos los floaps y finalizaremos
			//Fin de la computación. Se calcula el tiempo de cálculo sonsumido y se muestra
			gettimeofday(&tf, NULL);
			microseconds = (tf.tv_usec - ti.tv_usec) + 1000000 * (tf.tv_sec - ti.tv_sec);
			fprintf (stderr, "(PERF) Tiempo computacion (seconds) para el proceso %d = %lf\n", rank, (double) microseconds / 1E6);
		}



		MPI_Reduce(&microseconds, &acu, 1, MPI_INT, MPI_SUM, 0, MPI_COMM_WORLD);//aqui calculo en el proceso 0 la media del tiempo de computacion
		if (rank == maestro) {
			fprintf (stderr, "\n Media de tiempo de computacion = %lf, calculado por el proceso: %d\n\n", (((double)acu) / ((double)numprocs)) / 1E6, rank);
			fflush(stderr);
		}

		//Esperamos a que todos los procesos terminen sus cálculos para que las medidas de tiempos de
		//comunicación no se vean afectadas por los tiempos de cálculo
		MPI_Barrier(MPI_COMM_WORLD);

/*		//Inicio de las comunicaciones
		gettimeofday(&ti, NULL);

		
		//Fin de las comunicaciones e impresión del tiempo consumido en la comunicación
		gettimeofday(&tf, NULL);
		microseconds = (tf.tv_usec - ti.tv_usec) + 1000000 * (tf.tv_sec - ti.tv_sec);
		fprintf (stderr, "(PERF) Tiempo de comunicacion (seconds) del proceso %d =  %lf\n", rank, (double) microseconds / 1E6); //para q lo imprima en el canal d errores y no se mezcle con lo que imprime x pantalla


		MPI_Reduce(&microseconds, &acu, 1, MPI_INT, MPI_SUM, 0, MPI_COMM_WORLD);//calculo en el proceso 0 la media del tiempo de comunicacion
		if (rank == maestro) {
			fprintf (stderr, "Media de tiempo de comunicacion = %lf, calculado por el proceso : %d\n\n", (((double)acu) / ((double)numprocs)) / 1E6, rank);
		}

*/

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
		fprintf(stderr, "Numero de flops para el proceso (%d) = %lf \n", rank, flopsp); //pinto los flops de cada proceso
		MPI_Barrier(MPI_COMM_WORLD);
		if (rank!=maestro) {
			fprintf(stderr, "El balanceo de carga para el proceso (%d) = %lf \n", rank, flops / (flopsp * numprocs)); //pinto el ratio para cada proceso
		}



		if ( DEBUG && (rank == maestro) ) {
			for (i = 0; i < X_RESN; i++) {
				for (j = 0; j < Y_RESN; j++)
					printf("%d\t", res[j][i]);
				printf("\n");
			}
		}

		MPI_Finalize();
		return  0;
	}
}
