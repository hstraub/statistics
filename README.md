# Program description

This program creates a probability tree ([see Wikipedia](https://en.wikipedia.org/wiki/Tree_diagram_%28probability_theory%29)) of white and black elements. White could be redundancy nodes and black the other nodes in an Elasticsearch cluster. For example:

Elasticsearch cluster with 16 nodes ( N=16)  and redundancy 3 ( R=3 ), then we have white=3 and black=N-R=13.

This program creates a list with probabilities of cluster fail function or data loss. For example N=4 and R=2 => white=2, black=2. Sample output:

```
first argument=white and the second argument=black
sbt run 2 2
Depth: 2 with 0.16666666666666666
Depth: 3 with 0.5
Depth: 4 with 0.9999999999999999

=> Two nodes lost: 16,6% probability of cluster failure or data loss
=> Three nodes lost: 50% probability of cluster failure or data loss
=> Four nodes lost: 100% probability of cluster failure or data loss
```

Other Example with N=12 and R=3
```
sbt run 3 9
Depth: 3 with 0.004545454545454546
Depth: 4 with 0.01818181818181818
Depth: 5 with 0.04545454545454546
Depth: 6 with 0.09090909090909093
Depth: 7 with 0.15909090909090912
Depth: 8 with 0.2545454545454546
Depth: 9 with 0.3818181818181811
Depth: 10 with 0.5454545454545439
Depth: 11 with 0.7499999999999972
Depth: 12 with 0.9999999999999958

```

# Build and run

```
git clone ...
sbt compile
sbt run 2 2
```
