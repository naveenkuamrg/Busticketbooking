class UpdateProfileCommand(repository: Repository, scanner: CustomScanner, override var code: String, sharedData: SharedData, print: Print):Command(repository,scanner,sharedData,print) {
    init {
        code = "update"
    }

    override fun execute() {
        if(sharedata.user == null){
            print.error("use 'signin' commend for login")
        }
        do {

            when (scanner.getNumber("update name , Press 1 . update password Press2 or press -1 to back")){
                1->{
                    val name = scanner.getName("Enter new name to update")
                    var failedPasswordAttempts = 0
                    do {
                        if(failedPasswordAttempts != 0){
                            print.error("Enter correct password")
                        }
                        if(failedPasswordAttempts == 3){
                            print.error("The maximum number of attempts has been reached. Please try again later.")
                            return
                        }
                        failedPasswordAttempts++
                    }while (!repository.matchPassword(sharedata.user!!.email,scanner.getInput("Enter the password")))
                    repository.updateUserName(sharedata.user!!.email,name)
                    sharedata.user?.updateNotifications(Notification("your name is change to $name"))
                    return

                }
                2->{
                    var failedPasswordAttempts = 0
                    do {
                        if(failedPasswordAttempts != 0){
                            print.error("Enter correct password")
                        }
                        if(failedPasswordAttempts == 3){
                            print.error("The maximum number of attempts has been reached. Please try again later.")
                            return
                        }
                        failedPasswordAttempts++
                    }while (!repository.matchPassword(sharedata.user!!.email,scanner.getInput("Enter the old password")))

                    val newlyEnteredPassword = scanner.getPassword("Enter the new password")
                    repository.updatePassword(sharedata.user!!.email,newlyEnteredPassword)
                    sharedata.user?.updateNotifications(Notification("your password is change successfully"))
                    return
                }
                -1->{
                    return
                }
                else-> print.error("choose correct option")
            }
        }while (true)
    }
}