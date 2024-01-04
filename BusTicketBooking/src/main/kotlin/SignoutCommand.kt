class SignoutCommand(repository: Repository, scanner: CustomScanner, override var code: String, sharedata: SharedData, print: Print) : Command(repository,scanner,sharedata,print){


    override fun execute() {
        if(sharedata.user == null){
            print.error("Unable to signout pleas sign-in first")
            return
        }

        sharedata.user = null
        print.success("Thank you! Have a nice day")
    }
}