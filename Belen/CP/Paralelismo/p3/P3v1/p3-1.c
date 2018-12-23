/*The Mandelbrot set is a fractal that is defined as the set of points c
in the complex plane for which the sequence z_{n+1} = z_n^2 + c
with z_0 = 0 does not tend to infinity.*/

/*This code computes an image of the Mandelbrot set.*/

#include <stdio.h>
#include <sys/time.h>
#include <mpi.h>

#define DEBUG	1
#define	X_RESN	1024	/* x resolution */
#define	Y_RESN	1024	/* y resolution */
#define	X_MIN	-2.0	/*Boundaries of the mandelbrot set*/
#define X_MAX	2.0
#define Y_MIN	-2.0
#define Y_MAX	2.0
#define	maxIterations	1000	/*Cuanto mayor, más detalle en la imagen y mayor coste computacional*/

typedef struct complextype{
	float real, imag;
} Compl;


int main (int argc, char** argv){
	
	int numprocs, rank, j_rel;

	MPI_Init(&argc, &argv);
	MPI_Comm_size(MPI_COMM_WORLD, &numprocs);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
			
	/* Mandelbrot variables */
	int i, j, k;
	Compl z, c;
	float lengthsq, temp;
	
	int filas_por_proceso = Y_RESN/numprocs;
	int res_parcial[filas_por_proceso][X_RESN];
	int res[Y_RESN][X_RESN];
	struct timeval ti, tf;
	int microseconds;

	/* Start measuring time */
	gettimeofday(&ti, NULL);

	/* Calculate and draw points */
	j_rel = 0;

	for(j=rank*filas_por_proceso; j<(rank+1)*filas_por_proceso; j++, j_rel++){
		for(i=0; i < X_RESN; i++){
			z.real = z.imag = 0.0;
			c.real = X_MIN + j * (X_MAX - X_MIN)/X_RESN;
			c.imag = Y_MAX - i * (Y_MAX - Y_MIN)/Y_RESN;
			k = 0;

			do { /* iterate for pixel color */
				temp = z.real*z.real - z.imag*z.imag + c.real;
				z.imag = 2.0*z.real*z.imag + c.imag;
				z.real = temp;
				lengthsq = z.real*z.real+z.imag*z.imag;
				k++;
			} while (lengthsq < 4.0 && k < maxIterations);

			if (k >= maxIterations){
				res_parcial[j_rel][i] = 0;			
			}else{
				res_parcial[j_rel][i] = k;
			}
		}
	}
	
	MPI_Gather(res_parcial, X_RESN * filas_por_proceso, MPI_INT, res, X_RESN * filas_por_proceso, MPI_INT, 0, MPI_COMM_WORLD);

	/* End measuring time */
	gettimeofday(&tf, NULL);
	microseconds = (tf.tv_usec - ti.tv_usec)+ 1000000 * (tf.tv_sec - ti.tv_sec);

	if( DEBUG && (rank == 0) ) {
		for(i=0;i<X_RESN;i++) {
			for(j=0;j<Y_RESN-1;j++){
				printf("%d\t", res[j][i]);
			}
			printf("%d\n", res[Y_RESN-1][i] );
		}
	}
	
	MPI_Finalize();
}
