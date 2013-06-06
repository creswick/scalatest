/*
* Copyright 2001-2013 Artima, Inc.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.scalatest

import org.scalautils.Equality
import org.scalautils.Normalization
import org.scalautils.StringNormalizations._
import SharedHelpers._
import FailureMessages.decorateToStringValue
import scala.collection.mutable.LinkedList

class ListShouldContainInOrderOnlyLogicalOrSpec extends Spec with Matchers {
  
  val invertedStringEquality =
    new Equality[String] {
      def areEqual(a: String, b: Any): Boolean = a != b
    }
  
  val invertedListOfStringEquality = 
    new Equality[List[String]] {
      def areEqual(a: List[String], b: Any): Boolean = a != b
    }
  
  private def upperCase(value: Any): Any = 
    value match {
      case l: List[_] => l.map(upperCase(_))
      case s: String => s.toUpperCase
      case c: Char => c.toString.toUpperCase.charAt(0)
      case (s1: String, s2: String) => (s1.toUpperCase, s2.toUpperCase)
      case _ => value
    }
  
  val upperCaseStringEquality =
    new Equality[String] {
      def areEqual(a: String, b: Any): Boolean = upperCase(a) == upperCase(b)
    }
  
  //ADDITIONAL//
  
  val fileName: String = "ListShouldContainInOrderOnlyLogicalOrSpec.scala"
  
  object `a List` {
    
    val fumList: List[String] = List("fum", "foe", "fie", "fee")
    val toList: List[String] = List("you", "to", "birthday", "happy")
    
    object `when used with (contain theSameElementsInOrderAs xx or contain theSameElementsInOrderAs xx)` {
      
      def `should do nothing if valid, else throw a TFE with an appropriate error message` {
        fumList should (newContain newTheSameElementsInOrderAs LinkedList("fum", "foe", "fie", "fee") or newContain newTheSameElementsInOrderAs LinkedList("fum", "foe", "fie", "fee"))
        fumList should (newContain newTheSameElementsInOrderAs LinkedList("fee", "fie", "foe", "fum") or newContain newTheSameElementsInOrderAs LinkedList("fum", "foe", "fie", "fee"))
        fumList should (newContain newTheSameElementsInOrderAs LinkedList("fum", "foe", "fie", "fee") or newContain newTheSameElementsInOrderAs LinkedList("fee", "fie", "foe", "fum"))
        val e1 = intercept[TestFailedException] {
          fumList should (newContain newTheSameElementsInOrderAs LinkedList("fee", "fie", "foe", "fum") or newContain newTheSameElementsInOrderAs LinkedList("happy", "birthday", "to", "you"))
        }
        checkMessageStackDepth(e1, Resources("didNotContainSameElementsInOrder", decorateToStringValue(fumList), decorateToStringValue(LinkedList("fee", "fie", "foe", "fum"))) + ", and " + Resources("didNotContainSameElementsInOrder", decorateToStringValue(fumList), decorateToStringValue(LinkedList("happy", "birthday", "to", "you"))), fileName, thisLineNumber - 2)
      }

      def `should use the implicit Equality in scope` {
        implicit val ise = upperCaseStringEquality
        fumList should (newContain newTheSameElementsInOrderAs LinkedList("FUM", "FOE", "FIE", "FEE") or newContain newTheSameElementsInOrderAs LinkedList("FUM", "FOE", "FIE", "FEE"))
        fumList should (newContain newTheSameElementsInOrderAs LinkedList("FEE", "FIE", "FOE", "FUM") or newContain newTheSameElementsInOrderAs LinkedList("FUM", "FOE", "FIE", "FEE"))
        fumList should (newContain newTheSameElementsInOrderAs LinkedList("FUM", "FOE", "FIE", "FEE") or newContain newTheSameElementsInOrderAs LinkedList("FEE", "FIE", "FOE", "FUM"))
        val e1 = intercept[TestFailedException] {
          fumList should (newContain newTheSameElementsInOrderAs LinkedList("FEE", "FIE", "FOE", "FUM") or (newContain newTheSameElementsInOrderAs LinkedList("FIE", "FEE", "FAM", "FOE")))
        }
        checkMessageStackDepth(e1, Resources("didNotContainSameElementsInOrder", decorateToStringValue(fumList), decorateToStringValue(LinkedList("FEE", "FIE", "FOE", "FUM"))) + ", and " + Resources("didNotContainSameElementsInOrder", decorateToStringValue(fumList), decorateToStringValue(LinkedList("FIE", "FEE", "FAM", "FOE"))), fileName, thisLineNumber - 2)
      }
      
      def `should use an explicitly provided Equality` {
        (fumList should (newContain newTheSameElementsInOrderAs LinkedList("FUM", "FOE", "FIE", "FEE") or newContain newTheSameElementsInOrderAs LinkedList("FUM", "FOE", "FIE", "FEE"))) (decided by upperCaseStringEquality, decided by upperCaseStringEquality)
        (fumList should (newContain newTheSameElementsInOrderAs LinkedList("FEE", "FIE", "FOE", "FUM") or newContain newTheSameElementsInOrderAs LinkedList("FUM", "FOE", "FIE", "FEE"))) (decided by upperCaseStringEquality, decided by upperCaseStringEquality)
        (fumList should (newContain newTheSameElementsInOrderAs LinkedList("FUM", "FOE", "FIE", "FEE") or newContain newTheSameElementsInOrderAs LinkedList("FEE", "FIE", "FOE", "FUM"))) (decided by upperCaseStringEquality, decided by upperCaseStringEquality)
        val e1 = intercept[TestFailedException] {
          (fumList should (newContain newTheSameElementsInOrderAs LinkedList("FEE", "FIE", "FOE", "FUM") or newContain newTheSameElementsInOrderAs LinkedList("FIE", "FEE", "FAM", "FOE"))) (decided by upperCaseStringEquality, decided by upperCaseStringEquality)
        }
        checkMessageStackDepth(e1, Resources("didNotContainSameElementsInOrder", decorateToStringValue(fumList), decorateToStringValue(LinkedList("FEE", "FIE", "FOE", "FUM"))) + ", and " + Resources("didNotContainSameElementsInOrder", decorateToStringValue(fumList), decorateToStringValue(LinkedList("FIE", "FEE", "FAM", "FOE"))), fileName, thisLineNumber - 2)
        (fumList should (newContain newTheSameElementsInOrderAs LinkedList(" FUM ", " FOE ", " FIE ", " FEE ") or newContain newTheSameElementsInOrderAs LinkedList(" FUM ", " FOE ", " FIE ", " FEE "))) (after being lowerCased and trimmed, after being lowerCased and trimmed)
      }
    }
    
    object `when used with (equal xx and contain theSameElementsInOrderAs xx)` {
      
      def `should do nothing if valid, else throw a TFE with an appropriate error message` {
        fumList should (equal (fumList) or newContain newTheSameElementsInOrderAs LinkedList("fum", "foe", "fie", "fee"))
        fumList should (equal (toList) or newContain newTheSameElementsInOrderAs LinkedList("fum", "foe", "fie", "fee"))
        fumList should (equal (fumList) or newContain newTheSameElementsInOrderAs LinkedList("fee", "fie", "foe", "fum"))
        val e1 = intercept[TestFailedException] {
          fumList should (equal (toList) or newContain newTheSameElementsInOrderAs LinkedList("fee", "fie", "foe", "fum"))
        }
        checkMessageStackDepth(e1, Resources("didNotEqual", decorateToStringValue(fumList), decorateToStringValue(toList)) + ", and " + Resources("didNotContainSameElementsInOrder", decorateToStringValue(fumList), decorateToStringValue(LinkedList("fee", "fie", "foe", "fum"))), fileName, thisLineNumber - 2)
      }
      
      def `should use the implicit Equality in scope` {
        implicit val ise = upperCaseStringEquality
        fumList should (equal (fumList) or newContain newTheSameElementsInOrderAs LinkedList("FUM", "FOE", "FIE", "FEE"))
        fumList should (equal (toList) or newContain newTheSameElementsInOrderAs LinkedList("FUM", "FOE", "FIE", "FEE"))
        fumList should (equal (fumList) or newContain newTheSameElementsInOrderAs LinkedList("FEE", "FIE", "FOE", "FUM"))
        val e1 = intercept[TestFailedException] {
          fumList should (equal (toList) or (newContain newTheSameElementsInOrderAs LinkedList("FEE", "FIE", "FOE", "FUM")))
        }
        checkMessageStackDepth(e1, Resources("didNotEqual", decorateToStringValue(fumList), decorateToStringValue(toList)) + ", and " + Resources("didNotContainSameElementsInOrder", decorateToStringValue(fumList), decorateToStringValue(LinkedList("FEE", "FIE", "FOE", "FUM"))), fileName, thisLineNumber - 2)
      }
      
      def `should use an explicitly provided Equality` {
        (fumList should (equal (toList) or newContain newTheSameElementsInOrderAs LinkedList("FUM", "FOE", "FIE", "FEE"))) (decided by invertedListOfStringEquality, decided by upperCaseStringEquality)
        (fumList should (equal (fumList) or newContain newTheSameElementsInOrderAs LinkedList("FUM", "FOE", "FIE", "FEE"))) (decided by invertedListOfStringEquality, decided by upperCaseStringEquality)
        (fumList should (equal (toList) or newContain newTheSameElementsInOrderAs (LinkedList("FEE", "FIE", "FOE", "FUM")))) (decided by invertedListOfStringEquality, decided by upperCaseStringEquality)
        val e1 = intercept[TestFailedException] {
          (fumList should (equal (fumList) or newContain newTheSameElementsInOrderAs (LinkedList("FEE", "FIE", "FOE", "FUM")))) (decided by invertedListOfStringEquality, decided by upperCaseStringEquality)
        }
        checkMessageStackDepth(e1, Resources("didNotEqual", decorateToStringValue(fumList), decorateToStringValue(fumList)) + ", and " + Resources("didNotContainSameElementsInOrder", decorateToStringValue(fumList), decorateToStringValue(LinkedList("FEE", "FIE", "FOE", "FUM"))), fileName, thisLineNumber - 2)
        (fumList should (equal (toList) or newContain newTheSameElementsInOrderAs LinkedList(" FEE ", " FIE ", " FOE ", " FUM "))) (decided by invertedListOfStringEquality, after being lowerCased and trimmed)
      }
    }
    
    object `when used with (legacyEqual xx and contain theSameElementsInOrderAs xx)` {
      
      def `should do nothing if valid, else throw a TFE with an appropriate error message` {
        fumList should (legacyEqual (fumList) or newContain newTheSameElementsInOrderAs LinkedList("fum", "foe", "fie", "fee"))
        fumList should (legacyEqual (toList) or newContain newTheSameElementsInOrderAs LinkedList("fum", "foe", "fie", "fee"))
        fumList should (legacyEqual (fumList) or newContain newTheSameElementsInOrderAs LinkedList("fee", "fie", "foe", "fum"))
        val e1 = intercept[TestFailedException] {
          fumList should (legacyEqual (toList) or newContain newTheSameElementsInOrderAs LinkedList("fee", "fie", "foe", "fum"))
        }
        checkMessageStackDepth(e1, Resources("didNotEqual", decorateToStringValue(fumList), decorateToStringValue(toList)) + ", and " + Resources("didNotContainSameElementsInOrder", decorateToStringValue(fumList), decorateToStringValue(LinkedList("fee", "fie", "foe", "fum"))), fileName, thisLineNumber - 2)
      }
      
      def `should use the implicit Equality in scope` {
        implicit val ise = upperCaseStringEquality
        fumList should (legacyEqual (fumList) or newContain newTheSameElementsInOrderAs LinkedList("FUM", "FOE", "FIE", "FEE"))
        fumList should (legacyEqual (toList) or newContain newTheSameElementsInOrderAs LinkedList("FUM", "FOE", "FIE", "FEE"))
        fumList should (legacyEqual (fumList) or newContain newTheSameElementsInOrderAs LinkedList("FEE", "FIE", "FOE", "FUM"))
        val e1 = intercept[TestFailedException] {
          fumList should (legacyEqual (toList) or (newContain newTheSameElementsInOrderAs LinkedList("FEE", "FIE", "FOE", "FUM")))
        }
        checkMessageStackDepth(e1, Resources("didNotEqual", decorateToStringValue(fumList), decorateToStringValue(toList)) + ", and " + Resources("didNotContainSameElementsInOrder", decorateToStringValue(fumList), decorateToStringValue(LinkedList("FEE", "FIE", "FOE", "FUM"))), fileName, thisLineNumber - 2)
      }
      
      def `should use an explicitly provided Equality` {
        (fumList should (legacyEqual (fumList) or newContain newTheSameElementsInOrderAs LinkedList("FUM", "FOE", "FIE", "FEE"))) (decided by upperCaseStringEquality)
        (fumList should (legacyEqual (toList) or newContain newTheSameElementsInOrderAs LinkedList("FUM", "FOE", "FIE", "FEE"))) (decided by upperCaseStringEquality)
        (fumList should (legacyEqual (fumList) or newContain newTheSameElementsInOrderAs LinkedList("FEE", "FIE", "FOE", "FUM"))) (decided by upperCaseStringEquality)
        val e1 = intercept[TestFailedException] {
          (fumList should (legacyEqual (toList) or newContain newTheSameElementsInOrderAs LinkedList("FEE", "FIE", "FOE", "FUM"))) (decided by upperCaseStringEquality)
        }
        checkMessageStackDepth(e1, Resources("didNotEqual", decorateToStringValue(fumList), decorateToStringValue(toList)) + ", and " + Resources("didNotContainSameElementsInOrder", decorateToStringValue(fumList), decorateToStringValue(LinkedList("FEE", "FIE", "FOE", "FUM"))), fileName, thisLineNumber - 2)
        (fumList should (legacyEqual (fumList) or newContain newTheSameElementsInOrderAs LinkedList(" FUM ", " FOE ", " FIE ", " FEE "))) (after being lowerCased and trimmed)
      }
    }

    object `when used with (contain theSameElementsInOrderAs xx and legacyEqual xx)` {
      
      def `should do nothing if valid, else throw a TFE with an appropriate error message` {
        fumList should (newContain newTheSameElementsInOrderAs LinkedList("fum", "foe", "fie", "fee") or legacyEqual (fumList))
        fumList should (newContain newTheSameElementsInOrderAs LinkedList("fee", "fie", "foe", "fum") or legacyEqual (fumList))
        fumList should (newContain newTheSameElementsInOrderAs LinkedList("fum", "foe", "fie", "fee") or legacyEqual (toList))
        val e1 = intercept[TestFailedException] {
          fumList should (newContain newTheSameElementsInOrderAs LinkedList("fee", "fie", "foe", "fum") or legacyEqual (toList))
        }
        checkMessageStackDepth(e1, Resources("didNotContainSameElementsInOrder", decorateToStringValue(fumList), decorateToStringValue(LinkedList("fee", "fie", "foe", "fum"))) + ", and " + Resources("didNotEqual", decorateToStringValue(fumList), decorateToStringValue(toList)), fileName, thisLineNumber - 2)
      }
      
      def `should use the implicit Equality in scope` {
        implicit val ise = upperCaseStringEquality
        fumList should (newContain newTheSameElementsInOrderAs LinkedList("FUM", "FOE", "FIE", "FEE") or legacyEqual (fumList))
        fumList should (newContain newTheSameElementsInOrderAs LinkedList("FEE", "FIE", "FOE", "FUM") or legacyEqual (fumList))
        fumList should (newContain newTheSameElementsInOrderAs LinkedList("FUM", "FOE", "FIE", "FEE") or legacyEqual (toList))
        val e1 = intercept[TestFailedException] {
          fumList should (newContain newTheSameElementsInOrderAs LinkedList("FEE", "FIE", "FOE", "FUM") or legacyEqual (toList))
        }
        checkMessageStackDepth(e1, Resources("didNotContainSameElementsInOrder", decorateToStringValue(fumList), decorateToStringValue(LinkedList("FEE", "FIE", "FOE", "FUM"))) + ", and " + Resources("didNotEqual", decorateToStringValue(fumList), decorateToStringValue(toList)), fileName, thisLineNumber - 2)
      }
      
      def `should use an explicitly provided Equality` {
        (fumList should (newContain newTheSameElementsInOrderAs LinkedList("FUM", "FOE", "FIE", "FEE") or legacyEqual (fumList))) (decided by upperCaseStringEquality)
        (fumList should (newContain newTheSameElementsInOrderAs LinkedList("FEE", "FIE", "FOE", "FUM") or legacyEqual (fumList))) (decided by upperCaseStringEquality)
        (fumList should (newContain newTheSameElementsInOrderAs LinkedList("FUM", "FOE", "FIE", "FEE") or legacyEqual (toList))) (decided by upperCaseStringEquality)
        val e1 = intercept[TestFailedException] {
          (fumList should (newContain newTheSameElementsInOrderAs LinkedList("FEE", "FIE", "FOE", "FUM") or legacyEqual (toList))) (decided by upperCaseStringEquality)
        }
        checkMessageStackDepth(e1, Resources("didNotContainSameElementsInOrder", decorateToStringValue(fumList), decorateToStringValue(LinkedList("FEE", "FIE", "FOE", "FUM"))) + ", and " + Resources("didNotEqual", decorateToStringValue(fumList), decorateToStringValue(toList)), fileName, thisLineNumber - 2)
        (fumList should (newContain newTheSameElementsInOrderAs LinkedList(" FUM ", " FOE ", " FIE ", " FEE ") or legacyEqual (fumList))) (after being lowerCased and trimmed)
      }
    }
    
    object `when used with (not contain theSameElementsInOrderAs xx and not contain theSameElementsInOrderAs xx)` {
      
      def `should do nothing if valid, else throw a TFE with an appropriate error message` {
        fumList should (not newContain newTheSameElementsInOrderAs (LinkedList("fee", "fie", "foe", "fum")) or not newContain newTheSameElementsInOrderAs (LinkedList("fee", "fie", "foe", "fum")))
        fumList should (not newContain newTheSameElementsInOrderAs (LinkedList("fum", "foe", "fie", "fee")) or not newContain newTheSameElementsInOrderAs (LinkedList("fee", "fie", "foe", "fum")))
        fumList should (not newContain newTheSameElementsInOrderAs (LinkedList("fee", "fie", "foe", "fum")) or not newContain newTheSameElementsInOrderAs (LinkedList("fum", "foe", "fie", "fee")))
        val e1 = intercept[TestFailedException] {
          fumList should (not newContain newTheSameElementsInOrderAs (LinkedList("fum", "foe", "fie", "fee")) or not newContain newTheSameElementsInOrderAs (LinkedList("fum", "foe", "fie", "fee")))
        }
        checkMessageStackDepth(e1, Resources("containedSameElementsInOrder", decorateToStringValue(fumList), decorateToStringValue(LinkedList("fum", "foe", "fie", "fee"))) + ", and " + Resources("containedSameElementsInOrder", decorateToStringValue(fumList), decorateToStringValue(LinkedList("fum", "foe", "fie", "fee"))), fileName, thisLineNumber - 2)
      }
      
      def `should use the implicit Equality in scope` {
        implicit val ise = upperCaseStringEquality
        fumList should (not newContain newTheSameElementsInOrderAs (LinkedList("FEE", "FIE", "FOE", "FUM")) or not newContain newTheSameElementsInOrderAs (LinkedList("FEE", "FIE", "FOE", "FUM")))
        fumList should (not newContain newTheSameElementsInOrderAs (LinkedList("FUM", "FOE", "FIE", "FEE")) or not newContain newTheSameElementsInOrderAs (LinkedList("FEE", "FIE", "FOE", "FUM")))
        fumList should (not newContain newTheSameElementsInOrderAs (LinkedList("FEE", "FIE", "FOE", "FUM")) or not newContain newTheSameElementsInOrderAs (LinkedList("FUM", "FOE", "FIE", "FEE")))
        val e1 = intercept[TestFailedException] {
          fumList should (not newContain newTheSameElementsInOrderAs (LinkedList("FUM", "FOE", "FIE", "FEE")) or not newContain newTheSameElementsInOrderAs (LinkedList("FUM", "FOE", "FIE", "FEE")))
        }
        checkMessageStackDepth(e1, Resources("containedSameElementsInOrder", decorateToStringValue(fumList), decorateToStringValue(LinkedList("FUM", "FOE", "FIE", "FEE"))) + ", and " + Resources("containedSameElementsInOrder", decorateToStringValue(fumList), decorateToStringValue(LinkedList("FUM", "FOE", "FIE", "FEE"))), fileName, thisLineNumber - 2)
      }
      
      def `should use an explicitly provided Equality` {
        (fumList should (not newContain newTheSameElementsInOrderAs (LinkedList("FEE", "FIE", "FOE", "FUM")) or not newContain newTheSameElementsInOrderAs (LinkedList("FEE", "FIE", "FOE", "FUM")))) (decided by upperCaseStringEquality, decided by upperCaseStringEquality)
        (fumList should (not newContain newTheSameElementsInOrderAs (LinkedList("FUM", "FOE", "FIE", "FEE")) or not newContain newTheSameElementsInOrderAs (LinkedList("FEE", "FIE", "FOE", "FUM")))) (decided by upperCaseStringEquality, decided by upperCaseStringEquality)
        (fumList should (not newContain newTheSameElementsInOrderAs (LinkedList("FEE", "FIE", "FOE", "FUM")) or not newContain newTheSameElementsInOrderAs (LinkedList("FUM", "FOE", "FIE", "FEE")))) (decided by upperCaseStringEquality, decided by upperCaseStringEquality)
        val e1 = intercept[TestFailedException] {
          (fumList should (not newContain newTheSameElementsInOrderAs (LinkedList("FUM", "FOE", "FIE", "FEE")) or not newContain newTheSameElementsInOrderAs (LinkedList("FUM", "FOE", "FIE", "FEE")))) (decided by upperCaseStringEquality, decided by upperCaseStringEquality)
        }
        checkMessageStackDepth(e1, Resources("containedSameElementsInOrder", decorateToStringValue(fumList), decorateToStringValue(LinkedList("FUM", "FOE", "FIE", "FEE"))) + ", and " + Resources("containedSameElementsInOrder", decorateToStringValue(fumList), decorateToStringValue(LinkedList("FUM", "FOE", "FIE", "FEE"))), fileName, thisLineNumber - 2)
      }
    }
    
    object `when used with (not equal xx and not contain theSameElementsInOrderAs xx)` {
      
      def `should do nothing if valid, else throw a TFE with an appropriate error message` {
        fumList should (not equal (toList) or not newContain newTheSameElementsInOrderAs (LinkedList("fee", "fie", "foe", "fum")))
        fumList should (not equal (fumList) or not newContain newTheSameElementsInOrderAs (LinkedList("fee", "fie", "foe", "fum")))
        fumList should (not equal (toList) or not newContain newTheSameElementsInOrderAs (LinkedList("fum", "foe", "fie", "fee")))
        val e1 = intercept[TestFailedException] {
          fumList should (not equal (fumList) or not newContain newTheSameElementsInOrderAs (LinkedList("fum", "foe", "fie", "fee")))
        }
        checkMessageStackDepth(e1, Resources("equaled", decorateToStringValue(fumList), decorateToStringValue(fumList)) + ", and " + Resources("containedSameElementsInOrder", decorateToStringValue(fumList), decorateToStringValue(LinkedList("fum", "foe", "fie", "fee"))), fileName, thisLineNumber - 2)
      }
      
      def `should use the implicit Equality in scope` {
        implicit val ise = upperCaseStringEquality
        fumList should (not equal (toList) or not newContain newTheSameElementsInOrderAs (LinkedList("FEE", "FIE", "FOE", "FUM")))
        fumList should (not equal (fumList) or not newContain newTheSameElementsInOrderAs (LinkedList("FEE", "FIE", "FOE", "FUM")))
        fumList should (not equal (toList) or not newContain newTheSameElementsInOrderAs (LinkedList("FUM", "FOE", "FIE", "FEE")))
        val e2 = intercept[TestFailedException] {
          fumList should (not equal (fumList) or (not newContain newTheSameElementsInOrderAs (LinkedList("FUM", "FOE", "FIE", "FEE"))))
        }
        checkMessageStackDepth(e2, Resources("equaled", decorateToStringValue(fumList), decorateToStringValue(fumList)) + ", and " + Resources("containedSameElementsInOrder", decorateToStringValue(fumList), decorateToStringValue(LinkedList("FUM", "FOE", "FIE", "FEE"))), fileName, thisLineNumber - 2)
      }
      
      def `should use an explicitly provided Equality` {
        (fumList should (not equal (fumList) or not newContain newTheSameElementsInOrderAs (LinkedList("FEE", "FIE", "FOE", "FUM")))) (decided by invertedListOfStringEquality, decided by upperCaseStringEquality)
        (fumList should (not equal (toList) or not newContain newTheSameElementsInOrderAs (LinkedList("FEE", "FIE", "FOE", "FUM")))) (decided by invertedListOfStringEquality, decided by upperCaseStringEquality)
        (fumList should (not equal (fumList) or not newContain newTheSameElementsInOrderAs (LinkedList("FUM", "FOE", "FIE", "FEE")))) (decided by invertedListOfStringEquality, decided by upperCaseStringEquality)
        val e1 = intercept[TestFailedException] {
          (fumList should (not equal (toList) or not newContain newTheSameElementsInOrderAs (LinkedList("FUM", "FOE", "FIE", "FEE")))) (decided by invertedListOfStringEquality, decided by upperCaseStringEquality)
        }
        checkMessageStackDepth(e1, Resources("equaled", decorateToStringValue(fumList), decorateToStringValue(toList)) + ", and " + Resources("containedSameElementsInOrder", decorateToStringValue(fumList), decorateToStringValue(LinkedList("FUM", "FOE", "FIE", "FEE"))), fileName, thisLineNumber - 2)
      }
    }
    
    object `when used with (not be xx and not contain theSameElementsInOrderAs xx)` {
      
      def `should do nothing if valid, else throw a TFE with an appropriate error message` {
        fumList should (not be (toList) or not newContain newTheSameElementsInOrderAs (LinkedList("fee", "fie", "foe", "fum")))
        fumList should (not be (fumList) or not newContain newTheSameElementsInOrderAs (LinkedList("fee", "fie", "foe", "fum")))
        fumList should (not be (toList) or not newContain newTheSameElementsInOrderAs (LinkedList("fum", "foe", "fie", "fee")))
        val e1 = intercept[TestFailedException] {
          fumList should (not be (fumList) or not newContain newTheSameElementsInOrderAs (LinkedList("fum", "foe", "fie", "fee")))
        }
        checkMessageStackDepth(e1, Resources("wasEqualTo", decorateToStringValue(fumList), decorateToStringValue(fumList)) + ", and " + Resources("containedSameElementsInOrder", decorateToStringValue(fumList), decorateToStringValue(LinkedList("fum", "foe", "fie", "fee"))), fileName, thisLineNumber - 2)
      }
      
      def `should use the implicit Equality in scope` {
        implicit val ise = upperCaseStringEquality
        fumList should (not be (toList) or not newContain newTheSameElementsInOrderAs (LinkedList("FEE", "FIE", "FOE", "FUM")))
        fumList should (not be (fumList) or not newContain newTheSameElementsInOrderAs (LinkedList("FEE", "FIE", "FOE", "FUM")))
        fumList should (not be (toList) or not newContain newTheSameElementsInOrderAs (LinkedList("FUM", "FOE", "FIE", "FEE")))
        val e1 = intercept[TestFailedException] {
          fumList should (not be (fumList) or (not newContain newTheSameElementsInOrderAs (LinkedList("FUM", "FOE", "FIE", "FEE"))))
        }
        checkMessageStackDepth(e1, Resources("wasEqualTo", decorateToStringValue(fumList), decorateToStringValue(fumList)) + ", and " + Resources("containedSameElementsInOrder", decorateToStringValue(fumList), decorateToStringValue(LinkedList("FUM", "FOE", "FIE", "FEE"))), fileName, thisLineNumber - 2)
      }
      
      def `should use an explicitly provided Equality` {
        (fumList should (not be (toList) or not newContain newTheSameElementsInOrderAs (LinkedList("FEE", "FIE", "FOE", "FUM")))) (decided by upperCaseStringEquality)
        (fumList should (not be (fumList) or not newContain newTheSameElementsInOrderAs (LinkedList("FEE", "FIE", "FOE", "FUM")))) (decided by upperCaseStringEquality)
        (fumList should (not be (toList) or not newContain newTheSameElementsInOrderAs (LinkedList("FUM", "FOE", "FIE", "FEE")))) (decided by upperCaseStringEquality)
        val e1 = intercept[TestFailedException] {
          (fumList should (not be (fumList) or not newContain newTheSameElementsInOrderAs (LinkedList("FUM", "FOE", "FIE", "FEE")))) (decided by upperCaseStringEquality)
        }
        checkMessageStackDepth(e1, Resources("wasEqualTo", decorateToStringValue(fumList), decorateToStringValue(fumList)) + ", and " + Resources("containedSameElementsInOrder", decorateToStringValue(fumList), decorateToStringValue(LinkedList("FUM", "FOE", "FIE", "FEE"))), fileName, thisLineNumber - 2)
        (fumList should (not newContain newTheSameElementsInOrderAs (Set(" FEE ", " FIE ", " FOE ", " FUU ")) or not newContain newTheSameElementsInOrderAs (Set(" FEE ", " FIE ", " FOE ", " FUU ")))) (after being lowerCased and trimmed, after being lowerCased and trimmed)
      }
    }
    
  }
  
  object `collection of Lists` {
    
    val list1s: Vector[List[Int]] = Vector(List(1, 2, 3), List(1, 2, 3), List(1, 2, 3))
    val lists: Vector[List[Int]] = Vector(List(1, 2, 3), List(1, 2, 3), List(2, 3, 4))
    val nils: Vector[List[Int]] = Vector(Nil, Nil, Nil)
    val listsNil: Vector[List[Int]] = Vector(List(1, 2, 3), List(1, 2, 3), Nil)
    val hiLists: Vector[List[String]] = Vector(List("hi", "hello"), List("hi", "hello"), List("hi", "hello"))
    val toLists: Vector[List[String]] = Vector(List("you", "to"), List("you", "to"), List("you", "to"))
    
    def allErrMsg(index: Int, message: String, lineNumber: Int, left: Any): String = 
      "'all' inspection failed, because: \n" +
      "  at index " + index + ", " + message + " (" + fileName + ":" + (lineNumber) + ") \n" +
      "in " + left
    
    object `when used with (contain theSameElementsInOrderAs xx and contain theSameElementsInOrderAs xx)` {
      
      def `should do nothing if valid, else throw a TFE with an appropriate error message` {
        all (list1s) should (newContain newTheSameElementsInOrderAs LinkedList(1, 2, 3) or newContain newTheSameElementsInOrderAs LinkedList(1, 2, 3))
        all (list1s) should (newContain newTheSameElementsInOrderAs LinkedList(3, 2, 5) or newContain newTheSameElementsInOrderAs LinkedList(1, 2, 3))
        all (list1s) should (newContain newTheSameElementsInOrderAs LinkedList(1, 2, 3) or newContain newTheSameElementsInOrderAs LinkedList(2, 3, 4))
        
        atLeast (2, lists) should (newContain newTheSameElementsInOrderAs LinkedList(1, 2, 3) or newContain newTheSameElementsInOrderAs LinkedList(1, 2, 3))
        atLeast (2, lists) should (newContain newTheSameElementsInOrderAs LinkedList(3, 6, 5) or newContain newTheSameElementsInOrderAs LinkedList(1, 2, 3))
        atLeast (2, lists) should (newContain newTheSameElementsInOrderAs LinkedList(1, 2, 3) or newContain newTheSameElementsInOrderAs LinkedList(8, 3, 4))
        
        val e1 = intercept[TestFailedException] {
          all (lists) should (newContain newTheSameElementsInOrderAs LinkedList(1, 2, 3) or newContain newTheSameElementsInOrderAs LinkedList(1, 2, 3))
        }
        checkMessageStackDepth(e1, allErrMsg(2, decorateToStringValue(List(2, 3, 4)) + " did not contain the same elements in the same (iterated) order as " + decorateToStringValue(LinkedList(1, 2, 3)) + ", and " + decorateToStringValue(List(2, 3, 4)) + " did not contain the same elements in the same (iterated) order as " + decorateToStringValue(LinkedList(1, 2, 3)), thisLineNumber - 2, lists), fileName, thisLineNumber - 2)
      }
      
      def `should use the implicit Equality in scope` {
        implicit val ise = upperCaseStringEquality
        
        all (hiLists) should (newContain newTheSameElementsInOrderAs LinkedList("HI", "HELLO") or newContain newTheSameElementsInOrderAs LinkedList("hi", "hello"))
        all (hiLists) should (newContain newTheSameElementsInOrderAs LinkedList("HELLO", "HO") or newContain newTheSameElementsInOrderAs LinkedList("hi", "hello"))
        all (hiLists) should (newContain newTheSameElementsInOrderAs LinkedList("HI", "HELLO") or newContain newTheSameElementsInOrderAs LinkedList("hello", "ho"))
        
        val e1 = intercept[TestFailedException] {
          all (hiLists) should (newContain newTheSameElementsInOrderAs LinkedList("HELLO", "HO") or newContain newTheSameElementsInOrderAs LinkedList("hello", "ho"))
        }
        checkMessageStackDepth(e1, allErrMsg(0, decorateToStringValue(List("hi", "hello")) + " did not contain the same elements in the same (iterated) order as " + decorateToStringValue(LinkedList("HELLO", "HO")) + ", and " + decorateToStringValue(List("hi", "hello")) + " did not contain the same elements in the same (iterated) order as " + decorateToStringValue(LinkedList("hello", "ho")), thisLineNumber - 2, hiLists), fileName, thisLineNumber - 2)
      }
      
      def `should use an explicitly provided Equality` {
        (all (hiLists) should (newContain newTheSameElementsInOrderAs LinkedList("HI", "HELLO") or newContain newTheSameElementsInOrderAs LinkedList("hi", "hello"))) (decided by upperCaseStringEquality, decided by upperCaseStringEquality)
        (all (hiLists) should (newContain newTheSameElementsInOrderAs LinkedList("HELLO", "HO") or newContain newTheSameElementsInOrderAs LinkedList("hi", "hello"))) (decided by upperCaseStringEquality, decided by upperCaseStringEquality)
        (all (hiLists) should (newContain newTheSameElementsInOrderAs LinkedList("HI", "HELLO") or newContain newTheSameElementsInOrderAs LinkedList("hello", "ho"))) (decided by upperCaseStringEquality, decided by upperCaseStringEquality)
        
        val e1 = intercept[TestFailedException] {
          (all (hiLists) should (newContain newTheSameElementsInOrderAs LinkedList("HELLO", "HO") or newContain newTheSameElementsInOrderAs LinkedList("hello", "ho"))) (decided by upperCaseStringEquality, decided by upperCaseStringEquality)
        }
        checkMessageStackDepth(e1, allErrMsg(0, decorateToStringValue(List("hi", "hello")) + " did not contain the same elements in the same (iterated) order as " + decorateToStringValue(LinkedList("HELLO", "HO")) + ", and " + decorateToStringValue(List("hi", "hello")) + " did not contain the same elements in the same (iterated) order as " + decorateToStringValue(LinkedList("hello", "ho")), thisLineNumber - 2, hiLists), fileName, thisLineNumber - 2)
      }
    }
    
    object `when used with (be xx and contain theSameElementsInOrderAs xx)` {
      
      def `should do nothing if valid, else throw a TFE with an appropriate error message` {
        all (list1s) should (be (List(1, 2, 3)) or newContain newTheSameElementsInOrderAs LinkedList(1, 2, 3))
        all (list1s) should (be (List(2, 3, 4)) or newContain newTheSameElementsInOrderAs LinkedList(1, 2, 3))
        all (list1s) should (be (List(1, 2, 3)) or newContain newTheSameElementsInOrderAs LinkedList(2, 3, 4))
        
        val e1 = intercept[TestFailedException] {
          all (list1s) should (be (List(2, 3, 4)) or newContain newTheSameElementsInOrderAs LinkedList(2, 3, 4))
        }
        checkMessageStackDepth(e1, allErrMsg(0, decorateToStringValue(List(1, 2, 3)) + " was not equal to " + decorateToStringValue(List(2, 3, 4)) + ", and " + decorateToStringValue(List(1, 2, 3)) + " did not contain the same elements in the same (iterated) order as " + decorateToStringValue(LinkedList(2, 3, 4)), thisLineNumber - 2, list1s), fileName, thisLineNumber - 2)
      }
      
      def `should use the implicit Equality in scope` {
        implicit val ise = upperCaseStringEquality
        
        all (hiLists) should (be (List("hi", "hello")) or newContain newTheSameElementsInOrderAs LinkedList("HI", "HELLO"))
        all (hiLists) should (be (List("ho", "hello")) or newContain newTheSameElementsInOrderAs LinkedList("HI", "HELLO"))
        all (hiLists) should (be (List("hi", "hello")) or newContain newTheSameElementsInOrderAs LinkedList("HELLO", "HI"))
        
        val e1 = intercept[TestFailedException] {
          all (hiLists) should (be (List("ho", "hello")) or newContain newTheSameElementsInOrderAs LinkedList("HELLO", "HI"))
        }
        checkMessageStackDepth(e1, allErrMsg(0, decorateToStringValue(List("hi", "hello")) + " was not equal to " + decorateToStringValue(List("ho", "hello")) + ", and " + decorateToStringValue(List("hi", "hello")) + " did not contain the same elements in the same (iterated) order as " + decorateToStringValue(LinkedList("HELLO", "HI")), thisLineNumber - 2, hiLists), fileName, thisLineNumber - 2)
      }
      
      def `should use an explicitly provided Equality` {
        (all (hiLists) should (be (List("hi", "hello")) or newContain newTheSameElementsInOrderAs LinkedList("HI", "HELLO"))) (decided by upperCaseStringEquality)
        (all (hiLists) should (be (List("ho", "hello")) or newContain newTheSameElementsInOrderAs LinkedList("HI", "HELLO"))) (decided by upperCaseStringEquality)
        (all (hiLists) should (be (List("hi", "hello")) or newContain newTheSameElementsInOrderAs LinkedList("HELLO", "HI"))) (decided by upperCaseStringEquality)
        
        val e1 = intercept[TestFailedException] {
          (all (hiLists) should (be (List("ho", "hello")) or newContain newTheSameElementsInOrderAs LinkedList("HELLO", "HI"))) (decided by upperCaseStringEquality)
        }
        checkMessageStackDepth(e1, allErrMsg(0, decorateToStringValue(List("hi", "hello")) + " was not equal to " + decorateToStringValue(List("ho", "hello")) + ", and " + decorateToStringValue(List("hi", "hello")) + " did not contain the same elements in the same (iterated) order as " + decorateToStringValue(LinkedList("HELLO", "HI")), thisLineNumber - 2, hiLists), fileName, thisLineNumber - 2)
      }
    }
    
    object `when used with (not contain theSameElementsInOrderAs xx and not contain theSameElementsInOrderAs xx)` {
      
      def `should do nothing if valid, else throw a TFE with an appropriate error message` {
        all (list1s) should (not newContain newTheSameElementsInOrderAs (LinkedList(3, 2, 8)) or not newContain newTheSameElementsInOrderAs (LinkedList(8, 3, 4)))
        all (list1s) should (not newContain newTheSameElementsInOrderAs (LinkedList(1, 2, 3)) or not newContain newTheSameElementsInOrderAs (LinkedList(8, 3, 4)))
        all (list1s) should (not newContain newTheSameElementsInOrderAs (LinkedList(3, 2, 8)) or not newContain newTheSameElementsInOrderAs (LinkedList(1, 2, 3)))
        
        val e1 = intercept[TestFailedException] {
          all (lists) should (not newContain newTheSameElementsInOrderAs (LinkedList(2, 3, 4)) or not newContain newTheSameElementsInOrderAs (LinkedList(2, 3, 4)))
        }
        checkMessageStackDepth(e1, allErrMsg(2, decorateToStringValue(List(2, 3, 4)) + " contained the same elements in the same (iterated) order as " + decorateToStringValue(LinkedList(2, 3, 4)) + ", and " + decorateToStringValue(List(2, 3, 4)) + " contained the same elements in the same (iterated) order as " + decorateToStringValue(LinkedList(2, 3, 4)), thisLineNumber - 2, lists), fileName, thisLineNumber - 2)
      }
      
      def `should use the implicit Equality in scope` {
        implicit val ise = upperCaseStringEquality
        
        all (hiLists) should (not newContain newTheSameElementsInOrderAs (LinkedList("HELLO", "HI")) or not newContain newTheSameElementsInOrderAs (LinkedList("hello", "hi")))
        all (hiLists) should (not newContain newTheSameElementsInOrderAs (LinkedList("HI", "HELLO")) or not newContain newTheSameElementsInOrderAs (LinkedList("hello", "hi")))
        all (hiLists) should (not newContain newTheSameElementsInOrderAs (LinkedList("HELLO", "HI")) or not newContain newTheSameElementsInOrderAs (LinkedList("hi", "hello")))
        
        val e1 = intercept[TestFailedException] {
          all (hiLists) should (not newContain newTheSameElementsInOrderAs (LinkedList("HI", "HELLO")) or not newContain newTheSameElementsInOrderAs (LinkedList("hi", "hello")))
        }
        checkMessageStackDepth(e1, allErrMsg(0, decorateToStringValue(List("hi", "hello")) + " contained the same elements in the same (iterated) order as " + decorateToStringValue(LinkedList("HI", "HELLO")) + ", and " + decorateToStringValue(List("hi", "hello")) + " contained the same elements in the same (iterated) order as " + decorateToStringValue(LinkedList("hi", "hello")), thisLineNumber - 2, hiLists), fileName, thisLineNumber - 2)
      }
      
      def `should use an explicitly provided Equality` {
        (all (hiLists) should (not newContain newTheSameElementsInOrderAs (LinkedList("HELLO", "HI")) or not newContain newTheSameElementsInOrderAs (LinkedList("hello", "hi")))) (decided by upperCaseStringEquality, decided by upperCaseStringEquality)
        (all (hiLists) should (not newContain newTheSameElementsInOrderAs (LinkedList("HI", "HELLO")) or not newContain newTheSameElementsInOrderAs (LinkedList("hello", "hi")))) (decided by upperCaseStringEquality, decided by upperCaseStringEquality)
        (all (hiLists) should (not newContain newTheSameElementsInOrderAs (LinkedList("HELLO", "HI")) or not newContain newTheSameElementsInOrderAs (LinkedList("hi", "hello")))) (decided by upperCaseStringEquality, decided by upperCaseStringEquality)
        
        val e1 = intercept[TestFailedException] {
          (all (hiLists) should (not newContain newTheSameElementsInOrderAs (LinkedList("HI", "HELLO")) or not newContain newTheSameElementsInOrderAs (LinkedList("hi", "hello")))) (decided by upperCaseStringEquality, decided by upperCaseStringEquality)
        }
        checkMessageStackDepth(e1, allErrMsg(0, decorateToStringValue(List("hi", "hello")) + " contained the same elements in the same (iterated) order as " + decorateToStringValue(LinkedList("HI", "HELLO")) + ", and " + decorateToStringValue(List("hi", "hello")) + " contained the same elements in the same (iterated) order as " + decorateToStringValue(LinkedList("hi", "hello")), thisLineNumber - 2, hiLists), fileName, thisLineNumber - 2)
      }
    }
    
    object `when used with (not be xx and not contain theSameElementsInOrderAs xx)` {
      
      def `should do nothing if valid, else throw a TFE with an appropriate error message` {
        all (list1s) should (not be (List(2)) or not newContain newTheSameElementsInOrderAs (LinkedList(8, 3, 4)))
        all (list1s) should (not be (List(1, 2, 3)) or not newContain newTheSameElementsInOrderAs (LinkedList(8, 3, 4)))
        all (list1s) should (not be (List(2)) or not newContain newTheSameElementsInOrderAs (LinkedList(1, 2, 3)))
        
        val e1 = intercept[TestFailedException] {
          all (list1s) should (not be (List(1, 2, 3)) or not newContain newTheSameElementsInOrderAs (LinkedList(1, 2, 3)))
        }
        checkMessageStackDepth(e1, allErrMsg(0, decorateToStringValue(List(1, 2, 3)) + " was equal to " + decorateToStringValue(List(1, 2, 3)) + ", and " + decorateToStringValue(List(1, 2, 3)) + " contained the same elements in the same (iterated) order as " + decorateToStringValue(LinkedList(1, 2, 3)), thisLineNumber - 2, list1s), fileName, thisLineNumber - 2)
      }
      
      def `should use the implicit Equality in scope` {
        implicit val ise = upperCaseStringEquality
        
        all (hiLists) should (not be (List("hello", "ho")) or not newContain newTheSameElementsInOrderAs (LinkedList("HELLO", "HO")))
        all (hiLists) should (not be (List("hi", "hello")) or not newContain newTheSameElementsInOrderAs (LinkedList("HELLO", "HO")))
        all (hiLists) should (not be (List("hello", "ho")) or not newContain newTheSameElementsInOrderAs (LinkedList("HI", "HELLO")))
        
        val e1 = intercept[TestFailedException] {
          all (hiLists) should (not be (List("hi", "hello")) or not newContain newTheSameElementsInOrderAs (LinkedList("HI", "HELLO")))
        }
        checkMessageStackDepth(e1, allErrMsg(0, decorateToStringValue(List("hi", "hello")) + " was equal to " + decorateToStringValue(List("hi", "hello")) + ", and " + decorateToStringValue(List("hi", "hello")) + " contained the same elements in the same (iterated) order as " + decorateToStringValue(LinkedList("HI", "HELLO")), thisLineNumber - 2, hiLists), fileName, thisLineNumber - 2)
      }
      
      def `should use an explicitly provided Equality` {
        (all (hiLists) should (not be (List("hello", "ho")) or not newContain newTheSameElementsInOrderAs (LinkedList("HELLO", "HO")))) (decided by upperCaseStringEquality)
        (all (hiLists) should (not be (List("hi", "hello")) or not newContain newTheSameElementsInOrderAs (LinkedList("HELLO", "HO")))) (decided by upperCaseStringEquality)
        (all (hiLists) should (not be (List("hello", "ho")) or not newContain newTheSameElementsInOrderAs (LinkedList("HI", "HELLO")))) (decided by upperCaseStringEquality)
        
        val e1 = intercept[TestFailedException] {
          (all (hiLists) should (not be (List("hi", "hello")) or not newContain newTheSameElementsInOrderAs (LinkedList("HI", "HELLO")))) (decided by upperCaseStringEquality)
        }
        checkMessageStackDepth(e1, allErrMsg(0, decorateToStringValue(List("hi", "hello")) + " was equal to " + decorateToStringValue(List("hi", "hello")) + ", and " + decorateToStringValue(List("hi", "hello")) + " contained the same elements in the same (iterated) order as " + decorateToStringValue(LinkedList("HI", "HELLO")), thisLineNumber - 2, hiLists), fileName, thisLineNumber - 2)
      }
    }
  }
}
