package de.htwg.zeta.parser.shape.check

import scala.annotation.tailrec

import de.htwg.zeta.common.models.project.concept.Concept
import de.htwg.zeta.parser.check.ErrorCheck
import de.htwg.zeta.parser.check.ErrorCheck.ErrorMessage
import de.htwg.zeta.parser.shape.parsetree.EdgeParseTree
import de.htwg.zeta.parser.shape.parsetree.ShapeParseTree

case class CheckEdgesForUndefinedConceptElements(shapeParseTrees: List[ShapeParseTree], concept: Concept) extends ErrorCheck[ErrorMessage] {

  override def check(): List[ErrorMessage] = {
    val edges = shapeParseTrees.collect { case e: EdgeParseTree => e }
    checkEdgesForUndefinedConceptElements(edges, concept)
  }

  // check if there are edges which reference undefined concept elements
  private def checkEdgesForUndefinedConceptElements(edges: List[EdgeParseTree], concept: Concept): List[ErrorMessage] = {
    edges.flatMap(edge => checkEdgeForUndefinedConceptElements(edge, concept))
  }

  private def errorIfEmpty[T](o: Option[T], error: Some[ErrorMessage]): Option[ErrorMessage] = if (o.isDefined) None else error

  private def checkEdgeForUndefinedConceptElements(edge: EdgeParseTree, concept: Concept): List[ErrorMessage] = {
    def checkConnReferenceParts(splitConnSeq: Seq[String]) = splitConnSeq.nonEmpty && splitConnSeq.length % 2 == 0

    @tailrec
    def checkConceptReference(referenceChain: List[String], previousErrors: List[ErrorMessage]): List[ErrorMessage] = {
      val conceptClass :: conceptConnection :: conceptTarget :: _ = referenceChain

      lazy val maybeConceptClassDoesNotExist = errorIfEmpty(
        concept.classes.find(_.name == conceptClass),
        Some(s"No concept class '$conceptClass' for edge '${edge.identifier}' exists!"))

      lazy val maybeConceptConnectionDoesNotExist = errorIfEmpty(
        concept.references.find(_.name == conceptConnection),
        Some(s"Concept connection '$conceptConnection' (in class '$conceptClass') for edge '${edge.identifier}' does not exist!"))

      lazy val maybeTargetClassDoesNotExist = errorIfEmpty(
        concept.classes.find(_.name == conceptTarget),
        Some(s"Target '${edge.conceptTarget.target}' for edge '${edge.identifier}' is not a concept class!"))

      lazy val maybeReferenceIsNotDefined = errorIfEmpty(
        concept.references.find(e => e.sourceClassName == conceptClass && e.name == conceptConnection && e.targetClassName == conceptTarget),
        Some(s"Reference '$conceptClass.$conceptConnection.$conceptTarget' in edge '${edge.identifier}' is not defined!"))

      val errorList = previousErrors ::: List(
        maybeConceptClassDoesNotExist,
        maybeConceptConnectionDoesNotExist,
        maybeTargetClassDoesNotExist,
        maybeReferenceIsNotDefined
      ).collectFirst({
        case Some(error) => error
      }).toList

      val _ :: _ :: followingChain = referenceChain
      followingChain match {
        case e: List[String] if e.size < 3 => errorList
        case e: List[String] => checkConceptReference(e, errorList)
      }
    }

    val conn = edge.conceptConnection
    val splitReferenceChain = conn.split("\\.").toList

    if (!checkConnReferenceParts(splitReferenceChain)) {
      List(s"Edge concept reference '$conn' is not a valid identifier <class>.<connection> or <class>.<connection>.<class>.<connection>!")
    } else {
      // append target as last element of reference chain
      checkConceptReference(splitReferenceChain ::: List(edge.conceptTarget.target), List())
    }
  }

}
