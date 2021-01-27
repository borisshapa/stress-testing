#!/bin/bash

cd $1

g++ code1.cpp -o code1
g++ code2.cpp -o code2
g++ script.cpp -o script

for i in {1..100}; do
	test=`./script`

	code1_result=`./code1 <<< $test`
	code2_result=`./code2 <<< $test`

	diff=`diff <(echo "$code1_result") <(echo "$code2_result")`
	if [ -n "$diff" ]; then
		echo "$diff"
		echo -e "\nTEST:"
		echo "$test"
		exit
	fi
done

echo "Outputs for 100 tests are the same."
