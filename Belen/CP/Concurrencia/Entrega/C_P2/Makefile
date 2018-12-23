
CC=gcc
CFLAGS=-Wall -pthread -g -O0
LIBS=
OBJS= producer.o options.o lista.o

PROGS= producer

all: $(PROGS)

%.o : %.c
	$(CC) $(CFLAGS) -c $<

producer: $(OBJS)
	$(CC) $(CFLAGS) -o $@ $(OBJS) $(LIBS)

ex:
	./producer

ex2:
	./producer -i 1
	
clean:
	rm -f $(PROGS) *.o *~
test:
	sh tests.sh

save:
	sh save.sh
cleanall:
	rm *.txt
