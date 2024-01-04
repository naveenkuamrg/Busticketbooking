
abstract class Command(val repository: Repository, val scanner: CustomScanner, val sharedata: SharedData, val print: Print) {


    abstract var  code : String

    fun matchCode(code : String) : Boolean{
        return this.code == code
    }
    abstract fun execute()
}