class UseCaseIO {
    data class Input(
        val param: Param
    ) {
        data class Param(
            val sample: String
        )
    }

    sealed class Output {
        data class Success(
            val data: Data
        ) : Output() {
            data class Data(
                val title: String,
                val description: String?
            )
        }

        object Failure : Output()
    }
}