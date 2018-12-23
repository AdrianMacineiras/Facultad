#include <stdio.h>
#include <mpi.h>
#include <math.h>
#include <stdlib.h>


int main(int argc, char *argv[]) {  //contador de argumentos; puntero al array donde empiezan los argumentos

	int numprocs, rank;

	MPI_Init(&argc, &argv);
	MPI_Comm_size(MPI_COMM_WORLD, &numprocs);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);

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

	int i, done = 0, n;
	int count=0;
	double PI25DT = 3.141592653589793238462643;
	double pi, h, sum, x, acu, total, subsuma;
	
	while (!done) {
		if (rank == 0) { //si se trata del proceso 0
		//Leo desde teclado el numero de intervalos y se lo envío a todos los procesos
			printf("Enter the number of intervals: (0 quits) \n");
			scanf("%d", &n);  // lee los datos de entrada; N= nº intervalos en los que se divide la curva
			//pones k=1 para que no exista la probabilidad de que exista un interbloqueo al hacer el recv ya que el proc 0 estaria esperando x algo que envia el mismo y que ya sabe
			for (int k = 1; k < numprocs; k++) 
				MPI_Send(&n, 1, MPI_INT, k, 0, MPI_COMM_WORLD);
			/*
			MPI_Send:
				void* buffer -> Dir del buffer de envío. Requiere un puntero.
							 -> Si se pretende enviar un elemento, se puede enviar el puntero a este (&elemento).
				int contador -> Numero de elementos a enviar, en nuestro caso es 1 porque es un solo elem
				MPI_Datatype -> tipoDeDato de cada elemento que se va a enviar
				int destino -> proceso al que le quieres enviar el dato.
				int etiqueta -> entero que representa la etiqueta del mensaje(en manos del usuario)
				MPI_Comm communicador -> comunicador utilizado para la comunicacion
			*/
				printf("Soy el proceso %d y he enviado N=%d\n", rank, n);

		}
		else {
			MPI_Recv(&n, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, NULL);
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
			printf("Soy el proceso %d y he recibido N=%d\n", rank, n);
		}

		if (n == 0) break;
		
		//calculo de pi para todos los procesos
		h = 1.0 / (double) n;
		sum = 0.0;
		for (i = rank + 1; i <= n; i += numprocs) { //bucle donde repartes los intervalos para cada proceso
			x = h * ((double)i - 0.5);
			sum += 4.0 / (1.0 + x * x);
			count ++; //numero de veces que se eejecuto un proceso
		}
		subsuma = h * sum;
		printf("Soy el proceso %d, ejecuté %d intervalos y mi subsuma es %f\n", rank, count, subsuma);

		total = 0;
		
		if (rank == 0) {
			for (int i = 1; i < numprocs; i++) {
				MPI_Recv(&acu, 1, MPI_DOUBLE, MPI_ANY_SOURCE, 1, MPI_COMM_WORLD, NULL); // MPI_ANY_SOURCE -> Acepta un mensaje de cualquiera.
				printf("Recibido del proceso %d la subsuma %f\n", i, acu);
				total = total + acu; //total de las subsumas de los otros procesos
			}
			pi = total + subsuma; //en total tienes la suma de los procesos<>0 por eso le sumas la subsuma(p0)
			printf("pi is approx. %.16f, Error: %.16f\n", pi, fabs(pi - PI25DT));
		}
		else {
			MPI_Send(&subsuma, 1, MPI_DOUBLE, 0, 1, MPI_COMM_WORLD);// Envio todos los datos al 0 para que haga la suma 
		}
	}
	MPI_Finalize(); //Corta la comunicación entre los procesos y elimina los tipos de datos creados para ello.

}