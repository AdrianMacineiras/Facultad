#!/bin/bash
echo "Compiling..."
mpicc mandelbrot.c -o mandelbrot
echo "###################################################################"
echo "Executing secuential..."
mpirun -np 2 mandelbrot > mandelbrot.txt
echo "###################################################################"
echo "Showing the image..."
python2 view.py mandelbrot.txt

