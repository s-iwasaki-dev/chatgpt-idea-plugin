package s.iwasaki.b.aiassistedtdd

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletionChunk
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.intellij.openapi.components.Service
import kotlinx.coroutines.flow.Flow

@Service
class AIAssistedTDDPluginService {
    private fun generatePrompt(testCode: String): String {
        return """
            以下のテストコードを満たすメソッドをKotlinを使用して簡潔に作成し、作成したソースコードの説明を箇条書きでお願いします。
            なお、回答はソースコードの記述から始めてください。
            ```
            $testCode
            ```
        """.trimIndent()
    }

    @OptIn(BetaOpenAI::class)
    fun generateProductCode(testCode: String, apiKey: String, modelId: String): Flow<ChatCompletionChunk> {
        val request = ChatCompletionRequest(
            model = ModelId(modelId),
            messages = listOf(
                ChatMessage(
                    role = ChatRole.User,
                    content = generatePrompt(testCode)
                )
            )
        )
        return OpenAI(apiKey).chatCompletions(request)
    }
}