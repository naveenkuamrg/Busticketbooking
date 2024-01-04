class SignupCommand(repository: Repository, scanner : CustomScanner, override var code: String, sharedata: SharedData, print: Print):Command(repository,scanner,sharedata,print) {



    override fun execute() {
        if(sharedata.user != null){
            print.error("Before signing up, sign-out using the 'signout' command")
            return
        }
        val userDetails  = mutableListOf<String>()

        val email : String = scanner.getEmail("Enter the email id ")
        if(email == "-1"){
            return
        }
        if(repository.isUserRegistered(email)){
            print.error("This email already register ")
            return
        }
        userDetails.add(scanner.getName("Enter your name"))
        userDetails.add(scanner.getPassword("Enter your password"))
        if (scanner.getInput("You ready to pay premium amount RS:200, Press 'T' or not, Press any key to return").equals("T",true)){
            userDetails.add("true")
        }else{
            userDetails.add("false")
        }
        repository.setUserDetails(email,userDetails)
        sharedata.user =  repository.getUser(email)
        sharedata.user?.updateNotifications(Notification("Welcome! ${repository.getUserName(email)}"))
        print.success("Your registration has been successful... ")
//        Use 'signin' command to sign-in
    }

}