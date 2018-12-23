#ifndef __OPTIONS_H__
#define __OPTIONS_H__

struct options {
	int num_producers;
	int num_consumers;
	int buffer_size;
	int iterations;
	//aÃ±adidos
	int num_full;
	int num_empty;
};

int read_options(int argc, char **argv, struct options *opt);


#endif
