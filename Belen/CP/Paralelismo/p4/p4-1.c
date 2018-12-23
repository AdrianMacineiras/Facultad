/*The Mandelbrot set is a fractal that is defined as the set of points c
in the complex plane for which the sequence z_{n+1} = z_n^2 + c
with z_0 = 0 does not tend to infinity.*/

/*This code computes an image of the Mandelbrot set.*/

#include <stdio.h>
#include <sys/time.h>
//#include <mpi.h>

#define DEBUG	1
#define	X_RESN	1024	/* x resolution */
#define	Y_RESN	1024	/* y resolution */
#define	X_MIN	-2.0	/*Boundaries of the mandelbrot set*/
#define X_MAX	2.0
#define Y_MIN	-2.0
#define Y_MAX	2.0
#define	maxIterations	1000	/*Cuanto mayor, más detalle en la imagen y mayor coste computacional*/

#define DIE_TAG	-1 		//Etiqueta que indica a un proceso "calculador" que tiene que finalizar
#define WORK_TAG 0 		//Etiqueta que indica a un proceso "calculador" que tiene que trabajarse una fila

typedef struct complextype{
	float real, imag;
} Compl;

//Esta función recibe las coordenadas del punto a analizar, un puntero
//flops a una variable que va acumulando el nº de operaciones en punto
//flotante (flops) que realiza el proceso llamante, y devuelve el valor de k
//obtenido para ese punto y el valor de flops actualizado (incrementado)
//en el nº de flops realizadas
int get_k(int i, int j, int *flops, float pasoX, float pasoY){
	
	Compl z, c;
	float lengthsq, temp;
	int k;
	
	z.real = z.imag = 0.0;
	c.real = X_MIN + i * pasoX;
	c.imag = Y_MAX - j * pasoY;
	//Lo anterior ejecuta 4 flops
	*flops += 4;
	k = 0;

	//Iteramos la ecuanción de Mandelbrot mientras el cuadrado
	//del módulo no supere 4 y k no supere el nº máximo de
	//iteraciones permitido
	do { /* iterate for pixel color (falso color)*/
		temp = z.real*z.real - z.imag*z.imag + c.real;
		z.imag = 2.0*z.real*z.imag + c.imag;
		z.real = temp;
		lengthsq = z.real*z.real+z.imag*z.imag;
		k++;
		//Lo anterior ejecuta 10 flops
		*flops += 10;
	} while (lengthsq < 4.0 && k < maxIterations);
	
	return k;
}


int main (int argc, char** argv){
	
	int numprocs, rank, trabajador, flops=0, flops_total=0;
	MPI_Status status;
	int filaRecibida, filasCalculadas = 0;
	
	/* Mandelbrot variables */
	int i, j, k;
	
	//Variables para la medición de tiempos.
	struct timeval ti, tf;
	int microseconds;
	
	//Matriz que almacenrá el nº de iteraciones realizado en cada punto
	int res[Y_RESN][X_RESN];
	//Vector con los resultados de una fila
	int resParcial[X_RESN];
	
	//Calculamos (antes de entrar en los procesos paralelos) los
	//pasos (incrementos) de X e Y en cada cambio de fila y columna
	float pasoX = (X_MAX - X_MIN)/X_RESN;
	float pasoY = (Y_MAX - Y_MIN)/Y_RESN;

	//Se inician los procesos paralelos
	MPI_Init(&argc, &argv);
	MPI_Comm_size(MPI_COMM_WORLD, &numprocs);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
				
	//Vector que almacena el nº de flops realizadas por cada proceso
	int flops_por_proceso[numprocs];
	
	//------------------ Código del servidor -----------------------------
	if(rank == 0){
		
		//Reparto 1 primera fila de trabajo a todos los clientes
		for(j=0; j<numprocs; j++){
			MPI_Send(&j,1,MPI_INT,j+1,WORK_TAG,MPI_COMM_WORLD);
		}
		
		//Mientras no se reciban los resultados de todas la s filas
		while(filasCalculadas < Y_RESN){
			//Me quedo esperando a que los clientes vayan devolviendo los
			//resultados ...
			MPI_Recv(&resParcial,X_RESN,MPI_INT,MPI_ANY_SOURCE,ANY_TAG,MPI_COMM_WORLD,&status);
			filaRecibida = status.MPI_TAG;
			trabajador = status.MPI_SOURCE;
			res[filaRecibida] = resParcial; //Escribir esto bien
			filasCalculadas ++;
						
			// ... y mientras queden filas por analizar sigo enviando
			//trabajos
			if (j < X_RESN) {
				MPI_Send(&j,1,MPI_INT,trabajador,WORK_TAG,MPI_COMM_WORLD);
				j++;
			}	
		}
		//Envío orden de parada (DIE_TAG) a todos los procesos
		
	}
	//----------------- Código de los clientes -----------------------------
	if(rank > 0){
		/* Start measuring time */
		gettimeofday(&ti, NULL);
		while(1){
			//Espera la llegada del trabajo encomendado
			MPI_Recv(&j,1,MPI_INT,0,MPI_ANYTAG,MPI_COMM_WORLD,&status);
			if(status.MPI_TAG == DIE_TAG) break;
			//Cada proceso calcula la fila asignada por el servidor
			for(i=0; i < X_RESN; i++){
				k = get_k(i, j, &flops, pasoX, pasoY);
				//Si k alcanzó (sin superar el módulo es valor 2) el máximo
				//nº de iteraciones permitidas colocamos 0, si el módulo superó 2
				//antes de alcanzar maxIterations colocamos k
				if (k >= maxIterations) fila[i] = 0;
				else fila[i] = k;
			}
			//Envía al sevidor los resultados del trabajo encomendado
			MPI_Send(fila,X_RESN,MPI_INT,0,j,MPI_COMM_WORLD);
		}
		//Llegó la orden de fin (DIE_TAG) y enviaremos los floaps y finalizaremos
		//Fin de la computación. Se calcula el tiempo de cálculo sonsumido y se muestra
		gettimeofday(&tf, NULL);
		microseconds = (tf.tv_usec - ti.tv_usec) + 1000000 * (tf.tv_sec - ti.tv_sec);
		fprintf (stderr, "%d > (PERF) Computation Time (seconds) = %lf\n", rank, (double) microseconds/1E6);
	}

	
	//Esperamos a que todos los procesos terminen sus cálculos para que las medidas de tiempos de 
	//comunicación no se vean afectadas por los tiempos de cálculo
	MPI_Barrier(MPI_COMM_WORLD);
	
	//Inicio de las comunicaciones
	gettimeofday(&ti, NULL);


	
	//Fin de las comunicaciones e impresión del tiempo consumido en la comunicación
	gettimeofday(&tf, NULL);
	microseconds = (tf.tv_usec - ti.tv_usec) + 1000000 * (tf.tv_sec - ti.tv_sec);
	fprintf (stderr, "%d > (PERF) Comunications Time (seconds) = %lf\n", rank, (double) microseconds/1E6);
	
	//El proceso root recupera el nº de floats de cada proceso.
	MPI_Gather(&flops, 1, MPI_INT, &(flops_por_proceso), 1, MPI_INT, 0, MPI_COMM_WORLD);
	
	//El proceso 0 totaliza el nº de flops consumidos por todos los procesos
	if(rank == 0){
		for(i = 0; i < numprocs; i++){
			flops_total += flops_por_proceso[i];
		}
		//Se muestra una métrica del reparto del trabajo realizado del tipo:
		//"¿He trabajado más(>1) o menos(<1) de lo que debería haber trabajado
		//si la carga de trabajo (en flops) estuviera bien repartida?"
		for(i = 0; i < numprocs; i++){
			fprintf(stderr, "b%d = %5f  floaps = %d\n", i, (flops_total*1.0) / (flops_por_proceso[i]*1.0*numprocs), flops_por_proceso[i]);
		}
	}

	//Se vuelca a la salida estándar los valores de k por punto probado
	//¡Ojo!: aunque en el caso en que Y_RESN no es múltiplo de numprocs
	//el número de filas de la matriz res supera el valor Y_RESN, ¡solo!
	//se imprimen (se vuelca a fichero out.txt) las primeras Y_RESN (que
	//son las que tienen valores de k calculados) las restantes no se han
	//llegado a escribir.
	if( DEBUG && (rank == 0) ) {
		for(j=0;j<Y_RESN;j++) {
			for(i=0;i<X_RESN-1;i++){
				printf("%d\t", res[j][i]);
			}
			printf("%d\n", res[j][X_RESN-1] );
		}
	}
	
	MPI_Finalize();
}
