package hu.bme.aut.department.model

class Department {
    var id: Long? = null
    var organizationId: Long? = null
    var name: String? = null
    var employees: List<Employee?>? = ArrayList()

    constructor()
    constructor(organizationId: Long?, name: String?) : super() {
        this.organizationId = organizationId
        this.name = name
    }

    override fun toString(): String {
        return "Department [id=$id, organizationId=$organizationId, name=$name]"
    }
}
