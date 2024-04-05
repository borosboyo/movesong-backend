package hu.bme.aut.organization.model

class Organization {
    @JvmField
	var id: Long? = null
    var name: String? = null
    var address: String? = null
    var departments: List<Department?>? = ArrayList()
    var employees: List<Employee?>? = ArrayList()

    constructor()
    constructor(name: String?, address: String?) {
        this.name = name
        this.address = address
    }

    override fun toString(): String {
        return "Organization [id=$id, name=$name, address=$address]"
    }
}
