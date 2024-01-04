
class InvalidCommand(repository: Repository, scanner: CustomScanner, override var code: String, sharedData: SharedData, print : Print):Command(repository,scanner, sharedData,print ) {


    override fun execute() {
        print.error("Enter the valid code")
    }

}