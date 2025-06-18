#!/bin/bash

if [[ $# -ne 1 ]] 
then
	echo "Usage: script.sh <logfile>"
	exit 1
fi

FILE=$1

COUNT_EVENTS=$(awk -F' *\\| *' '{event[$2]++} END {for (e in event) {printf "%s: %d\n", e, event[e]}}' "$FILE")

echo "========== Event Type Counts =========="
echo "$COUNT_EVENTS"

UNIQUE_USERS=$(awk -F' *\\| *' '{users[$4]++} END {for (u in users) printf "%s\n", u}' "$FILE")

echo "======= List of Unique Users =========="
echo "$UNIQUE_USERS"

EVENTS_PER_USER=$(awk -F' *\\| *' '{users[$4]++} END {for (u in users) {printf "%s: %d\n", u, users[u]}}' "$FILE" | sort -t':' -k2 -r -n)
echo "======= Number of Events per User ====="
echo "$EVENTS_PER_USER"

MOST_FREQ_EVENT=$(awk -F' *\\| *' '{events[$2]++} END {max=0; for (e in events) {if (max<events[e]){max=events[e]; max_event=e}}; printf "%s %d", max_event, max}' "$FILE")
echo "======= Most Frequent Event Type ======="
echo "$MOST_FREQ_EVENT"
