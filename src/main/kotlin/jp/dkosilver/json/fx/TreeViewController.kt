package jp.dkosilver.json.fx

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.json.JsonMapper
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeTableColumn
import javafx.scene.control.TreeTableView
import javafx.scene.control.cell.TextFieldTreeTableCell
import javafx.scene.layout.AnchorPane
import javafx.stage.FileChooser
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File


class TreeViewController {
    companion object {
        @JvmStatic
        val log: Logger = LogManager.getLogger(TreeViewController::class.java)
    }

    @FXML
    lateinit var treeTableView: TreeTableView<JsonData>

    @FXML
    lateinit var rootPane: AnchorPane

    lateinit var jsonNode: JsonNode

    @Suppress("unused")
    fun initialize() {
        log.trace("start initialize")
        log.trace("end initialize")
    }

    @FXML
    fun handleLoadButtonAction() {
        // FIXME 汎用にする場合は不要
        val fileChooser = FileChooser()
        val url = TreeViewController::class.java.getResource("/data.json")
        if (url != null) {
            fileChooser.initialDirectory = File(url.toURI()).parentFile
        }
        fileChooser.title = "load json data"
        fileChooser.initialFileName = "data.json"
        val selectedFile = fileChooser.showOpenDialog(rootPane.scene.window)
        if (selectedFile != null) {
            load(selectedFile)
        }
    }

    @FXML
    fun handleSaveButtonAction() {
        // TODO 実装が必要
    }

    private fun load(selectedFile: File) {
        log.info("selectedFile absolutePath:{}", selectedFile.absolutePath)
        jsonNode = JsonMapper().readTree(selectedFile)

        // 編集可能に設定。（実際の編集可能設定については個別に設定が必要）
        treeTableView.isEditable = true

        // treeView用データ設定
        val viewRoot = TreeItem(JsonData("/", "root", ""))
        for (field in jsonNode.fields()) {
            createTreeItem(viewRoot.children, "/", field.key, field.value)
        }

        // 名前列 表示設定
        val nameColumn = TreeTableColumn<JsonData, String>("name")
        nameColumn.prefWidth = 150.0
        nameColumn.setCellValueFactory { param: TreeTableColumn.CellDataFeatures<JsonData, String?> ->
            ReadOnlyStringWrapper(
                param.value.value.name
            )
        }
        // 値列 表示設定
        val valueColumn = TreeTableColumn<JsonData, String>("value")
        valueColumn.prefWidth = 150.0
        valueColumn.setCellValueFactory { param: TreeTableColumn.CellDataFeatures<JsonData, String?> ->
            ReadOnlyStringWrapper(
                param.value.value.value
            )
        }
        // 値列編集設定
        valueColumn.cellFactory = TextFieldTreeTableCell.forTreeTableColumn()

        // パス列表示設定
        val pathColumn = TreeTableColumn<JsonData, String>("path")
        pathColumn.prefWidth = 150.0
        pathColumn.setCellValueFactory { param: TreeTableColumn.CellDataFeatures<JsonData, String?> ->
            ReadOnlyStringWrapper(
                param.value.value.path
            )
        }
        // treeViewへデータ設定
        treeTableView.root = viewRoot
        // treeView表示設定
        treeTableView.columns.setAll(nameColumn, valueColumn, pathColumn)
    }

    private fun createTreeItem(
        children: ObservableList<TreeItem<JsonData>>,
        parentPath: String,
        name: String,
        node: JsonNode
    ) {
        if (node.isArray) {
            val jsonData = JsonData("$parentPath$name/", name, "")
            log.trace("json data:{}", jsonData)
            val treeItem = TreeItem(jsonData)
            children.add(treeItem)
            for ((index, value) in node.withIndex()) {
                createTreeItem(treeItem.children, "$parentPath$name/", index, value)
            }
        } else if (node.isObject) {
            val jsonData = JsonData("$parentPath$name/", name, "")
            log.trace("json data:{}", jsonData)
            val treeItem = TreeItem(jsonData)
            children.add(treeItem)
            for (field in node.fields()) {
                createTreeItem(treeItem.children, "$parentPath$name/", field.key, field.value)
            }
        } else {
            val jsonData = JsonData("$parentPath$name/", name, node.asText())
            log.trace("json data:{}", jsonData)
            children.add(TreeItem(jsonData))
        }
    }

    private fun createTreeItem(
        children: ObservableList<TreeItem<JsonData>>,
        parentPath: String,
        index: Int,
        node: JsonNode
    ) {
        createTreeItem(children, parentPath, index.toString(), node)
    }


}


class JsonData(val path: String, val name: String, val value: String) {
    override fun toString(): String {
        return "JsonData(path='$path', name='$name', value='$value')"
    }
}
