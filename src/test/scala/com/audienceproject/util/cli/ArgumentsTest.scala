package com.audienceproject.util.cli

import org.scalatest.FunSuite

class ArgumentsTest extends FunSuite {

    test("When provided with an array of arguments that are in pairs where the first key starts with -- and the value follows" +
        " the instance should initialize correctly") {
        implicit val sample = Array[String]("--asdfsaf", "asdfasdf", "--asdfasdfasdf", "asdfsadf", "--asdfasdf", "asdfasdfasdf")
        val arguments = new Arguments
        assert(arguments != null)
    }

    test("The instance should not initialize when the first argument does not start with --") {
        implicit val sample = Array[String]("-asdfsaf", "asdfasdf", "--asdfasdfasdf", "asdfsadf", "--asdfasdf", "asdfasdfasdf")
        val thrown = intercept[Exception] {
            new Arguments
        }
        assert(thrown != null)
    }

    test("When provided with an array of two arguments where the first start with -- and the second is the value" +
        " calling getOption on the key should return the defined corresponding value") {
        implicit val sample = Array[String]("--akey", "justavalue")
        val arguments = new Arguments
        assert(arguments.getOption("akey").get.equals("justavalue"))
    }

    test("When provided with an array of two arguments where the first start with -- and the second is a" +
        " coma separated list of values, calling getOptionArray on the key should return an array of values") {
        implicit val sample = Array[String]("--akey", "value1,value2,value3")
        val arguments = new Arguments
        assert(arguments.getOptionArray("akey").get.length == 3)
    }

    test("The instance should initialize correctly when the last argument is a flag") {
        implicit val sample = Array[String]("--asdfsaf", "asdfasdf", "--local")
        val arguments = new Arguments
        assert(arguments.isSet("local"))
    }
}
