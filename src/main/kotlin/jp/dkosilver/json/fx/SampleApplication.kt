package jp.dkosilver.json.fx

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class SampleApplication : Application() {
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(SampleApplication::class.java.getResource("tree-view.fxml"))
        val scene = Scene(fxmlLoader.load())
        stage.title = "TreeView"
        stage.scene = scene
        stage.sizeToScene()
        stage.show()
    }
}

fun main() {
    Application.launch(SampleApplication::class.java)
}