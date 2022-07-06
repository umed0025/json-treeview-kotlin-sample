package jp.dkosilver.json.fx

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.json.JsonMapper
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class SampleApplication : Application() {
    override fun start(stage: Stage) {
        val url = TreeViewController::class.java.getResource("/data.json")
        TreeViewController.log.info("url:{}", url)
        val jsonNode: JsonNode = JsonMapper().readTree(url)

        val fxmlLoader = FXMLLoader(SampleApplication::class.java.getResource("tree-view.fxml"))
        val controller = TreeViewController(jsonNode)
        fxmlLoader.setController(controller)
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