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
	int i, j, k, acu, acu_com;
	Compl   z, c;
	float   lengthsq, temp;
	struct timeval  ti, tf;
	int microseconds;
	int microcomunicacion;
	double flopsp = 0;
	double flops = 0;
	int maestro = 0;
	int filasCalculadas = 0;
	int resParcial[X_RESN];
	int trabajador = 0;
	int fila[X_RESN];
	int  DIE = X_RESN + 1; /*indicar que el proceso tiene que trabajar*/
	int pose = 0;
	int posr = 0;
	double ttotal = 0.0;

	//INICIALIZAMOS
	MPI_Status status;
	int numprocs, rank; // numero de procesos e id del proceso

	int res[X_RESN][Y_RESN]; // Matriz resultado

	MPI_Init(&argc, &argv);
	MPI_Comm_size(MPI_COMM_WORLD, &numprocs);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
	MPI_Request request;

	if ( numprocs < 2) {
		fprintf(stderr, "\n Introduzca un numero de procesos >=2\n\n" );
		MPI_Finalize();
	} else {
		/**********CODIGO DEL MAESTRO*************/
		int n_esclavos = numprocs - 1;
		MPI_Barrier(MPI_COMM_WORLD);

		if (rank == maestro) {
			gettimeofday(&ti, NULL);
			for (j = 0; j < n_esclavos && j < Y_RESN; j++) { //MANDO 1 PRIMERA FILA DE TRABAJO A TODOS LOS PROCESOS
				// Pongo el j<Y_RESN por si abro mas procesos que filas que tengo que calcular
				MPI_Isend(&j, 1, MPI_INT, j + 1, pose, MPI_COMM_WORLD, &request); //en j esta la pos de la fila; se lo mandas al proc j+1
				//Rutina de envío de mensaje de un único origen a un destino no bloqueante.
				//POSE: TAG QUE CONTIENE LA FILA QUE SE TIENE QUE CALCULAR -> LO USAREMOS PARA MONTAR LA MATRIZ RESULTANTE
				pose++;
			}
			gettimeofday(&tf, NULL);
			microcomunicacion += (tf.tv_usec - ti.tv_usec) + 1000000 * (tf.tv_sec - ti.tv_sec);

			while (filasCalculadas < X_RESN) {
				gettimeofday(&ti, NULL);

				MPI_Recv(&resParcial, Y_RESN, MPI_INT, MPI_ANY_SOURCE, MPI_ANY_TAG, MPI_COMM_WORLD, &status);
				/*
				MPI_Recv: (Esta función bloquea el proceso hasta que se reciba un mensaje con las características especificadas)
				void* buffer -> Buffer de entrada en el que se guarda el contenido del mensaje enviado
				int contador -> Entero que indica el número máximo de elementos que se espera recibir en el buffer de entrada
				MPI_Datatype -> Tipo de dato de cada elemento que se va a recibir.
				int origen -> Rango del proceso de origen esperado
				int etiqueta -> entero que representa la etiqueta del mensaje(en manos del usuario)
				MPI_Comm communicador -> comunicador utilizado para la comunicacion
				Status -> Objeto de tipo MPI_Status, contiene datos relevantes sobre el mensaje (como son el origen (MPI_SOURCE) , la etiqueta (MPI_TAG)y el tamaño (size)).
				*/
				gettimeofday(&tf, NULL);
				microcomunicacion += (tf.tv_usec - ti.tv_usec) + 1000000 * (tf.tv_sec - ti.tv_sec);

				filasCalculadas++;
				posr = status.MPI_TAG;    // Guardo en el tag la fila que esta calculada
				trabajador = status.MPI_SOURCE; //GUARDAS EL PROCESO QUE REALIZO EL CALCULO DE DICHA FILA
				for (int i = 0; i < Y_RESN; i++) { //VOY MONTANDO LA MATRIZ RESULTADO
					res[posr][i] = resParcial[i];  // recibes un vector fila
				}
				//fprintf(stderr, "Soy el proceso %d y me toca calcular la fila %d\n", trabajador, status.MPI_TAG );

				gettimeofday(&ti, NULL);
				if (j < X_RESN) {
					MPI_Isend(&j, 1, MPI_INT, trabajador, pose, MPI_COMM_WORLD, &request);
					pose++;
					j++;
				} else  {
					for (i = 1; i <= n_esclavos; i++) {
						MPI_Isend(&j, 1, MPI_INT, i, DIE, MPI_COMM_WORLD, &request);
						//ENVIAMOS J POR ENVIAR CUALQUIER COSA
						/*
						void* buffer -> Dir del buffer de envío. Requiere un puntero.
						int contador -> Numero de elementos a enviar, en nuestro caso es 1 porque es un solo elem
						MPI_Datatype -> tipoDeDato de cada elemento que se va a enviar
						int destino -> proceso al que le quieres enviar el dato.
						int etiqueta -> entero que representa la etiqueta del mensaje(en manos del usuario)
						MPI_Comm communicador -> comunicador utilizado para la comunicacion
						REQUEST
						*/
					}
				}
				gettimeofday(&tf, NULL);
				microcomunicacion += (tf.tv_usec - ti.tv_usec) + 1000000 * (tf.tv_sec - ti.tv_sec);
			}
			fprintf (stderr, "\n (PERF) Tiempo de comunicacion(seconds) para el proceso %d = %lf\n", rank, (double) microcomunicacion / 1E6);
			//
		}

		/**********CODIGO DEL ESCLAVO*************/
//		MPI_Barrier(MPI_COMM_WORLD);

		if (rank != maestro) {
			while (1) {
				gettimeofday(&ti, NULL);

				//Espera la llegada del trabajo encomendado
				MPI_Recv(&j, 1, MPI_INT, maestro, MPI_ANY_TAG, MPI_COMM_WORLD, &status);
				/*
				MPI_Recv: (Esta función bloquea el proceso hasta que se reciba un mensaje con las características especificadas)
				void* buffer -> Buffer de entrada en el que se guarda el contenido del mensaje enviado
				int contador -> Entero que indica el número máximo de elementos que se espera recibir en el buffer de entrada
				MPI_Datatype -> Tipo de dato de cada elemento que se va a recibir.
				int origen -> Rango del proceso de origen esperado
				int etiqueta -> entero que representa la etiqueta del mensaje(en manos del usuario)
				MPI_Comm communicador -> comunicador utilizado para la comunicacion
				Status -> Objeto de tipo MPI_Status, contiene datos relevantes sobre el mensaje (como son el origen (MPI_SOURCE) , la etiqueta (MPI_TAG)y el tamaño (size)).
				*/
				gettimeofday(&tf, NULL);
				microcomunicacion += (tf.tv_usec - ti.tv_usec) + 1000000 * (tf.tv_sec - ti.tv_sec);
				if (status.MPI_TAG == DIE) break;

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
				gettimeofday(&tf, NULL);
				microseconds = (tf.tv_usec - ti.tv_usec) + 1000000 * (tf.tv_sec - ti.tv_sec);
				fprintf (stderr, "(PERF) Tiempo computacion (seconds) para el proceso %d = %lf\n", rank, (double) microseconds / 1E6);
				
				gettimeofday(&ti, NULL);
				//Envía al sevidor los resultados del trabajo encomendado
				MPI_Send(fila, Y_RESN, MPI_INT, 0, j, MPI_COMM_WORLD);
				//en el lugar del tag va la j -> para decirle al maestro la linea que calcule
				/*
				MPI_Send:
				void* buffer -> Dir del buffer de envío. Requiere un puntero.
				int contador -> Numero de elementos a enviar, en nuestro caso es 1 porque es un solo elem
				MPI_Datatype -> tipoDeDato de cada elemento que se va a enviar
				int destino -> proceso al que le quieres enviar el dato.
				int etiqueta -> entero que representa la etiqueta del mensaje(en manos del usuario)
				MPI_Comm communicador -> comunicador utilizado para la comunicacion
				*/
				gettimeofday(&tf, NULL);
				microcomunicacion += (tf.tv_usec - ti.tv_usec) + 1000000 * (tf.tv_sec - ti.tv_sec);
				fprintf (stderr, "\n (PERF) Tiempo de comunicacion(seconds) para el proceso %d = %lf\n", rank, (double) microcomunicacion / 1E6);

			}
			//Llegó la orden de fin (DIE_TAG) y enviaremos los floaps y finalizaremos
			//Fin de la computación. Se calcula el tiempo de cálculo sonsumido y se muestra

		}

		MPI_Reduce(&microseconds, &acu, 1, MPI_INT, MPI_SUM, 0, MPI_COMM_WORLD);//aqui calculo en el proceso 0 la media del tiempo de computacion
		/*
		MPI_Reduce: realizan una operación de reducción global (en este caso calculamos la suma sobre cada uno de los miembros del grupo)
			void* buffer -> Dirección inicial del buffer en envío.
			void* recvbuffer -> Dirección donde se va a almacenar el resultado
			int contador -> Entero que indica el número máximo de elementos que se espera recibir en el buffer de entrada
			MPI_Datatype -> Tipo de dato de cada elemento que se va a recibir.
			MPI_OP op -> operacion que quieres realizar
			int root -> Rango del proceso de origen esperado
			MPI_Comm communicador -> comunicador utilizado para la comunicacion
			Status -> Objeto de tipo MPI_Status, contiene datos relevantes sobre el mensaje (como son el origen (MPI_SOURCE) , la etiqueta (MPI_TAG)y el tamaño (size)).
		*/
		if (rank == maestro) {
			fprintf (stderr, "\n Media de tiempo de computacion = %lf, calculado por el proceso: %d\n\n", (((double)acu) / ((double)numprocs)) / 1E6, rank);
			fflush(stderr);
		}
		MPI_Reduce(&microcomunicacion, &acu_com, 1, MPI_INT, MPI_SUM, 0, MPI_COMM_WORLD);//aqui calculo en el proceso 0 la media del tiempo de computacion
		if (rank == maestro) {
			fprintf (stderr, "\n Media de tiempo de comunicación = %lf, calculado por el proceso: %d\n\n", (((double)acu_com) / ((double)numprocs)) / 1E6, rank);
			fflush(stderr);
		}

		//Esperamos a que todos los procesos terminen sus cálculos para que las medidas de tiempos de
		//comunicación no se vean afectadas por los tiempos de cálculo
		MPI_Barrier(MPI_COMM_WORLD);
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
		if (rank != maestro) {
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
