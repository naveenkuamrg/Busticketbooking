class Repository{

    private val vehicleRouteTimetables = mutableMapOf<List<String>,MutableMap<Bus,String>>()
    private  val vehicleJourneyPrices = mutableMapOf<Map<Bus,String>,Int>()
    private val userPersonalDetails : MutableMap<String,MutableList<String>> = mutableMapOf()
    private val userMapByEmail = mutableMapOf<String,User>()
    private val ticketVehicleAssociations = mutableMapOf<Map<Bus,String>,MutableList<Ticket>>()

    fun updateMapTicketAndVehicle(vehicle: Map<Bus,String>,ticket : Ticket){
        val listTicket = ticketVehicleAssociations[vehicle]
        if(listTicket == null){
            ticketVehicleAssociations[vehicle] = mutableListOf()
        }
//        println(ticketVehicleAssociations[vehicle])
        ticketVehicleAssociations[vehicle]?.add(ticket)
    }

    fun getBookedSeats(vehicle: Map<Bus, String>,source: String,destination: String):MutableList<String>{
        var isSeatAvailable: Boolean
        val bookedSeats = mutableListOf<String>()
        val route = getRouteByVehicle(vehicle)
//        print( "route  $route")
//        println(ticketVehicleAssociations[vehicle])
        val tickets = ticketVehicleAssociations[vehicle]
        if(tickets != null){
            val routeCostMap = mutableMapOf<String,Int>()
            var rate = 1
            for (place in route!!){
                routeCostMap[place] = rate
                rate++
            }

            for(ticket in tickets) {
//                println(routeCostMap)
//                println("${ticket.source}=${routeCostMap[ticket.source]} ${ticket.destination}=${routeCostMap[ticket.destination]}")
                isSeatAvailable = false
//                println("${routeCostMap[ticket.destination]!! <= routeCostMap[source]!!}  ${routeCostMap[ticket.source]!! >= routeCostMap[destination]!!}")
                if (routeCostMap[ticket.destination]!! <= routeCostMap[source]!!) {
                    isSeatAvailable = true
                }
                if (routeCostMap[ticket.source]!! >= routeCostMap[destination]!!) {
                    isSeatAvailable = true
                }
                if (!isSeatAvailable) {
                    for ((_, seatNo) in ticket.passengerSeatMap) {
//                        println(seatNo)
                        if (!bookedSeats.contains(seatNo)) {
                            if(seatNo != "CAN") {
                                bookedSeats.add(seatNo)
                            }
                        }
                    }
                }
            }
        }
//        println("bookesr ${bookedSeats.toString()}")
        return bookedSeats
    }

    fun upgradeUserToPremium(email: String){
        userPersonalDetails[email]?.set(2, "true")
        val premiumUser = PremiumUser(email)
        val user =  userMapByEmail[email]
        for(ticket in user?.tickets!!){
            premiumUser.tickets.add(ticket)
        }
        for(notification in user.notifications){
            premiumUser.notifications.add(notification)
        }
        userMapByEmail[email] = premiumUser
    }

    fun isPremiumUser(email: String) : Boolean{
        return userPersonalDetails[email]?.get(2) == "true"
    }
    
    fun getUser(email : String) : User? {
        return userMapByEmail[email]
    }
    fun getUserName(email : String) : String{
        return userPersonalDetails[email]!![0]
    }

    fun updateUserName(email: String , newName : String){
        userPersonalDetails[email]?.add(0,newName)
    }

    fun matchPassword(email: String , password :String) : Boolean {
        return  userPersonalDetails[email]?.get(1).equals(password)
    }

    fun updatePassword(email : String, newPassword : String){
        userPersonalDetails[email]?.add(1,newPassword)
        userMapByEmail[email]?.updateNotifications(Notification("Your password is update"))
    }

    fun isUserRegistered(email : String):Boolean{
        return userPersonalDetails.containsKey(email)
    }



    fun setUserDetails(email : String , userDetails : MutableList<String>){
        this.userPersonalDetails[email] = userDetails
        if(userDetails[2] == "true"){
            userMapByEmail[email] = PremiumUser(email)
        }else {
            userMapByEmail[email] = User(email)
        }
    }



    fun getAvailableVehicles(source : String,destination : String): Map<Bus,String>{
        val vehicles = mutableMapOf<Bus, String>()
        for ((key, value)  in vehicleRouteTimetables.entries) {
            for (i in key.indices) {
                if (key[i].equals(source, ignoreCase = true)) {
                    for (j in i until key.size) {
                        if (key[j].equals(destination, ignoreCase = true)) {
                            for ((key_, value_) in value.entries) {
                                vehicles[key_] = value_
                            }
                        }
                    }
                }
            }
        }

        return vehicles
    }




    fun setVehicleAndDepartureTimeToRoute(vehicle: Bus, route: List<String>, rate: Int, time: String) {
        if(vehicleRouteTimetables[route] == null ){
            vehicleRouteTimetables[route] = mutableMapOf()
        }
        val vehicleMap = vehicleRouteTimetables[route]
        vehicleMap?.put(vehicle,time)
//        println(vehicleMap)
        vehicleJourneyPrices[mapOf(vehicle to time)] = rate
    }


    fun  getVehicleJourneyPrice(vehicle : Map<Bus,String>) : Int {
        return vehicleJourneyPrices[vehicle]!!
    }

    fun getAllRoutes() : List<List<String>>{
        val routes = mutableListOf<List<String>>()
        for((route) in vehicleRouteTimetables.entries){
//            println(route)
            routes.add(route)
        }
        return  routes
    }



    fun getRouteByVehicle(vehicleMap: Map<Bus,String>):List<String>?{
        for ((route,vehicles) in vehicleRouteTimetables.entries){
            for((_vehicle,_time) in vehicles){
               for((vehicle,time) in vehicleMap){
                   if(_vehicle == vehicle && _time == time){
                       return route
                   }
               }
            }
        }
        return  null
    }

    fun getVehicle(vehicleNo : String , time : String,source: String,destination: String ) : Map<Bus,String>?{

        val vehicles = getAvailableVehicles(source,destination)
        for ((_vehicle, _time) in vehicles.entries) {
            if (_vehicle.vehicleNumber == vehicleNo && time == _time) {
               return   mapOf(_vehicle to time)

            }
        }
        return  null
    }




}