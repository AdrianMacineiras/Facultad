#!/bin/bash
echo "Compiling..."
mpicc mandel.c -o mandel
echo "###################################################################"
echo "Executing secuential..."
mpirun -np 2 mandel > mandel.txt
echo "###################################################################"
echo "Showing the image..."
python2 view.py mandel.txt

