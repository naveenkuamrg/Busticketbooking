fun main() {
    val repository =  Repository()
    repository.setVehicleAndDepartureTimeToRoute(Sleeper("TN001",16), listOf("chennai","cheyyar"),30,"2.00PM")
    repository.setVehicleAndDepartureTimeToRoute(NonSleeper("TN004",30), listOf("chennai","cheyyar"),30,"2.00PM")
    repository.setVehicleAndDepartureTimeToRoute(Sleeper("TN002",16), listOf("cheyyar","chennai"),30,"2.00PM")
    repository.setVehicleAndDepartureTimeToRoute(SleeperAndNonSleeper("TN003",10,15), listOf("cheyyar","chennai"),30,"4.00PM")
    repository.setVehicleAndDepartureTimeToRoute(NonSleeper("TN004",30), listOf("chennai","cheyyar"),30,"2.00PM")
    val print = Print()
    val scanner = CustomScanner(print)
    val sharedData = SharedData()
    val commandExecutor = CommandExecutor(repository,scanner,print,sharedData)
    println("use 'signup' commend to sing-up ")
    while (true){
        commandExecutor.executeCommand(scanner.getInput("Enter the command"))
    }

}