
class TicketPrintCommand(repository: Repository, scanner: CustomScanner, override var code: String, sharedData: SharedData, print: Print):Command(repository, scanner,sharedData,print) {

    override fun execute() {

        if(sharedata.user == null){
            print.error("please sign-in use 'signin' command")
        }
        val tickets : MutableList<Ticket> = sharedata.user?.tickets ?: return
        if (tickets.isEmpty()) {
            print.error("You don't have any tickets to print")
            return
        }

        var index = 1
        for (ticket in tickets){
            println(index)
            println("---------------------------------------------------------")
            println("vehicleNo ${ticket.vehicleNo}  ${ticket.source} -> ${ticket.destination}  rs : ${ticket.price} timing ${ticket.timing} ")
            println("---------------------------------------------------------")
            index++
        }
        var ticketIndex : Int
        do{
            ticketIndex = scanner.getNumber("Enter the index to print the ticket")
            if (ticketIndex <=0 || ticketIndex > index-1 ){
                print.error("Enter the correct index")
            }else{
                break
            }
        }while (true)
        print.ticket(tickets[ticketIndex-1])

    }

}