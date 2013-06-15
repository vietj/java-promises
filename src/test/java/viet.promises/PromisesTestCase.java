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

import org.junit.Before;
import org.junit.Test;
import vietj.promises.Deferred;
import vietj.promises.Promise;

import java.util.Arrays;
import java.util.LinkedList;

import static org.junit.Assert.*;

/** @author Julien Viet */
public class PromisesTestCase {

  LinkedList<String> done;
  LinkedList<Exception> failed;
  Deferred<Integer> deferred;
  Promise<Integer> promise;

  @Before
  public void setup() {
    done = new LinkedList<String>();
    failed = new LinkedList<Exception>();
    deferred = new Deferred<Integer>();
    promise = deferred.promise;
    promise.then(Callbacks.<Integer>toStringHandler(), Callbacks.<Exception>toStringHandler()).then(Callbacks.addToHandler(done), Callbacks.addToHandler(failed));
  }

  @Test
  public void testFulfilledFulfillmentHandlerReturnsValue() {
    deferred.resolve(3);
    assertEquals(Arrays.asList("3"), done);
    assertEquals(Arrays.<Exception>asList(), failed);
  }

  @Test
  public void testRejectedRejectionHandlerReturnsAValue() {
    Exception e = new Exception() {
      @Override
      public String toString() {
        return "4";
      }
    };
    deferred.reject(e);
    assertEquals(Arrays.asList("4"), done);
    assertEquals(Arrays.<Exception>asList(), failed);
  }

}
