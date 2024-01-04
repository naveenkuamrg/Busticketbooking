import java.time.format.DateTimeFormatter

class NotificationCommand(repository : Repository, scanner: CustomScanner, override var code: String, sharedata: SharedData, print: Print):Command(repository,scanner,sharedata,print) {


    override fun execute() {
        if(sharedata.user == null){
            print.error("use 'signin' command for sign-in before notify")
            return
        }
        val myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy  HH:mm:ss")
        val notifications = sharedata.user?.notifications?.sortedByDescending { it.date }

        println("------------Notifications-----------")
        for (notification in notifications!!) {
            print(notification.date.format(myFormatObj) + "  ")
            println(notification.notification)
        }
    }
}