package jp.dkosilver.json.fx

import com.fasterxml.jackson.databind.JsonNode
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeTableColumn
import javafx.scene.control.TreeTableView
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger


class TreeViewController(private val jsonNode: JsonNode) {
    companion object {
        @JvmStatic
        val log: Logger = LogManager.getLogger(TreeViewController::class.java)
    }

    @FXML
    lateinit var treeTableView: TreeTableView<JsonData>

    @Suppress("unused")
    fun initialize() {
        log.trace("start initialize")
        dataInitialize()
    }

    private fun dataInitialize() {

        val viewRoot = TreeItem(JsonData("/", "root", ""))
        for (field in jsonNode.fields()) {
            createTreeItem(viewRoot.children, "/", field.key, field.value)
        }
        val nameColumn = TreeTableColumn<JsonData, String>("name")
        nameColumn.prefWidth = 150.0
        nameColumn.setCellValueFactory { param: TreeTableColumn.CellDataFeatures<JsonData, String?> ->
            ReadOnlyStringWrapper(
                param.value.value.name
            )
        }
        val valueColumn = TreeTableColumn<JsonData, String>("value")
        valueColumn.prefWidth = 150.0
        valueColumn.setCellValueFactory { param: TreeTableColumn.CellDataFeatures<JsonData, String?> ->
            ReadOnlyStringWrapper(
                param.value.value.value
            )
        }
        val pathColumn = TreeTableColumn<JsonData, String>("path")
        pathColumn.prefWidth = 150.0
        pathColumn.setCellValueFactory { param: TreeTableColumn.CellDataFeatures<JsonData, String?> ->
            ReadOnlyStringWrapper(
                param.value.value.path
            )
        }
        treeTableView.root = viewRoot
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
