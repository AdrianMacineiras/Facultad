#include <stdio.h>
#include <mpi.h>
#include <math.h>
#include <stdlib.h>
//mpicc -o pi2 pi2.c -lm
// El binomial bcast es util en maquinas conectadas en red con pocos procesadores porque cada proceso se encarga de ir enviandolo a los demas no es uno solo el que lo hace
int MPI_BinomialBcast(void *buffer, int count, MPI_Datatype datatype, int root, MPI_Comm comm){
	int i, orig, dest, rank, numprocs, returnValue;														
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
	MPI_Comm_size(MPI_COMM_WORLD, &numprocs);
	//numero de pasos que hay que realizar ejm: con 8 procesos necesitas log2(8)=3 pasos
	//empizas en i=1 para que 2^i-1 cuando el rango=0 sea 1 (paso 1)
	for(i = 1;;i++){ // 2 cond siempre true; 
		// Me toca enviar
		if(rank < pow(2, i-1)){
			dest = rank + pow(2, i-1);
			if(dest >= numprocs) break;
			//Guardamos el valor devuelto por MPI_Send, y si es diferente de MPI_SUCCESS
			//(constante de la librería que indica ejecución sin error) finalizamos
			//el bucle de forma abrupta y devolvemos el error
			returnValue = MPI_Send(buffer, count, datatype, dest, 0, comm);
			/*
			MPI_Send:
				void* buffer -> Dir del buffer de envío. Requiere un puntero.
				int contador -> Numero de elementos a enviar, en nuestro caso es 1 porque es un solo elem
				MPI_Datatype -> tipoDeDato de cada elemento que se va a enviar
				int destino -> proceso al que le quieres enviar el dato.
				int etiqueta -> entero que representa la etiqueta del mensaje(en manos del usuario)
				MPI_Comm communicador -> comunicador utilizado para la comunicacion
			*/
			if(returnValue != MPI_SUCCESS){
				break;
			}
		}
		// Quizás me toque recibir
		else {
			orig = rank - pow(2, i-1);
			// Efectivamente me toca recibir
			if(orig < pow(2, i-1)){
				//Guardamos el valor devuelto por MPI_Recv para devolverlo al salir de la función
				returnValue = MPI_Recv(buffer, count, datatype, orig, 0, comm, MPI_STATUS_IGNORE);
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
			}
		}
	}
	return returnValue;
}

/***********************************/

int MPI_FattreeColectiva(void *buffer, int count, MPI_Datatype datatype, int root, MPI_Comm comm) {
	int i, rank, numprocs;
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
	MPI_Comm_size(MPI_COMM_WORLD, &numprocs);
	MPI_Status status;

	if (rank == root) { //si soy el raiz tengo que enviar los datos
		for (i = 0; i < numprocs; i++) //empiezas en 0 porque si el root no es el 0 tendrias que enviarle la info a el tambien
			if (i != root) //para que no se produzca un bloqueo cuando se envia el 0 a si mismo
				MPI_Send(buffer, count, datatype, i, 0, comm);
	} else { //si no soy el raiz recivo
		MPI_Recv(buffer, count, datatype, root, 0, comm, &status);
		if (status.MPI_ERROR != MPI_SUCCESS) { //si la operacion no ha salido bien error!
			printf("Se ha producido un error\n");
			MPI_Abort(comm, 1); //le paso el comunicador y un codigo de error
		}
	}
	return status.MPI_ERROR;
}

/***********************************/

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
	int count = 0;
	double PI25DT = 3.141592653589793238462643;
	double pi, h, sum, x;

	while (!done) {
		if (rank == 0) { //si se trata del proceso 0
			//Leo desde teclado el numero de intervalos y se lo envío a todos los procesos
			printf("Enter the number of intervals: (0 quits) \n");
			scanf("%d", &n);  // lee los datos de entrada; N= nº intervalos en los que se divide la curva
		}
		//MPI_Bcast(&n, 1, MPI_INT, 0, MPI_COMM_WORLD); //MPI_Bcast
		//MPI_FattreeColectiva(&n, 1, MPI_INT, 0, MPI_COMM_WORLD); //MPI_Bcast
		MPI_BinomialBcast(&n, 1, MPI_INT, 0, MPI_COMM_WORLD); //MPI_Bcast
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
			count ++; //numero de veces que se ejecutó un proceso
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