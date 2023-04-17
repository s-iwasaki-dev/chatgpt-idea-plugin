import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

internal class ViewStateConverterTest {
    private val subject = ViewStateConverter()

    @Test
    fun updatedViewState_UseCaseがSuccessのとき_返却されたデータでviewStateを更新する() {
        // arrange
        val viewState = ViewState.createDefault()
        val output = mockk<UseCaseIO.Output.Success>()

        every { output.data.title } returns "title"
        every { output.data.description } returns "description"

        // act
        val actual = subject.updatedViewState(viewState, output)

        // assert
        val expected = ViewState.createDefault().copy(
            title = "title", description = "description"
        )
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun updatedViewState_UseCaseがSuccessかつdescriptionがnullのとき_返却されたデータと固定文言でviewStateを更新する() {
        // arrange
        val viewState = ViewState.createDefault()
        val output = mockk<UseCaseIO.Output.Success>()

        every { output.data.title } returns "title"
        every { output.data.description } returns null

        // act
        val actual = subject.updatedViewState(viewState, output)

        // assert
        val expected = ViewState.createDefault().copy(
            title = "title", description = "preset text"
        )
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun updatedViewState_UseCaseがFailureのとき_viewStateを更新しない() {
        // arrange
        val viewState = ViewState.createDefault()
        val output = mockk<UseCaseIO.Output.Failure>()

        // act
        val actual = subject.updatedViewState(viewState, output)

        // assert
        val expected = ViewState.createDefault()
        assertThat(actual).isEqualTo(expected)
    }
}