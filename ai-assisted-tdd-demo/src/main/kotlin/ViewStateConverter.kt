
class ViewStateConverter {
    fun updatedViewState(viewState: ViewState, output: UseCaseIO.Output): ViewState {
        return when (output) {
            is UseCaseIO.Output.Success -> {
                viewState.copy(
                    title = output.data.title ?: viewState.title,
                    description = output.data.description ?: "preset text"
                )
            }
            is UseCaseIO.Output.Failure -> viewState
        }
    }
}