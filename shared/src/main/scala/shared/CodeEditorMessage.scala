package shared

import scalot.Operation

sealed trait CodeEditorMessage

object CodeEditorMessage {

  case class TextOperation(op: Operation, docId: String) extends CodeEditorMessage

  case class DocAdded(str: String,
                      revision: Int,
                      docType:String,
                      title: String,
                      id: String,
                      diagramId: String) extends CodeEditorMessage

  case class DocDeleted(id:String, diagramId: String) extends CodeEditorMessage
}