data class FurnitureInput(
    val furnitureType: String,
    val direction: String
)

data class FurnitureOutput (
    var furnitureType: String,
//    var direction: String,
    var reason: String,
    var isCorrect: Boolean
)

class VastuLogic {

    fun evaluateFurnitureInputList(furnitureInputList: List<FurnitureInput>) : List<FurnitureOutput> {
        val evaluatedFurnitureObjects = mutableListOf<FurnitureOutput>()

        for (i in furnitureInputList) {
            evaluatedFurnitureObjects.add(checkFurnitureDirection(i))
        }

        return evaluatedFurnitureObjects
    }

    private fun checkFurnitureDirection(input: FurnitureInput): FurnitureOutput {
        return when (input.furnitureType.lowercase()) {

            "bed" -> {
                if (input.direction.equals("South", ignoreCase = true)) {
                    FurnitureOutput(
                        furnitureType = input.furnitureType,
                        reason = "According to Vastu, bed should be placed in the South direction for better sleep.",
                        isCorrect = true
                    )
                } else {
                    FurnitureOutput(
                        furnitureType = input.furnitureType,
                        reason = "Placing bed in ${input.direction} is not recommended as per Vastu.",
                        isCorrect = false
                    )
                }
            }

            "study table" -> {
                if (input.direction.equals("East", ignoreCase = true)) {
                    FurnitureOutput(
                        furnitureType = input.furnitureType,
                        reason = "Study tables are best placed facing East for better concentration.",
                        isCorrect = true
                    )
                } else {
                    FurnitureOutput(
                        furnitureType = input.furnitureType,
                        reason = "Study table in ${input.direction} is less ideal as per Vastu.",
                        isCorrect = false
                    )
                }
            }

            "sofa" -> {
                if (input.direction.equals("North", ignoreCase = true)) {
                    FurnitureOutput(
                        furnitureType = input.furnitureType,
                        reason = "Sofas facing North are considered welcoming as per Vastu.",
                        isCorrect = true
                    )
                } else {
                    FurnitureOutput(
                        furnitureType = input.furnitureType,
                        reason = "Sofa in ${input.direction} may not align with Vastu recommendations.",
                        isCorrect = false
                    )
                }
            }

            else -> {
                FurnitureOutput(
                    furnitureType = input.furnitureType,
                    reason = "No Vastu rule defined for this furniture.",
                    isCorrect = false
                )
            }
        }
    }
}

