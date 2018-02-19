#!/bin/bash
# credits to AI CG:D
TEST_DIR=test
TEST_LIST=$TEST_DIR/tests

# Compile student homework
make build

# Run tests
score=0
total_score=0
missed=0

while read test_case
do
	# Parse the components of the line
	weight="$(echo $test_case | cut -d' ' -f 1)"
	file_name="$(echo $test_case | cut -d' ' -f 2)"
	turing_machine="$(echo $test_case | cut -d' ' -f 3)"
	tape_input="$(echo $test_case | cut -d' ' -f 4)"
	tape_output="$(echo $test_case | cut -d' ' -f 5)"

	# Run the student homework
	tape_student_output="$(make -s run arg1=$TEST_DIR/$file_name arg2=$turing_machine arg3=$tape_input)"

	# Update scores
    total_score=$(($total_score + $weight))
	if [ "$tape_output" = "$tape_student_output" ]
    then echo -e "\e[32mPASSED\e[0m Test $file_name/$turing_machine $tape_input. You won: $weight"; score=$(($score + $weight));
    else echo -e "\e[31mFAILED\e[0m Test $file_name/$turing_machine $tape_input. You lost $weight"; echo "failed output = "$tape_student_output; echo "official output = "$tape_output; missed=$(($missed + $weight))
	fi
done < $TEST_LIST

grade=$(($score * 100 / $total_score))
echo "Score: " $score " out of 100"
echo "Lost: $missed"
