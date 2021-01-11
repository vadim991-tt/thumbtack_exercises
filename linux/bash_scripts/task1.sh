#!/bin/bash

mkdir Task1 
cd ./Task1
for ((a=0; a<366; a++))
do
	date=$(date -I -d "2020-01-01 + $a day")
	file=$date'.csv'
	touch $file
	echo "cite;country;date;views;clicks" >> $file
	echo "www.abc.com;USA;$date;$RANDOM;$RANDOM" >> $file
	echo "www.bca.com;France;$date;$RANDOM;$RANDOM" >> $file

done

