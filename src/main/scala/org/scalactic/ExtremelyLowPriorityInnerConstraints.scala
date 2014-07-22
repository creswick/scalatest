/*
 * Copyright 2001-2013 Artima, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.scalactic

import annotation.implicitNotFound
import scala.language.higherKinds

/**
 * Abstract class used to enforce type constraints for equality checks.
 *
 * <p>
 * For more information on how this class is used, see the documentation of <a href="TripleEqualsSupport.html"><code>TripleEqualsSupport</code></a>.
 * </p>
 */
trait ExtremelyLowPriorityInnerConstraints {
  import scala.language.implicitConversions

  // ELG Element Left Good
  // ELB Element Left Bad
  // ORL Or Left
  // ERG Element Right Good
  // ERB Element Right Bad
  // ORR Or Right
  // This one will provide an equality constraint if the Bad types have an inner constraint. It doesn't matter
  // in this case what the Good type does. If there was a constraint available for the Good types, then it would
  // use the higher priority implicit Constraint.orEqualityConstraint and never get here. 
  implicit def lowPriorityOrEqualityConstraint[ELG, ELB, ERG, ERB](implicit ev: InnerConstraint[ELB, ERB]): InnerConstraint[Or[ELG, ELB], Or[ERG, ERB]] = new InnerConstraint[Or[ELG, ELB], Or[ERG, ERB]]

  // This must be low priority to allow Every on both sides
  implicit def everyOnRightEqualityConstraint[EA, CA[ea] <: Every[ea], EB](implicit ev: InnerConstraint[EA, EB]): InnerConstraint[CA[EA], Every[EB]] = new InnerConstraint[CA[EA], Every[EB]]

  // Either (in x === y, x is the "target" of the === invocation, y is the "parameter")
  // ETL Element Target Left
  // ETR Element Target Right
  // EPL Element Parameter Left
  // EPR Element Parameter Right
  // This one will provide an equality constraint if the Bad types have an inner constraint. It doesn't matter
  // in this case what the Good type does. If there was a constraint available for the Good types, then it would
  // use the higher priority implicit Constraint.orEqualityConstraint and never get here. 
  implicit def lowPriorityEitherEqualityConstraint[ETL, ETR, EPL, EPR](implicit ev: InnerConstraint[ETR, EPR]): InnerConstraint[Either[ETL, ETR], Either[EPL, EPR]] = new InnerConstraint[Either[ETL, ETR], Either[EPL, EPR]]
}