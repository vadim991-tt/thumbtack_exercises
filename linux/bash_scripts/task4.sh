#!/bin/bash

cd ~/Documents
touch File_with_arrays
IFS=$'\n' array=( $(find ~/Thumbtack/ -type f -name *.java) )
for file in ${array[@]}; do 
    if grep -Fq "ArrayList" $file
    then
    	cat $file >> File_with_arrays
    fi
done
