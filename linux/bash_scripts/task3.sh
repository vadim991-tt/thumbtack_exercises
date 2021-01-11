#!/bin/bash

function extract_day()
{
	filename=$(basename -- "$file")
	filename="${filename%.*}"
	echo $(date -d $filename +%a)
}

cd ~/Documents/Task1
touch monday.csv tuesday.csv wenesday.csv thursday.csv friday.csv saturday.csv sunday.csv 
for file in ./2020*
do
	day=$(extract_day $file)
	case $day in 
	Пн)
	cat $file >> monday.csv;;
	Вт)
	cat $file >> tuesday.csv;;
	Ср)
	cat $file >> wednesday.csv;;
	Чт)
	cat $file >> thursday.csv;;
	Пт)
	cat $file >> friday.csv;;
	Сб)
	cat $file >> saturday.csv;;
	Вс)
	cat $file >> sunday.csv;;
	esac
done
rm 2020*
for file in ./*
do
	awk '/date/&&c++ {next} 1' $file > temp && mv temp $file
done

