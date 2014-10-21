#!/bin/bash

datadir="data/"
solndir="soln/"
#Problem 0
echo "Problem 0"
prob="prob0"
sat="sat_8"
for n in 0 1 2 3 4
do
  infile="${datadir}${prob}/${sat}_${n}.txt"
  outfile="${solndir}${sat}_${n}_soln.txt"
  java -jar SATSolver.jar ${infile} ${outfile}
    
  echo "${infile}"
  java -jar spec/evaluator.jar ${infile} ${outfile}
done
echo

#Problem 1
echo "Problem 1"
prob="prob1"
sat="sat_32"
for n in 0 1 2 3 4
do
  infile="${datadir}${prob}/${sat}_${n}.txt"
  outfile="${solndir}${sat}_${n}_soln.txt"
  java -jar SATSolver.jar ${infile} ${outfile}
    
  echo "${infile}"
  java -jar spec/evaluator.jar ${infile} ${outfile}
done
echo

#Problem 2
echo "Problem 2"
prob="prob2"
sat="sat_64"
for n in 0 1 2 3 4
do
  infile="${datadir}${prob}/${sat}_${n}.txt"
  outfile="${solndir}${sat}_${n}_soln.txt"
  java -jar SATSolver.jar ${infile} ${outfile}
    
  echo "${infile}"
  java -jar spec/evaluator.jar ${infile} ${outfile}
done
echo

#Problem 3
echo "Problem 3"
prob="prob3"
sat="sat_128"
for n in 0 1 2 3 4
do
  infile="${datadir}${prob}/${sat}_${n}.txt"
  outfile="${solndir}${sat}_${n}_soln.txt"
  java -jar SATSolver.jar ${infile} ${outfile}
    
  echo "${infile}"
  java -jar spec/evaluator.jar ${infile} ${outfile}
done
echo
