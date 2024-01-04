
class CustomScanner(var print : Print) {
    private  var  isVaild : Boolean = false
    fun getInput(message : String):String{
        print.message(message)
        var input : String
        do{
            input = readln()
            isVaild = if(input.isBlank()){
                print.error("Empty space are not allowed please enter again")
                false
            }else{
                true
            }
        }while (!isVaild)

        return  input
    }

    fun getName(message: String):String {
        val nameRegx = Regex("[a-zA-Z]+")
        var input  : String
        do {
            input = getInput(message)
            if (!nameRegx.matches(input)) {
                print.error("non alphanumeric character are not allowed")
                isVaild = false
            }else{
                isVaild = true
            }
        }while (!isVaild)
        return  input

    }


    fun getEmail(info: String?): String {
        var email : String
        do {
             email = getInput(info!!+"or press -1 to return")
            if(email == "-1"){
                return email
            }
            isVaild = isValidEmail(email)
            if (!isVaild){
                print.error("Enter the correct email address or press -1 to return")
            }

        }while (!isVaild)

        return  email
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[a-z0-9]+@[a-z]+\\.[a-z]+$")
        return emailRegex.matches(email)
    }

    fun getPassword(info: String): String {
        var password: String

        do {
            password = getInput(info)
        } while (!validatePassword(password))

        while (!password.equals(getInput("Confirm the password"))) {
            print.error("Password does not match. Try again")
        }

        return password
    }


    private fun validatePassword(password: String): Boolean {
        val lowercaseRegex = """(?=.*[a-z])"""
        val uppercaseRegex = """(?=.*[A-Z])"""
        val digitRegex = """(?=.*\d)"""
        val specialCharRegex = """(?=.*[@$!%*?&])"""
        val lengthRegex = """.{8,}"""

        val regex = Regex(lowercaseRegex + uppercaseRegex + digitRegex + specialCharRegex + lengthRegex)

        if (!regex.matches(password)) {
            val errorMessage = buildString {
                append("Password must contain:\n")
                if (!password.matches(Regex("$lowercaseRegex.*"))) append("at least one lowercase letter\n")
                if (!password.matches(Regex("$uppercaseRegex.*"))) append("at least one uppercase letter\n")
                if (!password.matches(Regex("$digitRegex.*"))) append("at least one digit\n")
                if (!password.matches(Regex("$specialCharRegex.*"))) append("at least one special character\n")
                if (!password.matches(Regex(lengthRegex))) append("be at least 8 characters long\n")
            }
            println(errorMessage)
            return false
        }
        return true
    }

    fun getNumber(message: String):Int{
        var number : Int?
        do{
             number = getInput(message).toIntOrNull()
            if(number == null){
                isVaild = false
                print.error("Enter number valid number")
            }else{
                isVaild = true
            }

        }while (!isVaild)
        return  number!!
    }

}
