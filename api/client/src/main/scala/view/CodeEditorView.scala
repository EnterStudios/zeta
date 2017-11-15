package view

import java.util.UUID

import scala.scalajs.js
import scala.scalajs.js.Any
import scala.scalajs.runtime.genTraversableOnce2jsArray
import scala.util.Random

import controller.CodeEditorController
import controller.ModeController
import facade.Delta
import facade.Editor
import facade.IEditSession
import facade.ace
import org.scalajs.dom
import org.scalajs.dom.html.Div
import org.scalajs.dom.html.Span
import org.scalajs.dom.raw.Node
import scalatags.JsDom
import scalatags.JsDom.GenericAttr
import scalatags.JsDom.all
import scalatags.JsDom.all.bindJsAnyLike
import scalot.Client
import scalot.Operation

class CodeEditorView(controller: CodeEditorController, metaModelId: UUID, dslType: String, autoSave: Boolean) {

  private val aceId: String = UUID.randomUUID.toString
  var selectedId: UUID = UUID.randomUUID

  createSkeleton()
  if (autoSave) {
    dom.document.getElementById("btn-save").classList.add("hidden")
  }

  private val editor: Editor = ace.ace.edit(s"$aceId")
  editor.setTheme("ace/theme/xcode")
  editor.getSession().setMode("ace/mode/scala")
  editor.$blockScrolling = Any.fromDouble(Double.PositiveInfinity)

  private val _true: Any = Any.fromBoolean(true)

  editor.setOptions(js.Dynamic.literal(
    ("enableBasicAutocompletion", _true),
    ("enableLiveAutocompletion", _true)
  ))

  private def stringAttrX = new GenericAttr[String]()

  private def createSkeleton(): Node = {
    dom.document.getElementById("editor").appendChild(
      createContainer().render
    )
  }

  private def createContainer(): JsDom.TypedTag[Div] = {
    all.div(
      all.cls.:=("container")(stringAttrX),
      all.div(
        all.cls.:=("panel panel-default")(stringAttrX),
        createHeader(),
        createBody()
      )
    )
  }

  private def createHeader(): JsDom.TypedTag[Div] = {
    all.div(
      all.cls.:=("panel-heading")(stringAttrX),
      all.h3(
        all.cls.:=("panel-title editor-title")(stringAttrX),
        all.strong(
          all.stringFrag(s"$dslType")
        ),
        createButton()
      )
    )
  }

  private def createButton(): JsDom.TypedTag[Span] = {
    all.span(
      all.cls.:=("btn btn-default glyphicon glyphicon-floppy-disk typcnbtn pull-right")(stringAttrX),
      all.id.:=("btn-save")(bindJsAnyLike),
      all.title.:=("Save Document")(bindJsAnyLike),
      all.onclick.:=((e: dom.MouseEvent) => {
        controller.saveCode()
      })
    )
  }

  private def createBody(): JsDom.TypedTag[Div] = {
    all.div(
      all.cls.:=("panel-body editor-body")(stringAttrX),
      all.div(
        all.style.:=("background-color: gray;")(bindJsAnyLike),
        all.div(
          all.cls.:=("editor")(stringAttrX),
          all.id.:=(aceId)(bindJsAnyLike)
        )
      )
    )
  }

  var broadcast: Boolean = true
  var session: IEditSession = null

  def displayDoc(doc: Client): js.Dynamic = {
    selectedId = UUID.fromString(doc.id)
    session = ace.ace.createEditSession(
      Any.fromString(doc.str),
      ModeController.getAllModesForModel(metaModelId)(doc.docType)
    )
    session.on("change", Any.fromFunction1((delta: js.Any) => {
      if (broadcast) {
        controller.operationFromLocal(
          ScalotAceAdaptor
            .aceDeltatoScalotOp(
              delta
                .asInstanceOf[js.Dynamic]
                .selectDynamic("data")
                .asInstanceOf[Delta],
              editor.getSession().getDocument()
            ),
          selectedId
        )
      }
    }))
    editor.setSession(session)
  }


  def updateView(op: Operation): Unit = {
    val was = broadcast
    broadcast = false
    val doc = editor.getSession().getDocument()
    doc.applyDeltas(genTraversableOnce2jsArray(ScalotAceAdaptor.scalotOpToAceDelta(op, doc)))
    broadcast = was
  }

}
