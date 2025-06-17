#!/bin/bash

if [[ $# -lt 2 ]] 
then
	echo "USAGE: <N> <file1> <file2> ..."
	exit 1
fi

N=$1
shift

OUTFILE="result.txt"

for file in "$@"; 
do
	awk -v n="$N" '{print substr($0, 1, n)}' "$file" >> "$OUTFILE" 
	echo "EXTRACTED FROM FILE: ${file}" >> "$OUTFILE"
done

