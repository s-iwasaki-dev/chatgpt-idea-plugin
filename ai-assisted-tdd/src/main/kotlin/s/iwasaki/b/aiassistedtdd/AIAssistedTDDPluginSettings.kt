package s.iwasaki.b.aiassistedtdd

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.project.Project
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JTextField

object AIAssistedTDDPluginSettings : Configurable {
    private const val CONFIG_PATH = ".chatgpt"
    private const val CONFIG_FILE_NAME = "ai-assisted-tdd.properties"
    private const val CONFIG_FILE_PATH = "$CONFIG_PATH/$CONFIG_FILE_NAME"
    private const val DEFAULT_MODEL_ID = "gpt-3.5-turbo"

    private var project: Project? = null
    private val properties: Properties = Properties()
    private var apiKeyTextField: JTextField? = null
    private var modelIdTextField: JTextField? = null

    var apiKey: String?
        get() = properties.getProperty("apiKey")
        set(value) { properties.setProperty("apiKey", value) }

    var modelId: String
        get() = properties.getProperty("modelId", DEFAULT_MODEL_ID)
        set(value) { properties.setProperty("modelId", value) }

    fun loadProject(project: Project) {
        this.project = project
        val propsFile = File(getConfigFilePath())
        if (propsFile.exists()) {
            FileInputStream(propsFile).use { properties.load(it) }
        } else {
            newProperties(propsFile)
        }
    }
    private fun getConfigFilePath(): String {
        return project?.basePath.orEmpty() + "/$CONFIG_FILE_PATH"
    }

    private fun newProperties(file: File) {
        file.parentFile.mkdirs()
        file.createNewFile()
    }

    private fun saveProperties() {
        FileOutputStream(File(getConfigFilePath())).use { properties.store(it, null) }
    }

    override fun getDisplayName(): String {
        return "AI Assisted TDD Plugin Settings"
    }

    override fun getPreferredFocusedComponent(): JComponent? {
        return apiKeyTextField
    }

    override fun createComponent(): JComponent? {
        apiKeyTextField = JTextField(apiKey).also { field ->
            field.setBounds(10, 30, 300, 40)
        }
        modelIdTextField = JTextField(modelId).also { field ->
            field.setBounds(10, 80, 300, 40)
        }
        return JPanel().also {
            it.layout = null
            it.add(apiKeyTextField)
            it.add(modelIdTextField)
        }
    }

    override fun isModified(): Boolean {
        return apiKeyTextField?.text != apiKey || modelIdTextField?.text != modelId
    }

    @Throws(ConfigurationException::class)
    override fun apply() {
        apiKey = apiKeyTextField?.text.orEmpty()
        modelId = modelIdTextField?.text.orEmpty()
        saveProperties()
    }

    override fun reset() {
        apiKeyTextField?.text = apiKey
        modelIdTextField?.text = modelId
    }
}