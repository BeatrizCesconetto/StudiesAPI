package br.com.beatriz.mapper

import com.github.dozermapper.core.DozerBeanMapperBuilder
import com.github.dozermapper.core.Mapper

//import org.modelmapper.ModelMapper


object DozerMapper {

    //Pode usar o dozer ou o ModelMapper
    private val mapper: Mapper = DozerBeanMapperBuilder.buildDefault()
    //private val mapper: ModelMapper = ModelMapper()

    fun <O, D> parseObject(origin: O, destination: Class<D>?): D {
        return mapper.map(origin, destination)
    }

    fun <O, D> parseListObjects(origin: List<O>, destination: Class<D>?): ArrayList<D> {
        val destinationObjects: ArrayList<D> = ArrayList()
        for (o in origin){
            destinationObjects.add(mapper.map(o, destination))
        }
        return destinationObjects
    }
}