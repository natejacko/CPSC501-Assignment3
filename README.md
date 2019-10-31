CPSC 501 - Assignment 3: Distributed Objects using Reflective Serialization/Deserialization

The following code base is for Assignment 3 offered in CPSC 501 by Jonathan Hudson at the University of Calgary.

The assignment description is as follows: The goal of this assignment is to create two programs (on two separate computers) that communicate with
each other over a network connection (see diagram below). The Sender program will create one or more
arbitrary objects, serialize these objects into a JDOM document, and then send this document as a stream of
bytes to the Receiver program using a socket connection. The Receiver program will convert the incoming
byte stream into a JDOM document, deserialize the document into objects, and display the objects to
screen. 

Full assignment spec can be found here http://pages.cpsc.ucalgary.ca/~hudsonj/CPSC501F19/CPSC501F19A3.pdf or included in the repo as CPSC501F19A3.pdf
