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
import vietj.promises.Promise;
import vietj.promises.PromisingCallback;

import java.util.LinkedList;

/** @author Julien Viet */
public class FooTestCase {

  @Test
  public void testFoo() {

    Deferred<Integer> d = new Deferred<Integer>();
    final Deferred<String> d2 = new Deferred<String>();
    Promise<String> p = d.promise.then(new PromisingCallback<Integer, String>() {
      public Promise<String> call(Integer arg) throws Exception {
        return d2.promise;
      }
    });
    LinkedList<String> done = new LinkedList<String>();
    p.then(Callbacks.addToHandler(done));
    d.resolve(4);
    System.out.println("done = " + done);
    d2.resolve("foo");
    System.out.println("done = " + done);

  }

}
