# Akka gRPC Widget Service Demo
This project holds sample Akka gRPC for the Widget service.

## Intro
This demo implements `Akka gRPC` for the Widget service. For more information on `Akka gRPC` and `gRPC` in general see the following:
- [Akka gRPC Documentation](https://doc.akka.io/docs/akka-grpc/current/index.html)
- [gRPC Documentation](https://grpc.io/docs/)

## Definitions, Implementations and Such...
In this demo we implement CRUD for the Widget Service's WidgetTeam. Some things to note:
- The majority of the code is auto-generated in `target/scala-2.13/akka-grpc/main/com/acmeco/widget` directory. This code should not be edited.
- The code in the above directory is generated from the `src/main/protobuf/widget_team.proto` file which looks something like:

### Service Definition
This protobuf definition file includes both the `messages` as well as the `service (rpc)` definition.

```
syntax = "proto3";
package com.acmeco.widget;

import "google/protobuf/timestamp.proto";

// WidgetTeam message definition
message WidgetTeam {
  string id = 1;
  string name = 2;
  string activity_type = 3;
  string program_id = 4; // NOTE protobuf doesn't support UUIDs. Use string in hexadecimal format.
  google.protobuf.Timestamp modified = 5;
  string user = 6;
}

// Request and Response message definitions for each RPC method

// CreateWidgetTeam
message CreateWidgetTeamRequest {
  WidgetTeam widget_team = 1;
}

message CreateWidgetTeamResponse {
  WidgetTeam widget_team = 1;
}

// ReadWidgetTeam
message ReadWidgetTeamRequest {
  string id = 1;
}

message ReadWidgetTeamResponse {
  WidgetTeam widget_team = 1;
}

// ReadAllWidgetTeam
message ReadAllWidgetTeamsRequest {
  // string id = 1;
}

message ReadAllWidgetTeamsResponse {
  repeated WidgetTeam widget_teams = 1;
}

// UpdateWidgetTeam
message UpdateWidgetTeamRequest {
  WidgetTeam widget_team = 1;
}

message UpdateWidgetTeamResponse {
  WidgetTeam widget_team = 1;
}

// DeleteWidgetTeam
message DeleteWidgetTeamRequest {
  string id = 1;
}

message DeleteWidgetTeamResponse {
  bool success = 1;
  string message = 2;
}

// WidgetTeamService definition with CRUD operations
service WidgetTeamService {
  rpc CreateWidgetTeam(CreateWidgetTeamRequest) returns (CreateWidgetTeamResponse);
  rpc ReadWidgetTeam(ReadWidgetTeamRequest) returns (ReadWidgetTeamResponse);
  rpc ReadAllWidgetTeams(ReadAllWidgetTeamsRequest) returns (ReadAllWidgetTeamsResponse);
  rpc UpdateWidgetTeam(UpdateWidgetTeamRequest) returns (UpdateWidgetTeamResponse);
  rpc DeleteWidgetTeam(DeleteWidgetTeamRequest) returns (DeleteWidgetTeamResponse);
}
```

### Compiling
To compile the code simply run `sbt clean compile`.
```
prompt> sbt clean compile
[info] welcome to sbt 1.9.3 (Eclipse Adoptium Java 11.0.20.1)
[info] loading settings for project acmeco-widget-build-build-build from metals.sbt ...
[info] loading project definition from /Users/duncan/Code/acmeco-widget/project/project/project
[info] loading settings for project acmeco-widget-build-build from metals.sbt ...
[info] loading project definition from /Users/duncan/Code/acmeco-widget/project/project
[success] Generated .bloop/acmeco-widget-build-build.json
[success] Total time: 0 s, completed Oct 2, 2023, 5:11:58 PM
[info] loading settings for project acmeco-widget-build from metals.sbt,plugins.sbt ...
[info] loading project definition from /Users/duncan/Code/acmeco-widget/project
[success] Generated .bloop/acmeco-widget-build.json
[success] Total time: 0 s, completed Oct 2, 2023, 5:11:58 PM
[info] loading settings for project acmeco-widget from build.sbt ...
[info] set current project to acmeco-widget (in build file:/Users/duncan/Code/acmeco-widget/)
[info] Executing in batch mode. For better performance use sbt's shell
[success] Total time: 0 s, completed Oct 2, 2023, 5:11:58 PM
[info] Compiling 1 protobuf files to /Users/duncan/Code/acmeco-widget/target/scala-2.13/akka-grpc/main
[info] Generating Akka gRPC service interface for com.acmeco.widget.WidgetTeamService
[info] Generating Akka gRPC client for com.acmeco.widget.WidgetTeamService
[info] Generating Akka gRPC service handler for com.acmeco.widget.WidgetTeamService
[info] compiling 19 Scala sources to /Users/duncan/Code/acmeco-widget/target/scala-2.13/classes ...
[success] Total time: 5 s, completed Oct 2, 2023, 5:12:03 PM
```
- Note the generated code output: `[info] Generating Akka gRPC service interface for com.acmeco.widget.WidgetTeamService`
- This will auto-generate the code found in `target/scala-2.13/akka-grpc/main/com/acmeco/widget`
- Once this code is generated you can implement it as found in `src/main/scala/com/acmeco/widget`

### Implementations
- For this project, we create four implementation:
  - `WidgetTeamClient.scala`
  - `WidgetTeamDAO.scala`
  - `WidgetTeamServer.scala`
  - `WidgetTeamServiceImpl.scala`

#### WidgetTeamClient.scala
This class is a representation of a client written in Scala. Typically your client would be Javascript or something else that consumes `gRPC`.

#### WidgetTeamDAO.scala
This class is a DAO using an `AtomicReference` and a Scala `Set[WidgetTeam]` as a database mock. This class would be replace by something like a `Slick` or `ScalaLikeJDBC` implementation.

#### WidgetTeamServer.scala
While this project may look like it doesn't use much Akka because you don't see any Actors, that is not the case. It very much relies on Akka and Akka HTTP to implement the protobuf solution. In this class, we stand up an `HTTP2` server with `SSL` enabled, all driven by Akka.

#### WidgetTeamServiceImpl.scala
This class is where we service the `Request/Response` cycle and call the underlying data store. Akka gRPC servers are implemented with Akka HTTP. In addition to the generated `WidgetTeamService`, a `WidgetTeamServiceHandler` was generated that wraps the implementation with the gRPC functionality to be plugged into an existing Akka HTTP server app.

## Running

### Running the Server
To run the server, execute the following in a terminal window:
```
prompt> sbt "runMain com.acmeco.widget.WidgetTeamServer"
```
You should see something like following output:
```
...
[info] running (fork) com.acmeco.widget.WidgetTeamServer 
[info] 2023-09-07 13:53:47,669 INFO  akka.event.slf4j.Slf4jLogger [] - Slf4jLogger started
[info] (gRPC server bound to {}:{},127.0.0.1,8080)
```
- Note the `gRPC server bound` above, showing the server successfully started

### Running the Client
To run the client, execute the following in a separate terminal window:
```
prompt> sbt "runMain com.acmeco.widget.WidgetTeamClient"
```
Upon running the client, you should see something like:
```
prompt> sbt "runMain com.acmeco.widget.WidgetTeamClient"
[info] welcome to sbt 1.9.3 (Eclipse Adoptium Java 11.0.20.1)
[info] loading settings for project acmeco-widget-build-build-build from metals.sbt ...
[info] loading project definition from /Users/duncan/Code/acmeco-widget/project/project/project
[info] loading settings for project acmeco-widget-build-build from metals.sbt ...
[info] loading project definition from /Users/duncan/Code/acmeco-widget/project/project
[success] Generated .bloop/acmeco-widget-build-build.json
[success] Total time: 0 s, completed Oct 2, 2023, 5:25:42 PM
[info] loading settings for project acmeco-widget-build from metals.sbt,plugins.sbt ...
[info] loading project definition from /Users/duncan/Code/acmeco-widget/project
[success] Generated .bloop/acmeco-widget-build.json
[success] Total time: 0 s, completed Oct 2, 2023, 5:25:42 PM
[info] loading settings for project acmeco-widget from build.sbt ...
[info] set current project to acmeco-widget (in build file:/Users/duncan/Code/acmeco-widget/)
[info] running (fork) com.acmeco.widget.WidgetTeamClient 
[info] 2023-10-02 17:25:44,328 INFO  akka.event.slf4j.Slf4jLogger [] - Slf4jLogger started
```
and then this (the create, read, update and delete actions firing):
```
[info] Create Response: CreateWidgetTeamResponse(Some(WidgetTeam(3,Widget Team 3,,,None,,UnknownFieldSet(Map()))),UnknownFieldSet(Map()))
[info] Read All Response: ReadAllWidgetTeamsResponse(Vector(WidgetTeam(1,Widget Team 1,,,None,,UnknownFieldSet(Map())), WidgetTeam(2,Widget Team 2,,,None,,UnknownFieldSet(Map()))),UnknownFieldSet(Map()))
[info] Read All Response: ReadAllWidgetTeamsResponse(Vector(WidgetTeam(1,Widget Team 1,,,None,,UnknownFieldSet(Map())), WidgetTeam(2,Widget Team 2,,,None,,UnknownFieldSet(Map())), WidgetTeam(3,Widget Team 3,,,None,,UnknownFieldSet(Map()))),UnknownFieldSet(Map()))
[info] Read Response: ReadWidgetTeamResponse(Some(WidgetTeam(3,Widget Team 3,,,None,,UnknownFieldSet(Map()))),UnknownFieldSet(Map()))
[info] Update Response: UpdateWidgetTeamResponse(Some(WidgetTeam(3,Widget Team 3 UPDATED,,,None,,UnknownFieldSet(Map()))),UnknownFieldSet(Map()))
[info] Read All Response: ReadAllWidgetTeamsResponse(Vector(WidgetTeam(1,Widget Team 1,,,None,,UnknownFieldSet(Map())), WidgetTeam(2,Widget Team 2,,,None,,UnknownFieldSet(Map())), WidgetTeam(3,Widget Team 3 UPDATED,,,None,,UnknownFieldSet(Map()))),UnknownFieldSet(Map()))
[info] Delete Response: DeleteWidgetTeamResponse(true,3,UnknownFieldSet(Map()))
[info] Read All Response: ReadAllWidgetTeamsResponse(Vector(WidgetTeam(1,Widget Team 1,,,None,,UnknownFieldSet(Map())), WidgetTeam(2,Widget Team 2,,,None,,UnknownFieldSet(Map()))),UnknownFieldSet(Map()))
```