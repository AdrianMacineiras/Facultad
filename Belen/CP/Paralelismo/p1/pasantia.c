#include <stdio.h>
#include <math.h>
#include <mpi.h>

//jorge gonzalez

int main(int argc, char *argv[]) //contadors de argumentos; puntero al array donde empiezan los argumentos
{
    int i, N, count=0;
    double PI25DT = 3.141592653589793238462643;
    double pi, h, sum, subsum, x;

	//Variables e inicialización del entorno MPI
	int numprocs, rank;
	
	MPI_Init(&argc, &argv);
	MPI_Comm_size(MPI_COMM_WORLD, &numprocs);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);

	if(rank == 0){
		//Leo desde teclado el numero de intervalos y se lo envío a todos los procesos
		printf("Enter the number of intervals: (0 quits) \n");
		scanf("%d", &N);
		
		for(i=1; i<numprocs; i++){
			MPI_Send(&N, 1, MPI_INT, i, 0, MPI_COMM_WORLD);
		}

		h = 1.0 / (double) N;
		sum = 0.0;

		//Quedo a la espera de los subresultados y los integro
		for(i=1; i<numprocs; i++){        //|quiero recibir del proceso i
			MPI_Recv(&subsum, 1, MPI_DOUBLE, i, 0, MPI_COMM_WORLD, NULL); //NUUUUUUUUUUUUUUUUUUUUL o mejor signal????
			printf("Recibido del proceso %d la subsuma %f\n", i, subsum);
			sum = sum + subsum;
		}
		pi = h * sum;
		
		printf("pi is approximately %.16f, Error is %.16f\n", pi, fabs(pi - PI25DT));
		
	}else{
		//Espero la recepción de n (el número de intervalos)
		MPI_Recv(&N, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, NULL);  //NUUUUUUUUUUUUUUUUUUUUL o mejor signal????
		printf("Soy el proceso %d y he recibido N=%d\n", rank, N);
		//Realizo el trabajo encomendado	
        h   = 1.0 / (double) N;
        subsum = 0.0;
        for (i = rank; i <= N; i+=(numprocs-1)) { //para repartir el trabajo 
            x = h * ((double)i - 0.5);
            subsum += 4.0 / (1.0 + x*x);
            count ++;
        }
        	
		//Devuelvo el subresultado
		printf("Soy el proceso %d, ejecuté %d intervalos y mi subsuma es %f\n", rank, count, subsum);
		MPI_Send(&subsum, 1, MPI_DOUBLE, 0, 0, MPI_COMM_WORLD);	
	}
	
	MPI_Finalize();
	
    
    return 0;
}
