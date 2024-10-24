package hu.bme.aut.organization.model

class Department {
    var id: Long? = null
    var name: String? = null
    var employees: List<Employee> = ArrayList()

    constructor()
    constructor(name: String?) : super() {
        this.name = name
    }

    override fun toString(): String {
        return "Department [id=$id, name=$name]"
    }
}
