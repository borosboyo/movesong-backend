package hu.bme.aut.employee.model

class Employee {
    var id: Long? = null
    var organizationId: Long? = null
    var departmentId: Long? = null
    var name: String? = null
    var age = 0
    var position: String? = null

    constructor()
    constructor(organizationId: Long?, departmentId: Long?, name: String?, age: Int, position: String?) {
        this.organizationId = organizationId
        this.departmentId = departmentId
        this.name = name
        this.age = age
        this.position = position
    }

    override fun toString(): String {
        return ("Employee [id=" + id + ", organizationId=" + organizationId + ", departmentId=" + departmentId
                + ", name=" + name + ", position=" + position + "]")
    }
}
