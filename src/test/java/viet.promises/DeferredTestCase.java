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
public class DeferredTestCase {

  /** . */
  Deferred<Integer> deferred;

  /** . */
  Promise<Integer> promise;

  /** . */
  LinkedList<Integer> done;

  /** . */
  LinkedList<Exception> failed;

  @Before
  public void setup() {
    deferred = new Deferred<Integer>();
    promise = deferred.promise;
    done = new LinkedList<Integer>();
    failed = new LinkedList<Exception>();
  }

  @Test
  public void testResolve1() {
    promise.then(Callbacks.addToHandler(done), Callbacks.addToHandler(failed));
    assertEquals(Arrays.<Integer>asList(), done);
    assertEquals(Arrays.<Exception>asList(), failed);
    deferred.resolve(3);
    assertEquals(Arrays.asList(3), done);
    assertEquals(Arrays.<Exception>asList(), failed);
  }

  @Test
  public void testResolve2() {
    deferred.resolve(4);
    promise.then(Callbacks.addToHandler(done), Callbacks.addToHandler(failed));
    assertEquals(Arrays.asList(4), done);
    assertEquals(Arrays.<Exception>asList(), failed);
  }

  @Test
  public void testResolve3() {
    promise.then(Callbacks.addToHandler(done), Callbacks.addToHandler(failed));
    deferred.resolve(3);
    deferred.resolve(4);
    assertEquals(Arrays.asList(3), done);
    assertEquals(Arrays.<Exception>asList(), failed);
    deferred.reject(new Exception());
    assertEquals(Arrays.asList(3), done);
    assertEquals(Arrays.<Exception>asList(), failed);
  }

  @Test
  public void testResolve4() {
    promise.then(Callbacks.addToHandler(done), Callbacks.addToHandler(failed));
    deferred.resolve(5);
    deferred.reject(new Exception());
    assertEquals(Arrays.asList(5), done);
    assertEquals(Arrays.<Exception>asList(), failed);
  }

  @Test
  public void testReject1() {
    promise.then(Callbacks.addToHandler(done), Callbacks.addToHandler(failed));
    assertEquals(Arrays.<Integer>asList(), done);
    assertEquals(Arrays.<Exception>asList(), failed);
    Exception reason = new Exception();
    deferred.reject(reason);
    assertEquals(Arrays.<Integer>asList(), done);
    assertEquals(Arrays.<Exception>asList(reason), failed);
  }

  @Test
  public void testReject2() {
    Exception reason = new Exception();
    deferred.reject(reason);
    promise.then(Callbacks.addToHandler(done), Callbacks.addToHandler(failed));
    assertEquals(Arrays.<Integer>asList(), done);
    assertEquals(Arrays.<Exception>asList(reason), failed);
  }

  @Test
  public void testReject3() {
    promise.then(Callbacks.addToHandler(done), Callbacks.addToHandler(failed));
    Exception reason = new Exception();
    deferred.reject(reason);
    deferred.resolve(5);
    assertEquals(Arrays.<Integer>asList(), done);
    assertEquals(Arrays.<Exception>asList(reason), failed);
  }

  @Test
  public void testReject4() {
    promise.then(Callbacks.addToHandler(done), Callbacks.addToHandler(failed));
    Exception reason = new Exception();
    deferred.reject(reason);
    deferred.reject(new Exception());
    assertEquals(Arrays.<Integer>asList(), done);
    assertEquals(Arrays.<Exception>asList(reason), failed);
  }
}
