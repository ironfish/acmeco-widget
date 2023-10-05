package com.acmeco.widget

import scala.concurrent.Future

import akka.NotUsed
import akka.actor.typed.ActorSystem

class WidgetTeamServiceImpl(system: ActorSystem[_]) extends WidgetTeamService {
  private implicit val sys: ActorSystem[_] = system

  override def createWidgetTeam(
      in: CreateWidgetTeamRequest
  ): Future[CreateWidgetTeamResponse] = {
    println(s"Received createWidgetTeam request: $in")
    val createEither: Either[String, WidgetTeam] =
      com.acmeco.widget.WidgetTeamDAO.create(in.widgetTeam.get)
    val result: Option[WidgetTeam] =
      if (createEither.isLeft) None else Some(createEither.toOption.get)
    Future.successful(CreateWidgetTeamResponse(result))
  }

  override def readWidgetTeam(
      in: ReadWidgetTeamRequest
  ): Future[ReadWidgetTeamResponse] = {
    println(s"Received readWidgetTeam request: $in")
    Future.successful(ReadWidgetTeamResponse(com.acmeco.widget.WidgetTeamDAO.read(in.id)))
  }

  override def readAllWidgetTeams(
      in: ReadAllWidgetTeamsRequest
  ): Future[ReadAllWidgetTeamsResponse] = {
    println(s"Received readAllWidgetTeams request: $in")
    Future.successful(ReadAllWidgetTeamsResponse(com.acmeco.widget.WidgetTeamDAO.readAll().toSeq))
  }

  override def updateWidgetTeam(
      in: UpdateWidgetTeamRequest
  ): Future[UpdateWidgetTeamResponse] = {
    println(s"Received updateWidgetTeam request: $in")
    val updateEither: Either[String, WidgetTeam] =
      com.acmeco.widget.WidgetTeamDAO.update(in.widgetTeam.get.id, in.widgetTeam.get)
    val result: Option[WidgetTeam] =
      if (updateEither.isLeft) None else Some(updateEither.toOption.get)
    Future.successful(UpdateWidgetTeamResponse(result))
  }

  override def deleteWidgetTeam(
      in: DeleteWidgetTeamRequest
  ): Future[DeleteWidgetTeamResponse] = {
    println(s"Received deleteWidgetTeam request: $in")
    val deleteEither: Either[String, String] = com.acmeco.widget.WidgetTeamDAO.delete(in.id)
    val result: Option[String] =
      if (deleteEither.isLeft) None else Some(deleteEither.toOption.get)
    result match {
      case Some(id) => Future.successful(DeleteWidgetTeamResponse(true, id))
      case None =>
        Future.successful(
          DeleteWidgetTeamResponse(false, deleteEither.toOption.get)
        )
    }
  }
}
