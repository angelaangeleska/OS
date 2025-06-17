#!/bin/bash

COUNT=$(ps aux | awk 'BEGIN {count=0} $11 ~ /^nano/ && $1 ~ /^[0-9]/ {count++}
END {print count}')

echo "Num. of students that are using command 'nano' is: ${COUNT}"
