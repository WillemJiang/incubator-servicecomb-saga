/*
 * Copyright 2017 Huawei Technologies Co., Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.servicecomb.saga.core;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

class SagaEventMatcher extends TypeSafeMatcher<SagaEvent> {

  private final String sagaId;
  private final Operation operation;
  private final Class<?> aClass;

  static Matcher<SagaEvent> eventWith(String sagaId, Operation operation, Class<?> aClass) {
    return new SagaEventMatcher(sagaId, operation, aClass);
  }

  static Matcher<SagaEvent> eventWith(Operation operation, Class<?> aClass) {
    return eventWith("0", operation, aClass);
  }

  private SagaEventMatcher(String sagaId, Operation operation, Class<?> aClass) {
    this.sagaId = sagaId;
    this.operation = operation;
    this.aClass = aClass;
  }

  @Override
  protected void describeMismatchSafely(SagaEvent item, Description description) {
    description
        .appendText("SagaEvent {sagaId=" + item.sagaId + ", operation=" + operation(item) + ", class=" + item.getClass());
  }

  @Override
  protected boolean matchesSafely(SagaEvent envelope) {
    return envelope.sagaId == sagaId
        && operation(envelope).equals(operation)
        && envelope.getClass().equals(aClass);
  }

  @Override
  public void describeTo(Description description) {
    description
        .appendText("SagaEvent {sagaId=" + sagaId + ", operation=" + operation + ", class=" + aClass.getCanonicalName());
  }

  private Operation operation(SagaEvent envelope) {
    return operation instanceof Compensation ? envelope.payload().compensation() : envelope.payload().transaction();
  }
}