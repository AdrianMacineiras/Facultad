
#include <getopt.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "options.h"

static struct option long_options[] = {
	{ .name = "producers", // nombre
	  .has_arg = required_argument,// has_arg nos dice si tiene argumentos 
	  .flag = NULL, // bandera
	  .val = 0}, // valor
	{ .name = "consumers",
	  .has_arg = required_argument,
	  .flag = NULL,
	  .val = 0},
	{ .name = "buffer_size",
	  .has_arg = required_argument,
	  .flag = NULL,
	  .val = 0},
	{ .name = "iterations",
	  .has_arg = required_argument,
	  .flag = NULL,
	  .val = 0},
	{ .name = "num_full",
	  .has_arg = required_argument,
	  .flag = NULL,
	  .val = 0},
	{ .name = "num_empty",
	  .has_arg = required_argument,
	  .flag = NULL,
	  .val = 0},
	{0, 0, 0, 0}
};

static void usage(int i) // esto es para cuando se lanza la ayuda.
{
	printf(
		"Usage:  producers [OPTION] [DIR]\n"
		"Launch producers and consumers\n"
		"Opciones:\n"
		"  -p n, --producers=<n>: number of producers\n"
		"  -c n, --consumers=<n>: number of consumers\n"
		"  -b n, --buffer_size=<n>: number of elements in buffer\n"
		"  -i n, --iterations=<n>: total number of iterations\n"
		"  -f n, --num_full=<n>: max. veces cola llena antes de crear nuevo consumidor\n"
		"  -e n, --num_empty=<n>: max. veces cola vacÃ­a antes de destruÃ­r un consumidor\n"
		"  -h, --help: muestra esta ayuda\n\n"
	);
	exit(i);
}

static int get_int(char *arg, int *value)
{
	char *end; // aqui es donde se guarda la salida.
	*value = strtol(arg, &end, 10);
		/*convierte la parte inicial de la cadena de  entrada
       arg  en  un  valor  entero de tipo long de acuerdo a la base dada (en este caso 10), que
       debe estar entre 2 y 36 ambos incluidos o ser el valor especial 0. La end es la salida.*/
	return (end != NULL); // devuelve el char end si no es nulo.
}

static void handle_long_options(struct option option, char *arg, struct options *opt)
{
	if (!strcmp(option.name, "help")) // strcmp compara dos strings
		usage(0);

	if (!strcmp(option.name, "producers")) {
		if (!get_int(arg, &opt->num_producers)
		    || opt->num_producers <= 0) {
			printf("'%s': no es un entero vÃ¡lido\n", arg);
			usage(-3);
		}
	}
	if (!strcmp(option.name, "consumers")) {
		if (!get_int(arg, &opt->num_consumers)
		    || opt->num_consumers <= 0) {
			printf("'%s': no es un entero vÃ¡lido\n", arg);
			usage(-3);
		}
	}
	if (!strcmp(option.name, "buffer_size")) {
		if (!get_int(arg, &opt->buffer_size)
		    || opt->buffer_size <= 0) {
			printf("'%s': no es un entero vÃ¡lido\n", arg);
			usage(-3);
		}
	}
	if (!strcmp(option.name, "iterations")) {
		if (!get_int(arg, &opt->iterations)
		    || opt->iterations <= 0) {
			printf("'%s': no es un entero vÃ¡lido\n", arg);
			usage(-3);
		}
	}
	if (!strcmp(option.name, "num_full")) {
		if (!get_int(arg, &opt->num_full)
		    || opt->num_full <= 0) {
			printf("'%s': no es un entero vÃ¡lido\n", arg);
			usage(-3);
		}
	}
	if (!strcmp(option.name, "num_empty")) {
		if (!get_int(arg, &opt->num_empty)
		    || opt->num_empty <= 0) {
			printf("'%s': no es un entero vÃ¡lido\n", arg);
			usage(-3);
		}
	}
}

static int handle_options(int argc, char **argv, struct options *opt)
{
	while (1) {
		int c;
		int option_index = 0;

		c = getopt_long (argc, argv, "hp:c:b:i:f:e:",// si no recibe argumentos despues no lleva los dos puntos
				 long_options, &option_index);
		if (c == -1)
			break;

		switch (c) {
		case 0:
			handle_long_options(long_options[option_index],
					    optarg, opt);
			break;

		case 'p':
			if (!get_int(optarg, &opt->num_producers)
			    || opt->num_producers <= 0) {
				printf("'%s': no es un entero vÃ¡lido\n",
				       optarg);
				usage(-3);
			}
			break;


		case 'c':
			if (!get_int(optarg, &opt->num_consumers)
			    || opt->num_consumers <= 0) {
				printf("'%s': no es un entero vÃ¡lido\n",
				       optarg);
				usage(-3);
			}
			break;

		case 'b':
			if (!get_int(optarg, &opt->buffer_size)
			    || opt->buffer_size <= 0) {
				printf("'%s': no es un entero vÃ¡lido\n",
				       optarg);
				usage(-3);
			}
			break;

		case 'i':
			if (!get_int(optarg, &opt->iterations)
			    || opt->iterations <= 0) {
				printf("'%s': no es un entero vÃ¡lido\n",
				       optarg);
				usage(-3);
			}
			break;
			
		case 'f':
			if (!get_int(optarg, &opt->num_full)
			    || opt->num_full <= 0) {
				printf("'%s': no es un entero vÃ¡lido\n",
				       optarg);
				usage(-3);
			}
			break;

		case 'e':
			if (!get_int(optarg, &opt->num_empty)
			    || opt->num_empty <= 0) {
				printf("'%s': no es un entero vÃ¡lido\n",
				       optarg);
				usage(-3);
			}
			break;

		case '?':
		case 'h':
			usage(0);
			break;

		default:
			printf ("?? getopt returned character code 0%o ??\n", c);
			usage(-1);
		}
	}
	return 0;
}


int read_options(int argc, char **argv, struct options *opt) {

	int result = handle_options(argc, argv, opt);

	if (result != 0)
		exit(result);

	if (argc - optind != 0) {
		printf ("Extra arguments\n\n");
		while (optind < argc)
			printf ("'%s' ", argv[optind++]);
		printf ("\n");
		usage(-2);
	}

	return 0;
}
