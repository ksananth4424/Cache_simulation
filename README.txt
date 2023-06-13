This is a program to replicate a cache in which the Least Recently Used(LRU) behaviour of a cache is implemented.

This is a program in Java where an ArrayList of ArrayList is treated as a cache.
The hexadecimal addresses provided to the program is present as .txt files named as trace files.
The directory also has a pythin code which is/was used to produce the trace files.
The output of the program provides the total misses, total hits, set-wise misses and the set-wise hits.

To compile and run the program we would have to run the below commands in the terminal(Ubuntu Linux):
    
    $ javac Cache.java
    $ java Cache cache_size associativity block_size trace_file

This will run the program and give the required results.
In order to implement the LRU the program has a time counter which increments each time the set is accessed.

NOTE: DO NOT open trace4.txt file in Text Editor as the file is too huge for it to handle.


Happy Coding!!
