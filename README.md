tcpudpperformancecompairison
============================

License
============================
The MIT License (MIT)

Copyright (c) 2013 Kai-Ting (Danil) Ko

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.


About
============================
To test UDP/TCP connection with different packet size

Compile
============================
On project git repo level, type
```
mvn clean package
```

Execute
============================
On project git repo level, type

Using maven
```
mvn exec:java
```

Usinge java command
```
mvn clean package
java -jar target/udptcpcomparision-0.0.1.jar
```

On GUI, specify host and port
Note, server will open on the configured host address and port, while client will use the configuration settings to connect the server

So server and client can be opened through one GUI session

Click on UDP Server to start UDP Server

Click on UDP Client to start UDP Client

Click on TCP Server to start TCP Server

Click on TCP Client to start TCP Client
