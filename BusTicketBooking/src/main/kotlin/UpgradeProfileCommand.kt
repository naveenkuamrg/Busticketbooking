class UpgradeProfileCommand(repository: Repository, scanner: CustomScanner, override var code: String, sharedData: SharedData, print: Print) : Command(repository,scanner,sharedData,print) {
    override fun execute() {
       if(sharedata.user == null){
           print.error("please login use 'signin' command to login")
           return
       }
        if(repository.isPremiumUser(sharedata.user!!.email)){
            print.error("your profile is already upgrade")
            return
        }

        while (true){
            if (scanner.getInput("You ready to pay premium amount RS:200, Press 'T' or not, Press any key to return").equals("T",true)){
                break
            }else{
                print.error("Back")
                return
            }
        }

        repository.upgradeUserToPremium(sharedata.user!!.email)
        print.success("Enjoy your premium Account")
        sharedata.user = repository.getUser(sharedata.user!!.email)
    }
}