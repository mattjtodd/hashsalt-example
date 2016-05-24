#!/bin/bash 
         
COUNTER=$1
ITERATIONS=$2
BLOCK=$3
RATE=$4
until [  $COUNTER -lt 1 ]; do
	echo COUNTER $COUNTER

	echo '{"password": "password'$COUNTER'"}'

	nohup curl --data '{"password": "password'$COUNTER'", "iterations": "'$ITERATIONS'", "block": "'$BLOCK'"}' --header "Content-Type:application/json" http://localhost:8080/encrypt </dev/null >/dev/null 2>&1 &
	
	sleep $RATE
	let COUNTER-=1
done