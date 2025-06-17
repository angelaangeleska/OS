#!/bin/bash

if [[ $# -ne 2 ]]
then
	echo "Usage: script.sh <START_DATE> <END_DATE> (format: YYYY-MM-DD)"
	exit 1
fi

START_DATE=$1
END_DATE=$2
FILE='logs.csv'

if [[ ! ${START_DATE} =~ ^[0-9]{4}-[0-9]{2}-[0-9]{2}$ ]]
then
	echo "Invalid start date format. Please input YYYY-MM-DD"
	exit 1
fi

if [[ ! ${END_DATE} =~ ^[0-9]{4}-[0-9]{2}-[0-9]{2}$ ]]
then
	echo "Invalid end date format. Please input: YYYY-MM-DD"
	exit 1
fi

if [[ ! ${START_DATE} -ge ${END_DATE} ]] 
then
	echo "Start date must be before end date"
	exit 1
fi

LOGS=$(awk -v s="$START_DATE" -v e="$END_DATE" '$1 >= s && $1 <= e {print}' "$FILE")

echo "$LOGS"
