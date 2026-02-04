package br.com.eduard.mine_toolkit.game

import br.com.eduard.java_utils.Copyable


/**
 * Representa uma Tag geralmente usado para Cargos de Permissão
 * @author Eduard
 * @version 2.0
 * @since EduardAPI 1.0
 */
class Tag(
    var name: String,
    var prefix: String,
    var suffix: String,
    var rank: Int
) {
    constructor() : this("Tag", "", "", 0)
    constructor(name: String, prefix: String, rank: Int) : this(name, prefix, "", rank)
    constructor(name: String, prefix: String) : this(name, prefix, "", 0)

    fun copy(): Tag {
        return Copyable.copyObject(this)
    }

    override fun toString(): String {
        return "Tag [prefix=$prefix, suffix=$suffix, name=$name, rank=$rank]"
    }
}