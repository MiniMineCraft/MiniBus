package me.camdenorrb.minibus.type


abstract class TypeHolder<T : Any> {

	val type by lazy {
		this::class.supertypes.first().arguments.first().type!!
	}

}

/*
abstract class TypeHolder<T : Any> {

	val raw: KClass<in T>

	lateinit var type: Type
		private set


	constructor(raw: KClass<in T>) {
		this.raw = raw
	}

	constructor(type: Type, raw: KClass<in T>) : this(raw) {
		this.type = type
	}


	init {
		if (!::type.isInitialized) {
			//this::class.supertypes.first()
			type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
		}
	}


	fun isAssignableFrom(clazz: KClass<T>): Boolean {
		if (clazz != raw) return false
		return isAssignableFrom(TypeHolder.of(clazz))
	}

	fun isAssignableFrom(typeHolder: TypeHolder<*>): Boolean {
		println("${type} ${typeHolder.type}")
		if (typeHolder.raw != raw) return false
		return isAssignableFrom(typeHolder.type)
	}

	fun isAssignableFrom(fromType: ParameterizedType, toType: Type): Boolean {

		if (toType !is ParameterizedType) {
			return false
		}

		val fromTypes = fromType.actualTypeArguments
		val toTypes = toType.actualTypeArguments

		if (fromTypes.size != toTypes.size) {
			return false
		}

		return (0 until fromTypes.size).all {
			isAssignableFrom(fromTypes[it], toTypes[it])
		}
	}

	fun isAssignableFrom(fromType: GenericArrayType, toType: Type): Boolean {

		if (toType !is GenericArrayType) {
			return false
		}

		return isAssignableFrom(fromType.genericComponentType, toType.genericComponentType)
	}


	fun isAssignableFrom(fromType: TypeVariable<*>, toType: Type): Boolean {

		if (toType !is TypeVariable<*>) {
			return false
		}

		val fromBounds = fromType.bounds
		val toBounds = toType.bounds

		if (fromBounds.size != toBounds.size) {
			return false
		}

		return (0 until fromBounds.size).all {
			isAssignableFrom(fromBounds[it], toBounds[it])
		}
	}

	fun isAssignableFrom(fromType: WildcardType, toType: Type): Boolean {

		if (toType !is WildcardType) {
			return false
		}

		val toUpperBounds = toType.upperBounds
		val fromUpperBounds = fromType.upperBounds

		if (toUpperBounds.size != fromUpperBounds.size) {
			return false
		}

		/*val toUpperBounds = toType.upperBounds
		val fromUpperBounds = fromType.upperBounds

		if (toUpperBounds.size != fromUpperBounds.size) {
			return false
		}
*/
		val isUpperValid = (0 until toUpperBounds.size).all {
			isAssignableFrom(fromUpperBounds[it], toUpperBounds[it])
		}

		return isUpperValid
		/*
		val isUpperValid = (0 until toUpperBounds.size).all {
			println("${fromUpperBounds[it]} ${toUpperBounds[it]}")
			isAssignableFrom(fromUpperBounds[it], toUpperBounds[it])
		}

		if (!isUpperValid) {
			return false
		}*/


	}

	fun isAssignableFrom(fromType: Type): Boolean {
		return isAssignableFrom(fromType, type)
	}

	private fun isAssignableFrom(fromType: Type, toType: Type): Boolean {
		return when (fromType) {

			is Class<*> -> {
				if (toType is Class<*>) fromType.isAssignableFrom(toType)
				else false
			}

			is ParameterizedType -> isAssignableFrom(fromType, toType)
			is GenericArrayType -> isAssignableFrom(fromType, toType)
			is WildcardType -> isAssignableFrom(fromType, toType)
			is TypeVariable<*> -> isAssignableFrom(fromType, toType)
			else -> error("Couldn't handle $fromType")
		}
	}


	companion object {

		inline fun <reified T : Any> of(): TypeHolder<T> {
			return object : TypeHolder<T>(T::class) {}
		}

		inline fun <reified T : Any> of(type: Type): TypeHolder<T> {
			return object : TypeHolder<T>(type, T::class) {}
		}

		fun <T : Any> of(clazz: KClass<T>): TypeHolder<T> {
			return object : TypeHolder<T>(clazz) {}
		}

		fun <T : Any> of(type: Type, clazz: KClass<T>): TypeHolder<T> {
			return object : TypeHolder<T>(type, clazz) {}
		}

	}

}*/