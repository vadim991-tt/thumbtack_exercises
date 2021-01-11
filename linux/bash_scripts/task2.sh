#!/bin/bash

cd ~/Documents/Task1
number=$(ls | wc -l)
test $number -eq 366 && echo True || echo False
for file in ./*
do
	awk 'BEGIN{FS=OFS=";"}{t=$1;$1=$3;$3=t;print}' $file > tmp && mv tmp $file
	sed -i 's:\([0-9][0-9][0-9][0-9]\)-\([0-9][0-9]\)-\([0-9][0-9]\):\3\/\2\/\1:g' $file 
done
