CC=mpicc

PROGS= producer

all: 
	$(CC) mandel.c -o mandel
	mpirun -np 2 mandel
ex:
	mpirun -np 2 mandel

save:
	mpirun -np 2 mandel > mandel.txt

clean:
	rm -f *.o
cleanall:
	rm *.txt
