class Print {
    fun availableSeat(bus : Bus, bookedTicketSeat : List<String>){
        when(bus.type){
            BusType.SLEEPER ->{ println("S -> Sleeper seat")}
            BusType.SLEEPER_AND_NON_SLEEPER->{ println("N -> NonSleeper seat\nS -> Sleeper seat")}
            BusType.NON_SLEEPER ->{ println("N -> NonSleeper seat")}
        }
        val seatLayout = bus.seatLayout
        for (row in seatLayout){
            for(seatName in row){
                if(!bookedTicketSeat.contains(seatName)){
                    print(seatName.padEnd(5,' '))
                }else{
                    print("X".padEnd(5,' '))
                }
            }
            println()
        }
    }

    fun message(message : String){
        println("-> $message")
    }

    fun error(message: String){
        println("Error -> $message")
    }

    fun success(message: String){
        println("Success -> $message")
    }

    fun ticket(ticket: Ticket){
        println("---------------------------------------------------------")
        println("vehicleNo ${ticket.vehicleNo}  ${ticket.source} -> ${ticket.destination}  rs : ${ticket.price} timing ${ticket.timing} ")
        println("passenger name and seat no")
        for((name,seatName) in ticket.passengerSeatMap){
            println("$name   -    $seatName ")
        }
        println("---------------------------------------------------------")
    }
}