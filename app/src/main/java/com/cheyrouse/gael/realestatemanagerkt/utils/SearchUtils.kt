package com.cheyrouse.gael.realestatemanagerkt.utils

class SearchUtils {

    // Search engine to request properties in database
    fun makeQuery(
        typeOfProperty: String, surfaceMin: Int, surfaceMax: Int,
        roomMin: Int, roomMax: Int, city: String, postalCode: String,
        country: String, shops: Boolean, airport: Boolean,
        park: Boolean, subway: Boolean, school: Boolean,
        trainStation: Boolean, sold: Boolean, available: Boolean,
        priceMin: Double, priceMax: Double, bedRoomsMin: Int,
        bedRoomsMax: Int, entryDate: String, maxDate: String,
        realtorName: String, numberOfBath: Int, numberOfImages: Int
    ): String {
        var query = "SELECT * FROM Property"
        val args = arrayListOf<Any>()
        var containsAnd = false

        //Type
        if (typeOfProperty.isNotEmpty() && typeOfProperty != "ALL") {
            query += " WHERE type = '$typeOfProperty'"
            args.add(typeOfProperty)
            containsAnd = true
        }
        //LivingSpace min
        if (surfaceMin != 0) {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "livingSpace >= $surfaceMin"
            args.add(surfaceMin)
        }
        //LivingSpace max
        if (surfaceMax != 0) {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "livingSpace <= $surfaceMax"
            args.add(surfaceMax)
        }
        //Number of min Rooms
        if (roomMin != 0) {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "rooms >= $roomMin"
            args.add(roomMin)
        }
        //Number of max Rooms
        if (roomMax != 0) {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "rooms <= $roomMax"
            args.add(roomMax)
        }
        //City
        if (city != "") {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "city = '$city'"
            args.add(city)
        }
        //Postal code
        if (postalCode != "") {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "postalCode = '$postalCode'"
            args.add(postalCode)
        }
        //Country
        if (country != "") {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "country = '$country'"
            args.add(country)
        }
        //Points of interest
        if ((shops)) {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "shops = 1"
            args.add(shops)
        }
        if ((airport)) {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "airport = 1"
            args.add(airport)
        }
        if ((park)) {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "park = 1"
            args.add(park)
        }
        if ((subway)) {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "subway = 1"
            args.add(subway)
        }
        if ((school)) {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "school = 1"
            args.add(school)
        }
        if ((trainStation)) {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "trainStation = 1"
            args.add(trainStation)
        }
        //Status
        if ((sold)) {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "status = 0"
            args.add(sold)
        }
        if ((available)) {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "status = 1"
            args.add(available)
        }
        //Price min
        if (priceMin != 0.0) {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "price >= ${priceMin.toInt()}"

            args.add(priceMin.toInt())
        }
        //Price max
        if (priceMax < 20000000) {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "price <= ${priceMax.toInt()}"
            args.add(priceMax.toInt())
        }
        //Number of min Bedrooms
        if (bedRoomsMin != 0) {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "numOfBed >= $bedRoomsMin"
            args.add(bedRoomsMin)
        }
        //Number of max Bedrooms
        if (bedRoomsMax != 0) {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "numOfBed <= $bedRoomsMax"
            args.add(bedRoomsMax)
        }
        //Entry date
        if (entryDate.isNotEmpty()) {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "dateOfEntry >= '$entryDate'"
            args.add(entryDate)
        }
        //Sold date
        if (maxDate.isNotEmpty()) {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "dateOfEntry <= '$maxDate'"
            args.add(maxDate)
        }
        //Realtor name
        if (realtorName != "") {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "realtor = '$realtorName'"
            args.add(realtorName)
        }
        //Number of Bathrooms
        if (numberOfBath != 0) {
            query += if (containsAnd) " AND " else " WHERE "
            query += "numOfBath = $numberOfBath"
            args.add(numberOfBath)
        }
        //Minimum number of Images
        if (numberOfImages != 0) {
            query += if (containsAnd) " AND " else " WHERE "
            query += "(LENGTH(pictures) - LENGTH(REPLACE(pictures, '{', ''))) >= $numberOfImages"
            args.add(numberOfImages)
        }
        return query
    }
}