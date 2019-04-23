package me.camdenorrb.minibus.utils


/*
Doesn't work apparently?

inline fun <reified T : Any> retrieveType() = object : TypeHolder<T>() {}

inline fun <reified T : Any> retrieveType2(): KType? {
	return TypeHolder.of<T>().type2
}*/