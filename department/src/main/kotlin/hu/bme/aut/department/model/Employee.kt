package hu.bme.aut.department.model

class Employee {
    var id: Long? = null
    var name: String? = null
    var age = 0
    var position: String? = null

    constructor()
    constructor(name: String?, age: Int, position: String?) {
        this.name = name
        this.age = age
        this.position = position
    }

    override fun toString(): String {
        return "Employee [id=$id, name=$name, position=$position]"
    }
}
