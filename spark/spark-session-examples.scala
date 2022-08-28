// DATASET example
case class Person(name: String, age: Int, email: String)
val persons = (1 to 100).map(i => Person(s"name$i", (Math.random()*100).toInt, s"name$i@mail.com"))

val personsDS = persons.toDS
personsDS.show
personsDS.filter(_.age > 18).map(p => (p.name, p.age)).show

personsDS.foreachPartition((p:Iterator[Person]) => println(p.size))


// Encoder
case class PersA(name: String, age: Int)
val newPers = personsDS.map(p => PersA(p.name, p.age))

class PersB(name: String, age: Int)
personsDS.map(p => new PersB(p.name, p.age))

implicit val persbEncoder = org.apache.spark.sql.Encoders.kryo[PersB]


// Dataframe - in comparison to DATASET
val personsDF = persons.toDF
personsDF.filter("age > 18").select("name","age").show


// Transformations : lazy, not-cached; Actions
val persAs = personsDS.map(p => {
    println("calculating PersA");
    PersA(p.name, p.age)
})
val oldPersAs = persAs.filter(p => {
    if(p.age > 20) true else {
        println(s"costly filtering logic")
        false
    }
})

oldPersAs.count
oldPersAs.collectAsList

// Wide Transformation
personsDS.groupByKey(_.age/10).count.show


// transformations...

val oldPeople = personsDS.filter(_.age > 18)

val otherPeople = (101 to 200).map(i => Person(s"name$i", (Math.random()*100).toInt, s"name$i@mail.com")).toDS

val allPeople = oldPeople.union(otherPeople)

def partitionCount[T](x: org.apache.spark.sql.Dataset[T]) = x.mapPartitions(_ => Seq(1).iterator).count
def partitionSizes[T](x: org.apache.spark.sql.Dataset[T]) = x.foreachPartition((p:Iterator[T]) => print(s"${p.size},"))

partitionCount(oldPeople)
partitionCount(otherPeople)
partitionCount(allPeople)

partitionSizes(oldPeople)
partitionSizes(otherPeople)
partitionSizes(allPeople)

val peopleByAge = allPeople.repartition($"age")
partitionCount(peopleByAge)
partitionSizes(peopleByAge)

val peopleByAge = allPeople.repartition(10)
partitionCount(peopleByAge)
partitionSizes(peopleByAge)
peopleByAge.foreachPartition((p:Iterator[Person]) => println(p.map(_.age).toList))

val peopleByAge = allPeople.repartition(13, $"age")
peopleByAge.foreachPartition((p:Iterator[Person]) => println(p.map(_.age).toList))

val joinedPeople = peopleByAge.joinWith(newPers, newPers("name")===peopleByAge("name"))
partitionCount(joinedPeople)
partitionSizes(joinedPeople)



// re:
case class Person(name: String, age: Int, email: String)
case class PersA(name: String, age: Int)

val personsDS = (1 to 100).map(i => Person(s"name$i", (Math.random()*100).toInt, s"name$i@mail.com")).toDS
val newPers = personsDS.map(p => PersA(p.name, p.age))

val oldPeople = personsDS.filter(_.age > 18)
val otherPeople = (101 to 200).map(i => Person(s"name$i", (Math.random()*100).toInt, s"name$i@mail.com")).toDS
val allPeople = oldPeople.union(otherPeople)

val peopleByAge = allPeople.repartition(13, $"age")

val joinedPeople = peopleByAge.joinWith(newPers, newPers("name")===peopleByAge("name"))
val result = joinedPeople.collectAsList
