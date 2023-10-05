package com.acmeco.widget

import java.util.concurrent.atomic.AtomicReference

object WidgetTeamDAO {
  private val db: AtomicReference[Set[WidgetTeam]] = new AtomicReference(
    Set.empty[WidgetTeam]
  )

  private def modifyDatabase[A](f: Set[WidgetTeam] => (Set[WidgetTeam], A)): A = {
    var result: Option[A] = None
    while (result.isEmpty) {
      val oldSet = db.get()
      val (newSet, returnValue) = f(oldSet)
      if (db.compareAndSet(oldSet, newSet)) {
        result = Some(returnValue)
      }
    }
    result.get
  }

  // Create
  def create(WidgetTeam: WidgetTeam): Either[String, WidgetTeam] = {
    modifyDatabase { oldSet =>
      if (oldSet.exists(_.id == WidgetTeam.id)) {
        (oldSet, Left(s"Widget team with id ${WidgetTeam.id} already exists."))
      } else {
        (oldSet + WidgetTeam, Right(WidgetTeam))
      }
    }
  }

  // Read
  def read(id: String): Option[WidgetTeam] = {
    val dataSet = db.get()
    dataSet.find(_.id == id)
  }

  // Read All
  def readAll(): Set[WidgetTeam] = {
    db.get()
  }

  // Update
  def update(id: String, newWidgetTeam: WidgetTeam): Either[String, WidgetTeam] = {
    modifyDatabase { oldSet =>
      oldSet.find(_.id == id) match {
        case Some(oldWidgetTeam) =>
          ((oldSet - oldWidgetTeam) + newWidgetTeam, Right(newWidgetTeam))
        case None => (oldSet, Left(s"Widget team with id $id not found."))
      }
    }
  }

  // Delete
  def delete(id: String): Either[String, String] = {
    modifyDatabase { oldSet =>
      oldSet.find(_.id == id) match {
        case Some(oldWidgetTeam) => (oldSet - oldWidgetTeam, Right(id))
        case None => (oldSet, Left(s"Widget team with id $id not found."))
      }
    }
  }

  // Add items to database
  create(WidgetTeam("1", "Widget Team 1"))
  create(WidgetTeam("2", "Widget Team 2"))
}
