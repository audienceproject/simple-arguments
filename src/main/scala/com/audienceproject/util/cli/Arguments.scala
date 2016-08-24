package com.audienceproject.util.cli

import scala.annotation.tailrec

// A simple class to parse command line arguments
class Arguments(implicit args: Array[String]) {

  @tailrec
  final def toPairs(base: Map[String, Option[String]], iterator: BufferedIterator[String]): Map[String, Option[String]] = {
    if ( !iterator.hasNext) {
      base
    } else if ( !iterator.head.matches("""^--.+$""")) {
      throw new IllegalArgumentException("Arguments need to be specified either in pairs where the key has two dashes in front of it (--)" +
                                           " and the value after it, or as flags where the key is specified with two dashes in front of it (--)")
    } else {
      val k: String = iterator.next().substring(2)
      if ( !iterator.hasNext || iterator.head.matches("""^--.+$""") ) {
        toPairs(base ++ Map(k -> None), iterator)
      } else {
        val v = iterator.next()
        toPairs(base ++ Map(k -> Option(v)), iterator)
      }
    }
  }

  val arguments = toPairs(Map(), args.iterator.buffered)

  /**
    * Get a list of [[String]] for a single CLI argument.
    * Comma `,` is used as a separator character.
    * This is useful when you need to process an arguments passed as `--severalValues one,two,three`.
    * Returns [[None]] if the key does not exist.
    *
    * Example:
    * {{{
    * import com.audienceproject.cli.Arguments
    *
    * object Test {
    *
    *     // args: Array("--severalValues", "one,two,three")
    *     def main(implicit args: Array[String]) {
    *         val arguments = new Arguments
    *         // Returns Option[Seq[String]] = Some(WrappedArray(one, two, three))
    *         arguments.getOptionArray("severalValues")
    *     }
    * }
    * }}}
    *
    * @param key The argument key
    */
  def getOptionArray(key: String ): Option[Seq[String]] = {
    if ( ! arguments.keySet.contains(key) || arguments(key).isEmpty) {
      None
    } else {
      Option(arguments(key).get.split(""","""))
    }
  }

  /**
    * Get a list of [[String]] for a single CLI argument, or a default user-provided list
    * in case the argument does not exist.
    * Comma `,` is used as a separator character.
    * This is useful when you need to process an arguments passed as `--severalValues one,two,three`,
    *
    * Example:
    * {{{
    * import com.audienceproject.cli.Arguments
    *
    * object Test {
    *
    *     // args: Array("--severalValues", "one,two,three")
    *     def main(implicit args: Array[String]) {
    *         val arguments = new Arguments
    *         // Returns Seq[String] = List(five, six)
    *         arguments.getOptionArray("noValues", Seq("five", "six"))
    *     }
    *
    * }
    * }}}
    *
    * @param key The argument key
    */
  def getOptionArray(key: String, default: Seq[String]): Seq[String] = {
    if ( ! arguments.keySet.contains(key) || arguments(key).isEmpty) {
      default
    } else {
      arguments(key).get.split(""",""")
    }
  }

  /**
    * Get the [[String]] value of an argument, or a user-provided [[String]] in case the
    * argument does not exist.
    * This is useful when you need to process an argument passed as `--aSetting someValue`.
    *
    * Example:
    * {{{
    * import com.audienceproject.cli.Arguments
    *
    * object Test {
    *
    *     // args: Array("--aSetting", "someValue")
    *     def main(implicit args: Array[String]) {
    *         val arguments = new Arguments
    *         // Returns String = "backup"
    *         arguments.getOption("noSetting", "backup")
    *     }
    *
    * }
    * }}}
    *
    * @param key The argument key
    */
  def getOption(key: String, default: String): String = {
    if ( ! arguments.keySet.contains(key) || arguments(key).isEmpty) {
      default
    } else {
      arguments(key).get
    }
  }

  /** Get the [[String]] value of an argument.
    * This is useful when you need to process an argument passed as `--aSetting someValue`,
    * Returns [[None]] if the key does not exist.
    *
    * Example:
    * {{{
    * import com.audienceproject.cli.Arguments
    *
    * object Test {
    *
    *     // args: Array("--aSetting", "someValue")
    *     def main(implicit args: Array[String]) {
    *         val arguments = new Arguments
    *         // Returns Option[String] = Some(someValue)
    *         arguments.getOption("aSetting")
    *     }
    *
    * }
    * }}}
    *
    * @param key The argument key
    */
  def getOption(key: String): Option[String] = {
    if ( ! arguments.keySet.contains(key) || arguments(key).isEmpty) {
      None
    } else {
      Option(arguments(key).get)
    }
  }

  /**
    * Check if a flag is set.
    * This is useful when you need to process an argument passed as `--turnItOn`.
    * Will return `false` if the flag is not present in the arguments list. Returns
    * `true` otherwise.
    *
    * Example:
    * {{{
    * import com.audienceproject.cli.Arguments
    *
    * object Test {
    *
    *     // args: Array("--turnItOn")
    *     def main(implicit args: Array[String]) {
    *         val arguments = new Arguments
    *         // Returns Boolean = true
    *         arguments.isSet("turnItOn")
    *         // Returns Boolean = false
    *         arguments.isSet("turnItOff")
    *     }
    *
    * }
    * }}}
    *
    * @param key The argument key
    */
  def isSet(key: String ): Boolean = {
    if ( ! arguments.keySet.contains(key)) {
      false
    } else {
      true
    }
  }

  /**
    * Get the [[A]] value of an argument, or a user-provided [[A]] in case the
    * argument does not exist.
    * This is useful when you need to process an argument which is one of:
    *
    *  - [[String]]
    *  - [[Boolean]]
    *  - [[Int]]
    *  - [[Long]]
    *  - [[Float]]
    *  - [[Double]]
    *
    * An example of such argument list is `--aString str --aBoolean true --anInt 201 --aLong 343 --aFloat 10.4 --aDouble 14.3`.
    *
    *
    * Example:
    * {{{
    * import com.audienceproject.cli.Arguments
    *
    * object Test {
    *
    *     // args: Array("--aString", "str", "--aBoolean", "true", "--anInt", "201", "--aLong", "343", "--aFloat", "10.4", "--aDouble", "14.3")
    *     def main(implicit args: Array[String]) {
    *         val arguments = new Arguments
    *
    *         // Returns String = "str"
    *         arguments.getTypeOption[String]("aString", "a")
    *
    *         // Returns Boolean = false
    *         arguments.getTypeOption[Boolean]("notBoolean", false)
    *
    *         // Returns Boolean = true
    *         arguments.getTypeOption[Boolean]("aBoolean", false)
    *
    *         // Returns Int = 201
    *         arguments.getTypeOption[Int]("anInt", 3)
    *
    *         // Returns Long = 343
    *         arguments.getTypeOption[Long]("aLong", 678)
    *
    *         // Returns Float = 10.4
    *         arguments.getTypeOption[Float]("aFloat", 134.43F)
    *
    *         // Returns Double = 14.3
    *         arguments.getTypeOption[Double]("aDouble", 4523.53)
    *     }
    *
    * }
    * }}}
    *
    * @param key The argument key
    */
  def getTypeOption[A](key: String, default: A): A = {
    if ( ! arguments.keySet.contains(key) || arguments(key).isEmpty) {
      default
    } else {
      default match {
        case _: String => arguments(key).get.asInstanceOf[A]
        case _: Boolean => arguments(key).get.toBoolean.asInstanceOf[A]
        case _: Int => arguments(key).get.toInt.asInstanceOf[A]
        case _: Long => arguments(key).get.toLong.asInstanceOf[A]
        case _: Float => arguments(key).get.toFloat.asInstanceOf[A]
        case _: Double => arguments(key).get.toDouble.asInstanceOf[A]
        case _ => throw new UnsupportedOperationException("This generic type is not supported. Look at the documentation for a list of supported types")
      }
    }
  }

  /**
    * Get the [[A]] value of an argument.
    * This is useful when you need to process an argument which is one of:
    *
    *  - [[String]]
    *  - [[Boolean]]
    *  - [[Int]]
    *  - [[Long]]
    *  - [[Float]]
    *  - [[Double]]
    *
    * An example of such argument list is `--aString str --aBoolean true --anInt 201 --aLong 343 --aFloat 10.4 --aDouble 14.3`
    *
    * Will return [[None]] if the key does not exist.
    *
    * Example:
    * {{{
    * import com.audienceproject.cli.Arguments
    *
    * object Test {
    *
    *     // args: Array("--aString", "str", "--aBoolean", "true", "--anInt", "201", "--aLong", "343", "--aFloat", "10.4", "--aDouble", "14.3")
    *     def main(implicit args: Array[String]) {
    *         val arguments = new Arguments
    *
    *         // Returns Option[String] = Some("str")
    *         arguments.getTypeOption[String]("aString")
    *
    *         // Returns Option[Boolean] = None
    *         arguments.getTypeOption[Boolean]("notBoolean")
    *
    *         // Returns Boolean = Some(true)
    *         arguments.getTypeOption[Boolean]("aBoolean")
    *
    *         // Returns Int = Some(201)
    *         arguments.getTypeOption[Int]("anInt")
    *
    *         // Returns Long = Some(343)
    *         arguments.getTypeOption[Long]("aLong")
    *
    *         // Returns Float = Some(10.4)
    *         arguments.getTypeOption[Float]("aFloat")
    *
    *         // Returns Double = Some(14.3)
    *         arguments.getTypeOption[Double]("aDouble")
    *     }
    *
    * }
    * }}}
    *
    * @param key The argument key
    * @return
    */
  def getTypeOption[A](key: String)(implicit m: Manifest[A]): Option[A] = {
    if ( ! arguments.keySet.contains(key) || arguments(key).isEmpty) {
      None
    } else {
      val stringManifest = manifest[String]
      val booleanManifest = manifest[Boolean]
      val intManifest = manifest[Int]
      val longManifest = manifest[Long]
      val floatManifest = manifest[Float]
      val doubleManifest = manifest[Double]
      m match {
        case `stringManifest` => Option(arguments(key).get.asInstanceOf[A])
        case `booleanManifest` => Option(arguments(key).get.toBoolean.asInstanceOf[A])
        case `intManifest` => Option(arguments(key).get.toInt.asInstanceOf[A])
        case `longManifest` => Option(arguments(key).get.toLong.asInstanceOf[A])
        case `floatManifest` => Option(arguments(key).get.toFloat.asInstanceOf[A])
        case `doubleManifest` => Option(arguments(key).get.toDouble.asInstanceOf[A])
        case _ => throw new UnsupportedOperationException("This generic type is not supported. Look at the documentation for a list of supported types")
      }
    }
  }

}