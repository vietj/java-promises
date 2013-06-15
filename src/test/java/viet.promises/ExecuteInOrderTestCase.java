/*
 * Copyright 2013 Julien Viet
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
package viet.promises;

import org.junit.Test;
import vietj.promises.Deferred;

import java.util.Arrays;
import java.util.LinkedList;

import static org.junit.Assert.*;

/** @author Julien Viet */
public class ExecuteInOrderTestCase {

  @Test
  public void testAllRespectiveFulfilledCallbacksMustExecuteInTheOrderOfTheirOriginatingCallsToThen() {
    LinkedList<Integer> callbacks = new LinkedList<Integer>();
    Deferred<String> deferred = new Deferred<String>();
    deferred.promise.then(Callbacks.<String, Integer>addToHandler(callbacks, 1));
    deferred.promise.then(Callbacks.<String, Integer>addToHandler(callbacks, 2));
    deferred.resolve("");
    assertEquals(Arrays.asList(1, 2), callbacks);
  }

  @Test
  public void testAllRespectiveRejectedCallbacksMustExecuteInTheOrderOfTheirOriginatingCallsToThen() {
    LinkedList<Integer> callbacks = new LinkedList<Integer>();
    Deferred<String> deferred = new Deferred<String>();
    deferred.promise.then(Callbacks.<String>nullHandler(), Callbacks.<Exception, Integer>addToHandler(callbacks, 1));
    deferred.promise.then(Callbacks.<String>nullHandler(), Callbacks.<Exception, Integer>addToHandler(callbacks, 2));
    deferred.reject(new Exception());
    assertEquals(Arrays.asList(1, 2), callbacks);
  }

  @Test
  public void testReturnedPromiseMustBeRejectWithSameReasonWhenOnFulfilledThrowsAnException() {
    LinkedList<String> done = new LinkedList<String>();
    LinkedList<Exception> failed = new LinkedList<Exception>();
    Deferred<Integer> deferred = new Deferred<Integer>();
    Exception e = new Exception();
    deferred.promise.
        then(Callbacks.<Integer>throwHandler(e), Callbacks.<Exception>throwHandler()).
        then(Callbacks.<Void, String>addToHandler(done, "abc"), Callbacks.addToHandler(failed));
    deferred.resolve(3);
    assertEquals(Arrays.<String>asList(), done);
    assertEquals(Arrays.asList(e), failed);
  }

  @Test
  public void testReturnedPromiseMustBeRejectWithSameReasonWhenOnRejectedThrowsAnException() {
    LinkedList<String> done = new LinkedList<String>();
    LinkedList<Exception> failed = new LinkedList<Exception>();
    Deferred<Integer> deferred = new Deferred<Integer>();
    Exception e = new Exception();
    deferred.promise.
        then(Callbacks.<Integer>throwHandler(), Callbacks.<Exception>throwHandler(e)).
        then(Callbacks.<Void, String>addToHandler(done, "abc"), Callbacks.addToHandler(failed));
    deferred.reject(new Exception());
    assertEquals(Arrays.<String>asList(), done);
    assertEquals(Arrays.asList(e), failed);
  }

  @Test
  public void testReturnedPromiseMustBeRejectedWithSameValueWhenOnRejectedIsNotAFunction() {
    LinkedList<String> done = new LinkedList<String>();
    LinkedList<Exception> failed = new LinkedList<Exception>();
    Deferred<String> deferred = new Deferred<String>();
    deferred.promise.then(Callbacks.<String>id()).then(Callbacks.addToHandler(done), Callbacks.addToHandler(failed));
    Exception e = new Exception();
    deferred.reject(e);
    assertEquals(Arrays.asList(e), failed);
  }
}
