package s.iwasaki.b.aiassistedtdd

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTextArea
import javax.swing.ScrollPaneConstants

class AIAssistedTDDPluginToolWindow : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        updateContent(toolWindow, "Select your test code and run \"Generate Product Code from Test Code\" in context menu.")
    }

    companion object {
        fun updateContent(toolWindow: ToolWindow, text: String) {
            val textArea = JBTextArea().also {
                it.isEditable = false
                it.text = text
            }
            val scrollPane = JBScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER)
            val panel = SimpleToolWindowPanel(true, true)
            panel.setContent(scrollPane)

            val content = toolWindow.contentManager.factory.createContent(panel, null, false)
            toolWindow.contentManager.removeAllContents(true)
            toolWindow.contentManager.addContent(content)
        }
    }
}