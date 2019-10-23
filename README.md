# IntraShareit
>Java Application for sharing files over a local network

Developed a desktop application to share files across machines connected to the same local network.
Can send various types of files such as text, audio, video etc.

Tools Used : Java Swing, Socket Programming

Compile and run first server then run client with corresponding parameters

#For TCPClient

 if at least three argument are passed, consider the first one as directory path,
 
 the second one as host address and the third one as port number
 
 If host address is not present, default it to "localhost"
 
 If port number is not present, default it to 3333
 
 If directory path is not present, show error

#For TCPServer

if at least two argument are passed, consider the first one as directory path

and the second one as port number

If port number is not present, default it to 3333
