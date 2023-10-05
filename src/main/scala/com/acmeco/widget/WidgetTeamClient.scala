package com.acmeco.widget

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Failure
import scala.util.Success
import akka.Done
import akka.NotUsed
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.grpc.GrpcClientSettings
import scala.concurrent.duration._
import scala.concurrent.Await

object WidgetTeamClient {

  def main(args: Array[String]): Unit = {
    implicit val sys: ActorSystem[_] =
      ActorSystem(Behaviors.empty, "WidgetTeamClient")
    implicit val ec: ExecutionContext = sys.executionContext

    val client = WidgetTeamServiceClient(
      GrpcClientSettings.fromConfig("com.acmeco.widget.WidgetTeamService")
    )

    // Read all widget teams
    def readAll(): Unit = {
      val readAllFuture = client.readAllWidgetTeams(ReadAllWidgetTeamsRequest())
      readAllFuture.onComplete {
        case Success(readAllResponse) =>
          println(s"Read All Response: $readAllResponse")
        case Failure(e) =>
          println(s"Error in readAll: $e")
      }
    }

    // Read all widget teams
    readAll()

    // Create WidgetTeam
    val createFuture = client.createWidgetTeam(
      CreateWidgetTeamRequest(Some(WidgetTeam("3", "Widget Team 3")))
    )
    val createResponse: CreateWidgetTeamResponse =
      Await.result(createFuture, 5.seconds)
    println(s"Create Response: $createResponse")

    // Read all widget teams
    readAll()

    // Read WidgetTeam
    val readFuture = client.readWidgetTeam(ReadWidgetTeamRequest("3"))
    val readResponse: ReadWidgetTeamResponse =
      Await.result(readFuture, 5.seconds)
    println(s"Read Response: $readResponse")

    // Update WidgetTeam
    val updateFuture = client.updateWidgetTeam(
      UpdateWidgetTeamRequest(Some(WidgetTeam("3", "Widget Team 3 UPDATED")))
    )
    val updateResponse = Await.result(updateFuture, 5.seconds)
    println(s"Update Response: $updateResponse")

    // Read all widget teams
    readAll()

    // Delete WidgetTeam
    val deleteFuture = client.deleteWidgetTeam(DeleteWidgetTeamRequest("3"))
    val deleteResponse = Await.result(deleteFuture, 5.seconds)
    println(s"Delete Response: $deleteResponse")

    // Read all widget teams
    readAll()
  }
}
