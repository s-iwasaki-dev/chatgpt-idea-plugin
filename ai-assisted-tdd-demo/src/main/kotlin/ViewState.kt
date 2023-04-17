data class ViewState(
    val title: String,
    val description: String,
) {
    companion object {
        fun createDefault() = ViewState(
            title = "",
            description = ""
        )
    }
}
