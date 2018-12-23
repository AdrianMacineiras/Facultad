#include <stdlib.h>
#include <stdio.h>
#include "lista.h"

int crearLista(lista *l){
	pnodo aux = (pnodo) malloc(sizeof(struct nodo));
	if (aux == NULL) {
		return -1;
	}
	aux->siguiente = NULL;
	*l = aux;
	return 0;
}

int esListaVacia(lista l){
	return (l->siguiente == NULL);
}

pnodo primero(lista l){
	return (l->siguiente);
}

pnodo ultimo(lista l){
	pnodo aux=l;
	while (aux->siguiente != NULL) aux = aux->siguiente;
	return aux;
}

void * obtenerDato(pnodo p){
	return (p->dato);
}


pnodo siguiente(pnodo p) {
	pnodo pos = p;
	pos = pos->siguiente;
	return pos;
}

void vaciarLista (lista *l, void (*liberar)(void *)) {
	//Recorre la lista de manera recursiva para ir eliminando el primer elemento
	//hasta que sea imposible hayar un nuevo primer elemento.
	pnodo aux = primero(*l);
	pnodo aux2;
	while (aux != NULL) {
		aux2 = aux;
		aux = aux -> siguiente;
		liberar(aux2->dato);
		free(aux2);

	}
	free(*l);
}

int insertarDato(void * n,pnodo pos){
	pnodo aux = (pnodo) malloc(sizeof(struct nodo));
	if (aux == NULL) {
		return -1;
	}
	aux->dato = n;
	aux->siguiente = pos->siguiente;
	pos->siguiente=aux;
	return 0;	
}

int finLista(lista *l, pnodo pos){
	if (pos==NULL)
	{
		return -1;
	}
	return 0;
}
