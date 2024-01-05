import java.util.*

class TicketBookingCommand(repository: Repository, scanner: CustomScanner, override var code: String, print : Print, sharedData: SharedData):Command(repository, scanner,sharedData,print) {

    override fun execute() {
       if(sharedata.user == null){
           print.error("Please Login")
           return
       }
        for(route in  repository.getAllRoutes()){
            for(place in route){
                print(" $place " )
                if(route.indexOf(place) != route.size -1 ){
                    print("->")
                }
            }
            println()
        }
        var source : String
        var destination : String
        var vehicles : Map<Bus,String>
        var availableSeats : MutableList<String>
        val bookedTickets : List<String>
        var vehicleMap: Map<Bus, String>? = null
        var vehicle : Bus? = null
        var noOfSeatToBook: Int
        val passengerSeatMap = mutableMapOf<String,String>()

        do {
            source = scanner.getInput("Enter the source")
            destination = scanner.getInput("Enter the destination")

            vehicles = repository.getAvailableVehicles(source, destination)

            if (vehicles.isNotEmpty()) {
                break
            } else {
                if (scanner.getInput("We couldn't find your journey. Enter -1 to exit, or press any other key to continue") == "-1") {
                    return
                }
            }
        }while (true)



//        do {

            selectVehicle@do{
                var index = 1
                for ((vehicle_, time) in vehicles.entries) {

                    println("$index -  ${vehicle_.vehicleNumber} in $time available seats ${vehicle_.getAllSeatNames().size - repository.getBookedSeats(
                        mapOf(vehicle_ to time),source,destination).size}  ${vehicle_.type}")
                    index++
                }
                do {
                    val indexNumber: Int = scanner.getNumber("Enter the index number or press -1 to back")
                    if (0 > indexNumber || indexNumber > vehicles.size) {
                        println("please enter the correct Index")
                        if (indexNumber == -1) {
                            return
                        }
                    } else {
                        index = 1

                        for ((_vehicle, time) in vehicles.entries) {
                            if (index == indexNumber) {
                                vehicle = _vehicle
                                vehicleMap = mapOf(_vehicle to time)
                            }
                            index++
                        }
                        break
                    }
                } while (true)


                    print.availableSeat(vehicle!!,repository.getBookedSeats(vehicleMap!!,source,destination))

                availableSeats = vehicle.getAllSeatNames()


                do {
                    noOfSeatToBook = scanner.getNumber("Enter number of passenger or press -1 to back")
                    if(noOfSeatToBook < -1){
                        print.error("Negative value are not allowed")
                        continue
                    }
                    if(noOfSeatToBook == -1 ){
                            break
                    }
                    if( noOfSeatToBook > availableSeats.size) {
                        print.error("Available less than is less than your need")
                        if(scanner.getInput("press -1 to back or any other key again selecting seats ") == "-1"){
                            break
                        }
                    }else{
                        break@selectVehicle
                    }
                } while (true)
            }while(true)
            var numberOfBookedSeats = 0
            bookedTickets = repository.getBookedSeats (vehicleMap!!,source,destination)
            while (true){
                if(numberOfBookedSeats == noOfSeatToBook){
                    break
                }
                val seatNo = scanner.getInput("Enter the seat number").uppercase(Locale.getDefault())
                if(availableSeats.contains(seatNo) || bookedTickets.contains(seatNo)) {
                    if (!bookedTickets.contains(seatNo)) {
                        availableSeats.remove(seatNo)
                        bookedTickets.add(seatNo)
                        var passengerName : String =  scanner.getName("enter the passenger name")
                        if (passengerSeatMap.contains(passengerName)) {
                            do {
                                passengerName += " "
                            } while (passengerSeatMap.contains(passengerName))
                        }
                        passengerSeatMap[ passengerName ] = seatNo
                        numberOfBookedSeats++
                    } else {
                        print.error("Ticket is AlreadyBooked")
                    }
                }else{
                    print.error("select proper seat number")
                }
            }


             var ticket : Ticket? = null
            for((_vehicle,time) in vehicleMap.entries) {
                ticket = Ticket(_vehicle.vehicleNumber,source,destination,passengerSeatMap,time,repository.getVehicleJourneyPrice(vehicleMap)*noOfSeatToBook)
                sharedata.user?.updateTicketList(ticket)
            }
            sharedata.user?.updateNotifications(Notification("your ticket $source to $destination is booked successfully"))
            repository.updateMapTicketAndVehicle(vehicleMap,ticket!!)
            print.success("Your journey is booked successfully use 'print' commend to print ticket ")


    }


}

