package de.htwg.zeta.server.model.modelValidator.validator.rules.metaModelDependent

import de.htwg.zeta.server.model.modelValidator.Util
import de.htwg.zeta.server.model.modelValidator.validator.rules.DslRule
import de.htwg.zeta.server.model.modelValidator.validator.rules.GeneratorRule
import de.htwg.zeta.server.model.modelValidator.validator.rules.SingleEdgeRule
import models.modelDefinitions.metaModel.MetaModel
import models.modelDefinitions.metaModel.elements.AttributeValue
import models.modelDefinitions.metaModel.elements.EnumSymbol
import models.modelDefinitions.metaModel.elements.ScalarValue.MBool
import models.modelDefinitions.metaModel.elements.ScalarValue.MDouble
import models.modelDefinitions.metaModel.elements.ScalarValue.MInt
import models.modelDefinitions.metaModel.elements.ScalarValue.MString
import models.modelDefinitions.model.elements.Edge

class EdgeAttributesLocalUnique(val edgeType: String, val attributeType: String) extends SingleEdgeRule with DslRule {
  override val name: String = getClass.getSimpleName
  override val description: String = s"Attribute values of attribute type $attributeType in Edge of type $edgeType must be unique locally."
  override val possibleFix: String = s"Remove all but one of the duplicated attribute values of type $attributeType in Edge of type $edgeType."

  override def isValid(edge: Edge): Option[Boolean] = if (edge.`type`.name == edgeType) Some(rule(edge)) else None

  def rule(edge: Edge): Boolean = {

    def handleStrings(values: Seq[AttributeValue]): Seq[String] = values.collect { case v: MString => v }.map(_.value)
    def handleBooleans(values: Seq[AttributeValue]): Seq[String] = values.collect { case v: MBool => v }.map(_.value.toString)
    def handleInts(values: Seq[AttributeValue]): Seq[String] = values.collect { case v: MInt => v }.map(_.value.toString)
    def handleDoubles(values: Seq[AttributeValue]): Seq[String] = values.collect { case v: MDouble => v }.map(_.value.toString)
    def handleEnums(values: Seq[AttributeValue]): Seq[String] = values.collect { case v: EnumSymbol => v }.map(_.toString)

    edge.attributes.find(_.name == attributeType) match {
      case None => true
      case Some(attribute) =>
        val attributeValues: Seq[String] = attribute.value.headOption match {
          case None => Seq()
          case Some(_: MString) => handleStrings(attribute.value)
          case Some(_: MBool) => handleBooleans(attribute.value)
          case Some(_: MInt) => handleInts(attribute.value)
          case Some(_: MDouble) => handleDoubles(attribute.value)
          case Some(_: EnumSymbol) => handleEnums(attribute.value)
        }

        if (attributeValues.isEmpty) {
          true
        } else {
          attributeValues.distinct.size == attribute.value.size
        }
    }
  }

  override val dslStatement: String = s"""Attributes ofType "$attributeType" inEdges "$edgeType" areLocalUnique ()"""
}

object EdgeAttributesLocalUnique extends GeneratorRule {
  override def generateFor(metaModel: MetaModel): Seq[DslRule] = metaModel.references.values
    .foldLeft(Seq[DslRule]()) { (acc, currentReference) =>
      acc ++ currentReference.attributes.filter(_.localUnique).map(attr => new EdgeAttributesLocalUnique(currentReference.name, attr.name))
    }
}
