class SwitchCommand(repository: Repository, scanner : CustomScanner, override var code: String, print : Print, sharedData: SharedData):Command(repository,scanner,sharedData,print) {

    override fun execute() {
        if(sharedata.user == null){
            print.error("Please login use 'signin' command")
            return
        }
        if(sharedata.user !is PremiumUser){
            print.error("Sorry your account not Premium")
            return
        }


        val ticket = getTicketToSwitchSeat() ?: return
        val vehicleMap = repository.getVehicle(ticket.vehicleNo,ticket.timing,ticket.source,ticket.destination)
        var vehicle : Bus? = null
        for ((vehicle_ , _) in vehicleMap!!){
            vehicle = vehicle_
        }
        print.availableSeat(vehicle!!,repository.getBookedSeats(vehicleMap,ticket.source,ticket.destination))
        var numberOfPassanger : Int
        do{
            numberOfPassanger = scanner.getNumber("Enter number of passenger to switch ")
            if(numberOfPassanger>0 && numberOfPassanger <= ticket.getPassengerNames().size){
                break
            }else{
                print.error("number of passenger size is ${ticket.getPassengerNames().size} give correct size")
            }

        }while(true)
        for (i in 1..numberOfPassanger){
        selectTicket(
            repository.getBookedSeats(vehicleMap,ticket.source,ticket.destination),
            vehicle.getAllSeatName(),
            ticket
        )
            print.ticket(ticket)
            print.availableSeat(vehicle,repository.getBookedSeats(vehicleMap,ticket.source,ticket.destination))

        }

    }

    private fun getTicketToSwitchSeat() : Ticket? {
        val resTicket: Ticket?
        var index = 1
        val unCancelTicket = mutableListOf<Ticket>()

        for (ticekt in  sharedata.user!!.tickets){
            if (ticekt.status != TicketStatus.CANCELLED){
                unCancelTicket.add(ticekt)
            }
        }
        if(unCancelTicket.size == 0){
            print.error("No ticket to switch")
            return  null
        }
        for (availableTicket in unCancelTicket){
            println("$index . ${availableTicket.vehicleNo}  ${availableTicket.source} ->  ${availableTicket.destination} ")
            index++
        }
        var ticketIndex : Int
        do{
           ticketIndex = scanner.getNumber("Enter the ticket index ")
            if(ticketIndex == -1 ){
                return null
            }
            if(ticketIndex<= 0 || ticketIndex > index-1){
                println("Enter the correct index")
            }else{
                break
            }

        }while (true)
        resTicket = unCancelTicket[ticketIndex-1]
        return  resTicket
    }

    private fun selectTicket(bookedSeats: MutableList<String>, availableSeats: MutableList<String>, ticket: Ticket){
        var newSeatName : String
        do {
            newSeatName = scanner.getInput("Enter the new SeatName or press -1 to skip").uppercase()
            if(newSeatName == "-1"){
                return
            }
            if(bookedSeats.contains(newSeatName)){
                println("this seat is already booked")
                continue
            }
            if(!availableSeats.contains(newSeatName)){
                println("Please enter the valid seat name")
                continue
            }
            val passengerNames = ticket.getPassengerNames()
            val seatName = ticket.getSeatNames()
            var index = 1
            for(name in passengerNames){
                println("$index . $name = ${seatName[index-1]}")
                index++
            }

            var nameIndex : Int
            do{
                nameIndex = scanner.getNumber("Enter the index of passenger name")
                if (nameIndex <= 0 && index-1 > nameIndex){
                    println("Enter the valid index")
                }else{
                    break
                }

            }while (true)
            bookedSeats.remove(ticket.passengerSeatMap[passengerNames[nameIndex-1]])
            availableSeats.add(ticket.passengerSeatMap[passengerNames[nameIndex-1]].toString())
            val pUser = sharedata.user as PremiumUser
            pUser.updateTicketSeat(ticket,passengerNames[nameIndex-1],newSeatName)
            bookedSeats.add(newSeatName)
            print.success("Your seat successfully switch")
            break

        }while (true)

    }

}