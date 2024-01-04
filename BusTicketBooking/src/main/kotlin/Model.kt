import java.time.LocalDateTime


open class User(val email: String) {
    val notifications : MutableList<Notification> = mutableListOf()
    val tickets : MutableList<Ticket> = mutableListOf()

    fun updateNotifications(notification: Notification) {
        notifications.add(notification)
    }

    fun updateTicketList(ticket: Ticket) {
        tickets.add(ticket)
    }
}

class PremiumUser(email: String):User(email){
    fun updateTicketSeat(ticket : Ticket, passengerName : String , updatedSeat : String){
        ticket.passengerSeatMap[passengerName] = updatedSeat
    }
}


data class Notification(var notification: String) {
    val date: LocalDateTime = LocalDateTime.now()
}

data class Ticket(val vehicleNo : String,
                  val source : String,
                  val destination: String,
                  val passengerSeatMap : MutableMap<String , String>,
                  val timing : String, val price : Int ){
    var status : TicketStatus = TicketStatus.CONFIRMED

    fun getPassengerNames():List<String>{
        val listOfPassengerNames = mutableListOf<String>()

        for (( passengerName , seatName) in passengerSeatMap.entries){
            if(seatName != "CAN") {
                listOfPassengerNames.add(passengerName)
            }
        }
        return  listOfPassengerNames
    }


    fun getSeatNames():List<String>{
        val listOfSeatNumbers = mutableListOf<String>()
        for((_,seatName) in passengerSeatMap.entries){
            if(seatName != "CAN" ) {
                listOfSeatNumbers.add(seatName)
            }
        }
        return  listOfSeatNumbers
    }

}

abstract class Bus(val vehicleNo : String, val noOfSeats : Int){
    val seatLayout : MutableList<MutableList<String>>
    abstract  var type : BusType
    init {
        seatLayout = setSeatLayout()
    }

    fun getAllSeatName() : MutableList<String>{
        val list = mutableListOf<String>()
        for (row in seatLayout){
            for(seatName in row){
                list.add(seatName)
            }
        }
        return list
    }

    protected abstract fun setSeatLayout(): MutableList<MutableList<String>>
}

class Sleeper(vehicleNo: String , noOfSeats: Int) :Bus(vehicleNo,noOfSeats){
    override var type: BusType = BusType.SLEEPER
    override fun setSeatLayout(): MutableList<MutableList<String>> {
        val seatLayout = mutableListOf<MutableList<String>>()
        var indexOfseat = 1

        for(i in 1..(noOfSeats/(noOfSeats/2))){
            val innerLayout = mutableListOf<String>()
            for ( j in 1..noOfSeats/2){
                innerLayout.add("S$indexOfseat")
                indexOfseat++
            }
            seatLayout.add(innerLayout)
        }
        return seatLayout
    }
}

class NonSleeper(vehicleNo: String , noOfSeats: Int) : Bus(vehicleNo,noOfSeats){
    override var type: BusType = BusType.NON_SLEEPER
    override fun setSeatLayout(): MutableList<MutableList<String>> {
        val seatLayout = mutableListOf<MutableList<String>>()
        var indexOfseat = 1

        for(i in 1..(noOfSeats/(noOfSeats/3))){
            val innerLayout = mutableListOf<String>()
            for ( j in 1..noOfSeats/3){
                innerLayout.add("N$indexOfseat")
                indexOfseat++
            }
            seatLayout.add(innerLayout)
        }
        return seatLayout
    }

}

class SleeperAndNonSleeper(vehicleNo: String , noOfSeats: Int) : Bus(vehicleNo,noOfSeats){
    override var type: BusType = BusType.SLEEPER_AND_NON_SLEEPER
    override fun setSeatLayout(): MutableList<MutableList<String>> {
        val seatLayout = mutableListOf<MutableList<String>>()
        var indexOfseat = 1

        for(i in 1..(noOfSeats/(noOfSeats/2))){
            val innerLayout = mutableListOf<String>()
            for ( j in 1..noOfSeats/2){
                if(j < (noOfSeats/2)/2) {
                    innerLayout.add("N$indexOfseat")
                }else{
                    innerLayout.add("S$indexOfseat")
                }
                indexOfseat++
            }
            seatLayout.add(innerLayout)
        }
        return seatLayout
    }
}


enum class TicketStatus {
    CONFIRMED,
    CANCELLED
}

enum class BusType{
    SLEEPER,NON_SLEEPER,SLEEPER_AND_NON_SLEEPER
}