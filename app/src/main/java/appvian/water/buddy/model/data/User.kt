package appvian.water.buddy.model.data

data class User(
    val name: String,
    val height: Float,
    val weight: Float,
    val cupList: List<Cup>
)