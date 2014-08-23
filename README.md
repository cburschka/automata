This is a simulator of cellular automata in Java. I wrote it in 2010-2011 as a class project, and then continued to develop it for a while. It is not an ongoing project and will likely not be developed further.

Developing and Building
=======================

The program is developed using Eclipse, and built with Apache Ant. The project meta-files are part of the source, so they should be imported into Eclipse without too many problems, but note that this version of Eclipse is very outdated.

Structure
---------

The simulator itself is in the "core" folder. Building it will produce an executable jar file that should work as-is.

Different kinds of cellular automata are maintained in the "modules" folder. Each module is a separate project with a build file. Building it will produce a jar file that can be loaded from the main program.

Modules
-------

The simulator's universe model is highly extendable and will work with many different kinds of universes. 

The most common universe, Conway's Game of Life, uses a simple two-state two-dimensional cell grid with universally applicable rules based on the immediate neighbourhood. Similar simulations like Wire World are also included.

Another module uses a non-discrete (floating point) cell state to simulate simple heat flow numerically. Note that while the cell state supports non-discrete values, the simulation uses fixed time steps and cannot accurately model non-discrete physical processes.

Yet another module overrides the transition function of the cellular grid to implement a Turmite universe, where the grid is only changed locally by a single Turing Machine-like actor that moves through it.
