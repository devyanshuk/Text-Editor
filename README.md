## Text Editor using sockets

This project uses sockets to allow multiple users to work on the same text-editor, like google docs, given that they connect to the same
server.

When a user writes something on their instance of the text-editor, the contents are broadcasted to all other users connected to the same server, and their text-editor contents get updated seamlessly, and concurrently.

When a client connects to the server, the server assigns a unique id to the client, which is then later used for content broadcasting.




### Running the program

#### Install the project
```
$ make install
```
__OR__
```
$ mvn install
```

#### Create a server
```
$ make server
```

#### Create as many clients as you need
```
$ make client
$ make client
```

#### Run text editor without creating a server
```
$ make editor
```

#### Create javadoc for the whole project inside the doc directory
```
$ make javadoc
```

#### Clean the project
```
$ make clean
```