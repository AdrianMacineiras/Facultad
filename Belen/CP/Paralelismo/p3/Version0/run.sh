#!/bin/bash
echo "Compiling..."
gcc mandel.c -o mandel
echo "###################################################################"
echo "Executing secuential..."
./mandel > mandel.txt
echo "###################################################################"
echo "Showing the image..."
python2 view.py mandel.txt

