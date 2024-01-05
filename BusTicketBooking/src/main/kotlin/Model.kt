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

abstract class Bus(val vehicleNumber : String, val noOfPrimarySeats : Int){
    val seatingLayout : MutableList<MutableList<String>> = mutableListOf()
    abstract  var type : BusType
//    init {
//        seatingLayout =  setSeatingLayout()
//    }

    fun getAllSeatNames() : MutableList<String>{
        val list = mutableListOf<String>()
        for (row in seatingLayout){
            for(seatName in row){
                list.add(seatName)
            }
        }
        return list
    }

    protected abstract fun setSeatingLayout()
}

class Sleeper(vehicleNo: String , numberOfSeats: Int) :Bus(vehicleNo,numberOfSeats){
    init {
        setSeatingLayout()
    }
    override var type: BusType = BusType.SLEEPER
    override fun setSeatingLayout(){
        var indexOfseat = 1

        for(i in 1..(noOfPrimarySeats/(noOfPrimarySeats/2))){
            val innerLayout = mutableListOf<String>()
            for ( j in 1..noOfPrimarySeats/2){
                innerLayout.add("S$indexOfseat")
                indexOfseat++
            }
            seatingLayout.add(innerLayout)
        }

    }
}

class NonSleeper(vehicleNo: String, numberOfSeats: Int) : Bus(vehicleNo,numberOfSeats){
    init {
        setSeatingLayout()
    }
    override var type: BusType = BusType.NON_SLEEPER
    override fun setSeatingLayout(){
        var indexOfseat = 1

        for(i in 1..(noOfPrimarySeats/(noOfPrimarySeats/3))){
            val row = mutableListOf<String>()
            for ( j in 1..noOfPrimarySeats/3){
                row.add("N$indexOfseat")
                indexOfseat++
            }
            seatingLayout.add(row)
        }
    }

}

class SleeperAndNonSleeper(vehicleNo: String, noNonSleeperSeat: Int, private val noSleeperSeat : Int ) : Bus(vehicleNo,noNonSleeperSeat){
    init {
        setSeatingLayout()
    }

    override var type: BusType = BusType.SLEEPER_AND_NON_SLEEPER
    override fun setSeatingLayout(){
        var indexNonSleeperSeats = 1
        var indexSleeperSeats = 1
        println(noSleeperSeat)
        for(i in 1..2){
            val row = mutableListOf<String>()
            for(j in 1..noOfPrimarySeats/2){
                row.add("N$indexNonSleeperSeats")
                indexNonSleeperSeats++
            }
            for(j in 1..noSleeperSeat/2){
                row.add("S$indexSleeperSeats")
                indexSleeperSeats++
            }
            seatingLayout.add(row)
        }
    }
}


enum class TicketStatus {
    CONFIRMED,
    CANCELLED
}

enum class BusType{
    SLEEPER,NON_SLEEPER,SLEEPER_AND_NON_SLEEPER
}