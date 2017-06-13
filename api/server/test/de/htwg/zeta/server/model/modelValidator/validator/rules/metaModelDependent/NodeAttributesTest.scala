package de.htwg.zeta.server.model.modelValidator.validator.rules.metaModelDependent

import scala.collection.immutable.Seq

import models.modelDefinitions.metaModel.elements.AttributeValue.MBool
import models.modelDefinitions.metaModel.elements.AttributeValue.MInt
import models.modelDefinitions.metaModel.elements.AttributeValue.MString
import models.modelDefinitions.metaModel.elements.MAttribute
import models.modelDefinitions.metaModel.elements.MClass
import models.modelDefinitions.model.elements.Node
import org.scalatest.FlatSpec
import org.scalatest.Matchers

class NodeAttributesTest extends FlatSpec with Matchers {

  val mClass = MClass("nodeType", abstractness = false, Seq.empty, Seq.empty, Seq.empty, Seq[MAttribute]())
  val rule = new NodeAttributes("nodeType", Seq("att1", "att2"))

  "isValid" should "return true for valid nodes" in {
    val attributes = Map(
      "att1" -> Seq(MString("")),
      "att2" -> Seq(MBool(false))
    )
    val node = Node("", mClass, Seq(), Seq(), attributes)

    rule.isValid(node).get should be(true)
  }

  it should "return false for invalid nodes" in {
    val attributes = Map(
      "att1" -> Seq(MString("")),
      "att2" -> Seq(MBool(false)),
      "att3" -> Seq(MInt(0))
    )
    val node = Node("", mClass, Seq(), Seq(), attributes)

    rule.isValid(node).get should be(false)
  }

  it should "return None on non-matching nodes" in {
    val differentMClass = MClass("differentNodeType", abstractness = false, Seq.empty, Seq.empty, Seq.empty, Seq[MAttribute]())
    val node = Node("", differentMClass, Seq(), Seq(),Map.empty)

    rule.isValid(node) should be(None)
  }

  "dslStatement" should "return the correct string" in {
    rule.dslStatement should be(
      """Attributes inNodes "nodeType" areOfTypes Seq("att1", "att2")""")
  }

}
