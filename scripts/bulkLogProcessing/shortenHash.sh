#!/bin/bash
loc=$1

OIFS=$IFS
IFS=$'\n'
cd $loc
for i in $(ls)
do
mv "$i" "${i:0:10}"

done


IFS=$OIFS
echo "done"
