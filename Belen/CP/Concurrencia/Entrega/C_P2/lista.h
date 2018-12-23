#include <time.h>
#include <pthread.h>
#ifndef LISTA_CP
#define LISTA_CP

struct nodo {
	void  *dato;
	struct nodo * siguiente;
};

typedef struct nodo *pnodo;
typedef pnodo lista;

//*****************************
int crearLista(lista *);
int esListaVacia(lista);
pnodo primero(lista);
pnodo ultimo(lista);
void * obtenerDato(pnodo);
int eliminarDato(int, lista *);
void vaciarLista (lista *, void (*liberar)(void *));
pnodo siguiente(pnodo);
int insertarDato(void *, pnodo);
void actualizarDato (void *, pnodo, lista *);
int finLista(lista *, pnodo);
// siguiente a partir de una posicion.
// devuelva un entero si estamos al final de la lista

#endif
