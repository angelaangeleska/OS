#!/bin/bash

if [ $# -ne 2 ]
then 
	echo "Usage: script.sh <STATUS> <MONTH>"
	exit 1
fi

STATUS="$1"
MONTH="$2"
FILE="system_logs.tsv"

if [[ ! "$MONTH" =~ ^[0-9]{4}-[0-9]{2}$ ]]
then
	echo "Invalid month format. Use YYYY-MM"
	exit 1
fi

if [ ! -f "system_logs.tsv" ]  # -f proveruva dali postoi file vo momentalniot direktorium
then
	echo "system_logs.tsv file does not exist!"
	exit 1
fi

COUNT=$(awk -F'\t' -v m="$MONTH" -v s="$STATUS" 'BEGIN {count=0} $3==s && $1 ~ "^"m {count++} END {print count}' "$FILE")
echo "Number of logs with status ${STATUS} in month ${MONTH}: ${COUNT}"

MOST_COMMON=$(awk -F'\t' -v s="$STATUS" '$3==s && NR>1 {adresses[$4]++} END {max=0; for(ip in adresses) if(adresses[ip]>max){ max=adresses[ip]; most_common=ip} print most_common}' "$FILE")
echo "Most common IP address for logs with status '${STATUS}': ${MOST_COMMON}"

STATUSES_COUNT=$(awk -F'\t' -v m="$MONTH" 'BEGIN {statuses["ERROR"]=0; statuses["WARN"]=0; statuses["INFO"]=0; statuses["DEBUG"]=0} $1~"^"m {statuses[$3]++} END{printf "%d\t%d\t%d\t%d", statuses["ERROR"], statuses["WARN"], statuses["INFO"], statuses["DEBUG"]}' "$FILE")

IFS=$'\t' read ERR WARN INFO DEB <<< "$STATUSES_COUNT"
echo "Count per status for month ${MONTH}:"
echo "- ERROR: ${ERR}"
echo "- WARN: ${WARN}"
echo "- INFO: ${INFO}"
echo "- DEBUG: ${DEB}"
