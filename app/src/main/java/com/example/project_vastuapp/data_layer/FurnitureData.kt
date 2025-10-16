package com.example.project_vastuapp.data_layer

class FurnitureDataSource {
    var furnitureObjects: List<FurnitureDataModel>? = null

    fun getFurnitureData(): List<FurnitureDataModel> {
        return listOf(
            FurnitureDataModel(room ="Living Room",furnitureType = "Sofa Set", direction = "South or West wall", benefits = "Promotes stability and grounding in social interactions.", avoid = "North-East – blocks spiritual and energy flow."),
            FurnitureDataModel(room = "Living Room",furnitureType = "TV / Entertainment Unit",direction = "South-East wall", benefits = "Keeps electronic energy aligned with Fire element.", avoid = "North wall – causes restlessness."),
            FurnitureDataModel(room = "Living Room",furnitureType = "Main Entrance Door",direction = "North or East", benefits = "Attracts positive energy and prosperity.", avoid = "South-West – brings obstacles and conflict."),
            FurnitureDataModel(room = "Living Room",furnitureType = "Center Table",direction = "Center (Brahmasthan), keep light and low", benefits = "Allows free energy flow.", avoid = "Center with heavy furniture – blocks energy flow."),
            FurnitureDataModel(room = "Living Room",furnitureType = "Wall Clock",direction = "North or East wall", benefits = "Symbol of progress and positive movement.", avoid = "South wall – associated with stagnation."),
            FurnitureDataModel(room = "Living Room",furnitureType = "Showcase / Cabinet",direction = "South or West wall", benefits = "Balances heavy elements, giving stability.", avoid = "North-East – adds heaviness to divine corner."),
            FurnitureDataModel(room = "Living Room",furnitureType = "Mirror / Decor Mirror",direction = "North or East wall", benefits = "Reflects positive energy and abundance.", avoid = "South or West – reflects negative energy."),
            FurnitureDataModel(room = "Living Room",furnitureType = "Aquarium / Water Feature",direction = "North or East", benefits = "Brings calmness, wealth, and harmony.", avoid = "South – Fire element conflict."),
            FurnitureDataModel(room = "Living Room",furnitureType = "Indoor Plants",direction = "North-East or East", benefits = "Purifies energy and enhances freshness.", avoid = "South-West – causes energy imbalance."),
            FurnitureDataModel(room = "Master Bedroom",furnitureType = "Bed",direction = "Head towards South or East", benefits = "Promotes sound sleep, health, and longevity.", avoid = "North – may cause disturbed sleep."),
            FurnitureDataModel(room = "Master Bedroom",furnitureType = "Wardrobe / Almirah",direction = "South-West", benefits = "Symbol of financial stability and strength.", avoid = "North-East – adds heaviness to sacred corner."),
            FurnitureDataModel(room = "Master Bedroom", furnitureType ="Dressing Table / Mirror",direction = "North or East wall (avoid facing bed)", benefits = "Enhances positivity and freshness.", avoid = "Facing bed – reflects energy back during sleep."),
            FurnitureDataModel(room = "Master Bedroom",furnitureType = "TV / Electronics",direction = "South-East wall", benefits = "Keeps Fire element balanced.", avoid = "North-East – disrupts peace and calmness."),
            FurnitureDataModel(room = "Master Bedroom",furnitureType = "Safe / Locker", direction ="South wall facing North", benefits = "Attracts wealth and prosperity.", avoid = "East or North-East – causes financial instability."),
            FurnitureDataModel(room = "Master Bedroom", furnitureType ="Clock / Artwork", direction ="East wall", benefits = "Maintains positive flow of time energy.", avoid = "South – symbolizes delay and struggle."),
            FurnitureDataModel(room = "Kitchen", furnitureType ="Cooking Stove / Gas Range",direction = "South-East corner", benefits = "Aligns with Fire element and promotes health.", avoid = "North-East – clashes with Water element."),
            FurnitureDataModel(room = "Kitchen", furnitureType ="Sink / Wash Area", direction ="North-East", benefits = "Maintains purity and balance of Water element.", avoid = "South-East – Fire-Water conflict."),
            FurnitureDataModel(room = "Kitchen",furnitureType = "Refrigerator", direction ="South-West or West", benefits = "Stabilizes food energy and preserves freshness.", avoid = "North-East – disrupts positive energy flow."),
            FurnitureDataModel(room = "Office / Study Room",furnitureType = "Study / Work Desk", direction ="East or North facing", benefits = "Improves focus, clarity, and concentration.", avoid = "South – drains energy and motivation.")
        )

//        if (furnitureObjects != null) {
//            return furnitureObjects
//        } else {
//            var listFurniture = mutableListOf<FurnitureDataModel>()
//            listFurniture.add(
//                FurnitureDataModel(
//                    "Main Entrance Door",
//                    "North or East",
//                    "Attracts positive energy and prosperity.",
//                    "South-West – brings obstacles and conflict."
//                    "Living Room"
//                )
//            )
//            return listFurniture;
//        }
    }
}
//        return listOf(
//            VastuObject("Living Room", "Sofa Set", getIconForObject("Sofa Set"), "South or West wall", whyThisDirection = "Promotes stability and grounding in social interactions.", directionsToAvoid = "North-East – blocks spiritual and energy flow."),
//            VastuObject("Living Room", "TV / Entertainment Unit", getIconForObject("TV"), "South-East wall", whyThisDirection = "Keeps electronic energy aligned with Fire element.", directionsToAvoid = "North wall – causes restlessness."),
//            VastuObject("Living Room", "Main Entrance Door", getIconForObject("Door"), "North or East", whyThisDirection = "Attracts positive energy and prosperity.", directionsToAvoid = "South-West – brings obstacles and conflict."),
//            VastuObject("Living Room", "Center Table", getIconForObject("Table"), "Center (Brahmasthan), keep light and low", whyThisDirection = "Allows free energy flow.", directionsToAvoid = "Center with heavy furniture – blocks energy flow."),
//            VastuObject("Living Room", "Wall Clock", getIconForObject("Clock"), "North or East wall", whyThisDirection = "Symbol of progress and positive movement.", directionsToAvoid = "South wall – associated with stagnation."),
//            VastuObject("Living Room", "Showcase / Cabinet", getIconForObject("Cabinet"), "South or West wall", whyThisDirection = "Balances heavy elements, giving stability.", directionsToAvoid = "North-East – adds heaviness to divine corner."),
//            VastuObject("Living Room", "Mirror / Decor Mirror", getIconForObject("Mirror"), "North or East wall", whyThisDirection = "Reflects positive energy and abundance.", directionsToAvoid = "South or West – reflects negative energy."),
//            VastuObject("Living Room", "Aquarium / Water Feature", getIconForObject("Aquarium"), "North or East", whyThisDirection = "Brings calmness, wealth, and harmony.", directionsToAvoid = "South – Fire element conflict."),
//            VastuObject("Living Room", "Indoor Plants", getIconForObject("Plants"), "North-East or East", whyThisDirection = "Purifies energy and enhances freshness.", directionsToAvoid = "South-West – causes energy imbalance."),
//            VastuObject("Master Bedroom", "Bed", getIconForObject("Bed"), "Head towards South or East", whyThisDirection = "Promotes sound sleep, health, and longevity.", directionsToAvoid = "North – may cause disturbed sleep."),
//            VastuObject("Master Bedroom", "Wardrobe / Almirah", getIconForObject("Wardrobe"), "South-West", whyThisDirection = "Symbol of financial stability and strength.", directionsToAvoid = "North-East – adds heaviness to sacred corner."),
//            VastuObject("Master Bedroom", "Dressing Table / Mirror", getIconForObject("Mirror"), "North or East wall (avoid facing bed)", whyThisDirection = "Enhances positivity and freshness.", directionsToAvoid = "Facing bed – reflects energy back during sleep."),
//            VastuObject("Master Bedroom", "TV / Electronics", getIconForObject("TV"), "South-East wall", whyThisDirection = "Keeps Fire element balanced.", directionsToAvoid = "North-East – disrupts peace and calmness."),
//            VastuObject("Master Bedroom", "Safe / Locker", getIconForObject("Safe"), "South wall facing North", whyThisDirection = "Attracts wealth and prosperity.", directionsToAvoid = "East or North-East – causes financial instability."),
//            VastuObject("Master Bedroom", "Clock / Artwork", getIconForObject("Clock"), "East wall", whyThisDirection = "Maintains positive flow of time energy.", directionsToAvoid = "South – symbolizes delay and struggle."),
//            VastuObject("Kitchen", "Cooking Stove / Gas Range", getIconForObject("Stove"), "South-East corner", whyThisDirection = "Aligns with Fire element and promotes health.", directionsToAvoid = "North-East – clashes with Water element."),
//            VastuObject("Kitchen", "Sink / Wash Area", getIconForObject("Sink"), "North-East", whyThisDirection = "Maintains purity and balance of Water element.", directionsToAvoid = "South-East – Fire-Water conflict."),
//            VastuObject("Kitchen", "Refrigerator", getIconForObject("Refrigerator"), "South-West or West", whyThisDirection = "Stabilizes food energy and preserves freshness.", directionsToAvoid = "North-East – disrupts positive energy flow."),
//            VastuObject("Office / Study Room", "Study / Work Desk", getIconForObject("Desk"), "East or North facing", whyThisDirection = "Improves focus, clarity, and concentration.", directionsToAvoid = "South – drains energy and motivation.")
//        )
//