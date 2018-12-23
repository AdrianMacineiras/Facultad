#include <stdio.h>
#include <mpi.h>
#include <math.h>
#include <stdlib.h>
//mpicc -o pi2 pi2.c -lm

int MPI_BinomialBcast(void *buffer, int count, MPI_Datatype datatype, int root,  MPI_Comm comm) {
	int i,numprocs, rank;
	MPI_Status status;
	MPI_Comm_size(MPI_COMM_WORLD, &numprocs);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);

	//empizas en i=1 para que 2^i-1 cuando el rango=0 sea 1 (paso 1)
	for (i = 1; i <= log2(numprocs); i++) { //numero de pasos que hay que realizar ejm: con 8 procesos necesitas log2(8)=3 pasos
		if ( rank < (int)pow(2, i - 1) )
			MPI_Send(buffer, count, datatype, rank + pow(2, i - 1), 0, comm);
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
		if (rank >= (int)pow(2, i - 1) && rank < (int)pow(2, i)) {
			MPI_Recv(buffer, count, datatype, i, 0, comm, &status);
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
			if (status.MPI_ERROR != MPI_SUCCESS) {//si la operacion no ha salido bien 
				printf("Se ha producido un error\n");
				MPI_exit();
			}

		}
	}
}

/************************************/

int MPI_FattreeReduce(void *sendBuffer, void *recvBuffer, int count, MPI_Datatype datatype, MPI_Op op, int root, MPI_Comm comm)
{
	int i,numprocs, rank;
	MPI_Status status;
	MPI_Comm_size(MPI_COMM_WORLD, &numprocs);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
	double subSuma;
	*(double*)recvBuffer = *(double*)sendBuffer; //lo que hay en el proceso raiz

	if (rank == root) { //si soy el raiz tengo que recibir de los demas procesos
		for (i = 0; i < numprocs; i++) { //llevar la cuenta de las sumas que estas haciendo
			if (rank != i) { //para que no se produzca un bloqueo cuando se envia el 0 a si mismo
				MPI_Recv(&subSuma, count, datatype, i, 0, comm, &status); //le pasamos la i porque es el proceso en el que estamos
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
				if (status.MPI_ERROR == MPI_SUCCESS) //si la operacion ha salido bien sumo(operacion que le mando hacer)!
					*(double *) recvBuffer += subSuma;
			}
		}
	}
	else
		MPI_Send(sendBuffer, count, datatype, root, 0, comm);
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
	return status.MPI_ERROR;
}

/************************************/

int MPI_FattreeColectiva(void *buffer, int count, MPI_Datatype datatype, int root, MPI_Comm comm){
	int i, rank, numprocs;
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
	MPI_Comm_size(MPI_COMM_WORLD, &numprocs);
	MPI_Status status;

	if(rank == root){ //si soy el raiz tengo que enviar los datos
		for(i = 0; i < numprocs; i++) //empiezas en 0 porque si el root no es el 0 tendrias que enviarle la info a el tambien
			if(i != root) //para que no se produzca un bloqueo cuando se envia el 0 a si mismo
				MPI_Send(buffer, count, datatype, i, 0, comm);
	} else { //si no soy el raiz recivo
		MPI_Recv(buffer, count, datatype, root, 0, comm, &status);
		if (status.MPI_ERROR != MPI_SUCCESS) //si la operacion no ha salido bien error!
				printf("Se ha producido un error\n");
	}
	return status.MPI_ERROR;
}

/************************************/

int main(int argc, char *argv[]) {  //contador de argumentos; puntero al array donde empiezan los argumentos

	int numprocs, rank;

	if (MPI_Init(&argc, &argv) != MPI_SUCCESS) exit(1);
	if (MPI_Comm_size(MPI_COMM_WORLD, &numprocs) != MPI_SUCCESS) exit(1);
	if (MPI_Comm_rank(MPI_COMM_WORLD, &rank) != MPI_SUCCESS) exit(1);
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
	int count = 0;
	double PI25DT = 3.141592653589793238462643;
	double pi, h, sum, x;

	while (!done) {
		if (rank == 0) { //si se trata del proceso 0
			//Leo desde teclado el numero de intervalos y se lo envío a todos los procesos
			printf("Enter the number of intervals: (0 quits) \n");
			scanf("%d", &n);  // lee los datos de entrada; N= nº intervalos en los que se divide la curva
		}
		MPI_FattreeColectiva(&n, 1, MPI_INT, 0, MPI_COMM_WORLD); //MPI_Bcast
		/*
		MPI_Bcast:  envía un único mensaje desde el proceso que tenga el rango de raíz (root) a todos los procesos del grupo
			void* buffer ->[El mismo que de salida] Dirección inicial del buffer (a enviar para la raíz).
			int contador -> Numero de elementos en el buffer, en nuestro caso es 1 porque es un solo elem
			MPI_Datatype -> tipoDeDato de cada elemento que se va a enviar
			int root -> Rango (rank) del proceso que transmitirá el mensaje al resto (int).
			MPI_Comm communicador -> comunicador utilizado para la comunicacion
		*/
		if (n == 0) break;

		//calculo de pi para todos los procesos
		h = 1.0 / (double) n;
		sum = 0.0;
		for (i = rank + 1; i <= n; i += numprocs) { //bucle donde repartes los intervalos para cada proceso -> le sumas rank +1 para que no de negatico los calculos
			x = h * ((double)i - 0.5);
			sum += 4.0 / (1.0 + x * x);
			count ++; //numero de veces que se eejecuto un proceso
		}
		pi = h * sum;

		double recive; //buffer donde almacenaremos la salida del MPI_reduce(suma)
		MPI_Reduce(&pi, &recive, 1, MPI_DOUBLE, MPI_SUM, 0, MPI_COMM_WORLD); 
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
		if (rank == 0)
			printf("pi is approx. %.16f, Error: %.16f\n", recive, fabs(recive - PI25DT));
	}
	MPI_Finalize(); //Corta la comunicación entre los procesos y elimina los tipos de datos creados para ello.
}