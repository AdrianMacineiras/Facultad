#!/bin/bash
echo "Compiling..."
mpicc p3-1.c -o p3-1
echo "###################################################################"
echo "Executing parallel ..."
mpirun -np 4 ./p3-1 > out.txt
echo "###################################################################"
echo "Showing the image..."
python2 view.py out.txt

