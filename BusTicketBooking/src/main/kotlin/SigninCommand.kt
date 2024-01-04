class SigninCommand(repository: Repository, scanner: CustomScanner, override var code: String, sharedata: SharedData, print: Print):Command(repository,scanner,sharedata,print) {


    override fun execute() {
       if(sharedata.user != null){
           print.error("you already sign-in ")
           return
       }

        val email = scanner.getEmail("Enter the email ")
        if (email == "-1"){
            print.message("Back")
            return
        }
        if(!repository.isUserRegistered(email)){
            print.error("This email ID is not registered. Please use the 'signup' command to create an account.")
            return
        }
        var failedPasswordAttempts = 0
        do {
            if(failedPasswordAttempts != 0){
                print.message("Enter correct password")
            }
            if(failedPasswordAttempts == 3){
                print.error("The maximum number of attempts has been reached. Please try again later.")
                return
            }
            failedPasswordAttempts++
        }while (!repository.matchPassword(email,scanner.getInput("Enter the password")))

        sharedata.user = repository.getUser(email)
        print.message("Hello ${repository.getUserName(sharedata.user!!.email)} !")

    }

}