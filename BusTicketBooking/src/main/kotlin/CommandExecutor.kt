
class CommandExecutor(val repository: Repository, val scanner : CustomScanner, print : Print,val sharedata : SharedData) {
    private  var commands = mutableListOf<Command>()
    private var invalidCommand : Command
    init {
        commands.add(SignupCommand(repository,scanner,"signup",sharedata,print))
        commands.add(SigninCommand(repository,scanner,"signin",sharedata,print))
        commands.add(SignoutCommand(repository,scanner,"signout",sharedata,print))
        commands.add(NotificationCommand(repository,scanner,"notify",sharedata,print))
        commands.add(TicketPrintCommand(repository, scanner,"print",sharedata,print))
        commands.add(TicketBookingCommand(repository, scanner,"book",print,sharedata))
        commands.add(TicketCancelCommand(repository,scanner,"cancel",sharedata,print))
        commands.add(UpdateProfileCommand(repository, scanner,"update",sharedata,print))
        commands.add(UpgradeProfileCommand(repository,scanner,"upgrade",sharedata,print))
        commands.add(SwitchCommand(repository,scanner,"switch",print,sharedata))
        invalidCommand = InvalidCommand(repository,scanner,"invalid",sharedata,print)
    }

    fun executeCommand(commandCode: String?) {
        val command = getCommand(commandCode)
        command.execute()
        if(sharedata.user == null){
            println("'signup' commend to sign-up \n'signin' command to sign-in ")
        }else {
            if (sharedata.user is PremiumUser) {
                println(" 'signout' for signout \n 'book' for booking \n 'cancel' cancelling the ticket \n 'notify' for notification \n 'signup' for register(after signout use signup command ) \n 'print' for print the tickets \n 'update' for update your profile \n 'switch' for for switch your seat  ")
            } else {
                println(" 'signout' for signout \n 'book' for booking \n 'cancel' cancelling the ticket \n 'notify' for notification \n 'signup' for register(after signout use signup command ) \n 'print' for print the tickets \n 'update' for update your profile \n 'upgrade' for upgrade your account to Premium  \n" +
                        " 'switch' for for switch your seat  ")
            }
        }
    }

    private fun getCommand(commandCode: String?): Command {
        for (command in commands) {
            if (command.matchCode(commandCode!!)) {
                return command
            }
        }
        return invalidCommand
    }
}

