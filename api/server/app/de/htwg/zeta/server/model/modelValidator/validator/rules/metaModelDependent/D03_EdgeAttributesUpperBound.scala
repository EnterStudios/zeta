package de.htwg.zeta.server.model.modelValidator.validator.rules.metaModelDependent

import de.htwg.zeta.server.model.modelValidator.Util
import de.htwg.zeta.server.model.modelValidator.validator.rules.{DslRule, GeneratorRule, SingleEdgeRule}
import models.modelDefinitions.metaModel.MetaModel
import models.modelDefinitions.model.elements.Edge

class D03_EdgeAttributesUpperBound(edgeType: String, attributeType: String, upperBound: Int) extends SingleEdgeRule with DslRule {
  override val name: String = getClass.getSimpleName
  override val description: String = s"Edges of type $edgeType must have a maximum of $upperBound attributes of type $attributeType."
  override val possibleFix: String = s"Remove attributes of type $attributeType from edges of type $edgeType until there are a maximum of $upperBound attributes."

  override def isValid(edge: Edge): Option[Boolean] = if (edge.`type`.name == edgeType) Some(rule(edge)) else None

  def rule(edge: Edge): Boolean = if (upperBound == -1) true else edge.attributes.find(_.name == attributeType) match {
    case Some(attribute) => attribute.value.size <= upperBound
    case None => true
  }

  override val dslStatement: String = s"""Attributes ofType "$attributeType" inEdges "$edgeType" haveUpperBound $upperBound"""
}

object D03_EdgeAttributesUpperBound extends GeneratorRule {
  override def generateFor(metaModel: MetaModel): Seq[DslRule] = Util.getReferences(metaModel)
    .foldLeft(Seq[DslRule]()) { (acc, currentReference) =>
      acc ++ currentReference.attributes.map(attr => new D03_EdgeAttributesUpperBound(currentReference.name, attr.name, attr.upperBound))
    }
}
