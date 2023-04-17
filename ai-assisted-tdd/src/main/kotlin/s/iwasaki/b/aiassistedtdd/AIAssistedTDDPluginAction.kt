package s.iwasaki.b.aiassistedtdd

import com.aallam.openai.api.BetaOpenAI
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.components.service
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.wm.ToolWindowManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class AIAssistedTDDPluginAction : AnAction() {
    @OptIn(BetaOpenAI::class)
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val editor = event.getData(CommonDataKeys.EDITOR) ?: return
        val selectedText = editor.selectionModel.selectedText ?: return
        val settings = AIAssistedTDDPluginSettings.also { it.loadProject(project) }

        if (settings.apiKey.isNullOrEmpty()) {
            ShowSettingsUtil.getInstance().showSettingsDialog(project, AIAssistedTDDPluginSettings::class.java)
        } else {
            val toolWindow = ToolWindowManager.getInstance(project).getToolWindow(AIAssistedTDDPluginToolWindow::class.java.simpleName) ?: return
            toolWindow.show()
            AIAssistedTDDPluginToolWindow.updateContent(toolWindow, "generate in progress...")

            var result = ""
            service<AIAssistedTDDPluginService>().generateProductCode(
                testCode = selectedText,
                apiKey = settings.apiKey!!,
                modelId = settings.modelId
            ).onEach { completion ->
                result += completion.choices.mapNotNull { it.delta?.content }.joinToString("")
                AIAssistedTDDPluginToolWindow.updateContent(toolWindow, result)
            }.launchIn(CoroutineScope(Dispatchers.Main))
        }
    }
}