# Visualisation of algorithms and data structures

This repository contains a desktop application for visualising sorting algorithms and John Conway's Game of Life,
written in pure Java and JavaFX.

## Sorting Algorithms

Current version implements 3 algorithms:

- Bubble Sort
- Merge Sort
- Quick Sort

Each can be run on randomised input or data provided by the user. 
It has its limitations on the size and integer values of the input: 

- max 20 elements
- max integer is 20


### Runtime 

Each active algortihm's performance is measured based on the total number of operations it took to sort the array.
Operation is defined as either:

- comparison - check values from two distinct indices, and compare them

  or

- swap / put - swap places of two values / put an element on the empty space

The only exception is divide-and-conquer (Merge Sort) technique where extra operation is introduced:

- divide - break array into two halfes

The reason merge is not included is that it is a product of put operations.


## John Conway's Game of Life

The rules of the game can be found here: https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life .
The grid on which the game is displayed is limited based on screen size. Cells on the border are always 'dead', they are not included in computing generations.


## How to run
To build, compile and run this application you need to:

### Clone this repository.

```sh
git clone https://github.com/adz011/Visualisation-of-data-structures-and-algorithms
```

### Install Java 15 or above
Code includes text blocks and switch cases, it won't compile with older versions of Java.

Remember to set your environment variable, for more info check https://www.java.com/en/download/help/path.html

### Install Maven 
Go to https://maven.apache.org/download.cgi and install the newest version of Maven.

Unzip it anywhere you want on your machine.

Add the bin directory of the created directory to the PATH environment variable

Confirm with mvn -v in a new shell. The result should look similar to

```sh
Apache Maven 3.9.5 (57804ffe001d7215b5e7bcb531cf83df38f93546)
Maven home: /opt/apache-maven-3.9.5
Java version: 1.8.0_45, vendor: Oracle Corporation
Java home: /Library/Java/JavaVirtualMachines/jdk1.8.0_45.jdk/Contents/Home/jre
Default locale: en_US, platform encoding: UTF-8
OS name: "mac os x", version: "10.8.5", arch: "x86_64", family: "mac"
```

After checking that everything is working, go to dev folder in your shell and build the application:
```sh
mvn clean package
```

If build is successfull, go to target folder and run jar file:
```sh
cd target
java -jar Demo-1.0-SNAPSHOT
```
