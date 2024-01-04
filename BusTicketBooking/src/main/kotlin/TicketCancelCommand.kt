class TicketCancelCommand(repository: Repository, scanner: CustomScanner, override var code: String, sharedData: SharedData, print: Print):Command(repository,scanner,sharedData,print) {

    override fun execute() {

        if(sharedata.user == null){
            print.error("Login use 'signin' commend")
            return
        }
        if(sharedata.user!!.tickets.isEmpty()){
            print.error("No ticket to cancel")
            return
        }

//        do {


            main@do{
                val cancelTicket : Ticket?
            when(scanner.getNumber("To cancel a ticket, press 1. To partially cancel a ticket, press 2. To back press -1")){
                1 -> {
                     cancelTicket = selectTicketToCancel()
                    if (cancelTicket == null){
                        print.message("BACK")
                        continue
                    }
                    for((passengerName, _ ) in cancelTicket.passengerSeatMap){
                        cancelTicket.passengerSeatMap[passengerName] = "CAN"
                    }
                    cancelTicket.status = TicketStatus.CANCELLED
                    sharedata.user?.updateNotifications(Notification("${cancelTicket.vehicleNo}  ${cancelTicket.source} -> ${cancelTicket.destination} has cancelled"))
                    return

                }
                2 -> {
                    cancelTicket = selectTicketToCancel()
                    if(cancelTicket == null){
                        print.message("BACK")
                        continue
                    }
                    val cancelPassengerNames  = mutableListOf<String>()
                    var noOfPassengersToCancel :Int
                    do {
                        noOfPassengersToCancel = scanner.getNumber("Enter the number of Passenger to cancel ")
                        if(-1 == noOfPassengersToCancel){
                            print.message("BACK")
                            continue@main
                        }
                        if(-1>noOfPassengersToCancel){
                            print.error("negative numbers are not allowed")
                        }else if(0==noOfPassengersToCancel || noOfPassengersToCancel>=cancelTicket.passengerSeatMap.size){
                            print.error("A cancellation quantity equal to or greater than or less than the passenger count is not valid for partial cancellation. Press -1 to go back.")
                        }else{
                            break
                        }

                    }while (true)
                    var cancelledPassengerCount = 0
                    val passengersName = cancelTicket!!.getPassengerNames()
                    var index = 1
                    for(name in passengersName){
                        println("$index . $name")
                        index++
                    }
                    while(true){

                        if(cancelledPassengerCount == noOfPassengersToCancel){
                            break
                        }
                        val passengerToCancelIndex = scanner.getNumber("Enter the cancel passenger Index")
                        if(0<=passengerToCancelIndex && passengerToCancelIndex>index){
                            print.error("Enter the correct Index")
                        }else{
                            cancelTicket.passengerSeatMap[passengersName[passengerToCancelIndex-1]] = "CAN"
                            cancelPassengerNames.add(passengersName[passengerToCancelIndex-1])
                            cancelledPassengerCount++
                        }


                    }
                    var notfyStr = ""
                    for (name in cancelPassengerNames){
                        notfyStr += "  $name   "
                    }
                    sharedata.user?.updateNotifications(Notification("$notfyStr  ${cancelTicket.vehicleNo}  ${cancelTicket.source} -> ${cancelTicket.destination} has cancelled"))
                    return
                }
                -1 ->{
                    print.message("BACK")
                    return
                }
                else -> {
                    print.error("chooses correct option ")
                }
            }

        }while(true)

    }

    private fun selectTicketToCancel():Ticket?{
        var index = 1
        var cancelTicket : Ticket? = null
        val nonCancelTicket = mutableListOf<Ticket>()
        for(ticket in sharedata.user!!.tickets){
            if(ticket.status != TicketStatus.CANCELLED) {
                nonCancelTicket.add(ticket)
            }
        }
        if(nonCancelTicket.isEmpty()){
            print.error("All your ticket are cancel already")
        }
        for(ticket in nonCancelTicket){
            println("$index . ${ticket.vehicleNo}  ${ticket.source} -> ${ticket.destination} ${ticket.timing}")
            index++
        }
        var cancelIndex : Int
        do {
            cancelIndex = scanner.getNumber("Enter the cancel index ticket")
            if(cancelIndex == -1){
                return  null
            }
        }while (cancelIndex !in 1 until index)
        index = 1
        for(ticket in nonCancelTicket){
            if(index == cancelIndex){
                cancelTicket = ticket
            }
            index++
        }

        return  cancelTicket
    }
}